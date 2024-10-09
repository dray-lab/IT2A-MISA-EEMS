package it2a.misa.eems;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Event {

    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:events.db");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Connection Failed: " + e.getMessage());
        }
        return con;
    }

    public static void createEventTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Event ("
                + "event_id INTEGER PRIMARY KEY AUTOINCREMENT, "
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
        createEventTable();

        while (true) {
            System.out.println("1. ADD EVENT");
            System.out.println("2. VIEW EVENT");
            System.out.println("3. UPDATE EVENT");
            System.out.println("4. EXIT");
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
        sc.nextLine(); // Clear the buffer
        String name = sc.nextLine();
        System.out.print("Event Type (e.g., Concert, Theater): ");
        String type = sc.nextLine();
        System.out.print("Event Date & Time: "); // Updated prompt
        String dateTimeString = sc.nextLine(); // Read as a string
        long dateTime = parseDate(dateTimeString); // Convert string to timestamp
        if (dateTime == -1) { // Check for parsing errors
            System.out.println("Invalid date format. Event not added.");
            return;
        }
        System.out.print("Event Venue: ");
        String venue = sc.nextLine();

        String sql = "INSERT INTO Event (event_name, event_type, event_datetime, event_venue) VALUES(?, ?, ?, ?)";

        try (Connection con = connectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {
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

    // Method to parse the date and return a timestamp
    public static long parseDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(dateString);
            return date.getTime(); // returns timestamp in milliseconds
        } catch (ParseException e) {
            System.out.println("Error parsing date: " + e.getMessage());
            return -1; // indicate an error
        }
    }

    // Method to view events
    public static void viewEvents() {
        String sqlQuery = "SELECT * FROM Event";
        String[] columnHeaders = {"Event ID", "Event Name", "Type", "Date & Time", "Venue"};
        String[] columnNames = {"event_id", "event_name", "event_type", "event_datetime", "event_venue"};

        viewRecords(sqlQuery, columnHeaders, columnNames);
    }

    // Method to display records
    public static void viewRecords(String sqlQuery, String[] columnHeaders, String[] columnNames) {
        if (columnHeaders.length != columnNames.length) {
            System.out.println("Error: Mismatch between column headers and column names.");
            return;
        }

        try (Connection conn = connectDB();
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

    // Method to update an event
    public static void updateEvent(Scanner sc) {
        System.out.print("Enter the ID of the event to update: ");
        int id = sc.nextInt();
        sc.nextLine(); // Clear the buffer

        System.out.print("Enter new Event Name: ");
        String name = sc.nextLine();
        System.out.print("Enter new Event Type: ");
        String type = sc.nextLine();
        System.out.print("Enter new Event Date & Time: "); // Updated prompt
        String dateTimeString = sc.nextLine(); // Read as a string
        long dateTime = parseDate(dateTimeString); // Convert string to timestamp
        if (dateTime == -1) { // Check for parsing errors
            System.out.println("Invalid date format. Event not updated.");
            return;
        }
        System.out.print("Enter new Event Venue: ");
        String venue = sc.nextLine();

        String qry = "UPDATE Event SET event_name = ?, event_type = ?, event_datetime = ?, event_venue = ? WHERE event_id = ?";

        try (Connection con = connectDB();
             PreparedStatement pst = con.prepareStatement(qry)) {
            pst.setString(1, name);
            pst.setString(2, type);
            pst.setLong(3, dateTime);
            pst.setString(4, venue);
            pst.setInt(5, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Event updated successfully!");
            } else {
                System.out.println("Event not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating event: " + e.getMessage());
        }
    }
}
