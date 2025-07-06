package ee.bcs.carportal.controller.car;

import ee.bcs.carportal.persistence.car.Car;
import ee.bcs.carportal.service.car.CarService;
import ee.bcs.carportal.service.car.dto.CarDetailedInfo;
import ee.bcs.carportal.service.car.dto.CarDto;
import ee.bcs.carportal.service.car.dto.CarInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping("/car")
    public void addCar(@RequestBody CarDto carDto) {
        carService.addCar(carDto);
    }

    @GetMapping("/car/{carId}")
    public CarInfo findCarInfo(@PathVariable Integer carId) {
        return carService.findCarInfo(carId);
    }

    @GetMapping("/car/detailed-info/{carId}")
    public CarDetailedInfo findCarDetailedInfo(@PathVariable int carId) {
        return carService.findCarDetailedInfo(carId);
    }

    @GetMapping("/cars/all")
    public List<CarInfo> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/cars/price-range")
    public List<Car> findCarsInPriceRange(@RequestParam Integer from, @RequestParam Integer to) {
        return carService.findCarsInPriceRange(from, to);
    }

    @GetMapping("/cars/price-range-fueltype")
    public List<Car> findCarsInPriceRangeWithFuelType(@RequestParam Integer from, @RequestParam Integer to,
            @RequestParam String fuelTypeCode) {
        return carService.findCarsInPriceRangeWithFuelType(from, to, fuelTypeCode);
    }

    @PutMapping("/car/{carId}")
    public void updateCar(@PathVariable int carId, @RequestBody CarDto carDto) {
        carService.updateCar(carId, carDto);
    }

}
