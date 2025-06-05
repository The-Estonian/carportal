package ee.bcs.carportal.service.car;

import ee.bcs.carportal.persistence.FuelType;
import ee.bcs.carportal.persistence.car.Car;
import ee.bcs.carportal.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CarServiceTest {

    @Autowired
    private CarService carService;

    @MockBean
    private CarRepository carRepository;

    Map<Integer, Car> carsMap = new HashMap<>();
    ArrayList<Car> carsList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        carsMap.clear();
        carsList.clear();
        carsMap.put(1, new Car(1, "Model 3", "Tesla", 2020, FuelType.ELECTRIC, 0.0, 44000));
        carsMap.put(2, new Car(2, "Civic", "Honda", 2021, FuelType.PETROL, 0.05, 25000));
        carsMap.put(3, new Car(3, "Camry", "Toyota", 2022, FuelType.PETROL, 0.04, 28000));
        carsMap.put(4, new Car(4, "F-150", "Ford", 2023, FuelType.PETROL, 0.1, 45000));
        carsMap.put(5, new Car(5, "Prius", "Toyota", 2020, FuelType.HYBRID, 0.03, 30000));
        carsList = new ArrayList<>(carsMap.values());
    }

    // Test Case 1: Adds a new car
    @Test
    void shouldAddNewCar() {
        // Arrange
        Car newCar = new Car(6,
                "Lada",
                "Ladanissimo",
                2000,
                FuelType.PETROL,
                1.0,
                10000);
        doNothing().when(carRepository).save(newCar);
        when(carRepository.getById(6)).thenReturn(newCar);
        // Act
        carService.addCar(newCar);
        // Assert
        assertEquals("Ladanissimo", carService.getCarBasicInfoByCarId(6).getManufacturer());
    }

    // Test Case 2: Returns all cars
    @Test
    void shouldReturnAllCars() {
        // Arrange
        when(carRepository.findAll()).thenReturn(carsList);
        // Act
        List<Car> cars = carService.getAllCars();
        // Assert
        verify(carRepository, times(1)).findAll();
        assertEquals(carsList.size(), cars.size());
        assertEquals(carsList, cars);
    }

    // Test Case 3: Returns cars in price range
    @Test
    void shouldReturnCarsInPriceRange() {
        // Arrange
        when(carRepository.findAll()).thenReturn(carsList);
        List<Car> expectedCars = new ArrayList<>();
        for (Car car : carsList) {
            boolean isWithinPriceRange = car.getPrice() >= 25000 && car.getPrice() <= 29000;
            if (isWithinPriceRange) {
                expectedCars.add(car);
            }
        }
        // Act
        List<Car> cars = carService.getCarsInPriceRange(25000, 29000);
        // Assert
        verify(carRepository, times(1)).findAll();
        assertEquals(expectedCars.size(), cars.size());
        assertEquals(expectedCars, cars);
    }

    // Test Case 4: Returns green cars in price range
    @Test
    void shouldReturnGreenCarsInPriceRange() {
        // Arrange
        when(carRepository.findAll()).thenReturn(carsList);
        List<Car> expectedCars = new ArrayList<>();
        for (Car car : carsList) {
            boolean isWithinPriceRange = car.getPrice() >= 25000 && car.getPrice() <= 45000;
            boolean isGreenCar = car.getFuelType().equals(FuelType.ELECTRIC)
                    || car.getFuelType().equals(FuelType.HYBRID);
            if (isWithinPriceRange && isGreenCar) {
                expectedCars.add(car);
            }
        }
        // Act
        List<Car> cars = carService.getGreenCarsInPriceRange(25000, 45000);
        // Assert
        verify(carRepository, times(1)).findAll();
        assertEquals(expectedCars.size(), cars.size());
        assertEquals(expectedCars, cars);
    }

    // Test Case 5: Returns car by ID when exists
    @Test
    void shouldReturnCarByIdWhenExists() {
        // Arrange
        int existingCarId = 1;
        when(carRepository.getById(existingCarId)).thenReturn(carsList.get(0));
        // Act
        Car car = carService.getCarBasicInfoByCarId(1);
        // Assert
        verify(carRepository, times(1)).getById(existingCarId);
        assertNotNull(car);
        assertEquals(car, carsList.get(0));
    }

    // Test Case 6: Updates an existing car
    @Test
    void shouldUpdateExistingCar() {
        // Arrange
        int carId = 1;
        Car originalCar = carsMap.get(carId);
        Car updatedCar = new Car(carId,
                "CyberTruck",
                "Tesla",
                2020,
                FuelType.ELECTRIC,
                0.0,
                100000);
        when(carRepository.getById(1)).thenReturn(carsList.get(0));
        // Act
        carService.updateCar(carId, updatedCar);
        // Assert
        verify(carRepository, times(1)).getById(carId);
        verify(carRepository, times(1)).save(originalCar);
    }

    // Test Case 7: Deletes a car by ID
    @Test
    void shouldDeleteCarById() {
        // Arrange
        int carId = 1;
        doAnswer(invocation -> {
            carsList.remove(0);
            return null;
        }).when(carRepository).deleteById(1);
        when(carRepository.findAll()).thenReturn(carsList);
        // Act
        carService.deleteCar(1);
        // Assert
        assertEquals(carsList, carService.getAllCars());
        verify(carRepository, times(1)).deleteById(carId);
    }
}