package it2a.misa.eems;

import java.sql.*;
import java.util.Scanner;

public class Artist {

    // Utility class for SQLite database connection
    public static class DBUtil {
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

        private DBUtil() {
            // Prevent instantiation
        }
    }

    // Validation utility: Ensure input contains only letters and spaces
    private static String getValidatedTextInput(Scanner sc, String prompt, String errorMessage) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = sc.nextLine();
            if (input.matches("[a-zA-Z ]+")) { // Only letters and spaces allowed
                return input;
            } else {
                System.out.println(errorMessage);
            }
        }
    }

    public static void addArtist(Scanner sc) {
        // Input validation for artist details
        String name = getValidatedTextInput(sc, "Artist Name (letters only): ",
                "Invalid input! Artist name must only contain letters and spaces.");
        String eventType = getValidatedTextInput(sc, "Event Type (letters only): ",
                "Invalid input! Event type must only contain letters and spaces.");
        String description = getValidatedTextInput(sc, "Description (letters only): ",
                "Invalid input! Description must only contain letters and spaces.");

        String sql = "INSERT INTO Artist (artist_name, event_type, description) VALUES (?, ?, ?)";

        try (Connection con = DBUtil.connectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, name);
            pst.setString(2, eventType);
            pst.setString(3, description);
            pst.executeUpdate();
            System.out.println("Artist added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding artist: " + e.getMessage());
        }
    }

    public static void updateArtist(Scanner sc) {
        System.out.print("Enter Artist ID to update: ");
        int artistId = sc.nextInt();
        sc.nextLine(); // Consume the leftover newline

        // Input validation for new artist details
        String newName = getValidatedTextInput(sc, "New Artist Name (letters only): ",
                "Invalid input! Artist name must only contain letters and spaces.");
        String newEventType = getValidatedTextInput(sc, "New Event Type (letters only): ",
                "Invalid input! Event type must only contain letters and spaces.");
        String newDescription = getValidatedTextInput(sc, "New Description (letters only): ",
                "Invalid input! Description must only contain letters and spaces.");

        String sql = "UPDATE Artist SET artist_name = ?, event_type = ?, description = ? WHERE artist_id = ?";

        try (Connection con = DBUtil.connectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, newName);
            pst.setString(2, newEventType);
            pst.setString(3, newDescription);
            pst.setInt(4, artistId);
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Artist updated successfully!");
            } else {
                System.out.println("No artist found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating artist: " + e.getMessage());
        }
    }

    public static void deleteArtist(Scanner sc) {
        System.out.print("Enter Artist ID to delete: ");
        int artistId = sc.nextInt();

        String sql = "DELETE FROM Artist WHERE artist_id = ?";

        try (Connection con = DBUtil.connectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, artistId);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Artist deleted successfully!");
            } else {
                System.out.println("No artist found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting artist: " + e.getMessage());
        }
    }

    public static void viewArtists() {
        String sql = "SELECT * FROM Artist";
        try (Connection con = DBUtil.connectDB();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            System.out.println("----------------------------------------------------------------------------");
            System.out.printf("| %-3s | %-20s | %-15s | %-30s |%n", "ID", "Name", "Event Type", "Description");
            System.out.println("----------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("| %-3d | %-20s | %-15s | %-30s |%n",
                        rs.getInt("artist_id"),
                        rs.getString("artist_name"),
                        rs.getString("event_type"),
                        rs.getString("description"));
            }
            System.out.println("----------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error viewing artists: " + e.getMessage());
        }
    }

    private Artist() {
        // Prevent instantiation
    }
}
