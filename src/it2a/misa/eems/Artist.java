package it2a.misa.eems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

public class Artist {

    public static void addArtist(Scanner sc) {
        System.out.print("Artist Name: ");
        String name = sc.nextLine();
        System.out.print("Event Type: ");
        String eventType = sc.nextLine();
        System.out.print("Description: ");
        String description = sc.nextLine();

        String sql = "INSERT INTO Artist (artist_name, event_type, description) VALUES (?, ?, ?)";

        try (Connection con = Config.connectDB();
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
        try (Connection con = Config.connectDB();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            System.out.println("Artist List:");
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Event Type: %s, Description: %s%n",
                        rs.getInt("artist_id"), rs.getString("artist_name"), rs.getString("event_type"), rs.getString("description"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing artists: " + e.getMessage());
        }
    }

    private Artist() {
    }
}
