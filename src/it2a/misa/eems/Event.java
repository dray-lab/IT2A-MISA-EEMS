package it2a.misa.eems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Event {

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
                        rs.getInt("event_id"), rs.getString("event_name"), rs.getString("event_type"), rs.getString("event_venue"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing events: " + e.getMessage());
        }
    }

    // Additional methods for update and delete would be similar to `addEvent` and `viewEvents`.

    static void deleteEvent(Scanner sc) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    static void updateEvent(Scanner sc) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
