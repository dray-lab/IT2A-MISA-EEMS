package it2a.misa.eems;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        createEventTable();
        createArtistTable();
        createTicketSalesTable();
    }

    private static void createEventTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Event ("
                + "event_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "event_name TEXT NOT NULL, "
                + "event_type TEXT NOT NULL, "
                + "event_venue TEXT NOT NULL"
                + ");";
        executeSQL(sql, "Event table created successfully!");
    }

    private static void createArtistTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Artist ("
                + "artist_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "artist_name TEXT NOT NULL, "
                + "event_type TEXT NOT NULL, "
                + "description TEXT"
                + ");";
        executeSQL(sql, "Artist table created successfully!");
    }

    private static void createTicketSalesTable() {
        String sql = "CREATE TABLE IF NOT EXISTS TicketSales ("
                + "ticket_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "event_id INTEGER NOT NULL, "
                + "silver_tickets INTEGER DEFAULT 0, "
                + "gold_tickets INTEGER DEFAULT 0, "
                + "vip_tickets INTEGER DEFAULT 0, "
                + "FOREIGN KEY (event_id) REFERENCES Event(event_id)"
                + ");";
        executeSQL(sql, "Ticket Sales table created successfully!");
    }

    private static void executeSQL(String sql, String successMessage) {
        try (Connection con = connectDB();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.executeUpdate();
            System.out.println(successMessage);
        } catch (SQLException e) {
            System.out.println("Error executing SQL: " + e.getMessage());
        }
    }

    private Config() {
    }
}
