# Email Verification and Forgot Password Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement email verification for registration (OTP) and a magic link flow for forgot password using Spring Boot Mail and Thymeleaf.

**Architecture:** 
- New `VerificationToken` entity and repository to store tokens/OTPs.
- `EmailService` with Thymeleaf templates.
- Update `AuthService` with new registration logic, OTP verification, and password reset.
- Whitelist new endpoints in `SecurityConstant`.

**Tech Stack:** Spring Boot 4.x, Spring Data JPA, MySQL, Flyway, Thymeleaf, Spring Mail.

---

### Task 1: Database Migration and VerificationToken Entity

**Files:**
- Create: `src/main/resources/db/migration/V4__verification_token.sql`
- Create: `src/main/java/com/uit/se109/entities/VerificationToken.java`
- Create: `src/main/java/com/uit/se109/enums/VerificationTokenType.java`
- Create: `src/main/java/com/uit/se109/repositories/VerificationTokenRepository.java`

- [ ] **Step 1: Create the Flyway migration file**
- [ ] **Step 2: Create `VerificationTokenType` enum**
- [ ] **Step 3: Create `VerificationToken` entity extending `BaseEntity`**
- [ ] **Step 4: Create `VerificationTokenRepository`**
- [ ] **Step 5: Verify entity mapping with a simple JPA test**

### Task 2: Email Service and Thymeleaf Templates

**Files:**
- Modify: `build.gradle.kts` (add Thymeleaf dependency)
- Create: `src/main/java/com/uit/se109/services/mail/EmailService.java`
- Create: `src/main/java/com/uit/se109/services/mail/EmailServiceImpl.java`
- Create: `src/main/resources/templates/mail/registration-otp.html`
- Create: `src/main/resources/templates/mail/forgot-password.html`

- [ ] **Step 1: Add `spring-boot-starter-thymeleaf` to `build.gradle.kts`**
- [ ] **Step 2: Create HTML templates in English for registration and forgot password**
- [ ] **Step 3: Implement `EmailService` to send HTML emails using `JavaMailSender` and `SpringTemplateEngine`**
- [ ] **Step 4: Write a test to verify email rendering and sending (mocking JavaMailSender)**

### Task 3: Update Registration and Implement OTP Verification

**Files:**
- Modify: `src/main/java/com/uit/se109/services/auth/AuthServiceImpl.java`
- Modify: `src/main/java/com/uit/se109/controllers/AuthController.java`
- Create: `src/main/java/com/uit/se109/dto/auth/VerifyOtpRequest.java`

- [ ] **Step 1: Update `register` method in `AuthServiceImpl` to generate 6-digit OTP, save it, and send email**
- [ ] **Step 2: Implement `verifyOtp` logic in `AuthServiceImpl`**
- [ ] **Step 3: Add `POST /verify-otp` to `AuthController`**
- [ ] **Step 4: Write integration test for the registration and verification flow**

### Task 4: Forgot Password and Reset Password Flow

**Files:**
- Modify: `src/main/java/com/uit/se109/services/auth/AuthServiceImpl.java`
- Modify: `src/main/java/com/uit/se109/controllers/AuthController.java`
- Create: `src/main/java/com/uit/se109/dto/auth/ForgotPasswordRequest.java`
- Create: `src/main/java/com/uit/se109/dto/auth/ResetPasswordRequest.java`

- [ ] **Step 1: Implement `forgotPassword` in `AuthServiceImpl` (generate UUID, send magic link)**
- [ ] **Step 2: Implement `resetPassword` in `AuthServiceImpl`**
- [ ] **Step 3: Add `POST /forgot-password` and `POST /reset-password` to `AuthController`**
- [ ] **Step 4: Write integration test for the forgot password flow**

### Task 5: Security Hardening and Whitelisting

**Files:**
- Modify: `src/main/java/com/uit/se109/services/auth/AuthServiceImpl.java` (login method)
- Modify: `src/main/java/com/uit/se109/constants/SecurityConstant.java`

- [ ] **Step 1: Update `login` method to reject users with status `PENDING` or `BLOCKED`**
- [ ] **Step 2: Add new endpoints to `SecurityConstant.PUBLIC_URLS`**
- [ ] **Step 3: Verify security constraints with integration tests**
