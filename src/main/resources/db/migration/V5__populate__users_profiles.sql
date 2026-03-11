-- Create some test users
INSERT INTO users (username, enabled) VALUES ('alice', true);
INSERT INTO users (username, enabled) VALUES ('bob', true);

-- Create some test posts for those users
INSERT INTO posts (content, user_id) VALUES ('Hello world!', 1);
INSERT INTO posts (content, user_id) VALUES ('Another post from Alice', 1);
INSERT INTO posts (content, user_id) VALUES ('Bob''s first post', 2);