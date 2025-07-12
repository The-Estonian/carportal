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
    private final CarMapper carMapper;
    private final ManufacturerRepository manufacturerRepository;
    private final FuelTypeRepository fuelTypeRepository;

    public void deleteCar(Integer carId){
        if(!carRepository.existsById(carId)){
            throw new IllegalArgumentException("Car not found: " + carId);
        }
        carRepository.deleteById(carId);
    }

    public void updateCar(Integer carId,CarDto carDto){
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found: " + carId));

        carMapper.updateCar(carDto,car);

        Manufacturer m = manufacturerRepository.findById(carDto.getManufacturerId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No manufacturer: " + carDto.getManufacturerId()));

        FuelType f = fuelTypeRepository.findById(carDto.getFuelTypeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No fuelType: " + carDto.getFuelTypeId()));

        car.setManufacturer(m);
        car.setFuelType(f);

        carRepository.save(car);
    }

    public void addCar(CarDto carDto){
        Car car = carMapper.toCar(carDto);

        Manufacturer manufacturer = manufacturerRepository
                .findById(carDto.getManufacturerId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Manufacturer with ID "
                                + carDto.getManufacturerId()
                                + " not found"));


        FuelType fuelType = fuelTypeRepository
                .findById(carDto.getFuelTypeId())
                .orElseThrow(() ->
                        new IllegalArgumentException("FuelType with ID "
                                + carDto.getFuelTypeId()
                                + " not found"));

        car.setManufacturer(manufacturer);
        car.setFuelType(fuelType);

        carRepository.save(car);
    }

    public CarDetailedInfo findCarDetailedInfo(Integer carId){
        Car car =carRepository.getReferenceById(carId);
                return carMapper.toCarDetailedInfo(car);
    }

    public CarInfo findCarInfo(Integer carId){
        Car car = carRepository.getReferenceById(carId);
        return carMapper.toCarInfo(car);
    }

    public List<CarInfo> getAllCars() {
        List<Car> cars = carRepository.findAll();
        return carMapper.toCarInfos(cars);
    }

    public List<Car> findCarsInPriceRange(Integer from, Integer to) {
        return carRepository.findCarsBy(from, to);
    }

    public List<Car> findCarsInPriceRangeWithFuelType(Integer from, Integer to, String fuelTypeCode ) {
        return carRepository.findCarsBy(from, to, fuelTypeCode);
    }

}
