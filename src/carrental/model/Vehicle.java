package carrental.model;

/**
 * Abstract Vehicle Class
 *
 * Represents a generic vehicle and serves as the superclass for specific vehicle types
 * (e.g., Car, SUV). Implements the Rentable interface.
 *
 * Demonstrates:
 * - Abstraction: Cannot be directly instantiated; provides abstract method calculateDailyRate()
 * - Interface Implementation: Implements Rentable methods rent() and returnVehicle()
 * - Encapsulation: Private/protected member variables with public accessors and mutators
 * - Code Reusability: Common vehicle attributes (ID, brand, model, rates, status) consolidated here
 */
public abstract class Vehicle implements Rentable {

    private String vehicleId;
    private String brand;
    private String model;
    protected double baseDailyRate;
    private boolean isAvailable;

    /**
     * Constructor for creating a new vehicle (defaults availability to true).
     *
     * @param vehicleId Unique identifier for the vehicle (e.g., "V101")
     * @param brand     Manufacturer brand (e.g., "Toyota")
     * @param model     Vehicle model name (e.g., "Camry")
     * @param baseDailyRate Base daily rental cost in currency units
     */
    public Vehicle(String vehicleId, String brand, String model, double baseDailyRate) {
        this(vehicleId, brand, model, baseDailyRate, true);
    }

    /**
     * Parameterized constructor specifying explicit availability status.
     *
     * @param vehicleId Unique identifier for the vehicle
     * @param brand     Manufacturer brand
     * @param model     Vehicle model name
     * @param baseDailyRate Base daily rental cost
     * @param isAvailable Initial availability status
     */
    public Vehicle(String vehicleId, String brand, String model, double baseDailyRate, boolean isAvailable) {
        this.vehicleId = vehicleId;
        this.brand = brand;
        this.model = model;
        this.baseDailyRate = baseDailyRate;
        this.isAvailable = isAvailable;
    }

    // --- Interface Implementations (Rentable) ---

    @Override
    public void rent() {
        this.isAvailable = false;
    }

    @Override
    public void returnVehicle() {
        this.isAvailable = true;
    }

    // --- Abstract Methods (Must be overridden by subclasses) ---

    /**
     * Calculates the effective daily rental rate.
     * Subclasses apply specific rates, insurance, or category multipliers.
     *
     * @return Effective daily rental rate in currency units
     */
    public abstract double calculateDailyRate();

    /**
     * Returns a human-friendly string representing the vehicle category/type.
     *
     * @return String vehicle type description
     */
    public abstract String getVehicleType();

    /**
     * Formats the vehicle record as a delimited line for text file persistence.
     *
     * @return Delimited string representation
     */
    public abstract String toFileString();

    // --- Encapsulation: Getters and Controlled Setters ---

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getBaseDailyRate() {
        return baseDailyRate;
    }

    public void setBaseDailyRate(double baseDailyRate) {
        if (baseDailyRate > 0) {
            this.baseDailyRate = baseDailyRate;
        }
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    /**
     * Displays formatted details of the vehicle on the console.
     */
    public void displayDetails() {
        System.out.printf("| %-7s | %-12s | %-14s | %-16s | Rs.%-10.2f | %-12s |%n",
                vehicleId,
                brand,
                model,
                getVehicleType(),
                calculateDailyRate(),
                (isAvailable ? "Available" : "Rented Out"));
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %s (%s) - Rate: Rs. %.2f/day - Status: %s",
                vehicleId, brand, model, getVehicleType(), calculateDailyRate(),
                (isAvailable ? "Available" : "Rented Out"));
    }
}
