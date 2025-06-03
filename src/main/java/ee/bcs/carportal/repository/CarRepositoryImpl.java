package ee.bcs.carportal.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import ee.bcs.carportal.persistence.FuelType;
import ee.bcs.carportal.persistence.car.Car;

@Repository
public class CarRepositoryImpl implements CarRepository {
    private Map<Integer, Car> cars = createCars();

    public void save(Car car) {
        cars.put(car.getId(), car);
    };

    public Car getById(int id) {
        if (cars.get(id) != null) {
            return cars.get(id);
        }
        return null;
    };

    public List<Car> findAll() {
        return new ArrayList<>(cars.values());
    };

    public void deleteById(int id) {
        if (cars.get(id) != null) {
            cars.remove(id);
        }
    };

    // extra methods

    // https://www.geeksforgeeks.org/hashmap-put-method-in-java/

    private Map<Integer, Car> createCars() {
        Map<Integer, Car> cars = new HashMap<>();
        cars.put(1, new Car(1, "Model 3", "Tesla", 2020, FuelType.ELECTRIC, 0.0, 44000));
        cars.put(2, new Car(2, "Civic", "Honda", 2021, FuelType.PETROL, 0.05, 25000));
        cars.put(3, new Car(3, "Camry", "Toyota", 2022, FuelType.PETROL, 0.04, 28000));
        cars.put(4, new Car(4, "F-150", "Ford", 2023, FuelType.PETROL, 0.1, 45000));
        cars.put(5, new Car(5, "Prius", "Toyota", 2020, FuelType.HYBRID, 0.03, 30000));
        return cars;
    }

    public void resetData() {
        cars = createCars();
    }
}
