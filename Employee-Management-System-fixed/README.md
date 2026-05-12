# 📋 Employee Management System
### Java + JDBC (MySQL) | Console Application

---

## 🗂️ Project Structure

```
Employee-Management-System/
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
├── mysql-connector-j-9.7.0.jar    ← MySQL JDBC driver
├── slf4j-api-1.7.32.jar
├── slf4j-nop-1.7.32.jar
├── run.sh                         ← Linux/Mac build & run
├── run.bat                        ← Windows build & run
├── .classpath                     ← Eclipse classpath config
├── .project                       ← Eclipse project config
├── Employee-Management-System.iml ← IntelliJ module config
└── README.md
```

---

## ⚙️ Prerequisites

| Tool | Version |
|------|---------|
| Java JDK | 17 or higher |
| MySQL Server | 8.0 or higher |
| MySQL JDBC Driver | 9.7.0 (included) |
| slf4j-api | 1.7.32 (included) |
| slf4j-nop | 1.7.32 (included) |

---

## 🛢️ MySQL Setup

1. Install and start MySQL Server
2. Open MySQL Workbench or terminal and run:
```sql
CREATE DATABASE employeedb;
```
3. Open `src/util/DBConnection.java` and update your password:
```java
private static final String PASSWORD = "your_mysql_password";
```

---

## 🚀 How to Run

### ▶️ IntelliJ IDEA
1. **File → Open** → select this project folder
2. IntelliJ auto-detects all JARs via `.iml` file
3. Click the green **Run** button ✅

### ▶️ Eclipse
1. **File → Open Projects from File System** → select this folder
2. Eclipse auto-detects all JARs via `.classpath` file
3. Right-click `Main.java` → **Run As → Java Application** ✅

### ▶️ Windows (Command Line)
```bat
run.bat
```

### ▶️ Linux / macOS
```bash
chmod +x run.sh
./run.sh
```

### ▶️ Manual Compile + Run
```bash
# Compile
javac -cp ".;mysql-connector-j-9.7.0.jar;slf4j-api-1.7.32.jar;slf4j-nop-1.7.32.jar" -sourcepath src -d out src/Main.java src/model/*.java src/util/*.java src/dao/*.java src/ui/*.java

# Run
java -cp "out;mysql-connector-j-9.7.0.jar;slf4j-api-1.7.32.jar;slf4j-nop-1.7.32.jar" Main
```

---

## 🏗️ Database Schema

### `employees` table
| Column     | Type        | Notes              |
|------------|-------------|---------------------|
| emp_id     | INT         | PK, Auto-increment |
| first_name | VARCHAR(50) | NOT NULL           |
| last_name  | VARCHAR(50) | NOT NULL           |
| email      | VARCHAR(100)| UNIQUE             |
| phone      | VARCHAR(20) |                    |
| hire_date  | DATE        | YYYY-MM-DD format  |
| job_title  | VARCHAR(100)| NOT NULL           |
| salary     | DOUBLE      | NOT NULL           |
| dept_id    | INT         | FK → departments   |

### `departments` table
| Column    | Type         | Notes            |
|-----------|--------------|------------------|
| dept_id   | INT          | PK, Auto-increment |
| dept_name | VARCHAR(100) | UNIQUE, NOT NULL |
| location  | VARCHAR(100) | NOT NULL         |

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

## 📌 Sample Data (Auto-seeded on first run)

| Name | Job Title | Department |
|------|-----------|------------|
| Aarav Sharma | Software Engineer | Engineering |
| Priya Reddy | HR Manager | Human Resources |
| Rahul Verma | Financial Analyst | Finance |
| Sneha Iyer | Marketing Lead | Marketing |
| Vikram Nair | Senior Engineer | Engineering |
