package ee.bcs.carportal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ee.bcs.carportal.persistence.FuelType;
import ee.bcs.carportal.persistence.car.Car;

@SpringBootTest
public class CarRepositoryImplTest {

    @Autowired
    private CarRepositoryImpl carRepo;

    @BeforeEach
    void setUp() {
        carRepo.resetData();
    }

    // save Method test if the new added object is of type Car

    @Test
    void shouldSaveANewCar() {
        List<Car> allCarsBeforeInjection = carRepo.findAll();
        carRepo.save(new Car(6,
                "Lada",
                "Ladanissimo",
                2000,
                FuelType.PETROL,
                1.0,
                10000));
        List<Car> allCarsAfterInjection = carRepo.findAll();
        assertNotEquals(allCarsBeforeInjection, allCarsAfterInjection);
        assertEquals("Ladanissimo", allCarsAfterInjection.get(5).getManufacturer());
        assertEquals(1.0, carRepo.getById(6).getEmissions());
        carRepo.getById(6).setEmissions(2.0);
        assertEquals(2.0, carRepo.getById(6).getEmissions());
    }

    @Test
    void shouldUpdateACarInfoInList() {
        carRepo.save(new Car(6,
                "Lada",
                "Ladanissimo",
                2000,
                FuelType.PETROL,
                1.0,
                10000));
        assertEquals(1.0, carRepo.getById(6).getEmissions());
        carRepo.getById(6).setEmissions(2.0);
        assertEquals(2.0, carRepo.getById(6).getEmissions());
    }

    @Test
    void shouldThrowExceptionOnInvalidCarSave() {
        carRepo.save(new Car(6,
                "Lada",
                "Ladanissimo",
                2000,
                FuelType.PETROL,
                1.0,
                10000));
        assertInstanceOf(Car.class, carRepo.getById(6));
        assertThrows(NullPointerException.class, () -> carRepo.save(null));
    }

    // getById Method With correct data and invalid data
    @Test
    void shouldReturnCarById() {
        Car getCarbyId1 = carRepo.getById(1);
        assertNotNull(getCarbyId1);
        assertEquals("Tesla", getCarbyId1.getManufacturer());

        Car getCarbyId2 = carRepo.getById(5);
        assertNotNull(getCarbyId2);
        assertEquals("Toyota", getCarbyId2.getManufacturer());

        assertEquals(carRepo.getById(8), null);
    }

    @Test
    void shouldReturnNullWhenInvalidCarId() {
        Car getCarbyId1 = carRepo.getById(6);
        assertNull(getCarbyId1);
    }

    // findAll method should list all cars

    @Test
    void shouldReturnInitialCarList() {
        List<Car> allCars = carRepo.findAll();
        assertEquals(5, allCars.size());
        assertEquals("Tesla", allCars.get(0).getManufacturer());
        assertEquals(FuelType.HYBRID, allCars.get(4).getFuelType());

    }

    @Test
    void shouldReturnListWithNewCar() {
        List<Car> allCars = carRepo.findAll();
        carRepo.save(new Car(6,
                "Lada",
                "Ladanissimo",
                2000,
                FuelType.PETROL,
                1.0,
                10000));

        allCars = carRepo.findAll();
        assertEquals(6, allCars.size());
        assertEquals("Ladanissimo", allCars.get(5).getManufacturer());
    }

    // deleteById should delete a car from the list

    @Test
    void shouldDeleteCarById() {
        List<Car> allCars = carRepo.findAll();
        assertEquals(5, allCars.size());
        assertEquals("Model 3", allCars.get(0).getCarModel());
        carRepo.deleteById(1);

        allCars = carRepo.findAll();
        assertEquals(4, allCars.size());
        assertNotEquals("Model 3", allCars.get(0).getCarModel());

        carRepo.deleteById(1);
        assertEquals(4, allCars.size());
    }

    @Test
    void shouldDeleteNotExistingIdGracefully() {
        List<Car> allCars = carRepo.findAll();
        assertEquals(5, allCars.size());
        carRepo.deleteById(1);

        allCars = carRepo.findAll();
        assertEquals(4, allCars.size());
        assertNotEquals("Model 3", allCars.get(0).getCarModel());

        carRepo.deleteById(1);
        assertEquals(4, allCars.size());
    }

}
