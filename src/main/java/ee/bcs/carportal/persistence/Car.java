package ee.bcs.carportal.persistence;

import java.util.Objects;

public class Car {
    private int id;
    private String carModel;
    private String manufacturer;
    private int modelYear;
    private FuelType fuelType;
    private double emissions;
    private int price;

    // Constructors
    public Car() {
    }

    public Car(int id, String carModel, String manufacturer, int modelYear, FuelType fuelType, double emissions,
            int price) {
        this.id = id;
        this.carModel = carModel;
        this.manufacturer = manufacturer;
        this.modelYear = modelYear;
        this.fuelType = fuelType;
        this.emissions = emissions;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getModelYear() {
        return modelYear;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public double getEmissions() {
        return emissions;
    }

    public void setEmissions(double emissions) {
        this.emissions = emissions;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((carModel == null) ? 0 : carModel.hashCode());
        result = prime * result + ((manufacturer == null) ? 0 : manufacturer.hashCode());
        result = prime * result + modelYear;
        result = prime * result + ((fuelType == null) ? 0 : fuelType.hashCode());
        long temp;
        temp = Double.doubleToLongBits(emissions);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + price;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Car other = (Car) obj;
        if (id != other.id)
            return false;
        if (carModel == null) {
            if (other.carModel != null)
                return false;
        } else if (!carModel.equals(other.carModel))
            return false;
        if (manufacturer == null) {
            if (other.manufacturer != null)
                return false;
        } else if (!manufacturer.equals(other.manufacturer))
            return false;
        if (modelYear != other.modelYear)
            return false;
        if (fuelType != other.fuelType)
            return false;
        if (Double.doubleToLongBits(emissions) != Double.doubleToLongBits(other.emissions))
            return false;
        if (price != other.price)
            return false;
        return true;
    }

    // toString() method
    @Override
    public String toString() {
        return "Id: " + id + "\n" +
                "Make: " + manufacturer + "\n" +
                "Model: " + carModel + "\n" +
                "Fuel type: " + fuelType + "\n" +
                "Emission: " + emissions + "\n" +
                "Price: €" + price;
    }
}
