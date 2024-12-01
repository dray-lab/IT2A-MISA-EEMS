package it2a.misa.eems;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Config {

    // Connection Method to SQLite
    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:events.db");
            System.out.println("Connection Successful");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Connection Failed: " + e);
        }
        return con;
    }

    public static void setupTables() {
        try (Connection con = connectDB()) {
            createEventTable(con);
            createArtistTable(con);
            createTicketSalesTable(con);
        } catch (SQLException e) {
            System.out.println("Error during setup: " + e.getMessage());
        }
    }

    private static void createEventTable(Connection con) {
        String sql = "CREATE TABLE IF NOT EXISTS Event ("
                + "event_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "event_name TEXT NOT NULL, "
                + "event_type TEXT NOT NULL, "
                + "event_venue TEXT NOT NULL"
                + ");";
        executeSQL(con, sql, "Event table created successfully!");
    }

    private static void createArtistTable(Connection con) {
        String sql = "CREATE TABLE IF NOT EXISTS Artist ("
                + "artist_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "artist_name TEXT NOT NULL, "
                + "event_type TEXT NOT NULL, "
                + "description TEXT"
                + ");";
        executeSQL(con, sql, "Artist table created successfully!");
    }

    private static void createTicketSalesTable(Connection con) {
        String sql = "CREATE TABLE IF NOT EXISTS TicketSales ("
                + "sale_id INTEGER PRIMARY KEY AUTOINCREMENT, " // sale_id as the primary key
                + "event_id INTEGER NOT NULL, "
                + "silver_tickets INTEGER DEFAULT 0, "
                + "gold_tickets INTEGER DEFAULT 0, "
                + "vip_tickets INTEGER DEFAULT 0, "
                + "FOREIGN KEY (event_id) REFERENCES Event(event_id)"
                + ");";
        executeSQL(con, sql, "Ticket Sales table created successfully!");
    }

    // Helper method to execute SQL commands
    private static void executeSQL(Connection con, String sql, String successMessage) {
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(sql);  // Execute the SQL statement
            System.out.println(successMessage);  // Print success message if executed correctly
        } catch (SQLException e) {
            System.out.println("Error executing SQL: " + e.getMessage());  // Handle errors
        }
    }

    private Config() {
        // Prevent instantiation
    }
}
