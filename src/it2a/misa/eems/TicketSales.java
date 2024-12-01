package it2a.misa.eems;

import java.sql.*;
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
            // Prevent instantiation
        }
    }

    public static void addTicketSales(Scanner sc) {
        System.out.print("Event ID: ");
        int eventId = sc.nextInt();

        // Validate ticket quantities for each category
        int silver = validateNonNegativeInput(sc, "Silver Tickets Sold");
        int gold = validateNonNegativeInput(sc, "Gold Tickets Sold");
        int vip = validateNonNegativeInput(sc, "VIP Tickets Sold");

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

            System.out.println("----------------------------------------------------------------------------------");
            System.out.printf("| %-5s | %-8s | %-15s | %-15s | %-15s |%n", "ID", "Event ID", "Silver Tickets", "Gold Tickets", "VIP Tickets");
            System.out.println("----------------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("| %-5d | %-8d | %-15d | %-15d | %-15d |%n",
                        rs.getInt("sale_id"),  // sale_id column
                        rs.getInt("event_id"),
                        rs.getInt("silver_tickets"),
                        rs.getInt("gold_tickets"),
                        rs.getInt("vip_tickets"));
            }
            System.out.println("----------------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error viewing ticket sales: " + e.getMessage());
        }
    }

    public static void updateTicketSales(Scanner sc) {
        System.out.print("Enter Sale ID to update: ");
        int saleId = sc.nextInt();

        // Validate ticket quantities for each category
        int silver = validateNonNegativeInput(sc, "New Silver Tickets Sold");
        int gold = validateNonNegativeInput(sc, "New Gold Tickets Sold");
        int vip = validateNonNegativeInput(sc, "New VIP Tickets Sold");

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

    // Helper method to validate non-negative input
    private static int validateNonNegativeInput(Scanner sc, String prompt) {
        int value;
        while (true) {
            System.out.print(prompt + ": ");
            value = sc.nextInt();
            if (value >= 0) {
                break;
            } else {
                System.out.println("Invalid input! Value must be a non-negative number.");
            }
        }
        return value;
    }

    private TicketSales() {
        // Prevent instantiation
    }
}
