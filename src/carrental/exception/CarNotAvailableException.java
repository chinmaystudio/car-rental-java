package carrental.exception;

/**
 * Custom checked exception thrown when an operation requires an available vehicle,
 * but the requested vehicle is already rented out or does not exist.
 *
 * Demonstrates:
 * - User-defined (Custom) Exception creation in Java
 * - Checked Exception inheritance (extends Exception)
 */
public class CarNotAvailableException extends Exception {

    /**
     * Constructs a CarNotAvailableException with the specified error message.
     * @param message Details about why the vehicle is unavailable.
     */
    public CarNotAvailableException(String message) {
        super(message);
    }
}
