package it2a.misa.eems;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Event {

    // SQLite connection method
    public static class Config {
        public static Connection connectDB() {
            Connection con = null;
            try {
                Class.forName("org.sqlite.JDBC"); // Load the SQLite JDBC driver
                con = DriverManager.getConnection("jdbc:sqlite:events.db"); // Establish connection
                System.out.println("Connection Successful");
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Connection Failed: " + e.getMessage());
            }
            return con;
        }

        private Config() {
        }
    }

    public static void addEvent(Scanner sc) {
        System.out.print("Event Name: ");
        String name = sc.nextLine();
        System.out.print("Event Type (Concert/Theater): ");
        String type = sc.nextLine();
        System.out.print("Event Venue: ");
        String venue = sc.nextLine();

        String sql = "INSERT INTO Event (event_name, event_type, event_venue) VALUES (?, ?, ?)";

        try (Connection con = Config.connectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, name);
            pst.setString(2, type);
            pst.setString(3, venue);
            pst.executeUpdate();
            System.out.println("Event added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding event: " + e.getMessage());
        }
    }

    public static void viewEvents() {
        String sql = "SELECT * FROM Event";
        try (Connection con = Config.connectDB();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            System.out.println("Event List:");
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Type: %s, Venue: %s%n",
                        rs.getInt("event_id"), rs.getString("event_name"),
                        rs.getString("event_type"), rs.getString("event_venue"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing events: " + e.getMessage());
        }
    }

    public static void updateEvent(Scanner sc) {
        System.out.print("Enter Event ID to update: ");
        int eventId = sc.nextInt();
        sc.nextLine(); 
        System.out.print("New Event Name: ");
        String newName = sc.nextLine();
        System.out.print("New Event Type: ");
        String newType = sc.nextLine();
        System.out.print("New Event Venue: ");
        String newVenue = sc.nextLine();

        String sql = "UPDATE Event SET event_name = ?, event_type = ?, event_venue = ? WHERE event_id = ?";

        try (Connection con = Config.connectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, newName);
            pst.setString(2, newType);
            pst.setString(3, newVenue);
            pst.setInt(4, eventId);
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Event updated successfully!");
            } else {
                System.out.println("No event found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating event: " + e.getMessage());
        }
    }

    public static void deleteEvent(Scanner sc) {
        System.out.print("Enter Event ID to delete: ");
        int eventId = sc.nextInt();

        String sql = "DELETE FROM Event WHERE event_id = ?";

        try (Connection con = Config.connectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, eventId);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Event deleted successfully!");
            } else {
                System.out.println("No event found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting event: " + e.getMessage());
        }
    }

    private Event() {
        // Prevent instantiation
    }
}
