DROP DATABASE IF EXISTS pms;
CREATE DATABASE pms;

-- run this if you're using MariaDB and run into a "--skip-grand-tables" error
FLUSH PRIVILEGES;--

CREATE USER IF NOT EXISTS 'bugbuster'@'localhost' IDENTIFIED BY 'bugbuster';
CREATE USER IF NOT EXISTS 'bugbuster'@'%' IDENTIFIED BY 'bugbuster';
GRANT ALL ON pms.* TO 'bugbuster'@'localhost';
GRANT ALL ON pms.* TO 'bugbuster'@'%';

USE pms;

CREATE TABLE paygrade (
    grade_id INT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
    grade_name VARCHAR(255) NOT NULL,
    grade_cost FLOAT NOT NULL,
    grade_year DATE NOT NULL,
    CONSTRAINT check_grade_name CHECK (grade_name IN ('JS', 'SS', 'DS', 'P1', 'P2', 'P3', 'P4', 'P5', 'P6'))
);

CREATE TABLE employee (
    emp_number INT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
    emp_username VARCHAR(255) NOT NULL,
    emp_password VARCHAR(255) NOT NULL,
    emp_name VARCHAR(255) NOT NULL,
    emp_type VARCHAR(255) NOT NULL,
    emp_deactivated BIT NOT NULL DEFAULT 0,
    emp_grade_id INT NOT NULL,
    emp_supervisor_id INT DEFAULT NULL,
    emp_approver_id INT DEFAULT NULL,
    FOREIGN KEY (emp_supervisor_id)
      REFERENCES employee (emp_number),
    FOREIGN KEY (emp_approver_id)
      REFERENCES employee (emp_number),
    FOREIGN KEY (emp_grade_id)
      REFERENCES paygrade (grade_id),
    CONSTRAINT check_emp_type CHECK (emp_type IN ('Employee', 'Admin', 'HR', 'PM'))
);

CREATE TABLE project (
    project_id INT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
    project_name VARCHAR(255) NOT NULL,
    project_description VARCHAR(255),
    project_allocated_budget FLOAT,
    project_manager_number INT NOT NULL,
    FOREIGN KEY (project_manager_number)
     REFERENCES employee (emp_number)
     ON DELETE CASCADE
);

CREATE TABLE employee_project (
    ep_id INT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
    ep_emp_id INT NOT NULL,
    ep_project_id INT NOT NULL,
    FOREIGN KEY (ep_emp_id)
		REFERENCES employee (emp_number)
        ON DELETE CASCADE,
    FOREIGN KEY (ep_project_id)
		REFERENCES project (project_id)
        ON DELETE CASCADE
);

CREATE TABLE budget (
    budget_id INT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
    budget_year INT NOT NULL,
    budget_JS FLOAT NOT NULL,
    budget_SS FLOAT NOT NULL,
    budget_DS FLOAT NOT NULL,
    budget_P1 FLOAT NOT NULL,
    budget_P2 FLOAT NOT NULL,
    budget_P3 FLOAT NOT NULL,
    budget_P4 FLOAT NOT NULL,
    budget_P5 FLOAT NOT NULL,
    budget_P6 FLOAT NOT NULL
);

-- budget_wp INT NOT NULL

CREATE TABLE work_package (
    wp_id VARCHAR(512) NOT NULL UNIQUE PRIMARY KEY,
    wp_name VARCHAR(255) NOT NULL,
    wp_start_date DATE,
    wp_end_date DATE,
    wp_project_id INT NOT NULL,
    wp_resp_eng_id INT NOT NULL,
    wp_eng_est_id INT NOT NULL,
    wp_rolling_est_id INT NOT NULL,
    wp_parent_id VARCHAR(512) DEFAULT NULL,
    UNIQUE (wp_id, wp_project_id),
    FOREIGN KEY (wp_project_id)
      REFERENCES project (project_id)
      ON DELETE CASCADE,
    FOREIGN KEY (wp_resp_eng_id)
      REFERENCES employee (emp_number)
      ON DELETE CASCADE,
    FOREIGN KEY (wp_eng_est_id)
      REFERENCES budget (budget_id)
      ON DELETE CASCADE,
    FOREIGN KEY (wp_rolling_est_id)
		  REFERENCES budget (budget_id)
      ON DELETE CASCADE,
    FOREIGN KEY (wp_parent_id)
		  REFERENCES work_package (wp_id)
      ON DELETE CASCADE
);

-- ALTER TABLE budget
-- ADD FOREIGN KEY (budget_wp)
--     REFERENCES work_package(wp_id)
--     ON DELETE CASCADE;

CREATE TABLE employee_work_package (
    ewp_id INT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
    ewp_emp_id INT NOT NULL,
    ewp_wp_id VARCHAR(512) NOT NULL,
    FOREIGN KEY (ewp_emp_id)
		REFERENCES employee (emp_number)
        ON DELETE CASCADE,
    FOREIGN KEY (ewp_wp_id)
		REFERENCES work_package (wp_id)
        ON DELETE CASCADE
);

-- Leaving number of rows in but it might not be needed depending on how much freedom we have with the code
CREATE TABLE timesheet (
    timesheet_id INT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
    timesheet_status VARCHAR(255) NOT NULL,
    timesheet_end_date DATE NOT NULL,
    timesheet_num_rows INT NOT NULL DEFAULT 0,
    timesheet_signed_date DATE,
    timesheet_response_date DATE,
    timesheet_overtime FLOAT NOT NULL DEFAULT 0,
    timesheet_flextime FLOAT NOT NULL DEFAULT 0,
    timesheet_comments VARCHAR(512),
    timesheet_emp INT,
    timesheet_emp_signature MEDIUMBLOB,
    timesheet_emp_name VARCHAR(255),
    timesheet_approver INT,
    timesheet_approver_signature MEDIUMBLOB,
    timesheet_approver_name VARCHAR(255),

    UNIQUE (timesheet_emp, timesheet_end_date),
    FOREIGN KEY (timesheet_emp)
       REFERENCES employee (emp_number)
       ON DELETE SET NULL,
    FOREIGN KEY (timesheet_approver)
       REFERENCES employee (emp_number)
       ON DELETE SET NULL
);

-- Week hours are packed into a single attribute
CREATE TABLE timesheet_row (
    row_id INT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
    row_num INT,
    row_sat_hours FLOAT DEFAULT 0,
    row_sun_hours FLOAT DEFAULT 0,
    row_mon_hours FLOAT DEFAULT 0,
    row_tue_hours FLOAT DEFAULT 0,
    row_wed_hours FLOAT DEFAULT 0,
    row_thu_hours FLOAT DEFAULT 0,
    row_fri_hours FLOAT DEFAULT 0,
    row_notes VARCHAR(256),
    row_timesheet_id INT NOT NULL,
    row_project_id INT NOT NULL,
    row_wp_id VARCHAR(255) NOT NULL,
    UNIQUE (row_project_id, row_wp_id, row_timesheet_id),
    FOREIGN KEY (row_timesheet_id)
       REFERENCES timesheet (timesheet_id)
       ON DELETE CASCADE,
    FOREIGN KEY (row_project_id)
       REFERENCES project (project_id)
       ON DELETE CASCADE,
    FOREIGN KEY (row_wp_id)
       REFERENCES work_package (wp_id)
       ON DELETE CASCADE
);

DELIMITER $$

CREATE
DEFINER = 'bugbuster'@'%'
TRIGGER after_timesheet_row_insertion
AFTER INSERT
ON timesheet_row
FOR EACH ROW
BEGIN
	DECLARE timesheetID INT;
    SET timesheetID = NEW.row_timesheet_id;

	-- update the timesheet's number of rows
UPDATE timesheet
SET timesheet_num_rows = timesheet_num_rows + 1
WHERE timesheet_id = timesheetID;

END $$

DELIMITER $$

CREATE
DEFINER = 'bugbuster'@'%'
TRIGGER after_timesheet_row_deletion
AFTER DELETE
ON timesheet_row
FOR EACH ROW
BEGIN
	DECLARE timesheetID INT;
    SET timesheetID = OLD.row_timesheet_id;

	-- update the timesheet's number of rows
UPDATE timesheet
SET timesheet_num_rows = timesheet_num_rows - 1
WHERE timesheet_id = timesheetID;

END $$

DELIMITER ;

-- Insertions

-- Paygrade (id, name, cost, year)

INSERT INTO paygrade VALUES(1, "JS", 20.00, '2023-01-01');
INSERT INTO paygrade VALUES(2, "SS", 21.00, '2023-01-01');
INSERT INTO paygrade VALUES(3, "DS", 22.00, '2023-01-01');
INSERT INTO paygrade VALUES(4, "P1", 23.00, '2023-01-01');
INSERT INTO paygrade VALUES(5, "P2", 24.00, '2023-01-01');
INSERT INTO paygrade VALUES(6, "P3", 25.00, '2023-01-01');
INSERT INTO paygrade VALUES(7, "P4", 26.00, '2023-01-01');
INSERT INTO paygrade VALUES(8, "P5", 27.00, '2023-01-01');
INSERT INTO paygrade VALUES(9, "P6", 28.00, '2023-01-01');

-- Needed for trigger

INSERT INTO employee VALUES(1, "blink", "temp", "Bruce Link", "Admin", 0, 9, DEFAULT, 1);
INSERT INTO project VALUES(3, "Paid Time Off", "Sick and Vacation Time For Employees", 0, 1);
INSERT INTO budget VALUES(17, '2023', 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO work_package VALUES("Sick", "Sick Time", '2023-01-01', '2099-01-01', 3, 1, 17, 17, DEFAULT);
INSERT INTO work_package VALUES("Vacation", "Vacation Time", '2023-01-01', '2099-01-01', 3, 1, 17, 17, DEFAULT);

DELIMITER $$

CREATE
DEFINER = 'bugbuster'@'%'
TRIGGER after_employee_insertion
AFTER INSERT
ON employee

FOR EACH ROW
BEGIN
    INSERT INTO employee_project VALUES(DEFAULT, NEW.emp_number, 3);
    INSERT INTO employee_work_package VALUES(DEFAULT, NEW.emp_number, "Sick");
    INSERT INTO employee_work_package VALUES(DEFAULT, NEW.emp_number, "Vacation");
END $$

DELIMITER ;

-- Employee (id, username, password, name, type, disabled, paygrade_id, supervisor_id, approver_id)

INSERT INTO employee VALUES(2, "nhughes", "temp", "Nick Hughes", "PM", 0, 9, 1, 1);
INSERT INTO employee VALUES(3, "maximoose", "temp", "Max Joe", "HR", 0, 5, 2, 2);
INSERT INTO employee VALUES(4, "benG", "temp", "Benjamin Friedman", "Employee", 0, 5, 2, 2);
INSERT INTO employee VALUES(5, "pm", "temp", "manager", "PM", 0, 9, 1, 1);
INSERT INTO employee VALUES(6, "emp1", "temp", "employee1", "Employee", 0, 5, 5, 5);
INSERT INTO employee VALUES(7, "emp2", "temp", "employee2", "Employee", 0, 5, 5, 5);

-- Project (id, name, description, alloc_budget, manager_id)

INSERT INTO project VALUES(1, "Test Project", "Test Desc", 1000.00, 2);
INSERT INTO project VALUES(2, "Test Project2", "Test Desc", 1000.00, 5);

-- Budget (id, year, JS, SS, DS, P1, P2, P3, P4, P5, P6)

INSERT INTO budget VALUES(1, '2023', 0, 0, 0, 0, 0, 900.00, 100.00, 0, 0);
INSERT INTO budget VALUES(2, '2023', 0, 0, 0, 0, 0, 900.00, 0, 0, 0);
INSERT INTO budget VALUES(3, '2023', 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000);
INSERT INTO budget VALUES(4, '2023', 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO budget VALUES(5, '2023', 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO budget VALUES(6, '2023', 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO budget VALUES(7, '2023', 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO budget VALUES(8, '2023', 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO budget VALUES(9, '2023', 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO budget VALUES(10, '2023', 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO budget VALUES(11, '2023', 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO budget VALUES(12, '2023', 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO budget VALUES(13, '2023', 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO budget VALUES(14, '2023', 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO budget VALUES(15, '2023', 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO budget VALUES(16, '2023', 0, 0, 0, 0, 0, 0, 0, 0, 0);


-- WorkPackage (id, name, start_date, end_date, project_id, resp_eng_id, wp_eng_est_id, wp_rolling_est_id, parent)

INSERT INTO work_package VALUES("P1WP1", "Test WP 1", '2023-05-01', '2023-06-01', 1, 2, 1, 2, DEFAULT);
INSERT INTO work_package VALUES("P1WP2", "Test WP 2", '2023-05-02', '2023-06-02', 1, 2, 3, 4, DEFAULT);
INSERT INTO work_package VALUES("P1WP3", "Test WP 3", '2023-05-03', '2023-06-03', 1, 2, 5, 6, DEFAULT);
INSERT INTO work_package VALUES("P1WP4", "Test WP 4", '2023-05-04', '2023-06-04', 1, 2, 7, 8, DEFAULT);
INSERT INTO work_package VALUES("P1WP5", "Test WP 5", '2023-05-05', '2023-06-05', 1, 2, 9, 10, DEFAULT);
INSERT INTO work_package VALUES("P1WP6", "Test WP 6", '2023-05-06', '2023-06-06', 1, 2, 11, 12, DEFAULT);
INSERT INTO work_package VALUES("P1WP7", "Test WP 7", '2023-05-07', '2023-06-07', 1, 2, 13, 14, DEFAULT);
INSERT INTO work_package VALUES("P1WP8", "Test WP 8", '2023-05-08', '2023-06-08', 1, 2, 15, 16, DEFAULT);


-- EmployeeWorkPackage (id, emp_id, wp_id)

INSERT INTO employee_work_package VALUES(DEFAULT, 3, "P1WP1");
INSERT INTO employee_work_package VALUES(DEFAULT, 4, "P1WP1");
INSERT INTO employee_work_package VALUES(DEFAULT, 6, "P1WP1");
INSERT INTO employee_work_package VALUES(DEFAULT, 6, "P1WP2");
INSERT INTO employee_work_package VALUES(DEFAULT, 6, "P1WP3");
INSERT INTO employee_work_package VALUES(DEFAULT, 6, "P1WP4");
INSERT INTO employee_work_package VALUES(DEFAULT, 7, "P1WP5");
INSERT INTO employee_work_package VALUES(DEFAULT, 7, "P1WP6");
INSERT INTO employee_work_package VALUES(DEFAULT, 3, "P1WP2");
INSERT INTO employee_work_package VALUES(DEFAULT, 4, "P1WP2");

-- Timesheet (id, status, end_date, num_rows, last_modified,
-- emp_id, emp_signature, emp_name,
-- approver_id, approver_signature, approver_name)

-- TimesheetRow (id, row_num, hours, notes, timesheet_id, project_id, wp_id)