# 📋 Employee Management System
### Java + JDBC (MySQL) | Console Application

---

## 🗂️ Project Structure

```
EmployeeManagementSystem/
├── src/
│   ├── Main.java                  ← Entry point
│   ├── model/
│   │   ├── Employee.java          ← Employee POJO
│   │   └── Department.java        ← Department POJO
│   ├── dao/
│   │   ├── EmployeeDAO.java       ← All employee JDBC operations
│   │   └── DepartmentDAO.java     ← All department JDBC operations
│   ├── ui/
│   │   └── ConsoleMenu.java       ← Interactive menu UI
│   └── util/
│       ├── DBConnection.java      ← Singleton JDBC connection
│       └── DBInitializer.java     ← Table creation + sample data
├── lib/
│   ├── mysql-connector-j-9.7.0.jar
│   ├── slf4j-api-1.7.32.jar
│   └── slf4j-nop-1.7.32.jar
├── run.sh                         ← Linux/Mac build & run
├── run.bat                        ← Windows build & run
└── README.md
```

---

## ⚙️ Prerequisites

| Tool | Version |
|------|---------|
| Java JDK | 17 or higher |
| MYSQ JDBC Driver | 3.44.x |
| slf4j-api | 1.7.x |
| slf4j-nop | 1.7.x |

---

## 🚀 How to Run

### Linux / macOS
```bash
chmod +x run.sh
./run.sh
```

### Windows
```bat
run.bat
```

### Manual Compile + Run
```bash
# Compile
javac -cp "lib/sqlite-jdbc-3.44.1.0.jar" \
      -sourcepath src \
      -d out \
      $(find src -name "*.java")

# Run
java -cp "out:lib/sqlite-jdbc-3.44.1.0.jar:lib/slf4j-api-1.7.32.jar:lib/slf4j-nop-1.7.32.jar" \
     Main
```

---

## 🏗️ Database Schema

### `employees` table
| Column     | Type    | Notes              |
|------------|---------|--------------------|
| emp_id     | INTEGER | PK, Auto-increment |
| first_name | TEXT    | NOT NULL           |
| last_name  | TEXT    | NOT NULL           |
| email      | TEXT    | UNIQUE             |
| phone      | TEXT    |                    |
| hire_date  | TEXT    | YYYY-MM-DD format  |
| job_title  | TEXT    | NOT NULL           |
| salary     | REAL    | NOT NULL           |
| dept_id    | INTEGER | FK → departments   |

### `departments` table
| Column    | Type    | Notes              |
|-----------|---------|--------------------|
| dept_id   | INTEGER | PK, Auto-increment |
| dept_name | TEXT    | UNIQUE, NOT NULL   |
| location  | TEXT    | NOT NULL           |

---

## ✨ Features

### Employee Management
- ✅ **Add** new employee with all fields
- ✅ **View** all employees (with department name via JOIN)
- ✅ **Search** employee by name (partial match)
- ✅ **View** employee by ID (detailed view)
- ✅ **Update** employee details (keep existing on blank)
- ✅ **Update** salary only (quick update)
- ✅ **Delete** employee (with confirmation)
- ✅ **Filter** by department

### Department Management
- ✅ **Add** new department
- ✅ **View** all departments with employee count
- ✅ **Update** department name/location
- ✅ **Delete** department

### Reports & Analytics
- ✅ **Salary Report** — Avg, Max, Min per department

---

## 🧑‍💻 Key JDBC Concepts Used

| Concept | Where Used |
|---------|-----------|
| `DriverManager.getConnection()` | `DBConnection.java` |
| `PreparedStatement` | All DAO insert/update/delete |
| `Statement` | Read-all queries |
| `ResultSet` | Data retrieval + mapping |
| `try-with-resources` | Auto-close conn/stmt/rs |
| SQL JOIN | Employee ↔ Department name |
| SQL Aggregate | AVG, MAX, MIN, COUNT in report |
| Foreign Key | `dept_id` references `departments` |

---

## 📌 Sample Data (Auto-seeded)

| Name | Job Title | Department |
|------|-----------|------------|
| Aarav Sharma | Software Engineer | Engineering |
| Priya Reddy | HR Manager | Human Resources |
| Rahul Verma | Financial Analyst | Finance |
| Sneha Iyer | Marketing Lead | Marketing |
| Vikram Nair | Senior Engineer | Engineering |
