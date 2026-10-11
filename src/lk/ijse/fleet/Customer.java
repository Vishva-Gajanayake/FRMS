
package lk.ijse.fleet;

public abstract class Customer implements Reportable {
    
    private String customerId;
    private String fullName;
    private String contactNumber;
    private static int idCounter = 1000;

    public Customer(String fullName, String contactNumber) {
        this.customerId = "CUS-" + idCounter++;
        this.fullName = fullName;
        this.contactNumber = contactNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    
    public abstract double applyDiscount(double subtotal);
    
    @Override
    public String getSummary(){
        return "Customer ID: " + getCustomerId() + ", Name: " + getFullName();
    }
    
    @Override
    public String getReportCategory(){
        return "CUSTOMER";
    }

    @Override
    public String toString() {
        return "Customer{" + "customerId=" + customerId + ", fullName=" + fullName + ", contactNumber=" + contactNumber + '}';
    }
    
}
