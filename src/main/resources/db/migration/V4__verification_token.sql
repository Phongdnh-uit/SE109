CREATE TABLE verification_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME(6) NOT NULL,
    created_by VARCHAR(255) NULL,
    updated_at DATETIME(6) NOT NULL,
    updated_by VARCHAR(255) NULL,

    user_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    expiry_at DATETIME(6) NOT NULL,

    CONSTRAINT fk_verification_tokens_user FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_verification_tokens_user ON verification_tokens (user_id);
CREATE INDEX idx_verification_tokens_token ON verification_tokens (token);
