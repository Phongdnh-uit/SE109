UPDATE wards w
JOIN provinces p ON w.province_code = p.code
SET w.province_id = p.id;

ALTER TABLE wards DROP COLUMN province_code;
