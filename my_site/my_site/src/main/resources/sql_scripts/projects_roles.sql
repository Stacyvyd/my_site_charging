DROP TABLE IF EXISTS projects_roles;
CREATE TABLE projects_roles (
                                id SERIAL PRIMARY KEY,
                                projects_id INT NOT NULL,
                                role_id INT NOT NULL,
                                Employee_id INT NOT NULL,
                                payment INT NOT NULL,
                                FOREIGN KEY (projects_id) REFERENCES projects(projects_id),
                                FOREIGN KEY (role_id) REFERENCES roles(role_id),
                                FOREIGN KEY (Employee_id) REFERENCES Employees(Employee_id)
);