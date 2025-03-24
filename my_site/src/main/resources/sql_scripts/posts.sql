DROP TABLE IF EXISTS posts;
CREATE TABLE posts (
    post_id SERIAL PRIMARY KEY,
    post_name VARCHAR(255) NOT NULL,
    payment INT NOT NULL
);