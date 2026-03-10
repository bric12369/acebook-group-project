DROP TABLE IF EXISTS likes;

CREATE TABLE likes(
    id bigserial PRIMARY KEY,
    post_id bigint REFERENCES posts(id) ON DELETE CASCADE,
    user_id bigint REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE(post_id, user_id)
);