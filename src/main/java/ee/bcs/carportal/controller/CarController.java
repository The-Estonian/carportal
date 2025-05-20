package ee.bcs.carportal.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ee.bcs.carportal.persistence.Car;
import ee.bcs.carportal.service.CarService;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class CarController {
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping("/car")
    @Tag(name = "Mandatory")
    public void addNewCar(@RequestBody Car car) {
        carService.addNewCar(car);
    }

    @PutMapping("/car/{carId}")
    @Tag(name = "Mandatory")
    public void replaceCar(@PathVariable int carId, @RequestBody Car car) {
        carService.replaceCar(carId, car);
    }

    @PatchMapping("/car/{carId}")
    @Tag(name = "Mandatory")
    public void updateCarPrice(@PathVariable int carId, @RequestParam int price) {
        carService.updateCarPrice(carId, price);
    }

    @DeleteMapping("/car/{carId}")
    @Tag(name = "Mandatory")
    public void deleteCar(@PathVariable int carId) {
        carService.deleteCar(carId);
    }

    @GetMapping("/cars/all")
    @Tag(name = "Mandatory")
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/cars/price-range")
    @Tag(name = "Mandatory")
    public List<Car> getCarsInPriceRange(@RequestParam int from, @RequestParam int to) {
        return carService.getCarsInPriceRange(from, to);
    }

    @GetMapping("/cars/green/price-range")
    @Tag(name = "Mandatory")
    public List<Car> getGreenCarsInPriceRange(@RequestParam int from, @RequestParam int to) {
        return carService.getGreenCarsInPriceRange(from, to);
    }

    @GetMapping("/car/{carId}/registration-tax")
    @Tag(name = "Mandatory")
    public String getCarRegistrationTaxByCarId(@PathVariable int carId, @RequestParam int baseYear) {
        return carService.getCarRegistrationTaxByCarId(carId, baseYear);
    }

    @GetMapping("/car/{carId}/annual-tax")
    @Tag(name = "Mandatory")
    public String getCarAnnualTaxByCarId(@PathVariable int carId, @RequestParam int baseYear) {
        return carService.getCarAnnualTaxByCarId(carId, baseYear);
    }

    @GetMapping("/car/random/basic-info")
    @Tag(name = "Mandatory")
    public Car getRandomCarBasicInfo() {
        return carService.getRandomCarBasicInfo();
    }

    @GetMapping("/car/random/detailed-info")
    @Tag(name = "Mandatory")
    public Car getRandomCarDetailedInfo() {
        return carService.getRandomCarDetailedInfo();
    }

    @GetMapping("/car/{carId}/basic-info")
    @Tag(name = "Mandatory")
    public Car getCarBasicInfoByCarId(@PathVariable int carId) {
        return carService.getCarBasicInfoByCarId(carId);
    }

    @GetMapping("/car/{carId}/detailed-info")
    @Tag(name = "Mandatory")
    public Car getCarDetailedInfoByCarId(@PathVariable int carId) {
        return carService.getCarDetailedInfoByCarId(carId);
    }

}
