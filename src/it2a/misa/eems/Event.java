package it2a.misa.eems;

import java.sql.*;
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
            // Prevent instantiation
        }
    }

    // Utility method for validating text inputs (letters and spaces only)
    private static String getValidatedTextInput(Scanner sc, String prompt, String errorMessage) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = sc.nextLine();
            if (input.matches("[a-zA-Z ]+")) { // Only letters and spaces are allowed
                return input;
            } else {
                System.out.println(errorMessage);
            }
        }
    }

    public static void addEvent(Scanner sc) {
        // Event Name validation
        String name = getValidatedTextInput(sc, "Event Name (letters only): ",
                "Invalid input! Event name must only contain letters and spaces.");

        // Event Type validation (Concert/Theater only)
        String type;
        while (true) {
            System.out.print("Event Type (Concert/Theater): ");
            type = sc.nextLine();
            if (type.equalsIgnoreCase("Concert") || type.equalsIgnoreCase("Theater")) {
                break;
            } else {
                System.out.println("Invalid input! Event type must be either 'Concert' or 'Theater'.");
            }
        }

        // Event Venue validation
        String venue = getValidatedTextInput(sc, "Event Venue (letters only): ",
                "Invalid input! Venue must only contain letters and spaces.");

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

            System.out.println("-------------------------------------------------------------");
            System.out.printf("| %-3s | %-20s | %-15s | %-15s |%n", "ID", "Name", "Type", "Venue");
            System.out.println("-------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("| %-3d | %-20s | %-15s | %-15s |%n",
                        rs.getInt("event_id"),
                        rs.getString("event_name"),
                        rs.getString("event_type"),
                        rs.getString("event_venue"));
            }
            System.out.println("-------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error viewing events: " + e.getMessage());
        }
    }

    public static void updateEvent(Scanner sc) {
        System.out.print("Enter Event ID to update: ");
        int eventId = sc.nextInt();
        sc.nextLine(); // Consume the leftover newline

        // New Event Name validation
        String newName = getValidatedTextInput(sc, "New Event Name (letters only): ",
                "Invalid input! Event name must only contain letters and spaces.");

        // New Event Type validation
        String newType;
        while (true) {
            System.out.print("New Event Type (Concert/Theater): ");
            newType = sc.nextLine();
            if (newType.equalsIgnoreCase("Concert") || newType.equalsIgnoreCase("Theater")) {
                break;
            } else {
                System.out.println("Invalid input! Event type must be either 'Concert' or 'Theater'.");
            }
        }

        // New Event Venue validation
        String newVenue = getValidatedTextInput(sc, "New Event Venue (letters only): ",
                "Invalid input! Venue must only contain letters and spaces.");

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
