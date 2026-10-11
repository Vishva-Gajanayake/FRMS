
package lk.ijse.fleet;

public class Van extends Vehicle {
    private int passengerCapacity;

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public void setPassengerCapacity(int passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }

    public Van(int passengerCapacity, String registrationNumber, String brand, String model, double dailyRate, FuelType fuelType) {
        super(registrationNumber, brand, model, dailyRate, fuelType);
        this.passengerCapacity = passengerCapacity;
    }
    

    @Override
    public double calculateRentalCost(int rentalDays) {
        
        double rentalCost = (getDailyRate() * rentalDays * getFuelType().getCostMultiplier()) + (getDailyRate() * 0.20);
        return rentalCost;
    }
    
    
}
