package ee.bcs.carportal.service.car.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {
    @NotNull
    private Integer manufacturerId;
    private Integer fuelTypeId;
    private String model;
    private Integer year;
    private BigDecimal emissions;
    private Integer price;

}
