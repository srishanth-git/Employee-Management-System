package dao;

import model.Employee;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * EmployeeDAO — All JDBC operations for the employees table.
 * Uses PreparedStatement to prevent SQL injection.
 */
public class EmployeeDAO {

    // ── CREATE ───────────────────────────────────────────────────────────────

    public boolean addEmployee(Employee emp) {
        String sql = """
            INSERT INTO employees
              (first_name, last_name, email, phone, hire_date, job_title, salary, dept_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, emp.getFirstName());
            ps.setString(2, emp.getLastName());
            ps.setString(3, emp.getEmail());
            ps.setString(4, emp.getPhone());
            ps.setString(5, emp.getHireDate());
            ps.setString(6, emp.getJobTitle());
            ps.setDouble(7, emp.getSalary());
            ps.setInt   (8, emp.getDeptId());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("[DAO] addEmployee error: " + e.getMessage());
            return false;
        }
    }

    // ── READ ALL ─────────────────────────────────────────────────────────────

    public List<Employee> getAllEmployees() {
        String sql = """
            SELECT e.*, d.dept_name
            FROM employees e
            LEFT JOIN departments d ON e.dept_id = d.dept_id
            ORDER BY e.emp_id
        """;

        List<Employee> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.err.println("[DAO] getAllEmployees error: " + e.getMessage());
        }

        return list;
    }

    // ── READ BY ID ───────────────────────────────────────────────────────────

    public Employee getEmployeeById(int empId) {
        String sql = """
            SELECT e.*, d.dept_name
            FROM employees e
            LEFT JOIN departments d ON e.dept_id = d.dept_id
            WHERE e.emp_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("[DAO] getEmployeeById error: " + e.getMessage());
        }

        return null;
    }

    // ── SEARCH ───────────────────────────────────────────────────────────────

    public List<Employee> searchByName(String keyword) {
        String sql = """
            SELECT e.*, d.dept_name
            FROM employees e
            LEFT JOIN departments d ON e.dept_id = d.dept_id
            WHERE e.first_name LIKE ? OR e.last_name LIKE ?
            ORDER BY e.first_name
        """;

        List<Employee> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("[DAO] searchByName error: " + e.getMessage());
        }

        return list;
    }

    public List<Employee> getEmployeesByDept(int deptId) {
        String sql = """
            SELECT e.*, d.dept_name
            FROM employees e
            LEFT JOIN departments d ON e.dept_id = d.dept_id
            WHERE e.dept_id = ?
            ORDER BY e.first_name
        """;

        List<Employee> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, deptId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("[DAO] getEmployeesByDept error: " + e.getMessage());
        }

        return list;
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────

    public boolean updateEmployee(Employee emp) {
        String sql = """
            UPDATE employees
            SET first_name = ?, last_name = ?, email = ?, phone = ?,
                hire_date  = ?, job_title = ?, salary = ?, dept_id = ?
            WHERE emp_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, emp.getFirstName());
            ps.setString(2, emp.getLastName());
            ps.setString(3, emp.getEmail());
            ps.setString(4, emp.getPhone());
            ps.setString(5, emp.getHireDate());
            ps.setString(6, emp.getJobTitle());
            ps.setDouble(7, emp.getSalary());
            ps.setInt   (8, emp.getDeptId());
            ps.setInt   (9, emp.getEmpId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[DAO] updateEmployee error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateSalary(int empId, double newSalary) {
        String sql = "UPDATE employees SET salary = ? WHERE emp_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, newSalary);
            ps.setInt   (2, empId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[DAO] updateSalary error: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ───────────────────────────────────────────────────────────────

    public boolean deleteEmployee(int empId) {
        String sql = "DELETE FROM employees WHERE emp_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, empId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[DAO] deleteEmployee error: " + e.getMessage());
            return false;
        }
    }

    // ── REPORTS ──────────────────────────────────────────────────────────────

    public void printSalaryReport() {
        String sql = """
            SELECT d.dept_name,
                   COUNT(e.emp_id)  AS total,
                   AVG(e.salary)    AS avg_salary,
                   MAX(e.salary)    AS max_salary,
                   MIN(e.salary)    AS min_salary
            FROM employees e
            JOIN departments d ON e.dept_id = d.dept_id
            GROUP BY d.dept_name
            ORDER BY avg_salary DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            System.out.println("\n" + "─".repeat(75));
            System.out.printf("%-22s | %-6s | %-12s | %-12s | %-12s%n",
                "Department", "Count", "Avg Salary", "Max Salary", "Min Salary");
            System.out.println("─".repeat(75));

            while (rs.next()) {
                System.out.printf("%-22s | %-6d | ₹%-11.0f | ₹%-11.0f | ₹%-11.0f%n",
                    rs.getString("dept_name"),
                    rs.getInt   ("total"),
                    rs.getDouble("avg_salary"),
                    rs.getDouble("max_salary"),
                    rs.getDouble("min_salary")
                );
            }
            System.out.println("─".repeat(75));

        } catch (SQLException e) {
            System.err.println("[DAO] salaryReport error: " + e.getMessage());
        }
    }

    // ── HELPER ───────────────────────────────────────────────────────────────

    private Employee mapRow(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        e.setEmpId    (rs.getInt   ("emp_id"));
        e.setFirstName(rs.getString("first_name"));
        e.setLastName (rs.getString("last_name"));
        e.setEmail    (rs.getString("email"));
        e.setPhone    (rs.getString("phone"));
        e.setHireDate (rs.getString("hire_date"));
        e.setJobTitle (rs.getString("job_title"));
        e.setSalary   (rs.getDouble("salary"));
        e.setDeptId   (rs.getInt   ("dept_id"));
        try { e.setDeptName(rs.getString("dept_name")); } catch (SQLException ignored) {}
        return e;
    }
}
