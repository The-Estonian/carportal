package ee.bcs.carportal.service.car;

import ee.bcs.carportal.persistence.FuelType;
import ee.bcs.carportal.persistence.car.Car;
import ee.bcs.carportal.repository.CarRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ee.bcs.carportal.persistence.FuelType.ELECTRIC;
import static ee.bcs.carportal.persistence.FuelType.HYBRID;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    public void addCar(Car car) {
        carRepository.save(car);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<Car> getCarsInPriceRange(int from, int to) {
        List<Car> carsInPriceRange = new ArrayList<>();
        for (Car car : carRepository.findAll()) {
            boolean isWithinPriceRange = car.getPrice() >= from && car.getPrice() <= to;
            if (isWithinPriceRange) {
                carsInPriceRange.add(car);
            }
        }
        return carsInPriceRange;
    }

    public List<Car> getGreenCarsInPriceRange(int from, int to) {
        List<Car> greenCarsInPriceRange = new ArrayList<>();
        for (Car car : carRepository.findAll()) {
            boolean isWithinPriceRange = car.getPrice() >= from && car.getPrice() <= to;
            boolean isGreenCar = car.getFuelType().equals(ELECTRIC) || car.getFuelType().equals(HYBRID);
            if (isWithinPriceRange && isGreenCar) {
                greenCarsInPriceRange.add(car);
            }
        }
        return greenCarsInPriceRange;
    }

    public Car getRandomCarBasicInfo() {
        int carId = new Random().nextInt(carRepository.findAll().size());
        return carRepository.getById(carId);
    }

    public Car getRandomCarDetailedInfo() {
        int carId = new Random().nextInt(carRepository.findAll().size());
        return carRepository.getById(carId);
    }

    public Car getCarBasicInfoByCarId(int carId) {
        return carRepository.getById(carId);
    }

    public Car getCarDetailedInfoByCarId(int carId) {
        return carRepository.getById(carId);
    }

    public void updateCar(int carId, Car newCar) {
        Car currentCar = carRepository.getById(carId);
        currentCar.setCarModel(newCar.getCarModel());
        currentCar.setManufacturer(newCar.getManufacturer());
        currentCar.setModelYear(newCar.getModelYear());
        currentCar.setFuelType(newCar.getFuelType());
        currentCar.setEmissions(newCar.getEmissions());
        currentCar.setPrice(newCar.getPrice());
        carRepository.save(currentCar);
    }

    public void updateCarPrice(int carId, int price) {
        Car currentCar = carRepository.getById(carId);
        currentCar.setPrice(price);
        carRepository.save(currentCar);
    }

    public void deleteCar(int carId) {
        carRepository.deleteById(carId);
    }
}
