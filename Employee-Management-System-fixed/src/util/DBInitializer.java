package util;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * DBInitializer — Creates required tables if they do not exist.
 * Run once at application startup.
 */
public class DBInitializer {

    public static void initialize() {
        String createDepartmentsTable = """
            CREATE TABLE IF NOT EXISTS departments (
                dept_id   INT PRIMARY KEY AUTO_INCREMENT,
                dept_name VARCHAR(100) NOT NULL UNIQUE,
                location  VARCHAR(100) NOT NULL
            );
        """;

        String createEmployeesTable = """
            CREATE TABLE IF NOT EXISTS employees (
                emp_id      INT PRIMARY KEY AUTO_INCREMENT,
                first_name  VARCHAR(50)  NOT NULL,
                last_name   VARCHAR(50)  NOT NULL,
                email       VARCHAR(100) NOT NULL UNIQUE,
                phone       VARCHAR(20),
                hire_date   DATE         NOT NULL,
                job_title   VARCHAR(100) NOT NULL,
                salary      DOUBLE       NOT NULL,
                dept_id     INT,
                FOREIGN KEY (dept_id) REFERENCES departments(dept_id)
            );
        """;

        String insertSampleDepts = """
            INSERT IGNORE INTO departments (dept_name, location) VALUES
                ('Engineering',     'Floor 3'),
                ('Human Resources', 'Floor 1'),
                ('Finance',         'Floor 2'),
                ('Marketing',       'Floor 4'),
                ('Operations',      'Floor 5');
        """;

        String insertSampleEmps = """
            INSERT IGNORE INTO employees
                (first_name, last_name, email, phone, hire_date, job_title, salary, dept_id)
            VALUES
                ('Aarav',  'Sharma', 'aarav.sharma@company.com',  '9876543210', '2022-01-15', 'Software Engineer', 72000, 1),
                ('Priya',  'Reddy',  'priya.reddy@company.com',   '9876543211', '2021-06-01', 'HR Manager',        65000, 2),
                ('Rahul',  'Verma',  'rahul.verma@company.com',   '9876543212', '2023-03-10', 'Financial Analyst', 68000, 3),
                ('Sneha',  'Iyer',   'sneha.iyer@company.com',    '9876543213', '2022-09-20', 'Marketing Lead',    70000, 4),
                ('Vikram', 'Nair',   'vikram.nair@company.com',   '9876543214', '2020-11-05', 'Senior Engineer',   90000, 1);
        """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createDepartmentsTable);
            stmt.execute(createEmployeesTable);
            stmt.execute(insertSampleDepts);
            stmt.execute(insertSampleEmps);
            System.out.println("[DB] Tables initialized with sample data.");

        } catch (SQLException e) {
            System.err.println("[DB] Initialization error: " + e.getMessage());
        }
    }
}
