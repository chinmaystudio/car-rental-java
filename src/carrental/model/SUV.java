package carrental.model;

/**
 * SUV Class
 *
 * Represents Sports Utility Vehicles with higher ground clearance, seating capacity,
 * and optional All-Wheel Drive (AWD/4x4). Extends the abstract Vehicle class.
 *
 * Demonstrates:
 * - Inheritance (SUV extends Vehicle)
 * - Constructor chaining using super(...)
 * - Method Overriding (calculateDailyRate incorporates AWD and heavy vehicle surcharges)
 * - Dynamic polymorphism
 */
public class SUV extends Vehicle {

    private boolean allWheelDrive; // true if 4x4 / AWD, false if standard 2WD

    /**
     * Parameterized constructor for a newly registered SUV.
     *
     * @param vehicleId     Unique vehicle identifier (e.g., "V102")
     * @param brand         Manufacturer brand (e.g., "Toyota")
     * @param model         Vehicle model (e.g., "Fortuner")
     * @param baseDailyRate Base daily rental cost
     * @param allWheelDrive True if vehicle is 4x4 / AWD equipped
     */
    public SUV(String vehicleId, String brand, String model, double baseDailyRate, boolean allWheelDrive) {
        super(vehicleId, brand, model, baseDailyRate);
        this.allWheelDrive = allWheelDrive;
    }

    /**
     * Constructor used during persistence file loading.
     *
     * @param vehicleId     Unique vehicle identifier
     * @param brand         Manufacturer brand
     * @param model         Vehicle model
     * @param baseDailyRate Base daily rental cost
     * @param isAvailable   Current availability status
     * @param allWheelDrive True if vehicle is 4x4 / AWD equipped
     */
    public SUV(String vehicleId, String brand, String model, double baseDailyRate, boolean isAvailable, boolean allWheelDrive) {
        super(vehicleId, brand, model, baseDailyRate, isAvailable);
        this.allWheelDrive = allWheelDrive;
    }

    /**
     * Calculates the effective daily rental rate for an SUV.
     * SUVs incur maintenance and capability premiums:
     * - AWD/4x4 models: +25% premium over base rate
     * - 2WD models: +15% premium over base rate
     *
     * @return Effective daily rate
     */
    @Override
    public double calculateDailyRate() {
        if (allWheelDrive) {
            return getBaseDailyRate() * 1.25;
        } else {
            return getBaseDailyRate() * 1.15;
        }
    }

    @Override
    public String getVehicleType() {
        return allWheelDrive ? "SUV (AWD 4x4)" : "SUV (2WD)";
    }

    @Override
    public String toFileString() {
        return "SUV," + getVehicleId() + "," + getBrand() + "," + getModel() + ","
                + getBaseDailyRate() + "," + isAvailable() + "," + allWheelDrive;
    }

    public boolean isAllWheelDrive() {
        return allWheelDrive;
    }

    public void setAllWheelDrive(boolean allWheelDrive) {
        this.allWheelDrive = allWheelDrive;
    }
}
