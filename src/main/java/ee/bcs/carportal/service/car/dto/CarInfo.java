package ee.bcs.carportal.service.car.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CarInfo {
    private int id;
    private String make;
    private String modelName;
    private Integer releaseYear;
}
