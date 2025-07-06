package ee.bcs.carportal.persistence.car;

import ee.bcs.carportal.service.car.dto.CarInfo;
import org.springframework.stereotype.Service;

@Service
public class CarMapperImpl {
    public CarInfo toCarInfo(Car car){
        if(car==null){
            return null;
        }
        CarInfo dto =new CarInfo();
        dto.setMake(carManufacturerName(car));
        dto.setModelName(car.getModel());
        dto.setReleaseYear(car.getYear());
        return dto;
    }
        //Helper to extract manufacturer name safely
    private String carManufacturerName(Car car){
        if (car==null){
            return null;
        }
        if (car.getManufacturer() == null) {
            return null;
    }
    return car.getManufacturer().getName();
    }
}
