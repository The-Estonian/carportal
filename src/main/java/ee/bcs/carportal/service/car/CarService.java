package ee.bcs.carportal.service.car;

import ee.bcs.carportal.persistence.car.Car;
import ee.bcs.carportal.persistence.car.CarMapperImplementation;
import ee.bcs.carportal.repository.car.CarRepository;
import ee.bcs.carportal.service.car.dto.CarInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final CarMapperImplementation carMapper;

    public CarInfo findCarInfo(int carId) {
        Car findCar = carRepository.getReferenceById(carId);
        return carMapper.toCarInfo(findCar);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<Car> findCarsInPriceRange(Integer from, Integer to) {
        return carRepository.findCarsBy(from, to);
    }

    public List<Car> findCarsInPriceRangeWithFuelType(Integer from, Integer to, String fuelTypeCode) {
        return carRepository.findCarsBy(from, to, fuelTypeCode);
    }

}
