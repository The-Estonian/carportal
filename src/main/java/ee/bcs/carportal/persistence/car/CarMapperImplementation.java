package ee.bcs.carportal.persistence.car;

import org.springframework.stereotype.Service;

import ee.bcs.carportal.persistence.manufacturer.Manufacturer;
import ee.bcs.carportal.service.car.dto.CarInfo;

@Service
public class CarMapperImplementation {

    public CarInfo toCarInfo(Car car) {
        if (car == null) {
            return null;
        }
        CarInfo newCarInfo = new CarInfo();
        newCarInfo.setMake(carManufacturerName(car));
        newCarInfo.setModelName(car.getModel());
        newCarInfo.setReleaseYear(car.getYear());
        return newCarInfo;
    }

    private String carManufacturerName(Car car) {
        Manufacturer manu = car.getManufacturer();
        if (manu == null) {
            return null;
        }
        return manu.getName();
    }
}
