# Email Verification and Forgot Password Design Specification

**Goal:** Implement a robust email-based verification system for user registration and a "Forgot Password" magic link flow.

**Architecture:**
- **Persistence:** Use a `verification_tokens` table via JPA and Flyway to store OTPs and reset tokens.
- **Email:** Use `Spring Boot Mail` with `Thymeleaf` for HTML-rich email templates.
- **Security:** Ensure only `ACTIVE` users can log in, and whitelist the new verification/reset endpoints.

---

## 1. Data Model

### `VerificationToken` Entity
Stores tokens associated with a user and a specific purpose.
- `id`: Long (Primary Key)
- `user`: User (ManyToOne)
- `token`: String (stores 6-digit OTP or UUID)
- `type`: Enum (`REGISTRATION_OTP`, `PASSWORD_RESET_TOKEN`)
- `expiryAt`: Instant
- `createdAt`: Instant (via BaseEntity)

### `VerificationTokenType` Enum
- `REGISTRATION_OTP`
- `PASSWORD_RESET_TOKEN`

---

## 2. Flyway Migration (`V4__verification_token.sql`)
```sql
CREATE TABLE verification_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    expiry_at DATETIME(6) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    CONSTRAINT fk_verification_token_user FOREIGN KEY (user_id) REFERENCES users(id)
);
```

---

## 3. Email Templates (Thymeleaf)
Location: `src/main/resources/templates/mail/`

### `registration-otp.html`
- Header: "Welcome to SE109"
- Body: "Your verification code is: **[OTP]**"
- Footer: "Valid for 5 minutes."

### `forgot-password.html`
- Header: "Reset Your Password"
- Body: "Click the link below to reset your password:"
- Button/Link: `[FrontendBase]/auth/forgot-password?token=[UUID]`
- Footer: "Valid for 15 minutes."

---

## 4. Endpoints (AuthController)

### Registration Flow
1. **`POST /api/v1/auth/register`** (Updated)
   - Save user with `status = PENDING`.
   - Generate 6-digit OTP.
   - Save `VerificationToken` (type: `REGISTRATION_OTP`).
   - Send `registration-otp.html` email.
2. **`POST /api/v1/auth/verify-otp`**
   - Payload: `{ email, otp }`
   - Validate OTP and expiry.
   - If valid: set user `status = ACTIVE`, delete token.

### Forgot Password Flow
1. **`POST /api/v1/auth/forgot-password`**
   - Payload: `{ email }`
   - If user exists: generate UUID.
   - Save `VerificationToken` (type: `PASSWORD_RESET_TOKEN`).
   - Send `forgot-password.html` email with magic link.
2. **`POST /api/v1/auth/reset-password`**
   - Payload: `{ token, newPassword }`
   - Validate UUID and expiry.
   - If valid: update user password, delete token.

---

## 5. Security Updates
- **`AuthServiceImpl.login`**: Check `if (user.getStatus() != UserStatus.ACTIVE) throw APP_EXCEPTION`.
- **`SecurityConstant.java`**: Whitelist `/api/v1/auth/verify-otp`, `/api/v1/auth/forgot-password`, `/api/v1/auth/reset-password`.

---

## 6. Testing Strategy
- **Unit Tests:** For token generation, expiry logic, and email service rendering.
- **Integration Tests:** End-to-end flow from registration -> verification, and forgot-password -> reset.
