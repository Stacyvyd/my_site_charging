DROP TABLE IF EXISTS projects_roles;
DROP TABLE IF EXISTS roles_of_employee;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS authorization_table;
DROP TABLE IF EXISTS awards;
DROP TABLE IF EXISTS experience_payment;
DROP TABLE IF EXISTS payment_history;
DROP TABLE IF EXISTS Employees_posts;
DROP TABLE IF EXISTS Employees;
DROP TABLE IF EXISTS posts;

-- Таблица posts
CREATE TABLE posts (
                       post_id SERIAL PRIMARY KEY,
                       post_name VARCHAR(255) NOT NULL,
                       payment INT NOT NULL
);

-- Таблица Employees
CREATE TABLE Employees (
                           Employee_id SERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           Address VARCHAR(255),
                           post_id INT,
                           experience INT,
                           birth_date DATE,
                           education VARCHAR(255),
                           FOREIGN KEY (post_id) REFERENCES posts(post_id)
);

CREATE TABLE Employees_posts (
                                 record_id SERIAL PRIMARY KEY,
                                 Employee_id INT NOT NULL,
                                 post_id INT NOT NULL,
                                 start_date DATE NOT NULL,
                                 end_date DATE,
                                 FOREIGN KEY (Employee_id) REFERENCES Employees(Employee_id),
                                 FOREIGN KEY (post_id) REFERENCES posts(post_id)
);

-- Таблица payment_history
CREATE TABLE payment_history (
                                 payment_id SERIAL PRIMARY KEY,
                                 Employee_id INT NOT NULL,
                                 payment INT NOT NULL,
                                 payment_type VARCHAR(50) NOT NULL CHECK (payment_type IN ('зарплата', 'премия')),
                                 payment_date DATE NOT NULL,
                                 FOREIGN KEY (Employee_id) REFERENCES Employees(Employee_id)
);

-- Таблица experience_payment
CREATE TABLE experience_payment (
                                    experience INT PRIMARY KEY,
                                    payment INT NOT NULL
);

-- Таблица awards
CREATE TABLE awards (
                        awards_id SERIAL PRIMARY KEY,
                        awards_name VARCHAR(255) NOT NULL,
                        payment INT NOT NULL,
                        description VARCHAR(255)
);

-- Таблица authorization_table
CREATE TABLE authorization_table (
                                     user_id SERIAL PRIMARY KEY,
                                     login VARCHAR(50) UNIQUE NOT NULL,
                                     password VARCHAR(50) NOT NULL
);

-- Таблица projects
CREATE TABLE projects (
                          projects_id SERIAL PRIMARY KEY,
                          projects_name VARCHAR(255) NOT NULL,
                          start_date DATE NOT NULL,
                          end_date DATE,
                          status VARCHAR(50) NOT NULL CHECK (status IN ('закончен', 'не начался', 'в процессе', 'закрыт'))
);

-- Таблица roles
CREATE TABLE roles (
                       role_id SERIAL PRIMARY KEY,
                       role_name VARCHAR(255) NOT NULL
);

-- Таблица roles_of_employee (добавляем PK id)
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

-- Таблица projects_roles (добавляем PK id)
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