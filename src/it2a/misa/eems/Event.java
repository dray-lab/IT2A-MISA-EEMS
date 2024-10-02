package it2a.misa.eems;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Event {

    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:events.db");
            System.out.println("Connection Successful");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Connection Failed: " + e.getMessage());
        }
        return con;
    }

    public static void createEventTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Event ("
                + "event_name TEXT NOT NULL, "
                + "event_type TEXT NOT NULL, "
                + "event_datetime INTEGER NOT NULL, "
                + "event_venue TEXT NOT NULL"
                + ");";

        try (Connection con = connectDB();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.executeUpdate();
            System.out.println("Event table created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Ensure the Event table is created before any other operations
        createEventTable();

        while (true) {
            System.out.println("1. ADD EVENT");
            System.out.println("2. VIEW EVENT");
            System.out.println("3. UPDATE EVENT");
            System.out.println("4. DELETE EVENT");
            System.out.println("5. EXIT");
            System.out.print("Enter Action: ");
            int action = sc.nextInt();

            switch (action) {
                case 1:
                    addEvent(sc);
                    break;
                case 2:
                    viewEvents();
                    break;
                case 3:
                    updateEvent(sc);
                    break;
                case 4:
                    deleteEvent(sc);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid action. Please try again.");
            }
        }
    }

    public static void addEvent(Scanner sc) {
        System.out.print("Event Name: ");
        String name = sc.next();
        System.out.print("Event Type (e.g., Concert, Theater): ");
        String type = sc.next();
        System.out.print("Event Date & Time (timestamp): ");
        long dateTime = sc.nextLong(); // Change to long for INTEGER
        System.out.print("Event Venue: ");
        String venue = sc.next();

        String sql = "INSERT INTO Event (event_name, event_type, event_datetime, event_venue) VALUES(?, ?, ?, ?)";

        try {
            Connection con = connectDB();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, type);
            pst.setLong(3, dateTime);
            pst.setString(4, venue);
            pst.executeUpdate();
            System.out.println("Event added successfully!");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void viewEvents() {
        String sql = "SELECT * FROM Event";

        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("----------------------------------------------------------------------------");
            System.out.printf("| %-20s | %-12s | %-25s | %-15s |\n", "Event Name", "Type", "Date & Time", "Venue");
            System.out.println("----------------------------------------------------------------------------");

            while (rs.next()) {
                String name = rs.getString("event_name");
                String type = rs.getString("event_type");
                long dateTime = rs.getLong("event_datetime");
                String venue = rs.getString("event_venue");

                System.out.printf("| %-20s | %-12s | %-25s | %-15s |\n", name, type, dateTime, venue);
            }

            System.out.println("----------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error retrieving events: " + e.getMessage());
        }
}
