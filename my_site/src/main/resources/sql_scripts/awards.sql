DROP TABLE IF EXISTS awards;
CREATE TABLE awards (
    awards_id SERIAL PRIMARY KEY,
    awards_name VARCHAR(255) NOT NULL,
    payment INT NOT NULL,
    description VARCHAR(255)
);