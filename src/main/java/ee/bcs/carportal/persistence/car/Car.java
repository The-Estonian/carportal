package ee.bcs.carportal.persistence.car;

import ee.bcs.carportal.persistence.FuelType;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
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

}
