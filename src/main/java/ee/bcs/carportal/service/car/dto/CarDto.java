package ee.bcs.carportal.service.car.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {
    int manufacturerId;
    int fuelTypeId;
    String model;
    int year;
    BigDecimal emissions;
    int price;
}
