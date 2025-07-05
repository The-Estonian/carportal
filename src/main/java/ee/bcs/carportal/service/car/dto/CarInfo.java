package ee.bcs.carportal.service.car.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarInfo {
    String make;
    String modelName;
    int releaseYear;
}
