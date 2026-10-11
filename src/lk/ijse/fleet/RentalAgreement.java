
package lk.ijse.fleet;


public class RentalAgreement {
    
    private String agreementId;
    private Vehicle vehicle;
    private Customer customer;
    private int rentalDays;
    private double totalCost;
    private int daysElapsed;
    private static int idCounter = 1000;

    public RentalAgreement(Vehicle vehicle, Customer customer, int rentalDays) {
        this.vehicle = vehicle;
        this.customer = customer;
        this.rentalDays = rentalDays;
        double subtotal = vehicle.calculateRentalCost(rentalDays);
        this.totalCost = customer.applyDiscount(subtotal);
        agreementId = "AGR-"+ idCounter++;
        daysElapsed = 0;
    }

    public int getDaysElapsed() {
        return daysElapsed;
    }
    
    public int getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(int rentalDays) {
        this.rentalDays = rentalDays;
    }
    
    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public void setDaysElapsed(int daysElapsed) {
        this.daysElapsed = daysElapsed;
    }

    @Override
    public String toString() {
        return "================================================================\n"
                + "RENTAL AGREEMENT RECEIPT\n"
                + "================================================================\n"
                + "Agreement ID : " + getAgreementId() + "\n"
                + "Vehicle : " + getVehicle().getVehicleId() + " - " + getVehicle().getBrand() + " "+ getVehicle().getModel() + " (" + getVehicle().getClass() + ")\n"
                + "Customer : " + getCustomer().getCustomerId() + " - " + getCustomer().getFullName() + " (" + getCustomer().getClass() + ")\n"
                + "Rental Days : " + getRentalDays() + "\n"
                + "Total Cost :  " + getTotalCost() + "\n"
                + "================================================================\n"
                + "";
    }
    
}
