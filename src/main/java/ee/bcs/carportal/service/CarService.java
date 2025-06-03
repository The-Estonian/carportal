package ee.bcs.carportal.service;

import ee.bcs.carportal.persistence.Car;
import ee.bcs.carportal.persistence.FuelType;
import ee.bcs.carportal.repository.CarRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ee.bcs.carportal.persistence.FuelType.ELECTRIC;
import static ee.bcs.carportal.persistence.FuelType.HYBRID;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    private static final double BASE_FEE = 50.0;

    public void addCar(Car car) {
        carRepository.save(car);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<Car> getCarsInPriceRange(int from, int to) {
        List<Car> carsInPriceRange = new ArrayList<>();
        for (Car car : carRepository.findAll()) {
            boolean isWithinPriceRange = car.getPrice() >= from && car.getPrice() <= to;
            if (isWithinPriceRange) {
                carsInPriceRange.add(car);
            }
        }
        return carsInPriceRange;
    }

    public List<Car> getGreenCarsInPriceRange(int from, int to) {
        List<Car> greenCarsInPriceRange = new ArrayList<>();
        for (Car car : carRepository.findAll()) {
            boolean isWithinPriceRange = car.getPrice() >= from && car.getPrice() <= to;
            boolean isGreenCar = car.getFuelType().equals(ELECTRIC) || car.getFuelType().equals(HYBRID);
            if (isWithinPriceRange && isGreenCar) {
                greenCarsInPriceRange.add(car);
            }
        }
        return greenCarsInPriceRange;
    }

    public String getCarRegistrationTaxByCarId(int carId, int baseYear) {
        double registrationTaxRate = calculateRegistrationTaxRate(carId, baseYear);
        double taxAmount = calculateTaxAmount(carId, registrationTaxRate);
        return "The registration tax rate for " + carRepository.getById(carId).getModelYear() + " "
                + carRepository.getById(carId).getManufacturer() + " "
                + carRepository.getById(carId).getCarModel() + " is " + registrationTaxRate
                + "% with total tax amount €"
                + String.format("%.0f", taxAmount);
    }

    public String getCarAnnualTaxByCarId(int carId, int baseYear) {
        double annualTax = calculateAnnualTax(carId, baseYear);
        return String.format("The annual tax for %d %s %s is €%.0f", carRepository.getById(carId).getModelYear(),
                carRepository.getById(carId).getManufacturer(), carRepository.getById(carId).getCarModel(), annualTax);
    }

    public Car getRandomCarBasicInfo() {
        int carId = new Random().nextInt(carRepository.findAll().size());
        return carRepository.getById(carId);
    }

    public Car getRandomCarDetailedInfo() {
        int carId = new Random().nextInt(carRepository.findAll().size());
        return carRepository.getById(carId);
    }

    public Car getCarBasicInfoByCarId(int carId) {
        return carRepository.getById(carId);
    }

    public Car getCarDetailedInfoByCarId(int carId) {
        return carRepository.getById(carId);
    }

    public void updateCar(int carId, Car newCar) {
        Car currentCar = carRepository.getById(carId);
        currentCar.setCarModel(newCar.getCarModel());
        currentCar.setManufacturer(newCar.getManufacturer());
        currentCar.setModelYear(newCar.getModelYear());
        currentCar.setFuelType(newCar.getFuelType());
        currentCar.setEmissions(newCar.getEmissions());
        currentCar.setPrice(newCar.getPrice());
        carRepository.save(currentCar);
    }

    public void updateCarPrice(int carId, int price) {
        Car currentCar = carRepository.getById(carId);
        currentCar.setPrice(price);
        carRepository.save(currentCar);
    }

    public void deleteCar(int carId) {
        carRepository.deleteById(carId);
    }

    public List<Car> getCarsByRegistrationTaxRange(int from, int to, int baseYear) {
        List<Car> carsInRegistrationTaxRange = new ArrayList<>();
        for (Car car : carRepository.findAll()) {
            double taxRate = calculateRegistrationTaxRate(car.getId(), baseYear);
            double taxAmount = calculateTaxAmount(car.getId(), taxRate);
            if (taxAmount >= from && taxAmount <= to) {
                carsInRegistrationTaxRange.add(car);
            }
        }
        return carsInRegistrationTaxRange;
    }

    public List<Car> getCarsByAnnualTaxRange(int from, int to, int baseYear) {
        List<Car> carsInAnnualTaxRange = new ArrayList<>();
        for (Car car : carRepository.findAll()) {
            double annualTax = calculateAnnualTax(car.getId(), baseYear);
            if (annualTax >= from && annualTax <= to) {
                carsInAnnualTaxRange.add(car);
            }
        }
        return carsInAnnualTaxRange;
    }

    private double calculateRegistrationTaxRate(int carId, int baseYear) {
        double taxRate = getFuelTypeAdjustedRegistrationTaxRate(carId);
        taxRate = getEmissionsAdjustedRegistrationTaxRate(carId, taxRate);
        taxRate = getModelYearAdjustedRegistrationTaxRate(carId, baseYear, taxRate);
        return getRoundedRegistrationTaxRate(taxRate);
    }

    private double getFuelTypeAdjustedRegistrationTaxRate(int carId) {
        final double BASE_TAX_RATE = 5.0;
        final double ELECTRIC_ADJUSTMENT = -1.5;
        final double HYBRID_ADJUSTMENT = -0.5;
        final double PETROL_ADJUSTMENT = 1.5;
        double taxRate = BASE_TAX_RATE; // Base tax rate of 5%

        switch (carRepository.getById(carId).getFuelType()) {
            case ELECTRIC -> taxRate += ELECTRIC_ADJUSTMENT;
            case HYBRID -> taxRate += HYBRID_ADJUSTMENT;
            case PETROL -> taxRate += PETROL_ADJUSTMENT;
        }
        return taxRate;
    }

    private double getEmissionsAdjustedRegistrationTaxRate(int carId, double taxRate) {
        final double EMISSION_MULTIPLIER = 10.0; // For each 0.01 in emissions, increase tax rate by 0.1%
        double emissionAdjustment = carRepository.getById(carId).getEmissions() * EMISSION_MULTIPLIER; // For each 0.01
                                                                                                       // in emissions,
        // increase tax rate by 0.1%
        taxRate += emissionAdjustment;
        return taxRate;
    }

    private double getModelYearAdjustedRegistrationTaxRate(int carId, int baseYear, double taxRate) {
        final double MODEL_YEAR_MULTIPLIER = 0.2; // Reduce 0.2% for each year older than the base year
        int yearDifference = baseYear - carRepository.getById(carId).getModelYear();
        double modelYearAdjustment = yearDifference * MODEL_YEAR_MULTIPLIER; // Reduce 0.2% for each year older than the
                                                                             // base year
        taxRate -= modelYearAdjustment;
        return taxRate;
    }

    private static double getRoundedRegistrationTaxRate(double taxRate) {
        return Math.round(taxRate * 10.0) / 10.0;
    }

    private double calculateTaxAmount(int carId, double registrationTaxRate) {
        return carRepository.getById(carId).getPrice() * (registrationTaxRate / 100);
    }

    private double calculateAnnualTax(int carId, int baseYear) {
        double annualTax = getFuelTypeAdjustedAnnualTax(carId);
        annualTax = getEmissionsAdjustedAnnualTax(carId, annualTax);
        annualTax = getModelYearAdjustedAnnualTax(carId, baseYear, annualTax);
        return getMinimumFeeAdjustedAnnualTax(annualTax);
    }

    private double getFuelTypeAdjustedAnnualTax(int carId) {
        final double HYBRID_ADJUSTMENT = 20.0;
        final double PETROL_ADJUSTMENT = 30.0;
        double annualTax = BASE_FEE; // Base annual tax fee

        switch (carRepository.getById(carId).getFuelType()) {
            case HYBRID -> annualTax += HYBRID_ADJUSTMENT;
            case PETROL -> annualTax += PETROL_ADJUSTMENT;
        }
        return annualTax;
    }

    private double getEmissionsAdjustedAnnualTax(int carId, double annualTax) {
        final double EMISSION_MULTIPLIER = 500.0; // For each 0.01 in emissions, increase tax by €5
        double emissionAdjustment = carRepository.getById(carId).getEmissions() * EMISSION_MULTIPLIER; // For each 0.01
                                                                                                       // in
                                                                                                       // emissions,
        // increase tax by €5
        annualTax += emissionAdjustment;
        return annualTax;
    }

    private double getModelYearAdjustedAnnualTax(int carId, int baseYear, double annualTax) {
        final double MODEL_YEAR_MULTIPLIER = 2.0; // Reduce €2 for each year older than the base year
        int yearDifference = baseYear - carRepository.getById(carId).getModelYear();
        double modelYearAdjustment = yearDifference * MODEL_YEAR_MULTIPLIER; // Reduce €2 for each year older than the
                                                                             // base year
        annualTax -= modelYearAdjustment;
        return annualTax;
    }

    private static double getMinimumFeeAdjustedAnnualTax(double annualTax) {
        boolean annualTaxIsBelowBaseFee = annualTax < BASE_FEE;
        if (annualTaxIsBelowBaseFee) {
            annualTax = BASE_FEE;
        }
        return annualTax;
    }

}
