package carrental.main;

import carrental.exception.CarNotAvailableException;
import carrental.exception.InvalidRentalException;
import carrental.model.Customer;
import carrental.model.Rental;
import carrental.service.CarRentalService;

import java.util.Scanner;

/**
 * Main Class - Car Rental Management System
 *
 * Provides the console menu-driven user interface for interacting with the Car Rental System.
 *
 * Demonstrates:
 * - Application entry point (public static void main)
 * - Scanner console input processing with defensive exception handling
 * - Clean user experience with menu loop and formatted output
 * - Handling custom checked exceptions (CarNotAvailableException, InvalidRentalException)
 * - Graceful shutdown and persistence invocation
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final CarRentalService service = new CarRentalService();

    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("       WELCOME TO CAR RENTAL MANAGEMENT SYSTEM (OOP JAVA)      ");
        System.out.println("===============================================================");

        boolean running = true;

        while (running) {
            displayMenu();
            System.out.print("Enter your choice (1-9): ");
            String input = scanner.nextLine().trim();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("\n[ERROR] Invalid input! Please enter a valid number between 1 and 9.\n");
                continue;
            }

            System.out.println();
            switch (choice) {
                case 1:
                    handleDisplayAllVehicles();
                    break;
                case 2:
                    handleDisplayAvailableVehicles();
                    break;
                case 3:
                    handleSearchVehicleById();
                    break;
                case 4:
                    handleRegisterCustomer();
                    break;
                case 5:
                    handleRentVehicle();
                    break;
                case 6:
                    handleReturnVehicle();
                    break;
                case 7:
                    handleCalculateBill();
                    break;
                case 8:
                    handleDisplayRentalHistory();
                    break;
                case 9:
                    handleSaveAndExit();
                    running = false;
                    break;
                default:
                    System.out.println("[ERROR] Invalid choice (" + choice + "). Please select an option between 1 and 9.");
            }
            System.out.println();
        }

        scanner.close();
        System.out.println("Program terminated successfully. Have a great day!");
    }

    private static void displayMenu() {
        System.out.println("---------------------------------------------------------------");
        System.out.println("                     MAIN CONTROL MENU                         ");
        System.out.println("---------------------------------------------------------------");
        System.out.println(" 1. Display all vehicles");
        System.out.println(" 2. Display available vehicles");
        System.out.println(" 3. Search for a vehicle by ID");
        System.out.println(" 4. Register a customer");
        System.out.println(" 5. Rent a vehicle");
        System.out.println(" 6. Return a rented vehicle");
        System.out.println(" 7. Calculate and display a rental bill");
        System.out.println(" 8. Display rental history");
        System.out.println(" 9. Save data and exit");
        System.out.println("---------------------------------------------------------------");
    }

    // --- Menu Action Handlers ---

    private static void handleDisplayAllVehicles() {
        System.out.println("--- FLEET OVERVIEW (ALL VEHICLES) ---");
        service.displayAllVehicles();
    }

    private static void handleDisplayAvailableVehicles() {
        System.out.println("--- CURRENTLY AVAILABLE VEHICLES FOR RENT ---");
        service.displayAvailableVehicles();
    }

    private static void handleSearchVehicleById() {
        System.out.print("Enter Vehicle ID to search (e.g., V101): ");
        String vehicleId = scanner.nextLine().trim();
        if (vehicleId.isEmpty()) {
            System.out.println("[ERROR] Vehicle ID cannot be blank.");
            return;
        }
        service.searchVehicleById(vehicleId);
    }

    private static void handleRegisterCustomer() {
        System.out.println("--- REGISTER NEW CUSTOMER ---");
        System.out.print("Enter Customer Full Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Customer Contact Number: ");
        String contact = scanner.nextLine().trim();

        try {
            Customer customer = service.registerCustomer(name, contact);
            System.out.println("\n[SUCCESS] Customer registered successfully!");
            System.out.println("Customer ID    : " + customer.getCustomerId());
            System.out.println("Name           : " + customer.getName());
            System.out.println("Contact Number : " + customer.getContactNumber());
        } catch (InvalidRentalException e) {
            System.out.println("[ERROR] Registration Failed: " + e.getMessage());
        }
    }

    private static void handleRentVehicle() {
        System.out.println("--- RENT A VEHICLE ---");
        System.out.print("Enter Registered Customer ID (e.g., C101): ");
        String customerId = scanner.nextLine().trim();

        System.out.print("Enter Vehicle ID to rent (e.g., V101): ");
        String vehicleId = scanner.nextLine().trim();

        System.out.print("Enter Rental Duration (number of days): ");
        String daysInput = scanner.nextLine().trim();
        int days;
        try {
            days = Integer.parseInt(daysInput);
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Rental days must be a positive whole number!");
            return;
        }

        System.out.print("Apply promotional discount? (y/n): ");
        String applyDiscount = scanner.nextLine().trim();
        double discountPercentage = 0.0;
        if (applyDiscount.equalsIgnoreCase("y") || applyDiscount.equalsIgnoreCase("yes")) {
            System.out.print("Enter discount percentage (0 - 100): ");
            String discInput = scanner.nextLine().trim();
            try {
                discountPercentage = Double.parseDouble(discInput);
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Invalid discount format. Rental cancelled.");
                return;
            }
        }

        try {
            Rental rental = service.rentVehicle(customerId, vehicleId, days, discountPercentage);
            System.out.println("\n[SUCCESS] Vehicle successfully rented!");
            System.out.println("Agreement Details:");
            System.out.println(" Rental ID    : " + rental.getRentalId());
            System.out.println(" Customer ID  : " + rental.getCustomerId());
            System.out.println(" Vehicle ID   : " + rental.getVehicleId());
            System.out.println(" Duration     : " + rental.getRentalDays() + " days");
            System.out.printf(" Total Bill   : Rs. %.2f%n", rental.getTotalBill());
            System.out.println(" Status       : " + rental.getStatus());
        } catch (CarNotAvailableException e) {
            System.out.println("\n[AVAILABILITY ERROR] " + e.getMessage());
        } catch (InvalidRentalException e) {
            System.out.println("\n[VALIDATION ERROR] " + e.getMessage());
        }
    }

    private static void handleReturnVehicle() {
        System.out.println("--- RETURN A RENTED VEHICLE ---");
        System.out.print("Enter Rental Agreement ID to return (e.g., R101): ");
        String rentalId = scanner.nextLine().trim();

        if (rentalId.isEmpty()) {
            System.out.println("[ERROR] Rental ID cannot be empty.");
            return;
        }

        try {
            Rental rental = service.returnRentedVehicle(rentalId);
            System.out.println("\n[SUCCESS] Vehicle returned successfully!");
            System.out.println(" Rental Agreement ID : " + rental.getRentalId());
            System.out.println(" Vehicle ID          : " + rental.getVehicleId());
            System.out.println(" Updated Status      : " + rental.getStatus());
            System.out.println("The vehicle is now marked available for new rentals.");
        } catch (InvalidRentalException e) {
            System.out.println("\n[RETURN ERROR] " + e.getMessage());
        }
    }

    private static void handleCalculateBill() {
        System.out.println("--- ESTIMATE RENTAL BILL ---");
        System.out.print("Enter Vehicle ID (e.g., V101): ");
        String vehicleId = scanner.nextLine().trim();

        System.out.print("Enter Number of Days: ");
        String daysInput = scanner.nextLine().trim();
        int days;
        try {
            days = Integer.parseInt(daysInput);
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Rental days must be a positive integer!");
            return;
        }

        System.out.print("Apply discount percentage? (y/n): ");
        String applyDisc = scanner.nextLine().trim();
        double discountPercentage = 0.0;
        if (applyDisc.equalsIgnoreCase("y") || applyDisc.equalsIgnoreCase("yes")) {
            System.out.print("Enter discount percentage (0 - 100): ");
            try {
                discountPercentage = Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Invalid discount format.");
                return;
            }
        }

        try {
            service.calculateAndDisplayBill(vehicleId, days, discountPercentage);
        } catch (InvalidRentalException e) {
            System.out.println("\n[BILLING ERROR] " + e.getMessage());
        }
    }

    private static void handleDisplayRentalHistory() {
        System.out.println("--- SYSTEM RENTAL HISTORY ---");
        service.displayRentalHistory();
    }

    private static void handleSaveAndExit() {
        System.out.println("Saving all fleet, customer, and rental records to disk...");
        boolean saved = service.saveData();
        if (saved) {
            System.out.println("[SUCCESS] All data has been saved to the 'data/' directory.");
        } else {
            System.out.println("[WARNING] Some records could not be saved to disk. Please check file permissions.");
        }
        System.out.println("Thank you for using Car Rental Management System!");
    }
}
