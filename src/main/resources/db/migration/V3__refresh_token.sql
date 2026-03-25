CREATE TABLE refresh_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME(6) NOT NULL,
    created_by VARCHAR(255) NULL,
    updated_at DATETIME(6) NOT NULL,
    updated_by VARCHAR(255) NULL,
    user_id BIGINT NOT NULL,
    token VARCHAR(500) NOT NULL,
    expiry_at DATETIME(6) NOT NULL,
    CONSTRAINT fk_refresh_tokens_user 
        FOREIGN KEY (user_id) REFERENCES users(id) 
        ON DELETE CASCADE, -- Xóa user thì xóa luôn token
    
    INDEX idx_rt_token (token),
    INDEX idx_rt_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
