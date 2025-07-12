package ee.bcs.carportal.controller.car;

import ee.bcs.carportal.persistence.car.Car;
import ee.bcs.carportal.service.car.CarService;
import ee.bcs.carportal.service.car.dto.CarDto;
import ee.bcs.carportal.service.car.dto.CarInfo;
import ee.bcs.carportal.service.car.dto.CarDetailedInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping("/car/{carId}")
    public ResponseEntity<CarInfo> findCarInfo(@PathVariable Integer carId) {
        CarInfo dto = carService.findCarInfo(carId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/car/detailed-info/{carId}")
    public ResponseEntity<CarDetailedInfo> findCarDetailedInfo(@PathVariable Integer carId) {
        CarDetailedInfo dto = carService.findCarDetailedInfo(carId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/cars/all")
    public ResponseEntity<List<CarInfo>> getAllCars() {
        List<CarInfo> list = carService.getAllCars();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/cars/price-range")
    public ResponseEntity<List<Car>> findCarsInPriceRange(
            @RequestParam Integer from,
            @RequestParam Integer to) {

        return ResponseEntity.ok(carService.findCarsInPriceRange(from, to));
    }

    @GetMapping("/cars/price-range-fueltype")
    public ResponseEntity<List<Car>> findCarsInPriceRangeWithFuelType(
            @RequestParam Integer from,
            @RequestParam Integer to,
            @RequestParam String fuelTypeCode) {

        return ResponseEntity.ok(carService.findCarsInPriceRangeWithFuelType(from, to, fuelTypeCode));
    }

    @PostMapping("/car")
    public ResponseEntity<Void> addCar(@Valid @RequestBody CarDto carDto) {
        carService.addCar(carDto);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/car/{carId}")
    public ResponseEntity<Void> updateCar(
            @PathVariable Integer carId,
            @Valid @RequestBody CarDto carDto) {

        carService.updateCar(carId, carDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/car/{carId}")
    public ResponseEntity<Void> deleteCar(@PathVariable Integer carId) {
        carService.deleteCar(carId);
        return ResponseEntity.ok().build();
    }
}