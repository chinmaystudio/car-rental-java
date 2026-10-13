package carrental.model;

/**
 * Rental Class
 *
 * Represents an active or completed rental agreement linking a Customer to a Vehicle.
 *
 * Demonstrates:
 * - Aggregation / Association (references customerId and vehicleId)
 * - Encapsulation (private fields and state management)
 * - State tracking ("ACTIVE" vs "RETURNED")
 */
public class Rental {

    private String rentalId;
    private String customerId;
    private String vehicleId;
    private int rentalDays;
    private double totalBill;
    private String status; // "ACTIVE" or "RETURNED"

    /**
     * Parameterized constructor for a Rental record.
     *
     * @param rentalId   Unique agreement identifier (e.g., "R101")
     * @param customerId Identifier of the renting customer
     * @param vehicleId  Identifier of the rented vehicle
     * @param rentalDays Number of days rented
     * @param totalBill  Final computed billing amount
     * @param status     Rental status ("ACTIVE" or "RETURNED")
     */
    public Rental(String rentalId, String customerId, String vehicleId, int rentalDays, double totalBill, String status) {
        this.rentalId = rentalId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.rentalDays = rentalDays;
        this.totalBill = totalBill;
        this.status = status;
    }

    // --- Encapsulation: Getters and Setters ---

    public String getRentalId() {
        return rentalId;
    }

    public void setRentalId(String rentalId) {
        this.rentalId = rentalId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(int rentalDays) {
        if (rentalDays > 0) {
            this.rentalDays = rentalDays;
        }
    }

    public double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(double totalBill) {
        this.totalBill = totalBill;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Formats rental record for file persistence (CSV format).
     *
     * @return Delimited string: "rentalId,customerId,vehicleId,rentalDays,totalBill,status"
     */
    public String toFileString() {
        return rentalId + "," + customerId + "," + vehicleId + "," + rentalDays + "," + totalBill + "," + status;
    }

    /**
     * Displays formatted details in tabular format.
     */
    public void displayDetails() {
        System.out.printf("| %-9s | %-9s | %-8s | %-5d days | Rs.%-10.2f | %-10s |%n",
                rentalId, customerId, vehicleId, rentalDays, totalBill, status);
    }

    @Override
    public String toString() {
        return String.format("[Rental %s] Customer: %s | Vehicle: %s | Days: %d | Total: Rs. %.2f | Status: %s",
                rentalId, customerId, vehicleId, rentalDays, totalBill, status);
    }
}
