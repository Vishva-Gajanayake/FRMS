package lk.ijse.fleet;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    
    public static FleetManager fleetManager = new FleetManager();
    public static UserManager userManager = new UserManager();
    public static Scanner scanner = new Scanner(System.in);
    
    public static SystemUser loggedInUser = null;
    
    public static void run(){
        
        fleetManager.setRentalEventListener(agreement
                -> System.out.println("[EVENT] New rental created: " + agreement.getAgreementId())
        );
        
        Thread statusThread = new Thread(new VehicleStatusMonitor(fleetManager));
        statusThread.setDaemon(true);
        statusThread.start();
        
        Thread overdueThread = new Thread(new OverdueReminderWorker(fleetManager));
        overdueThread.setDaemon(true);
        overdueThread.start();

        showAuthMenu();
    }
    
    public static void showAuthMenu() {
        
        System.out.println("========================================================");
        System.out.println("         FLEET RENTAL MANAGEMENT SYSTEM (FRMS)");
        System.out.println("========================================================");
        System.out.println("[1] Login");
        System.out.println("[2] Register New User");
        System.out.println("[0] Exit");
        System.out.print("Enter Choice > ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                login();
                break;
            case "2":
                registerNewUser();
                break;
            case "0":
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice..!\n");
        }
    }
    
    public static void showMainMenu() {
        
        System.out.println("========================================================");
        System.out.println("FRMS MAIN MENU | Logged in as: " + loggedInUser.getFullName());
        System.out.println("========================================================");
        System.out.println("[1] Add Vehicle");
        System.out.println("[2] Add Customer");
        System.out.println("[3] View Available Vehicles");
        System.out.println("[4] Create Rental Agreement");
        System.out.println("[5] Return Vehicle");
        System.out.println("[6] View Revenue Report");
        System.out.println("[7] View Vehicles by Fuel Type");
        System.out.println("[8] View Combined Fleet & Customer Report");
        System.out.println("[9] View All Customers");
        System.out.println("[10] View All Rental Agreements");
        System.out.println("[0] Logout");
        System.out.print("Enter choice > ");
        String choice = scanner.nextLine();
        
        switch (choice) {
            case "1":
                addVehicle();
                break;
            case "2":
                addCustomer();
                break;
            case "3":
                viewAvailableVehicles();
                break;
            case "4": 
                createRentalAgreement();
                break;
            case "5":
                retrunVehicle();
                break;
            case "6":
                viewRevenueReport();
                break;
            case "7":
                viewVehicleByFuelType();
                break;
            case "8":
                fleetCustomerReport();
                break;
            case "9":
                viewAllCustomer();
                break;
            case "10":
                viewAllRentalAgreements();
                break;
            case "0":
                loggedInUser = null;
                showAuthMenu();
                break;
            default:
                System.out.println("Invalid choice..!\n");
        }
    }
   
    public static void main(String[] args) {
        
        run();
    }

    public static void addVehicle() {
        
        System.out.println("========================================================");
        System.out.println("FRMS ADD VEHICLE | Logged in as: " + loggedInUser.getFullName());
        System.out.println("========================================================");
        
        System.out.print("Enter Reg. number: ");
        String regNum = scanner.nextLine();
        System.out.print("Enter brand: ");
        String brand = scanner.nextLine();
        System.out.print("Enter model: ");
        String model = scanner.nextLine();
        System.out.print("Enter daily rate: ");
        double dailyRate = scanner.nextDouble();
        scanner.nextLine();
        
        FuelType fuelType = null;
        
        while (fuelType == null) { 
            System.out.print("Fuel Type:\n[1] Petrol\n[2] Diesal\n[3] Electric\n[4] Hybrid\nChoice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> fuelType = FuelType.PETROL;
                case "2" -> fuelType = FuelType.DIESEL;
                case "3" -> fuelType = FuelType.ELECTRIC;
                case "4" -> fuelType = FuelType.HYBRID;
                default -> System.out.println("Invalid choice..!\n");
            }
        }
        
        while (true) {          
            
            System.out.println("Vehicle Types: ");
            System.out.println("[1]. Car");
            System.out.println("[2]. Van");
            System.out.println("[3]. Truck");
            System.out.print("Choice > ");

            String c = scanner.nextLine();
            
            switch (c) {
                case "1" -> {
                    Car car = new Car(regNum, brand, model, dailyRate, fuelType);
                    fleetManager.addVehicle(car);
                    return;
                }
                case "2" -> {
                    System.out.print("Enter passenger capacity: ");
                    int passengerCapacity = scanner.nextInt();
                    scanner.nextLine();
                    Van van = new Van(passengerCapacity, regNum, brand, model, dailyRate, fuelType);
                    fleetManager.addVehicle(van);
                    return;
                }
                case "3" -> {
                    System.out.print("Enter cargo capacity(Tons): ");
                    double cargoCapacity = scanner.nextDouble();
                    scanner.nextLine();
                    Truck truck = new Truck(cargoCapacity, regNum, brand, model, dailyRate, fuelType);
                    fleetManager.addVehicle(truck);
                    return;
                }
                default -> System.out.println("Invalid choice..!\n");
            }
        }  
    }

    private static void addCustomer() {
        
        System.out.println("========================================================");
        System.out.println("FRMS ADD CUSTOMER | Logged in as: " + loggedInUser.getFullName());
        System.out.println("========================================================");
        
        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();
        System.out.print("Enter contact: ");
        String contact = scanner.nextLine();
        
        while (true) {            
            System.out.println("Customer Types: ");
            System.out.println("[1]. Individual");
            System.out.println("[2]. Corporate");
            System.out.print("Choice > ");

            String c = scanner.nextLine();

            switch (c) {
                case "1" -> {
                    System.out.print("Enter loyal points: ");
                    int loyalPoints = scanner.nextInt();
                    scanner.nextLine();
                    IndividualCustomer ic = new IndividualCustomer(fullName, contact, loyalPoints);
                    fleetManager.addCustomer(ic);
                    return;
                }
                case "2" -> {
                    System.out.print("Enter credit limit: ");
                    double cl = scanner.nextDouble();
                    scanner.nextLine();
                    CorporateCustomer cc = new CorporateCustomer(cl, fullName, contact);
                    fleetManager.addCustomer(cc);
                    return;
                }
                default -> System.out.println("Invalid choice..!\n");
            }
        }
    }

    private static void viewAllCustomer() {
        System.out.println("========================================================");
        System.out.println("FRMS VIEW CUSTOMERS | Logged in as: " + loggedInUser.getFullName());
        System.out.println("========================================================");
        if (fleetManager.getAllCustomers() != null) {
            
            for (Customer cus : fleetManager.getAllCustomers()) {
                if (cus instanceof CorporateCustomer) {
                    CorporateCustomer cc = (CorporateCustomer)cus;
                    System.out.println("Customer ID: " + cc.getCustomerId());
                    System.out.println("Full name: " + cc.getFullName());
                    System.out.println("Contact: " + cc.getContactNumber());
                    System.out.println("Credit limit: " + cc.getCreditLimit());
                    System.out.println("Outstanding balance: " + cc.getOutstandingBalance());
                    System.out.println("========================================================");
                    
                }else if(cus instanceof IndividualCustomer){
                    IndividualCustomer ic = (IndividualCustomer)cus;
                    System.out.println("Customer ID: " + ic.getCustomerId());
                    System.out.println("Full name: " + ic.getFullName());
                    System.out.println("Contact: " + ic.getContactNumber());
                    System.out.println("Credit limit: " + ic.getLoyalPoints());
                    System.out.println("========================================================");
                }
            }
        }else {
            System.out.println("No customers..!\n");
        }
        
    }

    private static void viewAllRentalAgreements() {
        System.out.println("========================================================");
        System.out.println("FRMS VIEW RENTAL AGREEMENTS | Logged in as: " + loggedInUser.getFullName());
        System.out.println("========================================================");
        if (fleetManager.getAllAgreements() != null) {
            for (RentalAgreement agreement : fleetManager.getAllAgreements()) {
                System.out.println("Agreement ID: " + agreement.getAgreementId());
                System.out.println("Customer ID: " + agreement.getCustomer().getCustomerId());
                System.out.println("Vehicle ID: " + agreement.getVehicle().getVehicleId());
                System.out.println("Rental days: " + agreement.getRentalDays());
                System.out.println("Total cost: " + agreement.getTotalCost());
                System.out.println("========================================================");
            }
        } else {
            System.out.println("No rental agreements..!\n");
        }
        
    }

    private static void displayVehicleListInfo(ArrayList<Vehicle> vehicles) {
        for (Vehicle v : vehicles) {

            System.out.println("Vehicle ID: " + v.getVehicleId());
            System.out.println("Registration number: " + v.getRegistrationNumber());
            System.out.println("Brand: " + v.getBrand());
            System.out.println("Model: " + v.getModel());
            
            if (v instanceof Car) {
                System.out.println("Type: Car");
            } else if (v instanceof Van) {
                System.out.println("Type: Van");
            } else if (v instanceof Truck) {
                System.out.println("Type: Truck");
            }
            System.out.println("========================================================");
        }
    }

    private static void viewAvailableVehicles() {
        
        System.out.println("========================================================");
        System.out.println("FRMS AVAILABLE VEHICLES | Logged in as: " + loggedInUser.getFullName());
        System.out.println("========================================================");

        ArrayList<Vehicle> availableVehicles = fleetManager.getAvailableVehicles();

        if (availableVehicles == null) {
            System.out.println("No available vehicles..!\n");
            return;
        }
        displayVehicleListInfo(availableVehicles);
    }

    private static void createRentalAgreement() {
        try {
            System.out.println("========================================================");
            System.out.println("FRMS CREATE RENTAL AGREEMENT | Logged in as: " + loggedInUser.getFullName());
            System.out.println("========================================================");

            System.out.print("Enter vehicle ID: ");
            String vehicleId = scanner.nextLine();
            System.out.print("Enter customer ID: ");
            String customerId = scanner.nextLine();
            System.out.print("Enter rental days: ");
            int days = scanner.nextInt();
            scanner.nextLine();

            RentalAgreement agreement = fleetManager.createRentalAgreement(vehicleId, customerId, days);
            if (agreement != null) {
                //
            }
        } catch (NumberFormatException | VehicleNotAvailableException | InsufficientCreditException e) {
            System.out.println("Rental failed: " + e.getMessage());
        }
    }

    private static void retrunVehicle() {
        System.out.println("========================================================");
        System.out.println("FRMS RETURN VEHICLE | Logged in as: " + loggedInUser.getFullName());
        System.out.println("========================================================");
        System.out.print("Enter agreement ID: ");
//                String agreementId = scanner.nextLine();
        fleetManager.returnVehicle(scanner.nextLine());
    }

    private static void viewRevenueReport() {
        System.out.println("========================================================");
        System.out.println("FLEET REVENUE REPORT");
        System.out.println("========================================================");
        System.out.println("Total Revenue: " + fleetManager.generateRevenueReport());
        System.out.println("--------------------------------------------------------\n");
    }

    private static void viewVehicleByFuelType() {
        System.out.println("========================================================");
        System.out.println("FRMS VIEW VEHICLES BY FUEL TYPE | Logged in as: " + loggedInUser.getFullName());
        System.out.println("========================================================");
        System.out.println("Fuel Type:\n[1] Petrol\n[2] Diesel\n[3] Electric\n[4] Hybrid\nChoice: ");
        String c = scanner.nextLine();
        switch (c) {
            case "1":
                if (fleetManager.getVehiclesByFuelType(FuelType.PETROL) == null) {
                    System.out.println("No Petrol Vehicles..!\n");
                    return;
                }
                System.out.println("===== Petrol Vehicle List =====");
                displayVehicleListInfo(fleetManager.getVehiclesByFuelType(FuelType.PETROL));
                break;
            case "2":
                if (fleetManager.getVehiclesByFuelType(FuelType.DIESEL) == null) {
                    System.out.println("No Diesel Vehicles..!\n");
                    return;
                }
                System.out.println("===== Diesel Vehicle List =====");
                displayVehicleListInfo(fleetManager.getVehiclesByFuelType(FuelType.DIESEL));
                break;
            case "3":
                if (fleetManager.getVehiclesByFuelType(FuelType.ELECTRIC) == null) {
                    System.out.println("No Electric Vehicles..!\n");
                    return;
                }
                System.out.println("===== Electric Vehicle List =====");
                displayVehicleListInfo(fleetManager.getVehiclesByFuelType(FuelType.ELECTRIC));
                break;
            case "4":
                if (fleetManager.getVehiclesByFuelType(FuelType.HYBRID) == null) {
                    System.out.println("No Hybrid Vehicles..!\n");
                    return;
                }
                System.out.println("===== Hybrid Vehicle List =====");
                displayVehicleListInfo(fleetManager.getVehiclesByFuelType(FuelType.HYBRID));
                break;
            default:
                System.out.println("Invalid fuel type..!\n");
        }
    }

    private static void fleetCustomerReport() {
        System.out.println("========================================================");
        System.out.println("FRMS COMBINED FLEET & CUSTOMER REPORT | Logged in as: " + loggedInUser.getFullName());
        System.out.println("========================================================");
        fleetManager.printAllReportables();
    }

    private static void login() {
        System.out.println("========================================================");
        System.out.println("FRMS LOGING");
        System.out.println("========================================================");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        loggedInUser = userManager.login(username, password);

        if (loggedInUser != null) {
            System.out.println("Welcome, " + loggedInUser.getFullName() + "! Loading FLeet Dashboard...");
            while (true) {
                showMainMenu();
            }
        } else {
            System.out.println("Invalid username or password\n");
            showAuthMenu();
        }
    }

    private static void registerNewUser() {
        System.out.println("========================================================");
        System.out.println("FRMS REGISTER NEW USER");
        System.out.println("========================================================");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();

        userManager.setUsers(userManager.registerUser(username, password, fullName));

        showAuthMenu();
    }
    
}
