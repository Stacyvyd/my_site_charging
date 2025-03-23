DROP TABLE IF EXISTS Employees_posts;
CREATE TABLE Employees_posts (
                                 id SERIAL PRIMARY KEY,
                                 Employee_id INT NOT NULL,
                                 post_id INT NOT NULL,
                                 start_date DATE NOT NULL,
                                 end_date DATE,
                                 FOREIGN KEY (Employee_id) REFERENCES Employees(Employee_id),
                                 FOREIGN KEY (post_id) REFERENCES posts(post_id)
);