CREATE TABLE properties (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME(6) NOT NULL, 
    created_by VARCHAR(255) NULL,
    updated_at DATETIME(6) NOT NULL,
    updated_by VARCHAR(255) NULL,
    title VARCHAR(255) NOT NULL,
    purpose VARCHAR(255) NULL,
    `type` VARCHAR(255) NULL,
    price DECIMAL(15, 2) NOT NULL,
    line_address VARCHAR(255) NULL,
    ward_id VARCHAR(255) NULL,
    land_area DECIMAL(10, 2) NULL,
    floor_area DECIMAL(10, 2) NULL,
    floors INT NULL,
    floor_number INT NULL,
    bedrooms INT NULL,
    bathrooms INT NULL,
    entrance_road_width DOUBLE NULL,
    balcony_direction VARCHAR(255) NULL,
    direction VARCHAR(255) NULL,
    interior VARCHAR(255) NULL,
    description TEXT NULL,
    status VARCHAR(50) NOT NULL,

    -- Index tối ưu
    INDEX idx_property_status (status),
    INDEX idx_property_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
