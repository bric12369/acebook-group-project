CREATE TABLE follows (
id BIGSERIAL PRIMARY KEY,
follower_id BIGINT NOT NULL,
following_id BIGINT NOT NULL,

CONSTRAINT fk_follower
FOREIGN KEY (follower_id)
REFERENCES users(id)
ON DELETE CASCADE,

CONSTRAINT fk_following
FOREIGN KEY (following_id)
REFERENCES users(id)
ON DELETE CASCADE,

CONSTRAINT unique_follow
UNIQUE (follower_id, following_id)
);

INSERT INTO follows (follower_id, following_id) VALUES
(1, 2),
(1, 3),
(2, 1);