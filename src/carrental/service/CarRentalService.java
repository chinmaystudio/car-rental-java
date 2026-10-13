package carrental.service;

import carrental.exception.CarNotAvailableException;
import carrental.exception.InvalidRentalException;
import carrental.model.Car;
import carrental.model.Customer;
import carrental.model.Rental;
import carrental.model.SUV;
import carrental.model.Vehicle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * CarRentalService
 *
 * Core service layer managing vehicle fleet, registered customers, rental agreements,
 * billing calculations, and file-based data persistence.
 *
 * Demonstrates:
 * - Arrays of Objects: Vehicle[], Customer[], Rental[] with dynamic resizing
 * - Encapsulation: Internal data structure isolation and business logic protection
 * - Polymorphism (Method Overloading): calculateBill(vehicle, days) vs calculateBill(vehicle, days, discount)
 * - Polymorphism (Dynamic Method Dispatch): Calling calculateDailyRate() on Vehicle references
 * - Interface Usage: Manipulating Rentable through Vehicle inheritance
 * - Exception Handling: Throwing and propagating custom checked exceptions
 * - File I/O: Using FileReader, BufferedReader, FileWriter, and BufferedWriter
 */
public class CarRentalService {

    // Arrays of objects with capacity tracking
    private Vehicle[] vehicles;
    private int vehicleCount;

    private Customer[] customers;
    private int customerCount;

    private Rental[] rentals;
    private int rentalCount;

    // ID generation sequence counters
    private int nextCustomerSeq = 101;
    private int nextRentalSeq = 101;
    private int nextVehicleSeq = 101;

    // Relative file paths for persistence
    private final String dataDirPath = "data";
    private final String vehiclesFilePath = "data" + File.separator + "vehicles.txt";
    private final String customersFilePath = "data" + File.separator + "customers.txt";
    private final String rentalsFilePath = "data" + File.separator + "rentals.txt";

    /**
     * Initializes the service with initial array capacities and loads persistent data.
     */
    public CarRentalService() {
        this.vehicles = new Vehicle[50];
        this.vehicleCount = 0;

        this.customers = new Customer[50];
        this.customerCount = 0;

        this.rentals = new Rental[50];
        this.rentalCount = 0;

        // Ensure data directory exists and load existing records
        loadData();
    }

    // ==========================================
    // ARRAY CAPACITY MANAGEMENT (DYNAMIC RESIZING)
    // ==========================================

    private void ensureVehicleCapacity() {
        if (vehicleCount >= vehicles.length) {
            Vehicle[] expanded = new Vehicle[vehicles.length * 2];
            System.arraycopy(vehicles, 0, expanded, 0, vehicles.length);
            vehicles = expanded;
        }
    }

    private void ensureCustomerCapacity() {
        if (customerCount >= customers.length) {
            Customer[] expanded = new Customer[customers.length * 2];
            System.arraycopy(customers, 0, expanded, 0, customers.length);
            customers = expanded;
        }
    }

    private void ensureRentalCapacity() {
        if (rentalCount >= rentals.length) {
            Rental[] expanded = new Rental[rentals.length * 2];
            System.arraycopy(rentals, 0, expanded, 0, rentals.length);
            rentals = expanded;
        }
    }

    // ==========================================
    // POLYMORPHISM: METHOD OVERLOADING (BILLING)
    // ==========================================

    /**
     * Overloaded billing method: Calculates standard bill without any discount.
     * Demonstrates compile-time polymorphism (Method Overloading).
     *
     * @param vehicle The vehicle to rent (calls dynamic calculateDailyRate)
     * @param days    Number of rental days
     * @return Gross rental cost
     * @throws InvalidRentalException if rental duration is not positive
     */
    public double calculateBill(Vehicle vehicle, int days) throws InvalidRentalException {
        if (vehicle == null) {
            throw new InvalidRentalException("Cannot compute bill: Vehicle reference is null.");
        }
        if (days <= 0) {
            throw new InvalidRentalException("Rental duration must be at least 1 day. Provided: " + days);
        }
        // Dynamic binding: vehicle.calculateDailyRate() calls Car or SUV override at runtime
        return vehicle.calculateDailyRate() * days;
    }

    /**
     * Overloaded billing method: Calculates discounted bill with discount validation.
     * Demonstrates compile-time polymorphism (Method Overloading).
     *
     * @param vehicle            The vehicle to rent
     * @param days               Number of rental days
     * @param discountPercentage Discount percentage to apply (0.0 to 100.0)
     * @return Net total bill after discount
     * @throws InvalidRentalException if days or discount percentage are invalid
     */
    public double calculateBill(Vehicle vehicle, int days, double discountPercentage) throws InvalidRentalException {
        if (discountPercentage < 0.0 || discountPercentage > 100.0) {
            throw new InvalidRentalException("Discount percentage must be between 0% and 100%. Provided: " + discountPercentage + "%");
        }
        double baseBill = calculateBill(vehicle, days); // Calls 2-parameter overloaded method
        double discountAmount = baseBill * (discountPercentage / 100.0);
        return baseBill - discountAmount;
    }

    // ==========================================
    // VEHICLE MANAGEMENT & SEARCH
    // ==========================================

    /**
     * Adds a vehicle to the internal array.
     *
     * @param vehicle Vehicle instance to add
     */
    public void addVehicle(Vehicle vehicle) {
        if (vehicle != null) {
            ensureVehicleCapacity();
            vehicles[vehicleCount++] = vehicle;
            updateNextVehicleSeq(vehicle.getVehicleId());
        }
    }

    /**
     * Finds a vehicle by unique vehicleId (case-insensitive).
     *
     * @param vehicleId ID to search for (e.g. "V101")
     * @return Vehicle reference, or null if not found
     */
    public Vehicle findVehicleById(String vehicleId) {
        if (vehicleId == null) return null;
        for (int i = 0; i < vehicleCount; i++) {
            if (vehicles[i].getVehicleId().equalsIgnoreCase(vehicleId.trim())) {
                return vehicles[i];
            }
        }
        return null;
    }

    /**
     * Displays all vehicles in the fleet.
     */
    public void displayAllVehicles() {
        if (vehicleCount == 0) {
            System.out.println("No vehicles currently in the system fleet.");
            return;
        }
        printVehicleTableHeader();
        for (int i = 0; i < vehicleCount; i++) {
            // Polymorphic call: displayDetails uses runtime calculateDailyRate() and getVehicleType()
            vehicles[i].displayDetails();
        }
        printVehicleTableFooter();
    }

    /**
     * Displays only vehicles whose isAvailable flag is true.
     */
    public void displayAvailableVehicles() {
        int availableCount = 0;
        for (int i = 0; i < vehicleCount; i++) {
            if (vehicles[i].isAvailable()) {
                if (availableCount == 0) {
                    printVehicleTableHeader();
                }
                vehicles[i].displayDetails();
                availableCount++;
            }
        }
        if (availableCount == 0) {
            System.out.println("Currently, there are no vehicles available for rent.");
        } else {
            printVehicleTableFooter();
            System.out.println("Total Available Vehicles: " + availableCount);
        }
    }

    /**
     * Searches for a vehicle by ID and prints its detailed profile.
     *
     * @param vehicleId ID to look up
     */
    public void searchVehicleById(String vehicleId) {
        Vehicle v = findVehicleById(vehicleId);
        if (v == null) {
            System.out.println("Vehicle with ID [" + vehicleId + "] was not found in the fleet.");
        } else {
            System.out.println("\nVehicle Record Found:");
            printVehicleTableHeader();
            v.displayDetails();
            printVehicleTableFooter();
        }
    }

    // ==========================================
    // CUSTOMER REGISTRATION
    // ==========================================

    /**
     * Registers a new customer into the system.
     *
     * @param name    Customer full name
     * @param contact Contact mobile/phone number
     * @return Registered Customer instance
     * @throws InvalidRentalException if name or contact are blank
     */
    public Customer registerCustomer(String name, String contact) throws InvalidRentalException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidRentalException("Customer name cannot be empty.");
        }
        if (contact == null || contact.trim().isEmpty()) {
            throw new InvalidRentalException("Contact number cannot be empty.");
        }

        String customerId = "C" + nextCustomerSeq++;
        Customer customer = new Customer(customerId, name.trim(), contact.trim());

        ensureCustomerCapacity();
        customers[customerCount++] = customer;
        return customer;
    }

    /**
     * Finds a customer by ID.
     *
     * @param customerId ID to search for (e.g. "C101")
     * @return Customer instance or null
     */
    public Customer findCustomerById(String customerId) {
        if (customerId == null) return null;
        for (int i = 0; i < customerCount; i++) {
            if (customers[i].getCustomerId().equalsIgnoreCase(customerId.trim())) {
                return customers[i];
            }
        }
        return null;
    }

    /**
     * Displays all registered customers.
     */
    public void displayAllCustomers() {
        if (customerCount == 0) {
            System.out.println("No registered customers found.");
            return;
        }
        System.out.println("+-----------+------------------------+------------------+");
        System.out.println("| Cust ID   | Name                   | Contact Number   |");
        System.out.println("+-----------+------------------------+------------------+");
        for (int i = 0; i < customerCount; i++) {
            customers[i].displayDetails();
        }
        System.out.println("+-----------+------------------------+------------------+");
    }

    // ==========================================
    // RENTAL & RETURN WORKFLOWS
    // ==========================================

    /**
     * Rents a vehicle to a customer with validation, rate computation, and state update.
     *
     * @param customerId         Registered customer ID
     * @param vehicleId          Vehicle ID to rent
     * @param days               Rental duration (must be > 0)
     * @param discountPercentage Discount percent (0 to 100)
     * @return Newly generated Rental record
     * @throws CarNotAvailableException if vehicle does not exist or is already rented
     * @throws InvalidRentalException   if customer not found, days <= 0, or discount invalid
     */
    public Rental rentVehicle(String customerId, String vehicleId, int days, double discountPercentage)
            throws CarNotAvailableException, InvalidRentalException {

        // 1. Validate Customer existence
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            throw new InvalidRentalException("Customer ID [" + customerId + "] is not registered. Please register the customer first.");
        }

        // 2. Validate Vehicle existence
        Vehicle vehicle = findVehicleById(vehicleId);
        if (vehicle == null) {
            throw new CarNotAvailableException("Vehicle ID [" + vehicleId + "] does not exist in the fleet.");
        }

        // 3. Validate Vehicle Availability (Data Integrity Requirement 8: Failed check must not mutate state)
        if (!vehicle.isAvailable()) {
            throw new CarNotAvailableException("Vehicle [" + vehicleId + " - " + vehicle.getBrand() + " "
                    + vehicle.getModel() + "] is currently RENTED OUT and unavailable.");
        }

        // 4. Validate Rental Duration and Discount
        if (days <= 0) {
            throw new InvalidRentalException("Rental duration must be at least 1 day. Provided: " + days);
        }
        if (discountPercentage < 0.0 || discountPercentage > 100.0) {
            throw new InvalidRentalException("Discount percentage must be between 0% and 100%. Provided: " + discountPercentage + "%");
        }

        // 5. Calculate Final Bill (Polymorphic Overloaded Method)
        double totalBill = calculateBill(vehicle, days, discountPercentage);

        // 6. Update Vehicle State via Rentable Interface contract
        vehicle.rent(); // Marks isAvailable = false

        // 7. Create Rental Record
        String rentalId = "R" + nextRentalSeq++;
        Rental rental = new Rental(rentalId, customer.getCustomerId(), vehicle.getVehicleId(), days, totalBill, "ACTIVE");

        ensureRentalCapacity();
        rentals[rentalCount++] = rental;

        return rental;
    }

    /**
     * Returns an active rented vehicle, restores vehicle availability, and marks rental as RETURNED.
     * Prevents duplicate returns.
     *
     * @param rentalId ID of the rental to return (e.g. "R101")
     * @return The updated Rental record
     * @throws InvalidRentalException if rental does not exist or is already returned
     */
    public Rental returnRentedVehicle(String rentalId) throws InvalidRentalException {
        if (rentalId == null || rentalId.trim().isEmpty()) {
            throw new InvalidRentalException("Rental ID cannot be blank.");
        }

        Rental rental = findRentalById(rentalId);
        if (rental == null) {
            throw new InvalidRentalException("Rental agreement [" + rentalId + "] was not found.");
        }

        // Critical Data Integrity Requirement 3: Reject returning twice
        if ("RETURNED".equalsIgnoreCase(rental.getStatus())) {
            throw new InvalidRentalException("Rental [" + rentalId + "] has ALREADY been returned. Cannot return a rental twice.");
        }

        // Find associated vehicle
        Vehicle vehicle = findVehicleById(rental.getVehicleId());
        if (vehicle != null) {
            // Restore availability via Rentable interface contract
            vehicle.returnVehicle(); // Marks isAvailable = true
        }

        // Update rental record status
        rental.setStatus("RETURNED");
        return rental;
    }

    /**
     * Finds a rental record by ID.
     *
     * @param rentalId ID to search for
     * @return Rental instance or null
     */
    public Rental findRentalById(String rentalId) {
        if (rentalId == null) return null;
        for (int i = 0; i < rentalCount; i++) {
            if (rentals[i].getRentalId().equalsIgnoreCase(rentalId.trim())) {
                return rentals[i];
            }
        }
        return null;
    }

    /**
     * Calculates and displays an itemized quotation / bill without committing a rental.
     *
     * @param vehicleId          Vehicle ID
     * @param days               Rental duration
     * @param discountPercentage Discount percentage
     * @throws InvalidRentalException if vehicle not found or parameters invalid
     */
    public void calculateAndDisplayBill(String vehicleId, int days, double discountPercentage)
            throws InvalidRentalException {

        Vehicle vehicle = findVehicleById(vehicleId);
        if (vehicle == null) {
            throw new InvalidRentalException("Vehicle with ID [" + vehicleId + "] does not exist.");
        }
        if (days <= 0) {
            throw new InvalidRentalException("Rental duration must be at least 1 day. Provided: " + days);
        }
        if (discountPercentage < 0.0 || discountPercentage > 100.0) {
            throw new InvalidRentalException("Discount percentage must be between 0% and 100%. Provided: " + discountPercentage + "%");
        }

        double dailyRate = vehicle.calculateDailyRate();
        double grossBill = calculateBill(vehicle, days);
        double discountAmount = grossBill * (discountPercentage / 100.0);
        double netPayable = grossBill - discountAmount;

        System.out.println("\n========================================================");
        System.out.println("                 RENTAL BILL ESTIMATION                 ");
        System.out.println("========================================================");
        System.out.printf(" Vehicle ID           : %s%n", vehicle.getVehicleId());
        System.out.printf(" Vehicle Model        : %s %s (%s)%n", vehicle.getBrand(), vehicle.getModel(), vehicle.getVehicleType());
        System.out.printf(" Base Daily Rate      : Rs. %.2f%n", vehicle.getBaseDailyRate());
        System.out.printf(" Effective Daily Rate : Rs. %.2f%n", dailyRate);
        System.out.printf(" Rental Duration      : %d day(s)%n", days);
        System.out.printf(" Gross Rental Amount  : Rs. %.2f%n", grossBill);
        System.out.printf(" Discount Applied     : %.1f%% (- Rs. %.2f)%n", discountPercentage, discountAmount);
        System.out.println("--------------------------------------------------------");
        System.out.printf(" NET TOTAL PAYABLE    : Rs. %.2f%n", netPayable);
        System.out.println("========================================================");
    }

    /**
     * Displays complete rental transaction history.
     */
    public void displayRentalHistory() {
        if (rentalCount == 0) {
            System.out.println("No rental agreements recorded in the system yet.");
            return;
        }
        System.out.println("+-----------+-----------+----------+------------+---------------+------------+");
        System.out.println("| Rental ID | Cust ID   | Veh ID   | Duration   | Total Bill    | Status     |");
        System.out.println("+-----------+-----------+----------+------------+---------------+------------+");
        for (int i = 0; i < rentalCount; i++) {
            rentals[i].displayDetails();
        }
        System.out.println("+-----------+-----------+----------+------------+---------------+------------+");
    }

    // ==========================================
    // PERSISTENCE: FILE HANDLING (LOAD & SAVE)
    // ==========================================

    /**
     * Loads vehicle, customer, and rental records from persistent text files.
     * If vehicles file is missing or empty, initializes sample default vehicles.
     * Ensures vehicle availability matches active rental records upon loading.
     */
    public void loadData() {
        File dataDir = new File(dataDirPath);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        loadVehiclesFromFile();
        loadCustomersFromFile();
        loadRentalsFromFile();

        // Critical Data Integrity Check 1 & 2:
        // Synchronize vehicle availability with active rental states
        synchronizeVehicleAvailability();
    }

    private void loadVehiclesFromFile() {
        File file = new File(vehiclesFilePath);
        if (!file.exists() || file.length() == 0) {
            // First run or missing file: initialize sample fleet
            initializeSampleVehicles();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                // Format: TYPE,ID,BRAND,MODEL,BASE_RATE,IS_AVAILABLE,EXTRA
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    String type = parts[0].trim();
                    String id = parts[1].trim();
                    String brand = parts[2].trim();
                    String model = parts[3].trim();
                    double baseRate = Double.parseDouble(parts[4].trim());
                    boolean available = Boolean.parseBoolean(parts[5].trim());
                    String extra = parts[6].trim();

                    Vehicle v;
                    if ("CAR".equalsIgnoreCase(type)) {
                        v = new Car(id, brand, model, baseRate, available, extra);
                    } else if ("SUV".equalsIgnoreCase(type)) {
                        boolean awd = Boolean.parseBoolean(extra);
                        v = new SUV(id, brand, model, baseRate, available, awd);
                    } else {
                        continue; // Skip unrecognized vehicle types
                    }

                    ensureVehicleCapacity();
                    vehicles[vehicleCount++] = v;
                    updateNextVehicleSeq(id);
                }
            }
        } catch (Exception e) {
            System.err.println("[Warning] Failed reading vehicles file: " + e.getMessage());
        }

        // If after reading the file was empty or corrupted, seed sample vehicles
        if (vehicleCount == 0) {
            initializeSampleVehicles();
        }
    }

    private void loadCustomersFromFile() {
        File file = new File(customersFilePath);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                // Format: ID,NAME,CONTACT
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String contact = parts[2].trim();

                    Customer c = new Customer(id, name, contact);
                    ensureCustomerCapacity();
                    customers[customerCount++] = c;
                    updateNextCustomerSeq(id);
                }
            }
        } catch (Exception e) {
            System.err.println("[Warning] Failed reading customers file: " + e.getMessage());
        }
    }

    private void loadRentalsFromFile() {
        File file = new File(rentalsFilePath);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                // Format: RENTAL_ID,CUSTOMER_ID,VEHICLE_ID,DAYS,TOTAL_BILL,STATUS
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String rentalId = parts[0].trim();
                    String custId = parts[1].trim();
                    String vehId = parts[2].trim();
                    int days = Integer.parseInt(parts[3].trim());
                    double totalBill = Double.parseDouble(parts[4].trim());
                    String status = parts[5].trim().toUpperCase();

                    Rental r = new Rental(rentalId, custId, vehId, days, totalBill, status);
                    ensureRentalCapacity();
                    rentals[rentalCount++] = r;
                    updateNextRentalSeq(rentalId);
                }
            }
        } catch (Exception e) {
            System.err.println("[Warning] Failed reading rentals file: " + e.getMessage());
        }
    }

    /**
     * Reconciles vehicle availability based on active rentals:
     * - If an ACTIVE rental exists for vehicle V, V must be isAvailable = false.
     * - If no ACTIVE rental exists for vehicle V, V is set to isAvailable = true.
     */
    private void synchronizeVehicleAvailability() {
        // Reset all vehicles to available first
        for (int i = 0; i < vehicleCount; i++) {
            vehicles[i].setAvailable(true);
        }
        // Mark any vehicle with an ACTIVE rental as unavailable
        for (int i = 0; i < rentalCount; i++) {
            Rental r = rentals[i];
            if ("ACTIVE".equalsIgnoreCase(r.getStatus())) {
                Vehicle v = findVehicleById(r.getVehicleId());
                if (v != null) {
                    v.setAvailable(false);
                }
            }
        }
    }

    /**
     * Seeds initial sample vehicles on first run and writes them to file.
     */
    private void initializeSampleVehicles() {
        addVehicle(new Car("V101", "Honda", "City", 2200.0, true, "Sedan"));
        addVehicle(new Car("V102", "Hyundai", "i20", 1600.0, true, "Hatchback"));
        addVehicle(new Car("V103", "Skoda", "Slavia", 2500.0, true, "Sedan"));
        addVehicle(new SUV("V104", "Toyota", "Fortuner", 4500.0, true, true));  // AWD
        addVehicle(new SUV("V105", "Mahindra", "Scorpio-N", 3200.0, true, false)); // 2WD
        addVehicle(new SUV("V106", "Tata", "Safari", 3500.0, true, false));       // 2WD

        // Save immediately so file exists
        saveVehiclesToFile();
    }

    /**
     * Persists all current records to disk.
     *
     * @return true if all files saved successfully, false otherwise.
     */
    public boolean saveData() {
        boolean vSuccess = saveVehiclesToFile();
        boolean cSuccess = saveCustomersToFile();
        boolean rSuccess = saveRentalsToFile();
        return vSuccess && cSuccess && rSuccess;
    }

    private boolean saveVehiclesToFile() {
        File dataDir = new File(dataDirPath);
        if (!dataDir.exists()) dataDir.mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(vehiclesFilePath))) {
            for (int i = 0; i < vehicleCount; i++) {
                bw.write(vehicles[i].toFileString());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("[Error] Could not save vehicles to file: " + e.getMessage());
            return false;
        }
    }

    private boolean saveCustomersToFile() {
        File dataDir = new File(dataDirPath);
        if (!dataDir.exists()) dataDir.mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(customersFilePath))) {
            for (int i = 0; i < customerCount; i++) {
                bw.write(customers[i].toFileString());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("[Error] Could not save customers to file: " + e.getMessage());
            return false;
        }
    }

    private boolean saveRentalsToFile() {
        File dataDir = new File(dataDirPath);
        if (!dataDir.exists()) dataDir.mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rentalsFilePath))) {
            for (int i = 0; i < rentalCount; i++) {
                bw.write(rentals[i].toFileString());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("[Error] Could not save rentals to file: " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // SEQUENCE & ID COLLISION PREVENTION
    // ==========================================

    private void updateNextCustomerSeq(String customerId) {
        try {
            int num = Integer.parseInt(customerId.replaceAll("\\D+", ""));
            if (num >= nextCustomerSeq) {
                nextCustomerSeq = num + 1;
            }
        } catch (Exception ignored) {}
    }

    private void updateNextRentalSeq(String rentalId) {
        try {
            int num = Integer.parseInt(rentalId.replaceAll("\\D+", ""));
            if (num >= nextRentalSeq) {
                nextRentalSeq = num + 1;
            }
        } catch (Exception ignored) {}
    }

    private void updateNextVehicleSeq(String vehicleId) {
        try {
            int num = Integer.parseInt(vehicleId.replaceAll("\\D+", ""));
            if (num >= nextVehicleSeq) {
                nextVehicleSeq = num + 1;
            }
        } catch (Exception ignored) {}
    }

    // ==========================================
    // TABLE FORMATTING HELPERS
    // ==========================================

    private void printVehicleTableHeader() {
        System.out.println("+---------+--------------+----------------+------------------+---------------+--------------+");
        System.out.println("| Veh ID  | Brand        | Model          | Type / Category  | Daily Rate    | Status       |");
        System.out.println("+---------+--------------+----------------+------------------+---------------+--------------+");
    }

    private void printVehicleTableFooter() {
        System.out.println("+---------+--------------+----------------+------------------+---------------+--------------+");
    }

    // Package-private / public getters for counts (useful for testing and monitoring)
    public int getVehicleCount() { return vehicleCount; }
    public int getCustomerCount() { return customerCount; }
    public int getRentalCount() { return rentalCount; }
    public Vehicle[] getVehicles() { return vehicles; }
    public Rental[] getRentals() { return rentals; }
}
