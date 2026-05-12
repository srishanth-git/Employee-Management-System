package dao;

import model.Department;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DepartmentDAO — JDBC operations for the departments table.
 */
public class DepartmentDAO {

    // ── CREATE ───────────────────────────────────────────────────────────────

    public boolean addDepartment(Department dept) {
        String sql = "INSERT INTO departments (dept_name, location) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dept.getDeptName());
            ps.setString(2, dept.getLocation());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[DAO] addDepartment error: " + e.getMessage());
            return false;
        }
    }

    // ── READ ALL ─────────────────────────────────────────────────────────────

    public List<Department> getAllDepartments() {
        String sql = """
            SELECT d.*, COUNT(e.emp_id) AS emp_count
            FROM departments d
            LEFT JOIN employees e ON d.dept_id = e.dept_id
            GROUP BY d.dept_id
            ORDER BY d.dept_id
        """;

        List<Department> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Department d = new Department();
                d.setDeptId       (rs.getInt   ("dept_id"));
                d.setDeptName     (rs.getString("dept_name"));
                d.setLocation     (rs.getString("location"));
                d.setEmployeeCount(rs.getInt   ("emp_count"));
                list.add(d);
            }

        } catch (SQLException e) {
            System.err.println("[DAO] getAllDepartments error: " + e.getMessage());
        }

        return list;
    }

    // ── READ BY ID ───────────────────────────────────────────────────────────

    public Department getDepartmentById(int deptId) {
        String sql = "SELECT * FROM departments WHERE dept_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, deptId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Department d = new Department();
                d.setDeptId  (rs.getInt   ("dept_id"));
                d.setDeptName(rs.getString("dept_name"));
                d.setLocation(rs.getString("location"));
                return d;
            }

        } catch (SQLException e) {
            System.err.println("[DAO] getDepartmentById error: " + e.getMessage());
        }

        return null;
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────

    public boolean updateDepartment(Department dept) {
        String sql = "UPDATE departments SET dept_name = ?, location = ? WHERE dept_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dept.getDeptName());
            ps.setString(2, dept.getLocation());
            ps.setInt   (3, dept.getDeptId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[DAO] updateDepartment error: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ───────────────────────────────────────────────────────────────

    public boolean deleteDepartment(int deptId) {
        String sql = "DELETE FROM departments WHERE dept_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, deptId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[DAO] deleteDepartment error: " + e.getMessage());
            return false;
        }
    }
}
