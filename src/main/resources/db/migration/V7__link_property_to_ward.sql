-- Sửa cột ward_id từ VARCHAR(255) sang BIGINT và thêm khóa ngoại
ALTER TABLE properties MODIFY COLUMN ward_id BIGINT;

ALTER TABLE properties 
ADD CONSTRAINT fk_property_ward FOREIGN KEY (ward_id) REFERENCES wards(id);
