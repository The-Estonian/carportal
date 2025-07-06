package ee.bcs.carportal.service.car.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CarDetailedInfo extends CarInfo {
    String fuelType;
    BigDecimal emissions;
    int price;
}
