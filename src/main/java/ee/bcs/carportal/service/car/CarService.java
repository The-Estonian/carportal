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
import ee.bcs.carportal.infrastructure.Error;

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
            throw new ResourceNotFoundException(Error.RESOURCE_NOT_FOUND);
        }
        carRepository.deleteById(carId);
    }

    public void updateCar(Integer carId, CarDto carDto) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException(Error.RESOURCE_NOT_FOUND));

        carMapper.updateCar(carDto, car);

        Manufacturer m = manufacturerRepository.findById(carDto.getManufacturerId())
                .orElseThrow(() -> new ResourceNotFoundException(Error.RESOURCE_NOT_FOUND));

        FuelType f = fuelTypeRepository.findById(carDto.getFuelTypeId())
                .orElseThrow(() -> new ResourceNotFoundException(Error.RESOURCE_NOT_FOUND));

        car.setManufacturer(m);
        car.setFuelType(f);

        carRepository.save(car);
    }

    public void addCar(CarDto carDto) {
        if (carRepository.carExistsBy(carDto.getManufacturerId(), carDto.getModel(), carDto.getYear())) {
            throw new DatabaseConflictException(Error.CAR_EXISTS);
        }

        Car car = carMapper.toCar(carDto);

        Manufacturer m = manufacturerRepository.findById(carDto.getManufacturerId())
                .orElseThrow(() -> new ResourceNotFoundException(Error.RESOURCE_NOT_FOUND));

        FuelType f = fuelTypeRepository.findById(carDto.getFuelTypeId())
                .orElseThrow(() -> new ResourceNotFoundException(Error.RESOURCE_NOT_FOUND));

        car.setManufacturer(m);
        car.setFuelType(f);

        carRepository.save(car);
    }

    public CarDetailedInfo findCarDetailedInfo(Integer carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException(Error.RESOURCE_NOT_FOUND));
        return carMapper.toCarDetailedInfo(car);
    }

    public CarInfo findCarInfo(Integer carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException(Error.RESOURCE_NOT_FOUND));
        return carMapper.toCarInfo(car);
    }

    public List<CarInfo> getAllCars() {
        return carMapper.toCarInfos(carRepository.findAll());
    }

    public List<CarInfo> findCarsInPriceRange(Integer from, Integer to) {
        List<Car> cars = carRepository.findCarsBy(from, to);
        return carMapper.toCarInfos(cars);
    }

    public List<CarInfo> findCarsInPriceRangeWithFuelType(Integer from, Integer to, String fuelTypeCode) {
        List<Car> cars = carRepository.findCarsBy(from, to, fuelTypeCode);
        return carMapper.toCarInfos(cars);
    }
}

