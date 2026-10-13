# Car Rental Management System (Core Java OOP Project)

A complete, robust, menu-driven **Car Rental Management System** built with **Core Java (Java 17+)** demonstrating all fundamental Object-Oriented Programming (OOP) concepts taught in first-year and second-year BTech / BCA / MCA engineering curricula.

---

## 1. Project Objectives
- Provide a clean, robust, interactive console application for vehicle rental, return, billing, and fleet tracking.
- Demonstrate core Java OOP tenets:
  - **Classes, Objects, and Constructors**
  - **Arrays of Objects** (`Vehicle[]`, `Customer[]`, `Rental[]`) with dynamic resizing
  - **Encapsulation** (private fields, accessors, and mutators)
  - **Package Architecture** (`carrental.main`, `carrental.model`, `carrental.service`, `carrental.exception`)
  - **Abstraction** (abstract class `Vehicle` and abstract method `calculateDailyRate()`)
  - **Inheritance** (`Car extends Vehicle`, `SUV extends Vehicle`)
  - **Polymorphism**:
    - *Compile-time (Method Overloading)*: Billing calculations with and without promotional discounts
    - *Runtime (Method Overriding & Dynamic Binding)*: Subclass-specific daily rate calculations
  - **Interfaces** (`Rentable` implemented by `Vehicle`)
  - **Custom Exception Handling** (`CarNotAvailableException`, `InvalidRentalException`)
  - **File Handling & Persistence** (`BufferedReader`, `BufferedWriter`, `FileReader`, `FileWriter`)
  - **Multithreading Extension** (standalone `RentalStatusMonitor` implementing `Runnable`)

---

## 2. Directory Structure

```
car-rental-java/
├── bin/                             # Compiled Java bytecodes (.class files)
├── data/                            # Persistent flat-file records
│   ├── vehicles.txt                 # Fleet data (Cars & SUVs)
│   ├── customers.txt                # Customer registration records
│   └── rentals.txt                  # Rental agreements & historical ledger
├── src/
│   └── carrental/
│       ├── exception/
│       │   ├── CarNotAvailableException.java   # Checked exception for availability conflicts
│       │   └── InvalidRentalException.java     # Checked exception for business validation failures
│       ├── main/
│       │   └── Main.java                       # Interactive console UI entry point
│       ├── model/
│       │   ├── Rentable.java                   # Contract interface for rental behaviors
│       │   ├── Vehicle.java                    # Abstract superclass
│       │   ├── Car.java                        # Concrete subclass (Sedan/Hatchback)
│       │   ├── SUV.java                        # Concrete subclass (AWD/2WD)
│       │   ├── Customer.java                   # Customer entity
│       │   └── Rental.java                     # Rental agreement record
│       └── service/
│           ├── CarRentalService.java           # Array management, billing & persistence logic
│           └── RentalStatusMonitor.java        # Optional multithreading background monitor
└── README.md                        # Documentation and viva preparation manual
```

---

## 3. How to Compile and Run (Windows PowerShell / CMD / VS Code)

### Prerequisites
- JDK 17 or higher installed (e.g., Oracle JDK or OpenJDK).
- Verify installation:
  ```powershell
  javac -version
  java -version
  ```

### Step 1: Navigate to Workspace Root
```powershell
cd "e:\JAVA project\car-rental-java"
```

### Step 2: Compile All Source Files
Compile all packages into the `bin/` directory:
```powershell
javac -d bin -sourcepath src src/carrental/model/*.java src/carrental/exception/*.java src/carrental/service/*.java src/carrental/main/*.java
```

### Step 3: Run the Main Console Application
```powershell
java -cp bin carrental.main.Main
```

### Step 4: Run the Standalone Multithreading Demonstration (Optional)
To demonstrate multithreading to an examiner:
```powershell
java -cp bin carrental.service.RentalStatusMonitor
```

---

## 4. Main Menu Capabilities

```
---------------------------------------------------------------
                     MAIN CONTROL MENU                         
---------------------------------------------------------------
 1. Display all vehicles
 2. Display available vehicles
 3. Search for a vehicle by ID
 4. Register a customer
 5. Rent a vehicle
 6. Return a rented vehicle
 7. Calculate and display a rental bill
 8. Display rental history
 9. Save data and exit
---------------------------------------------------------------
```

---

## 5. Persistence File Specifications

All files reside in the `data/` directory and use standard CSV formatting:

1. **`data/vehicles.txt`**:
   `TYPE,ID,BRAND,MODEL,BASE_RATE,IS_AVAILABLE,EXTRA`
   - *Car*: `CAR,V101,Honda,City,2200.0,true,Sedan`
   - *SUV*: `SUV,V104,Toyota,Fortuner,4500.0,true,true` (where the last field indicates AWD status)

2. **`data/customers.txt`**:
   `CUSTOMER_ID,NAME,CONTACT_NUMBER`
   - Example: `C101,Rahul Verma,9876543210`

3. **`data/rentals.txt`**:
   `RENTAL_ID,CUSTOMER_ID,VEHICLE_ID,DAYS,TOTAL_BILL,STATUS`
   - Example: `R101,C101,V101,3,5940.0,RETURNED`

---

## 6. College Viva Quick Reference

| Concept | Demonstrated In | Key Method / Feature |
| :--- | :--- | :--- |
| **Abstraction** | `Vehicle.java` | Abstract method `public abstract double calculateDailyRate();` |
| **Interface** | `Rentable.java` | Methods `rent()` and `returnVehicle()` implemented in `Vehicle` |
| **Inheritance** | `Car.java`, `SUV.java` | `extends Vehicle`, calls `super(...)` |
| **Method Overriding** | `Car.java`, `SUV.java` | Different implementations of `calculateDailyRate()` |
| **Method Overloading** | `CarRentalService.java`| `calculateBill(vehicle, days)` vs `calculateBill(vehicle, days, discount)` |
| **Arrays of Objects** | `CarRentalService.java`| `Vehicle[]`, `Customer[]`, `Rental[]` with dynamic doubling |
| **Custom Exceptions** | `carrental.exception` | `CarNotAvailableException`, `InvalidRentalException` (checked) |
| **File I/O** | `CarRentalService.java`| `BufferedReader`, `BufferedWriter`, `FileReader`, `FileWriter` |
| **Multithreading** | `RentalStatusMonitor.java` | `implements Runnable`, `Thread.sleep()`, daemon loop |
