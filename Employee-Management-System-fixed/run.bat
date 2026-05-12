@echo off
REM ─────────────────────────────────────────────────────────────
REM  Employee Management System — Windows Build & Run Script
REM  MySQL Version | JARs are in the project root folder
REM ─────────────────────────────────────────────────────────────
setlocal

set OUT=out
set JAR_MYSQL=mysql-connector-j-9.7.0.jar
set JAR_SLF4J_API=slf4j-api-1.7.32.jar
set JAR_SLF4J_NOP=slf4j-nop-1.7.32.jar

set CP_COMPILE=.;%JAR_MYSQL%;%JAR_SLF4J_API%;%JAR_SLF4J_NOP%
set CP_RUN=%OUT%;%JAR_MYSQL%;%JAR_SLF4J_API%;%JAR_SLF4J_NOP%

echo.
echo [1/2] Compiling...
if not exist %OUT% mkdir %OUT%

javac -cp "%CP_COMPILE%" -sourcepath src -d %OUT% ^
    src\Main.java ^
    src\model\*.java ^
    src\util\*.java ^
    src\dao\*.java ^
    src\ui\*.java

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Compilation FAILED! Check the errors above.
    pause
    exit /b 1
)

echo [2/2] Compilation successful!
echo.
echo Starting Employee Management System...
echo ─────────────────────────────────────────
echo.

java -cp "%CP_RUN%" Main

echo.
pause
endlocal
