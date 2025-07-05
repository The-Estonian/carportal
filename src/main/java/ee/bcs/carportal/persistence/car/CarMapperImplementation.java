package ee.bcs.carportal.persistence.car;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ee.bcs.carportal.persistence.manufacturer.Manufacturer;
import ee.bcs.carportal.service.car.dto.CarInfo;

@Service
public class CarMapperImplementation {

    public List<CarInfo> toCarInfos(List<Car> cars) {
        if (cars == null) {
            return null;
        }
        List<CarInfo> returnList = new ArrayList<>();
        for (Car car : cars) {
            returnList.add(toCarInfo(car));
        }
        return returnList;
    }

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
