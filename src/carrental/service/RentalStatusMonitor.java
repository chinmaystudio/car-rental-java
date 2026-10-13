package carrental.service;

import carrental.model.Rental;
import carrental.model.Vehicle;

/**
 * RentalStatusMonitor (Multithreading Extension)
 *
 * Demonstrates:
 * - Multithreading via the java.lang.Runnable interface
 * - Background task execution using Thread and Thread.sleep()
 * - Independent execution: Contains its own standalone main() method for viva demo
 * - Graceful loop termination using a volatile boolean flag
 *
 * College Viva Relevance:
 * Allows a student to demonstrate the difference between single-threaded execution
 * and asynchronous background monitoring in Core Java without making the core console UI
 * dependent on it.
 */
public class RentalStatusMonitor implements Runnable {

    private final CarRentalService service;
    private volatile boolean running;
    private final int intervalSeconds;

    /**
     * Initializes the monitor with a target service and polling interval.
     *
     * @param service         The service containing fleet records
     * @param intervalSeconds Polling interval in seconds
     */
    public RentalStatusMonitor(CarRentalService service, int intervalSeconds) {
        this.service = service;
        this.intervalSeconds = intervalSeconds;
        this.running = true;
    }

    /**
     * Entry method executed when the thread is started.
     */
    @Override
    public void run() {
        System.out.println("[Background Monitor Thread Started - Thread ID: "
                + Thread.currentThread().threadId() + ", Name: " + Thread.currentThread().getName() + "]");

        int cycle = 1;
        while (running) {
            try {
                System.out.println("\n--- [Fleet Audit #" + cycle + " | Background Thread] ---");
                Vehicle[] vehicles = service.getVehicles();
                int totalVehicles = service.getVehicleCount();
                int availableCount = 0;
                int rentedCount = 0;

                for (int i = 0; i < totalVehicles; i++) {
                    if (vehicles[i].isAvailable()) {
                        availableCount++;
                    } else {
                        rentedCount++;
                    }
                }

                Rental[] rentals = service.getRentals();
                int totalRentals = service.getRentalCount();
                int activeRentals = 0;
                for (int i = 0; i < totalRentals; i++) {
                    if ("ACTIVE".equalsIgnoreCase(rentals[i].getStatus())) {
                        activeRentals++;
                    }
                }

                System.out.printf("[Audit Summary] Total Vehicles: %d | Available: %d | Rented: %d | Active Rentals: %d%n",
                        totalVehicles, availableCount, rentedCount, activeRentals);
                System.out.println("----------------------------------------------------------------");

                cycle++;
                // Sleep for specified interval (TIMED_WAITING state)
                Thread.sleep(intervalSeconds * 1000L);

            } catch (InterruptedException e) {
                System.out.println("[Monitor Thread Interrupted: Terminating safely]");
                break;
            }
        }
        System.out.println("[Background Monitor Thread Stopped]");
    }

    /**
     * Signals the background thread loop to terminate cleanly.
     */
    public void stopMonitoring() {
        this.running = false;
    }

    /**
     * Standalone main method allowing the student to demonstrate multithreading
     * independently to the examiner.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("     MULTITHREADING EXTENSION DEMO: FLEET STATUS MONITOR       ");
        System.out.println("===============================================================");
        System.out.println("Initializing CarRentalService on Main Thread...");

        CarRentalService service = new CarRentalService();

        // Create the Runnable task
        RentalStatusMonitor monitorTask = new RentalStatusMonitor(service, 3);

        // Wrap Runnable inside a Thread object
        Thread monitorThread = new Thread(monitorTask, "FleetMonitorThread");

        System.out.println("Main thread starting background monitor thread...");
        monitorThread.start();

        System.out.println("Main thread is free to perform other computations concurrently!");
        try {
            // Let the background thread run for 10 seconds (approx 3 cycles)
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted: " + e.getMessage());
        }

        System.out.println("\nMain thread requesting monitor to stop...");
        monitorTask.stopMonitoring();
        monitorThread.interrupt();

        try {
            // Wait for background thread to conclude
            monitorThread.join();
        } catch (InterruptedException e) {
            System.err.println("Join interrupted: " + e.getMessage());
        }

        System.out.println("Multithreading demonstration completed cleanly!");
    }
}
