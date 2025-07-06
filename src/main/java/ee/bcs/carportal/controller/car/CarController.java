package ee.bcs.carportal.controller.car;

import ee.bcs.carportal.infrastructure.ApiError;
import ee.bcs.carportal.persistence.car.Car;
import ee.bcs.carportal.service.car.CarService;
import ee.bcs.carportal.service.car.dto.CarDetailedInfo;
import ee.bcs.carportal.service.car.dto.CarDto;
import ee.bcs.carportal.service.car.dto.CarInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
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
@Tag(name = "Car Management", description = "Operations related to car management")
public class CarController {

    private final CarService carService;

    @Operation(summary = "Adds a new car to the system", description = "Accepts a CarDto object and saves it to the database")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Car added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })

    @PostMapping("/car")
    public void addCar(@Valid @RequestBody CarDto carDto) {
        carService.addCar(carDto);
    }

    @Operation(summary = "Retrieves basic car information", description = "Returns car details for a given carId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Car found"),
            @ApiResponse(responseCode = "404", description = "Car not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })

    @GetMapping("/car/{carId}")
    public CarInfo findCarInfo(@PathVariable Integer carId) {
        return carService.findCarInfo(carId);
    }

    @Operation(summary = "Retrieves detailed car information", description = "Returns comprehensive details of a car using carId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Car found"),
            @ApiResponse(responseCode = "404", description = "Car not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/car/detailed-info/{carId}")
    public CarDetailedInfo findCarDetailedInfo(@PathVariable int carId) {
        return carService.findCarDetailedInfo(carId);
    }

    @Operation(summary = "Fetches a list of all cars", description = "Returns a list of all available cars in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of cars retrieved successfully"),
    })
    @GetMapping("/cars/all")
    public List<CarInfo> getAllCars() {
        return carService.getAllCars();
    }

    @Operation(summary = "Retrieves cars within a specified price range", description = "Returns a list of cars with prices between from and to")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of cars retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid price range", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/cars/price-range")
    public List<Car> findCarsInPriceRange(@RequestParam Integer from, @RequestParam Integer to) {
        return carService.findCarsInPriceRange(from, to);
    }

    @Operation(summary = "Retrieves cars based on price and fuel type", description = "Returns a list of cars within a price range that match the specified fuel type")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of cars retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/cars/price-range-fueltype")
    public List<Car> findCarsInPriceRangeWithFuelType(@RequestParam Integer from, @RequestParam Integer to,
            @RequestParam String fuelTypeCode) {
        return carService.findCarsInPriceRangeWithFuelType(from, to, fuelTypeCode);
    }

    @Operation(summary = "Updates car details", description = "Modifies an existing car record identified by carId with new data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Car updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Car not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/car/{carId}")
    public void updateCar(@PathVariable int carId, @Valid @RequestBody CarDto carDto) {
        carService.updateCar(carId, carDto);
    }

    @Operation(summary = "Removes a car from the system", description = "Deletes the car entry associated with the given carId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Car deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Car not found", content = @Content(schema = @Schema(implementation = ApiError.class))),
    })
    @DeleteMapping("/car/{carId}")
    public void deleteCar(@PathVariable int carId) {
        carService.deleteCar(carId);
    }

}
