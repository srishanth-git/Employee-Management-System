import ui.ConsoleMenu;
import util.DBConnection;
import util.DBInitializer;

/**
 * Main — Entry point for the Employee Management System.
 *
 * Compile:
 *   javac -cp lib/sqlite-jdbc-3.45.3.0.jar \
 *         -d out \
 *         src/**\/*.java src/Main.java
 *
 * Run:
 *   java -cp "out:lib/sqlite-jdbc-3.45.3.0.jar" Main
 *
 * Windows paths use semicolons:
 *   java -cp "out;lib/sqlite-jdbc-3.45.3.0.jar" Main
 */
public class Main {

    public static void main(String[] args) {

        // Step 1: Create tables & seed sample data
        DBInitializer.initialize();

        // Step 2: Launch the console UI
        ConsoleMenu menu = new ConsoleMenu();
        menu.start();

        // Step 3: Close DB connection on exit
        DBConnection.closeConnection();
    }
}
