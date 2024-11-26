    package it2a.misa.eems;

    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.Scanner;

    public class Artist {

        // Utility class for SQLite database connection
        public static class DBUtil {
            public static Connection connectDB() {
                Connection con = null;
                try {
                    Class.forName("org.sqlite.JDBC"); // Load the SQLite JDBC driver
                    con = DriverManager.getConnection("jdbc:sqlite:voters.db"); // Establish connection
                    System.out.println("Connection Successful");
                } catch (Exception e) {
                    System.out.println("Connection Failed: " + e.getMessage());
                }
                return con;
            }

            private DBUtil() {
                // Prevent instantiation
            }
        }

        public static void addArtist(Scanner sc) {
            System.out.print("Artist Name: ");
            String name = sc.nextLine();
            System.out.print("Event Type: ");
            String eventType = sc.nextLine();
            System.out.print("Description: ");
            String description = sc.nextLine();

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

        public static void viewArtists() {
            String sql = "SELECT * FROM Artist";
            try (Connection con = DBUtil.connectDB();
                 PreparedStatement pst = con.prepareStatement(sql);
                 ResultSet rs = pst.executeQuery()) {
                System.out.println("Artist List:");
                while (rs.next()) {
                    System.out.printf("ID: %d, Name: %s, Event Type: %s, Description: %s%n",
                            rs.getInt("artist_id"), rs.getString("artist_name"),
                            rs.getString("event_type"), rs.getString("description"));
                }
            } catch (SQLException e) {
                System.out.println("Error viewing artists: " + e.getMessage());
            }
        }

        public static void updateArtist(Scanner sc) {
            System.out.print("Enter Artist ID to update: ");
            int artistId = sc.nextInt();
            sc.nextLine(); 
            System.out.print("New Artist Name: ");
            String newName = sc.nextLine();
            System.out.print("New Event Type: ");
            String newEventType = sc.nextLine();
            System.out.print("New Description: ");
            String newDescription = sc.nextLine();

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

        private Artist() {
            // Prevent instantiation
        }
    }
