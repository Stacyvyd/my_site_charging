DROP TABLE IF EXISTS authorization_table;
CREATE TABLE authorization_table (
    user_id INT PRIMARY KEY,
    login VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL
);