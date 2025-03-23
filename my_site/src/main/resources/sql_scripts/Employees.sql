DROP TABLE IF EXISTS Employees;
CREATE TABLE Employees (
    Employee_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    Address VARCHAR(255),
    post_id INT,
    experience INT,
    birth_date DATE NOT NULL,
    education VARCHAR(255),
    FOREIGN KEY (post_id) REFERENCES posts(post_id)
);