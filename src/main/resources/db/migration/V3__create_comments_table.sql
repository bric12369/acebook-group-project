DROP TABLE IF EXISTS comments;

CREATE TABLE comments(
    id SERIAL PRIMARY KEY,
    content VARCHAR,
    post_id INT REFERENCES posts(id),
    user_id INT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);