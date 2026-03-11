DROP TABLE IF EXISTS comments;

CREATE TABLE comments(
    id bigserial PRIMARY KEY,
    content VARCHAR,
    post_id bigint REFERENCES posts(id),
    user_id bigint REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);