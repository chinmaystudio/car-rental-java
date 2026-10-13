package carrental.model;

/**
 * Customer Class
 *
 * Represents an individual client registered in the car rental management system.
 *
 * Demonstrates:
 * - Encapsulation (private attributes with public getters and setters)
 * - Object modeling for real-world entities
 * - Formatted console presentation
 */
public class Customer {

    private String customerId;
    private String name;
    private String contactNumber;

    /**
     * Parameterized constructor to initialize a Customer.
     *
     * @param customerId    Unique client identifier (e.g., "C101")
     * @param name          Full legal name of the customer
     * @param contactNumber Contact telephone or mobile number
     */
    public Customer(String customerId, String name, String contactNumber) {
        this.customerId = customerId;
        this.name = name;
        this.contactNumber = contactNumber;
    }

    // --- Encapsulation: Getters and Setters ---

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /**
     * Formats customer record for text file persistence (CSV format).
     *
     * @return Delimited string: "customerId,name,contactNumber"
     */
    public String toFileString() {
        return customerId + "," + name + "," + contactNumber;
    }

    /**
     * Displays formatted details in tabular format.
     */
    public void displayDetails() {
        System.out.printf("| %-9s | %-22s | %-16s |%n", customerId, name, contactNumber);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (Contact: %s)", customerId, name, contactNumber);
    }
}
