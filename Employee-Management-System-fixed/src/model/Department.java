package model;

/**
 * Department — POJO mapping to the departments table.
 */
public class Department {

    private int    deptId;
    private String deptName;
    private String location;
    private int    employeeCount; // Populated via aggregate query

    // ── Constructors ────────────────────────────────────────────────────────

    public Department() {}

    public Department(String deptName, String location) {
        this.deptName = deptName;
        this.location = location;
    }

    // ── Getters & Setters ───────────────────────────────────────────────────

    public int    getDeptId()            { return deptId; }
    public void   setDeptId(int v)       { this.deptId = v; }

    public String getDeptName()           { return deptName; }
    public void   setDeptName(String v)   { this.deptName = v; }

    public String getLocation()           { return location; }
    public void   setLocation(String v)   { this.location = v; }

    public int    getEmployeeCount()          { return employeeCount; }
    public void   setEmployeeCount(int v)     { this.employeeCount = v; }

    @Override
    public String toString() {
        return String.format(
            "ID:%-4d | %-20s | Location: %-15s | Employees: %d",
            deptId, deptName, location, employeeCount
        );
    }
}
