package it2a.misa.eems;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TicketSales {

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

    public static void addTicketSales(Scanner sc) {
        System.out.print("Event ID: ");
        int eventId = sc.nextInt();
        System.out.print("Silver Tickets Sold: ");
        int silver = sc.nextInt();
        System.out.print("Gold Tickets Sold: ");
        int gold = sc.nextInt();
        System.out.print("VIP Tickets Sold: ");
        int vip = sc.nextInt();

        String sql = "INSERT INTO TicketSales (event_id, silver_tickets, gold_tickets, vip_tickets) VALUES (?, ?, ?, ?)";

        try (Connection con = Config.connectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, eventId);
            pst.setInt(2, silver);
            pst.setInt(3, gold);
            pst.setInt(4, vip);
            pst.executeUpdate();
            System.out.println("Ticket sales added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding ticket sales: " + e.getMessage());
        }
    }

    public static void viewTicketSales() {
        String sql = "SELECT * FROM TicketSales";
        try (Connection con = Config.connectDB();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            System.out.println("Ticket Sales List:");
            while (rs.next()) {
                System.out.printf("ID: %d, Event ID: %d, Silver: %d, Gold: %d, VIP: %d%n",
                        rs.getInt("sale_id"), rs.getInt("event_id"),
                        rs.getInt("silver_tickets"), rs.getInt("gold_tickets"), rs.getInt("vip_tickets"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing ticket sales: " + e.getMessage());
        }
    }

    public static void updateTicketSales(Scanner sc) {
        System.out.print("Enter Sale ID to update: ");
        int saleId = sc.nextInt();
        System.out.print("New Silver Tickets Sold: ");
        int silver = sc.nextInt();
        System.out.print("New Gold Tickets Sold: ");
        int gold = sc.nextInt();
        System.out.print("New VIP Tickets Sold: ");
        int vip = sc.nextInt();

        String sql = "UPDATE TicketSales SET silver_tickets = ?, gold_tickets = ?, vip_tickets = ? WHERE sale_id = ?";

        try (Connection con = Config.connectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, silver);
            pst.setInt(2, gold);
            pst.setInt(3, vip);
            pst.setInt(4, saleId);
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Ticket sales updated successfully!");
            } else {
                System.out.println("No sales record found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating ticket sales: " + e.getMessage());
        }
    }

    public static void deleteTicketSales(Scanner sc) {
        System.out.print("Enter Sale ID to delete: ");
        int saleId = sc.nextInt();

        String sql = "DELETE FROM TicketSales WHERE sale_id = ?";

        try (Connection con = Config.connectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, saleId);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Ticket sales deleted successfully!");
            } else {
                System.out.println("No sales record found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting ticket sales: " + e.getMessage());
        }
    }

    private TicketSales() {
    }
}
