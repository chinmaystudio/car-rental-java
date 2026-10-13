package carrental.model;

/**
 * Car Class
 *
 * Represents standard passenger cars (e.g., Sedan, Hatchback, Compact).
 * Extends the abstract Vehicle class.
 *
 * Demonstrates:
 * - Inheritance (Car extends Vehicle)
 * - Constructor chaining using super(...)
 * - Method Overriding (calculateDailyRate, getVehicleType, toFileString)
 * - Polymorphism at runtime
 */
public class Car extends Vehicle {

    private String carType; // e.g., Sedan, Hatchback, Coupe

    /**
     * Parameterized constructor for a newly registered Car.
     *
     * @param vehicleId Unique vehicle identifier (e.g., "V101")
     * @param brand     Manufacturer brand (e.g., "Honda")
     * @param model     Vehicle model (e.g., "City")
     * @param baseDailyRate Base daily rental cost
     * @param carType   Sub-body type (e.g., "Sedan", "Hatchback")
     */
    public Car(String vehicleId, String brand, String model, double baseDailyRate, String carType) {
        super(vehicleId, brand, model, baseDailyRate);
        this.carType = carType;
    }

    /**
     * Constructor used during persistence file loading.
     *
     * @param vehicleId Unique vehicle identifier
     * @param brand     Manufacturer brand
     * @param model     Vehicle model
     * @param baseDailyRate Base daily rental cost
     * @param isAvailable Current availability status
     * @param carType   Sub-body type
     */
    public Car(String vehicleId, String brand, String model, double baseDailyRate, boolean isAvailable, String carType) {
        super(vehicleId, brand, model, baseDailyRate, isAvailable);
        this.carType = carType;
    }

    /**
     * Calculates the effective daily rate for a Car.
     * Standard passenger cars rent at their exact base rate without heavy vehicle surcharges.
     *
     * @return Effective daily rental rate
     */
    @Override
    public double calculateDailyRate() {
        return getBaseDailyRate();
    }

    @Override
    public String getVehicleType() {
        return "Car (" + carType + ")";
    }

    @Override
    public String toFileString() {
        return "CAR," + getVehicleId() + "," + getBrand() + "," + getModel() + ","
                + getBaseDailyRate() + "," + isAvailable() + "," + carType;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }
}
