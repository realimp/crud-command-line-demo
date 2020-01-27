CREATE TABLE IF NOT EXISTS employees (
id INTEGER AUTO_INCREMENT,
name VARCHAR(55) NOT NULL,
birth_date DATETIME NOT NULL,
employee_position VARCHAR(55) NOT NULL,
date_hired DATETIME NOT NULL,
department_id INTEGER NOT NULL,
PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS departments (
id INTEGER AUTO_INCREMENT,
name VARCHAR(55) NOT NULL,
department_head INTEGER NOT NULL,
PRIMARY KEY (id),
KEY department_head (department_head),
CONSTRAINT departments_ibfk_1 FOREIGN KEY (department_head) REFERENCES employees(id)
);

ALTER TABLE employees ADD FOREIGN KEY (department_id) REFERENCES departments(id);