package it2a.misa.eems;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

class config {

    // Connection Method to SQLITE
    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC"); // Load the SQLite JDBC driver
            con = DriverManager.getConnection("jdbc:sqlite:events.db"); // Establish connection
            System.out.println("Connection Successful");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Connection Failed: " + e);
        }
        return con;
    }

    // Add event record
    public void addEvent(String name, String type, long dateTime, String venue) {
        String sql = "INSERT INTO Event (event_name, event_type, event_datetime, event_venue) VALUES(?, ?, ?, ?)";

        try (Connection conn = config.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setLong(3, dateTime);
            pstmt.setString(4, venue);

            pstmt.executeUpdate();
            System.out.println("Event added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding event: " + e.getMessage());
        }
    }

    // Dynamic view method to display records from any table
    public void viewRecords(String sqlQuery, String[] columnHeaders, String[] columnNames) {
        if (columnHeaders.length != columnNames.length) {
            System.out.println("Error: Mismatch between column headers and column names.");
            return;
        }

        try (Connection conn = config.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
             ResultSet rs = pstmt.executeQuery()) {

            // Print the headers dynamically
            StringBuilder headerLine = new StringBuilder();
            headerLine.append("--------------------------------------------------------------------------------\n| ");
            for (String header : columnHeaders) {
                headerLine.append(String.format("%-20s | ", header));
            }
            headerLine.append("\n--------------------------------------------------------------------------------");

            System.out.println(headerLine.toString());

            // Print the rows dynamically based on the provided column names
            while (rs.next()) {
                StringBuilder row = new StringBuilder("| ");
                for (String colName : columnNames) {
                    String value = rs.getString(colName);
                    if (colName.equals("event_datetime")) {
                        // Convert the timestamp back to a formatted date string
                        long timestamp = rs.getLong(colName);
                        Date date = new Date(timestamp);
                        value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date); // Formatting to readable date
                    }
                    row.append(String.format("%-20s | ", value != null ? value : ""));
                }
                System.out.println(row.toString());
            }
            System.out.println("--------------------------------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("Error retrieving records: " + e.getMessage());
        }
    }

    // Method to view events
    public void viewEvents() {
        String sqlQuery = "SELECT * FROM Event";
        String[] columnHeaders = {"Event Name", "Type", "Date & Time", "Venue"};
        String[] columnNames = {"event_name", "event_type", "event_datetime", "event_venue"};

        viewRecords(sqlQuery, columnHeaders, columnNames);
    }

    // Method to create the Event table without event_id
    public void createEventTable() {
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

    // Method to update an event
    public void updateEvent(String name, String type, long dateTime, String venue, int id) {
        String sql = "UPDATE Event SET event_name = ?, event_type = ?, event_datetime = ?, event_venue = ? WHERE rowid = ?";

        try (Connection conn = config.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setLong(3, dateTime);
            pstmt.setString(4, venue);
            pstmt.setInt(5, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Event updated successfully!");
            } else {
                System.out.println("Event not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating event: " + e.getMessage());
        }
    }

    // Method to delete an event
    public void deleteEvent(int id) {
        String sql = "DELETE FROM Event WHERE rowid = ?";

        try (Connection conn = config.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Event deleted successfully!");
            } else {
                System.out.println("Event not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting event: " + e.getMessage());
        }
    }
}
