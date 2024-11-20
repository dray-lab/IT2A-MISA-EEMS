package it2a.misa.eems;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class TicketSales {

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

    static void viewTicketSales() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    static void updateTicketSales(Scanner sc) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    static void deleteTicketSales(Scanner sc) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
