ALTER TABLE users ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'PENDING';
CREATE INDEX idx_users_status ON users(status);

