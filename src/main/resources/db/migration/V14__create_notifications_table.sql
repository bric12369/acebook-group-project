CREATE TABLE notifications (
id BIGSERIAL PRIMARY KEY,
user_id BIGINT REFERENCES users(id),
content TEXT,
created_at TIMESTAMP
);
