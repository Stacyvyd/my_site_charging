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

CREATE TABLE posts (
    post_id SERIAL PRIMARY KEY,
    post_name VARCHAR(255) NOT NULL,
    payment INT NOT NULL
);

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

CREATE TABLE Employees_posts (
    Employee_id INT NOT NULL,
    post_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    PRIMARY KEY (Employee_id, post_id),
    FOREIGN KEY (Employee_id) REFERENCES Employees(Employee_id),
    FOREIGN KEY (post_id) REFERENCES posts(post_id)
);

CREATE TABLE payment_history (
    payment_id SERIAL PRIMARY KEY,
    Employee_id INT NOT NULL,
    payment INT NOT NULL,
    payment_type VARCHAR(50) NOT NULL CHECK (payment_type IN ('зарплата', 'премия')),
    payment_date DATE,
    FOREIGN KEY (Employee_id) REFERENCES Employees(Employee_id)
);

CREATE TABLE experience_payment (
    experience INT PRIMARY KEY,
    payment INT NOT NULL
);

CREATE TABLE awards (
    awards_id SERIAL PRIMARY KEY,
    awards_name VARCHAR(255) NOT NULL,
    payment INT NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE authorization_table (
    user_id INT PRIMARY KEY,
    login VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE projects (
    projects_id SERIAL PRIMARY KEY,
    projects_name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    status VARCHAR(50) NOT NULL CHECK (status IN ('закончен', 'не начался', 'в процессе', 'закрыт'))
);

CREATE TABLE roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL
);

CREATE TABLE roles_of_employee (
    Employee_id INT NOT NULL,
    projects_id INT NOT NULL,
    role_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    PRIMARY KEY (Employee_id, projects_id, role_id),
    FOREIGN KEY (Employee_id) REFERENCES Employees(Employee_id),
    FOREIGN KEY (projects_id) REFERENCES projects(projects_id),
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

CREATE TABLE projects_roles (
    projects_id INT NOT NULL,
    role_id INT NOT NULL,
    Employee_id INT NOT NULL,
    payment INT NOT NULL,
    PRIMARY KEY (projects_id, role_id, Employee_id),
    FOREIGN KEY (projects_id) REFERENCES projects(projects_id),
    FOREIGN KEY (role_id) REFERENCES roles(role_id),
    FOREIGN KEY (Employee_id) REFERENCES Employees(Employee_id)
);