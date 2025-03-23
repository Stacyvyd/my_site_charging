DROP TABLE IF EXISTS payment_history;
CREATE TABLE payment_history (
    payment_id SERIAL PRIMARY KEY,
    Employee_id INT NOT NULL,
    payment INT NOT NULL,
    payment_type VARCHAR(50) NOT NULL CHECK (payment_type IN ('зарплата', 'премия')),
    payment_date DATE,
    FOREIGN KEY (Employee_id) REFERENCES Employees(Employee_id)
);