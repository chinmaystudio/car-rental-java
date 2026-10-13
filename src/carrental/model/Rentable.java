package carrental.model;

/**
 * Rentable Interface
 *
 * Defines the contract of operations that any rentable asset in the
 * system must implement.
 *
 * Demonstrates:
 * - Interface definition in Java
 * - Contract-driven development
 * - Pure abstraction (all interface methods are implicitly public and abstract)
 */
public interface Rentable {

    /**
     * Marks the entity as rented (unavailable for subsequent rentals).
     */
    void rent();

    /**
     * Marks the entity as returned (available again for future rentals).
     */
    void returnVehicle();
}
