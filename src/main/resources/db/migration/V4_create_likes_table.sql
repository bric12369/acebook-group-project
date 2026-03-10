DROP TABLE IF EXISTS comments;

CREATE TABLE likes(
    id bigserial PRIMARY KEY,
    post_id bigint REFERENCES posts(id) ON DELETE CASCADE,
    user_id bigint REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(post_id, user_id)
);