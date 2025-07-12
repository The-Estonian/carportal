package ee.bcs.carportal.service.car;

import ee.bcs.carportal.infrastructure.exception.DatabaseConflictException;
import ee.bcs.carportal.infrastructure.exception.ResourceNotFoundException;
import ee.bcs.carportal.persistence.car.Car;
import ee.bcs.carportal.persistence.car.CarMapper;
import ee.bcs.carportal.persistence.car.CarRepository;
import ee.bcs.carportal.persistence.fueltype.FuelType;
import ee.bcs.carportal.persistence.fueltype.FuelTypeRepository;
import ee.bcs.carportal.persistence.manufacturer.Manufacturer;
import ee.bcs.carportal.persistence.manufacturer.ManufacturerRepository;
import ee.bcs.carportal.service.car.dto.CarDto;
import ee.bcs.carportal.service.car.dto.CarInfo;
import ee.bcs.carportal.service.car.dto.CarDetailedInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final ManufacturerRepository manufacturerRepository;
    private final FuelTypeRepository fuelTypeRepository;

    public void deleteCar(Integer carId) {
        if (!carRepository.existsById(carId)) {
            throw new ResourceNotFoundException("Resource not found");
        }
        carRepository.deleteById(carId);
    }

    public void updateCar(Integer carId, CarDto carDto) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        carMapper.updateCar(carDto, car);

        Manufacturer m = manufacturerRepository.findById(carDto.getManufacturerId())
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        FuelType f = fuelTypeRepository.findById(carDto.getFuelTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        car.setManufacturer(m);
        car.setFuelType(f);

        carRepository.save(car);
    }

    public void addCar(CarDto carDto) {
        if (carRepository.carExistsBy(carDto.getManufacturerId(), carDto.getModel(), carDto.getYear())) {
            throw new DatabaseConflictException("Car already exists");
        }

        Car car = carMapper.toCar(carDto);

        Manufacturer m = manufacturerRepository.findById(carDto.getManufacturerId())
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        FuelType f = fuelTypeRepository.findById(carDto.getFuelTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        car.setManufacturer(m);
        car.setFuelType(f);

        carRepository.save(car);
    }

    public CarDetailedInfo findCarDetailedInfo(Integer carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return carMapper.toCarDetailedInfo(car);
    }

    public CarInfo findCarInfo(Integer carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return carMapper.toCarInfo(car);
    }

    public List<CarInfo> getAllCars() {
        return carMapper.toCarInfos(carRepository.findAll());
    }

    public List<Car> findCarsInPriceRange(Integer from, Integer to) {
        return carRepository.findCarsBy(from, to);
    }

    public List<Car> findCarsInPriceRangeWithFuelType(Integer from, Integer to, String fuelTypeCode) {
        return carRepository.findCarsBy(from, to, fuelTypeCode);
    }
}

