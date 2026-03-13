ALTER TABLE users
ADD COLUMN profile_image_url TEXT;

UPDATE users
SET profile_image_url = CASE
    WHEN id = 1 THEN 'avatar_1.jpeg'
    WHEN id = 2 THEN 'avatar_2.jpeg'
    WHEN id = 3 THEN 'avatar_3.jpeg'
    END
WHERE id IN (1, 2, 3);