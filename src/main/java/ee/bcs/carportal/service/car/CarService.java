package ee.bcs.carportal.service.car;

import ee.bcs.carportal.persistence.car.Car;
import ee.bcs.carportal.persistence.car.CarMapper;
import ee.bcs.carportal.persistence.car.CarRepository;
import ee.bcs.carportal.service.car.dto.CarDetailedInfo;
import ee.bcs.carportal.service.car.dto.CarDto;
import ee.bcs.carportal.service.car.dto.CarInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    public void addCar(CarDto carDto) {
        carRepository.save(carMapper.toCar(carDto));
    };

    public CarInfo findCarInfo(int carId) {
        Car findCar = carRepository.getReferenceById(carId);
        return carMapper.toCarInfo(findCar);
    }

    public CarDetailedInfo findCarDetailedInfo(int carId) {
        Car findCar = carRepository.getReferenceById(carId);
        CarDetailedInfo convertedCar = carMapper.toCarDetailedInfo(findCar);
        return convertedCar;
    }

    public List<CarInfo> getAllCars() {
        List<Car> allCars = carRepository.findAll();
        return carMapper.toCarInfos(allCars);
    }

    public List<Car> findCarsInPriceRange(Integer from, Integer to) {
        return carRepository.findCarsBy(from, to);
    }

    public List<Car> findCarsInPriceRangeWithFuelType(Integer from, Integer to, String fuelTypeCode) {
        return carRepository.findCarsBy(from, to, fuelTypeCode);
    }

}
