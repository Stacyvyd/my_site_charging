DROP TABLE IF EXISTS projects;
CREATE TABLE projects (
    projects_id SERIAL PRIMARY KEY,
    projects_name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    status VARCHAR(50) NOT NULL CHECK (status IN ('закончен', 'не начался', 'в процессе', 'закрыт'))
);
