package ee.bcs.carportal.repository;

import java.util.List;

import ee.bcs.carportal.persistence.Car;

public interface CarRepository {
    public void save(Car car);

    public Car getById(int id);

    public List<Car> findAll();

    public void deleteById(int id);
}
