package ui;

import dao.DepartmentDAO;
import dao.EmployeeDAO;
import model.Department;
import model.Employee;

import java.util.List;
import java.util.Scanner;

/**
 * ConsoleMenu — Interactive text-based UI for the EMS.
 * Delegates all DB work to EmployeeDAO and DepartmentDAO.
 */
public class ConsoleMenu {

    private final EmployeeDAO   empDAO  = new EmployeeDAO();
    private final DepartmentDAO deptDAO = new DepartmentDAO();
    private final Scanner       sc      = new Scanner(System.in);

    // ── Entry Point ──────────────────────────────────────────────────────────

    public void start() {
        banner();
        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = readInt("Enter choice");

            switch (choice) {
                case 1  -> employeeMenu();
                case 2  -> departmentMenu();
                case 3  -> reportsMenu();
                case 0  -> running = false;
                default -> warn("Invalid option. Try again.");
            }
        }

        System.out.println("\n👋  Thank you for using Employee Management System. Goodbye!");
    }

    // ── Main Menu ────────────────────────────────────────────────────────────

    private void printMainMenu() {
        System.out.println("""

            ╔══════════════════════════════════════╗
            ║       EMPLOYEE MANAGEMENT SYSTEM     ║
            ╠══════════════════════════════════════╣
            ║  1. Employee Management              ║
            ║  2. Department Management            ║
            ║  3. Reports & Analytics              ║
            ║  0. Exit                             ║
            ╚══════════════════════════════════════╝""");
    }

    // ── Employee Sub-Menu ────────────────────────────────────────────────────

    private void employeeMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("""

            ── EMPLOYEE MENU ──────────────────────
              1. View All Employees
              2. Search Employee by Name
              3. View Employee by ID
              4. Add New Employee
              5. Update Employee Details
              6. Update Employee Salary
              7. Delete Employee
              8. View Employees by Department
              0. Back
            ───────────────────────────────────────""");

            int ch = readInt("Choice");

            switch (ch) {
                case 1 -> viewAllEmployees();
                case 2 -> searchEmployee();
                case 3 -> viewEmployeeById();
                case 4 -> addEmployee();
                case 5 -> updateEmployee();
                case 6 -> updateSalary();
                case 7 -> deleteEmployee();
                case 8 -> empsByDept();
                case 0 -> back = true;
                default -> warn("Invalid option.");
            }
        }
    }

    // ── Department Sub-Menu ──────────────────────────────────────────────────

    private void departmentMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("""

            ── DEPARTMENT MENU ────────────────────
              1. View All Departments
              2. Add New Department
              3. Update Department
              4. Delete Department
              0. Back
            ───────────────────────────────────────""");

            int ch = readInt("Choice");

            switch (ch) {
                case 1 -> viewAllDepts();
                case 2 -> addDepartment();
                case 3 -> updateDepartment();
                case 4 -> deleteDepartment();
                case 0 -> back = true;
                default -> warn("Invalid option.");
            }
        }
    }

    // ── Reports Sub-Menu ─────────────────────────────────────────────────────

    private void reportsMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("""

            ── REPORTS & ANALYTICS ────────────────
              1. Salary Report by Department
              0. Back
            ───────────────────────────────────────""");

            int ch = readInt("Choice");

            switch (ch) {
                case 1 -> empDAO.printSalaryReport();
                case 0 -> back = true;
                default -> warn("Invalid option.");
            }
        }
    }

    // ── Employee Operations ──────────────────────────────────────────────────

    private void viewAllEmployees() {
        List<Employee> list = empDAO.getAllEmployees();
        if (list.isEmpty()) { info("No employees found."); return; }

        System.out.println("\n" + "─".repeat(120));
        System.out.printf("%-4s | %-22s | %-30s | %-15s | %-25s | %-12s | %s%n",
            "ID", "Full Name", "Email", "Phone", "Job Title", "Salary", "Department");
        System.out.println("─".repeat(120));
        list.forEach(System.out::println);
        System.out.println("─".repeat(120));
        System.out.printf("Total employees: %d%n", list.size());
    }

    private void searchEmployee() {
        String kw = readString("Enter name to search");
        List<Employee> results = empDAO.searchByName(kw);

        if (results.isEmpty()) { info("No match found for: " + kw); return; }

        System.out.println("\nSearch results:");
        results.forEach(System.out::println);
    }

    private void viewEmployeeById() {
        int id = readInt("Enter Employee ID");
        Employee e = empDAO.getEmployeeById(id);

        if (e == null) { warn("No employee found with ID: " + id); return; }

        System.out.println("\n── Employee Details ───────────────────────────");
        System.out.printf("  ID         : %d%n",  e.getEmpId());
        System.out.printf("  Name       : %s%n",  e.getFullName());
        System.out.printf("  Email      : %s%n",  e.getEmail());
        System.out.printf("  Phone      : %s%n",  e.getPhone());
        System.out.printf("  Hire Date  : %s%n",  e.getHireDate());
        System.out.printf("  Job Title  : %s%n",  e.getJobTitle());
        System.out.printf("  Salary     : ₹%.0f%n", e.getSalary());
        System.out.printf("  Department : %s%n",  e.getDeptName());
        System.out.println("───────────────────────────────────────────────");
    }

    private void addEmployee() {
        System.out.println("\n── Add New Employee ───────────────────────────");
        viewAllDepts();

        Employee emp = new Employee();
        emp.setFirstName(readString("First Name"));
        emp.setLastName (readString("Last Name"));
        emp.setEmail    (readString("Email"));
        emp.setPhone    (readString("Phone"));
        emp.setHireDate (readString("Hire Date (YYYY-MM-DD)"));
        emp.setJobTitle (readString("Job Title"));
        emp.setSalary   (readDouble("Salary"));
        emp.setDeptId   (readInt   ("Department ID"));

        if (empDAO.addEmployee(emp)) success("Employee added successfully!");
        else                         warn   ("Failed to add employee.");
    }

    private void updateEmployee() {
        int id = readInt("Enter Employee ID to update");
        Employee emp = empDAO.getEmployeeById(id);

        if (emp == null) { warn("Employee not found."); return; }

        System.out.println("Current: " + emp);
        System.out.println("Enter new values (press Enter to keep current):");

        String fn = readStringOpt("First Name [" + emp.getFirstName() + "]");
        if (!fn.isBlank()) emp.setFirstName(fn);

        String ln = readStringOpt("Last Name [" + emp.getLastName() + "]");
        if (!ln.isBlank()) emp.setLastName(ln);

        String em = readStringOpt("Email [" + emp.getEmail() + "]");
        if (!em.isBlank()) emp.setEmail(em);

        String ph = readStringOpt("Phone [" + emp.getPhone() + "]");
        if (!ph.isBlank()) emp.setPhone(ph);

        String jt = readStringOpt("Job Title [" + emp.getJobTitle() + "]");
        if (!jt.isBlank()) emp.setJobTitle(jt);

        String sal = readStringOpt("Salary [" + emp.getSalary() + "]");
        if (!sal.isBlank()) emp.setSalary(Double.parseDouble(sal));

        String di = readStringOpt("Dept ID [" + emp.getDeptId() + "]");
        if (!di.isBlank()) emp.setDeptId(Integer.parseInt(di));

        if (empDAO.updateEmployee(emp)) success("Employee updated successfully!");
        else                            warn   ("Update failed.");
    }

    private void updateSalary() {
        int    id     = readInt   ("Enter Employee ID");
        double newSal = readDouble("Enter new salary");

        if (empDAO.updateSalary(id, newSal)) success("Salary updated to ₹" + newSal);
        else                                 warn   ("Update failed.");
    }

    private void deleteEmployee() {
        int id = readInt("Enter Employee ID to delete");
        Employee emp = empDAO.getEmployeeById(id);
        if (emp == null) { warn("Employee not found."); return; }

        System.out.println("About to delete: " + emp.getFullName());
        String confirm = readString("Type YES to confirm");

        if ("YES".equalsIgnoreCase(confirm)) {
            if (empDAO.deleteEmployee(id)) success("Employee deleted.");
            else                           warn   ("Deletion failed.");
        } else {
            info("Deletion cancelled.");
        }
    }

    private void empsByDept() {
        viewAllDepts();
        int deptId = readInt("Enter Department ID");
        List<Employee> list = empDAO.getEmployeesByDept(deptId);

        if (list.isEmpty()) { info("No employees in this department."); return; }

        System.out.println("\nEmployees in Department ID " + deptId + ":");
        list.forEach(System.out::println);
    }

    // ── Department Operations ────────────────────────────────────────────────

    private void viewAllDepts() {
        List<Department> list = deptDAO.getAllDepartments();
        System.out.println("\n" + "─".repeat(60));
        System.out.printf("%-4s | %-20s | %-15s | %s%n",
            "ID", "Department", "Location", "Employees");
        System.out.println("─".repeat(60));
        list.forEach(System.out::println);
        System.out.println("─".repeat(60));
    }

    private void addDepartment() {
        Department d = new Department();
        d.setDeptName(readString("Department Name"));
        d.setLocation(readString("Location"));

        if (deptDAO.addDepartment(d)) success("Department added!");
        else                          warn   ("Failed to add department.");
    }

    private void updateDepartment() {
        viewAllDepts();
        int id = readInt("Enter Department ID to update");
        Department d = deptDAO.getDepartmentById(id);

        if (d == null) { warn("Department not found."); return; }

        d.setDeptName(readString("New Name [" + d.getDeptName() + "]"));
        d.setLocation(readString("New Location [" + d.getLocation() + "]"));

        if (deptDAO.updateDepartment(d)) success("Department updated!");
        else                             warn   ("Update failed.");
    }

    private void deleteDepartment() {
        viewAllDepts();
        int id = readInt("Enter Department ID to delete");
        String confirm = readString("Type YES to confirm deletion");

        if ("YES".equalsIgnoreCase(confirm)) {
            if (deptDAO.deleteDepartment(id)) success("Department deleted.");
            else                              warn   ("Deletion failed (employees may exist).");
        } else {
            info("Cancelled.");
        }
    }

    // ── Input Helpers ────────────────────────────────────────────────────────

    private int readInt(String prompt) {
        while (true) {
            System.out.print("  " + prompt + ": ");
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { warn("Please enter a valid number."); }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print("  " + prompt + ": ");
            try { return Double.parseDouble(sc.nextLine().trim()); }
            catch (NumberFormatException e) { warn("Please enter a valid number."); }
        }
    }

    private String readString(String prompt) {
        System.out.print("  " + prompt + ": ");
        return sc.nextLine().trim();
    }

    private String readStringOpt(String prompt) {
        System.out.print("  " + prompt + ": ");
        return sc.nextLine();
    }

    // ── Console Formatting ───────────────────────────────────────────────────

    private void banner() {
        System.out.println("""
            ╔════════════════════════════════════════════════════╗
            ║        EMPLOYEE MANAGEMENT SYSTEM  v1.0            ║
            ║           Java + JDBC (MySQ)                       ║
            ╚════════════════════════════════════════════════════╝
        """);
    }

    private void success(String msg) { System.out.println("  ✅  " + msg); }
    private void warn   (String msg) { System.out.println("  ⚠️   " + msg); }
    private void info   (String msg) { System.out.println("  ℹ️   " + msg); }
}
