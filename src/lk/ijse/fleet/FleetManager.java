package lk.ijse.fleet;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class FleetManager {
    
    private ArrayList<Vehicle> vehicles = new ArrayList<>();
    private ArrayList<Customer> customers = new ArrayList<>();
    private ArrayList<RentalAgreement> agreements = new ArrayList<>();
    private RentalEventListener rentalEventListener;
    
    
    public void addVehicle(Vehicle v){
        if (v != null) {
            vehicles.add(v);
            System.out.println("Vehicle added sucessfully..!");
        }
    }
    
    public void addCustomer(Customer c){
        if (c != null) {
            customers.add(c);
            System.out.println("Customer added sucessfully..!");
        }
    }
    
    public Vehicle findVehicleById(String id) {
        
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVehicleId().equals(id)) {
                return vehicle;
            }
        }
        System.out.println("Vehicle not found..!");
        return null;
    }
    public Customer findCustomerById(String id) {
        
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(id)) {
                return customer;
            }
        }
        System.out.println("Customer not found..!");
        return null;
    }
    
    public void setRentalEventListener(RentalEventListener listener){
        this.rentalEventListener = listener;
    }
    
    public RentalEventListener getRentalEventListener(){
        return rentalEventListener;
    }

    public ArrayList<Vehicle> getAvailableVehicles() {
    
//        ArrayList<Vehicle> availableVehicles = new ArrayList<>();
//        for (Vehicle vehicle : vehicles) {
//            if (vehicle.getStatus().equals(RentalStatus.AVAILABLE)) {
//                availabVehicles.add(vehicle);
//            }
//        }
        
        return (ArrayList<Vehicle>) vehicles.stream()
                .filter(v -> v.getStatus() == RentalStatus.AVAILABLE)
                .collect(Collectors.toList());
        
    }
    
    public RentalAgreement createRentalAgreement(String vehicleId, String customerId, int rentalDays) throws VehicleNotAvailableException, InsufficientCreditException{
        
        Vehicle vehicle = findVehicleById(vehicleId);
        
        if (vehicle == null) {
            return null;
        }
        
        if (vehicle.getStatus() != RentalStatus.AVAILABLE) {
            throw new VehicleNotAvailableException("Vehicle not available");
        }
        
        Customer customer = findCustomerById(customerId);
        
        if (customer == null) {
            return null;
        }
        
        RentalAgreement agreement = new RentalAgreement(vehicle, customer, rentalDays);
        
        double cost = agreement.getTotalCost();
        
        if (customer instanceof CorporateCustomer ) {
            if (!((CorporateCustomer) customer).hasSufficientCredit(cost)) {
                throw new InsufficientCreditException("Insufficient credits..!");
            }
        }
        
        agreement.setVehicle(vehicle);
        agreement.setCustomer(customer);
        agreement.setRentalDays(rentalDays);
        vehicle.setStatus(RentalStatus.RENTED);
        
        agreements.add(agreement);
        
        if (getRentalEventListener() != null) {
            rentalEventListener.onRentalCreated(agreement);
        }
        
        return agreement;
    }
    
    public void returnVehicle(String agreementId){
    
        for (RentalAgreement agreement : agreements) {
            if (agreement.getAgreementId().equals(agreementId)) {
                agreement.getVehicle().setStatus(RentalStatus.AVAILABLE);
                System.out.println("Vehicle returned sucessfully..!");
                return;
            }
        }
        System.out.println("Agreement not found..!");
    }
    
    public FleetSnapshot generateSnapshot(){
        return new FleetSnapshot();
    }
    
    synchronized public void printFleetStatus(){
        generateSnapshot();//printSummary();
    }
    
    synchronized public void checkOverdueAgreements(){
        for (RentalAgreement agreement : agreements) {
            agreement.setDaysElapsed(agreement.getDaysElapsed() + 1);
            
            if (agreement.getDaysElapsed() > agreement.getRentalDays()) {
                System.out.println("[OVERDUE ALERT] Agreemenet " + agreement.getAgreementId() + " has exceeded its " + agreement.getRentalDays() + "-day rental period.");
            }
        }
    }
    
    public ArrayList<Vehicle> getVehiclesByFuelType(FuelType type) {
        
        return (ArrayList<Vehicle>) vehicles.stream()
                .filter(v -> v.getFuelType() == type)
                .collect(Collectors.toList());
        
    }
    
    public ArrayList<Customer> getCustomersSortedByName() {
        
        return (ArrayList<Customer>) customers.stream()
                .sorted(Comparator.comparing(c -> c.getFullName()))
                .collect(Collectors.toList());

    }
    
    public double generateRevenueReport() {
    
        return agreements.stream()
                .mapToDouble(a -> a.getTotalCost())
                .sum();
        
    }
    
    public void printAllReportables() {
        
        ArrayList<Reportable> reportables = new ArrayList<>();
        reportables.addAll(vehicles);
        reportables.addAll(customers);
        for (Reportable item : reportables) {
            System.out.println(item.getReportCategory() + ": " + item.getSummary());
        }

    }
    
    public ArrayList<Customer> getAllCustomers() {
        return customers;
    }
    
    public ArrayList<RentalAgreement> getAllAgreements() {
        return agreements;
    }
    
    class FleetSnapshot {
        private int availableCount;
        private int rentedCount;
        private double totalRevenue;

        public int getAvailableCount() {
            return availableCount;
        }

        public void setAvailableCount(int availableCount) {
            this.availableCount = availableCount;
        }

        public int getRentedCount() {
            return rentedCount;
        }

        public void setRentedCount(int rentedCount) {
            this.rentedCount = rentedCount;
        }

        public double getTotalRevenue() {
            return totalRevenue;
        }

        public void setTotalRevenue(double totalRevenue) {
            this.totalRevenue = totalRevenue;
        }
        
        FleetSnapshot(){
            for (Vehicle vehicle : vehicles) {
                if (vehicle.getStatus() == RentalStatus.AVAILABLE) {
                    availableCount++;
                } else if (vehicle.getStatus() == RentalStatus.RENTED) {
                    rentedCount++;
                }
            }

            for (RentalAgreement agreement : agreements) {
                totalRevenue += agreement.getTotalCost();
            }
        }
        
        public void printSummary(){
            System.out.println("[FLEET STATUS] Available: "+ getAvailableCount() +" Rented: "+getRentedCount()+" Total Revenue So Far: "+ getTotalRevenue());
        }
    }
}
