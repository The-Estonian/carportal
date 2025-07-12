package ee.bcs.carportal.controller.car;

import ee.bcs.carportal.persistence.car.Car;
import ee.bcs.carportal.service.car.CarService;
import ee.bcs.carportal.service.car.dto.CarDetailedInfo;
import ee.bcs.carportal.service.car.dto.CarDto;
import ee.bcs.carportal.service.car.dto.CarInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PutMapping("/car/{carId}")
    public void updateCar(@PathVariable Integer carId,@RequestBody CarDto carDto){
        return carService.updateCar(carId,carDto);
    }

    @GetMapping("/car/detailed-info/{carId}")
        public CarDetailedInfo findCarDetailedInfo(@PathVariable("carId")  Integer carId){
        return carService.findCarDetailedInfo(carId);
    }

    @PostMapping("/car")
    public void addCar(@RequestBody CarDto carDto){
        carService.addCar(carDto);
    }

    @GetMapping("/car/{carId}")
    public CarInfo findCarInfo(@PathVariable("carId") Integer carId){
        return carService.findCarInfo(carId);
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
    public List<Car> findCarsInPriceRangeWithFuelType(@RequestParam Integer from, @RequestParam Integer to, @RequestParam String fuelTypeCode ) {
        return carService.findCarsInPriceRangeWithFuelType(from, to, fuelTypeCode);

    }

}
