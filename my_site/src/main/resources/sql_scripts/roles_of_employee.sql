DROP TABLE IF EXISTS roles_of_employee;
CREATE TABLE roles_of_employee (
                                   id SERIAL PRIMARY KEY,
                                   Employee_id INT NOT NULL,
                                   projects_id INT NOT NULL,
                                   role_id INT NOT NULL,
                                   start_date DATE NOT NULL,
                                   end_date DATE,
                                   FOREIGN KEY (Employee_id) REFERENCES Employees(Employee_id),
                                   FOREIGN KEY (projects_id) REFERENCES projects(projects_id),
                                   FOREIGN KEY (role_id) REFERENCES roles(role_id)
);