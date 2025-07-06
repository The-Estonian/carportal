package ee.bcs.carportal.controller.car;

import ee.bcs.carportal.persistence.car.Car;
import ee.bcs.carportal.service.car.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping("car/{carId}")
    public findCarInfo(@PathVariable Integer carId){
        return carService.findCarInfo(carId);
    }


    @GetMapping("/cars/all")
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/cars/price-range")
    public List<Car> findCarsInPriceRange(@RequestParam Integer from, @RequestParam Integer to) {
        return carService.findCarsInPriceRange(from, to);
    }

    @GetMapping("/cars/price-range-fueltype")
    public List<Car> findCarsInPriceRangeWithFuelType(@RequestParam Integer from, @RequestParam Integer to, @RequestParam String fuelTypeCode ) {
        return carService.findCarsInPriceRangeWithFuelType(from, to, fuelTypeCode);

    }

}
