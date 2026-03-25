CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME(6) NOT NULL,
    created_by VARCHAR(255) NULL,
    updated_at DATETIME(6) NOT NULL,
    updated_by VARCHAR(255) NULL,

    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Độ dài 255 để chứa BCrypt/Argon2 hash
    email VARCHAR(150) NOT NULL UNIQUE,
    full_name VARCHAR(255) NULL,
    phone_number VARCHAR(20) NULL,

    INDEX idx_users_username (username),
    INDEX idx_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
