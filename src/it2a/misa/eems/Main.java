package it2a.misa.eems;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Config.setupTables();  // Initialize database and create tables if not already created
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Manage Events");
            System.out.println("2. Manage Artists");
            System.out.println("3. Manage Ticket Sales");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();  

            switch (choice) {
                case 1:
                    eventMenu(sc);
                    break;
                case 2:
                    artistMenu(sc);
                    break;
                case 3:
                    ticketSalesMenu(sc);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    // Menu for managing events
    private static void eventMenu(Scanner sc) {
        while (true) {
            System.out.println("\nEvent Management:");
            System.out.println("1. Add Event");
            System.out.println("2. View Events");
            System.out.println("3. Update Event");
            System.out.println("4. Delete Event");
            System.out.println("5. Return to Main Menu");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1:
                    Event.addEvent(sc);
                    break;
                case 2:
                    Event.viewEvents();
                    break;
                case 3:
                    Event.updateEvent(sc);
                    break;
                case 4:
                    Event.deleteEvent(sc);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    // Menu for managing artists
    private static void artistMenu(Scanner sc) {
        while (true) {
            System.out.println("\nArtist Management:");
            System.out.println("1. Add Artist");
            System.out.println("2. View Artists");
            System.out.println("3. Update Artist");
            System.out.println("4. Delete Artist");
            System.out.println("5. Return to Main Menu");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();  

            switch (choice) {
                case 1:
                    Artist.addArtist(sc);
                    break;
                case 2:
                    Artist.viewArtists();
                    break;
                case 3:
                    Artist.updateArtist(sc);
                    break;
                case 4:
                    Artist.deleteArtist(sc);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    // Menu for managing ticket sales
    private static void ticketSalesMenu(Scanner sc) {
        while (true) {
            System.out.println("\nTicket Sales Management:");
            System.out.println("1. Add Ticket Sales");
            System.out.println("2. View Ticket Sales");
            System.out.println("3. Update Ticket Sales");
            System.out.println("4. Delete Ticket Sales");
            System.out.println("5. Return to Main Menu");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();  

            switch (choice) {
                case 1:
                    TicketSales.addTicketSales(sc);
                    break;
                case 2:
                    TicketSales.viewTicketSales();
                    break;
                case 3:
                    TicketSales.updateTicketSales(sc);
                    break;
                case 4:
                    TicketSales.deleteTicketSales(sc);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}
