package ee.bcs.carportal.service.tax;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TaxServiceTest {

    @MockBean
    private CarRepository carRepository;

    @Autowired
    private TaxService taxService;

    Map<Integer, Car> carsMap = new HashMap<>();
    ArrayList<Car> carsList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        carsMap.put(1, new Car(1, "Model 3", "Tesla", 2020, FuelType.ELECTRIC, 0.0, 44000));
        carsMap.put(2, new Car(2, "Civic", "Honda", 2021, FuelType.PETROL, 0.05, 25000));
        carsMap.put(3, new Car(3, "Camry", "Toyota", 2022, FuelType.PETROL, 0.04, 28000));
        carsMap.put(4, new Car(4, "F-150", "Ford", 2023, FuelType.PETROL, 0.1, 45000));
        carsMap.put(5, new Car(5, "Prius", "Toyota", 2020, FuelType.HYBRID, 0.03, 30000));
        carsList = new ArrayList<>(carsMap.values());
    }

    // Test Case 1: Calculates registration tax for existing car
    @Test
    public void shouldCalculateRegistrationTaxForExistingCar() {
        // Arrange
        int carId = 1;
        when(carRepository.getById(carId)).thenReturn(carsMap.get(carId));
        // Act
        String result = taxService.getCarRegistrationTaxByCarId(carId, 2025);
        // Assert
        String expected = "The registration tax rate for 2020 Tesla Model 3 is 2.5% with total tax amount €1100";
        assertEquals(result, expected);
    }

    // Test Case 2: Calculates annual tax for existing car
    @Test
    void shouldCalculateAnnualTaxForExistingCar() {
        // Arrange
        int carId = 2;
        when(carRepository.getById(carId)).thenReturn(carsMap.get(carId));
        // Act
        String result = taxService.getCarAnnualTaxByCarId(carId, 2025);
        // Assert
        String expected = "The annual tax for 2021 Honda Civic is €97";
        assertEquals(result, expected);
    }

    // Test Case 3: Finds all cars within registration tax range
    @Test
    void shouldReturnCarsByRegistrationTaxRange() {
        // Arrange
        when(carRepository.findAll()).thenReturn(carsList);
        when(carRepository.getById(1)).thenReturn(carsMap.get(1));
        when(carRepository.getById(2)).thenReturn(carsMap.get(2));
        when(carRepository.getById(3)).thenReturn(carsMap.get(3));
        when(carRepository.getById(4)).thenReturn(carsMap.get(4));
        when(carRepository.getById(5)).thenReturn(carsMap.get(5));

        List<Car> expectedCars = new ArrayList<>();
        expectedCars.add(carsMap.get(1));
        expectedCars.add(carsMap.get(5));

        // Act
        List<Car> cars = taxService.getCarsByRegistrationTaxRange(1100, 1200, 2025);

        // Assert
        verify(carRepository, times(1)).findAll();
        verify(carRepository, times(20)).getById(anyInt());
        assertEquals(expectedCars.size(), cars.size());
        assertTrue(expectedCars.containsAll(cars));
    }

    // Test Case 4: Finds all cars within annual tax range
    @Test
    void shouldReturnCarsByAnnualTaxRange() {
        // Arrange
        when(carRepository.findAll()).thenReturn(carsList);
        when(carRepository.getById(1)).thenReturn(carsMap.get(1));
        when(carRepository.getById(2)).thenReturn(carsMap.get(2));
        when(carRepository.getById(3)).thenReturn(carsMap.get(3));
        when(carRepository.getById(4)).thenReturn(carsMap.get(4));
        when(carRepository.getById(5)).thenReturn(carsMap.get(5));

        List<Car> expectedCars = new ArrayList<>();
        expectedCars.add(carsMap.get(1));
        expectedCars.add(carsMap.get(2));
        expectedCars.add(carsMap.get(3));
        expectedCars.add(carsMap.get(5));

        // Act
        List<Car> cars = taxService.getCarsByAnnualTaxRange(50, 100, 2025);

        // Assert
        verify(carRepository, times(1)).findAll();
        verify(carRepository, times(15)).getById(anyInt());
        assertEquals(expectedCars.size(), cars.size());
        assertTrue(expectedCars.containsAll(cars));
    }
}