package ee.bcs.carportal.service.car;

import ee.bcs.carportal.persistence.car.Car;
import ee.bcs.carportal.persistence.car.CarMapper;
import ee.bcs.carportal.persistence.car.CarRepository;
import ee.bcs.carportal.persistence.fueltype.FuelType;
import ee.bcs.carportal.persistence.fueltype.FuelTypeRepository;
import ee.bcs.carportal.persistence.manufacturer.Manufacturer;
import ee.bcs.carportal.persistence.manufacturer.ManufacturerRepository;
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
    private final FuelTypeRepository fuelTypeRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final CarMapper carMapper;

    public void addCar(CarDto carDto) {
        Car newCar = carMapper.toCar(carDto);
        FuelType getFuelType = fuelTypeRepository.getReferenceById(carDto.getFuelTypeId());
        Manufacturer getManufacturer = manufacturerRepository.getReferenceById(carDto.getManufacturerId());
        newCar.setFuelType(getFuelType);
        newCar.setManufacturer(getManufacturer);
        carRepository.save(newCar);
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

    public void updateCar(int carId, CarDto carDto) {
        Car currentCar = carRepository.getReferenceById(carId);
        FuelType getFuelType = fuelTypeRepository.getReferenceById(carDto.getFuelTypeId());
        Manufacturer getManufacturer = manufacturerRepository.getReferenceById(carDto.getManufacturerId());
        carMapper.updateCar(carDto, currentCar);
        currentCar.setFuelType(getFuelType);
        currentCar.setManufacturer(getManufacturer);
        carRepository.save(currentCar);
    }

    public void deleteCar(int carId) {
        Car currentCar = carRepository.getReferenceById(carId);
        carRepository.delete(currentCar);
    }

}
