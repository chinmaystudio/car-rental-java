package carrental.exception;

/**
 * Custom checked exception thrown when a rental operation violates business rules,
 * such as non-positive rental days, invalid discount percentage, attempting to
 * return an already-returned rental, or invalid customer/vehicle identifiers.
 *
 * Demonstrates:
 * - User-defined (Custom) Exception creation in Java
 * - Checked Exception inheritance (extends Exception)
 */
public class InvalidRentalException extends Exception {

    /**
     * Constructs an InvalidRentalException with the specified error message.
     * @param message Details regarding the invalid rental operation.
     */
    public InvalidRentalException(String message) {
        super(message);
    }
}
