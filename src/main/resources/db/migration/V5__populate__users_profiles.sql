-- Insert users only if they don't already exist
INSERT INTO users (username, enabled)
SELECT 'alice', true
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username='alice');

INSERT INTO users (username, enabled)
SELECT 'bob', true
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username='bob');

INSERT INTO users (username, enabled)
SELECT 'baz', true
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username='baz');

-- Insert posts dynamically using the actual user IDs
INSERT INTO posts (content, user_id)
SELECT 'Hello world!', id FROM users WHERE username='alice'
                                       AND NOT EXISTS (
        SELECT 1 FROM posts WHERE content='Hello world!'
    );

INSERT INTO posts (content, user_id)
SELECT 'Another post from Alice', id FROM users WHERE username='alice'
                                                  AND NOT EXISTS (
        SELECT 1 FROM posts WHERE content='Another post from Alice'
    );

INSERT INTO posts (content, user_id)
SELECT 'Bob''s first post', id FROM users WHERE username='bob'
                                            AND NOT EXISTS (
        SELECT 1 FROM posts WHERE content='Bob''s first post'
    );