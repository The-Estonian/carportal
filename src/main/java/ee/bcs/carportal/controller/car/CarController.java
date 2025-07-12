package ee.bcs.carportal.controller.car;

import ee.bcs.carportal.infrastructure.ApiError;
import ee.bcs.carportal.persistence.car.Car;
import ee.bcs.carportal.service.car.CarService;
import ee.bcs.carportal.service.car.dto.CarDto;
import ee.bcs.carportal.service.car.dto.CarInfo;
import ee.bcs.carportal.service.car.dto.CarDetailedInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Car Management", description = "Operations related to car management")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping("/car/{carId}")
    @Operation(
            summary     = "Retrieves basic car information",
            description = "Returns car details for a given carId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Car found"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Car not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    public ResponseEntity<CarInfo> findCarInfo(
            @Parameter(description = "ID of the car to retrieve", required = true)
            @PathVariable Integer carId
    ) {
        CarInfo dto = carService.findCarInfo(carId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/car/detailed-info/{carId}")
    @Operation(
            summary     = "Retrieves detailed car information",
            description = "Returns comprehensive details of a car using carId"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Car found"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Car not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    public ResponseEntity<CarDetailedInfo> findCarDetailedInfo(
            @Parameter(description = "ID of the car to retrieve detailed info", required = true)
            @PathVariable Integer carId) {
        CarDetailedInfo dto = carService.findCarDetailedInfo(carId);
        return ResponseEntity.ok(dto);
    }

    @@GetMapping("/cars/all")
    @Operation(
            summary     = "Fetches a list of all cars",
            description = "Returns a list of all available cars in the system"

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of cars retrieved successfully")
    })
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
    @Operation(
            summary     = "Adds a new car to the system",
            description = "Accepts a CarDto object and saves it to the database")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Car added successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    public ResponseEntity<Void> addCar(
            @Valid @RequestBody CarDto carDto
    ) {
        carService.addCar(carDto);
        return ResponseEntity.ok().build();
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