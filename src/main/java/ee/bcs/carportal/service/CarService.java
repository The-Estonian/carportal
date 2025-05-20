package ee.bcs.carportal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import ee.bcs.carportal.persistence.Car;
import ee.bcs.carportal.persistence.FuelType;

@Service
public class CarService {
    private static final double BASE_FEE = 50.0;
    public static List<Car> cars = createCars();

    public void addNewCar(Car car) {
        cars.add(car);
    }

    public List<Car> getAllCars() {
        return cars;
    }

    public List<Car> getCarsInPriceRange(@RequestParam int from, @RequestParam int to) {
        List<Car> carFilter = new ArrayList<>();
        for (Car car : cars) {
            if (car.getPrice() >= from && car.getPrice() <= to) {
                carFilter.add(car);
            }
        }
        return carFilter;
    }

    public List<Car> getGreenCarsInPriceRange(@RequestParam int from, @RequestParam int to) {
        List<Car> carFilter = new ArrayList<>();
        for (Car car : cars) {
            if (car.getPrice() >= from && car.getPrice() < to && car.getFuelType().equals(FuelType.ELECTRIC) || car
                    .getFuelType().equals(FuelType.HYBRID)) {
                carFilter.add(car);
            }
        }
        return carFilter;
    }

    public String getCarRegistrationTaxByCarId(@PathVariable int carId, @RequestParam int baseYear) {
        double registrationTaxRate = calculateRegistrationTaxRate(carId, baseYear);
        double taxAmount = calculateTaxAmount(carId, registrationTaxRate);
        return "The registration tax rate for " + cars.get(carId).getModelYear() + " "
                + cars.get(carId).getManufacturer() + " "
                + cars.get(carId).getCarModel() + " is " + registrationTaxRate + "% with total tax amount €"
                + String.format("%.0f", taxAmount);
    }

    public String getCarAnnualTaxByCarId(@PathVariable int carId, @RequestParam int baseYear) {
        double annualTax = calculateAnnualTax(carId, baseYear);
        return String.format("The annual tax for %d %s %s is €%.0f",
                cars.get(carId).getModelYear(),
                cars.get(carId).getManufacturer(),
                cars.get(carId).getCarModel(), annualTax);
    }

    public Car getRandomCarBasicInfo() {
        int carId = new Random().nextInt(0, 5);
        return cars.get(carId);
    }

    public Car getRandomCarDetailedInfo() {
        int carId = new Random().nextInt(0, 5);
        return cars.get(carId);
    }

    public Car getCarBasicInfoByCarId(@PathVariable int carId) {
        return cars.get(carId);
    }

    public Car getCarDetailedInfoByCarId(@PathVariable int carId) {
        return cars.get(carId);
    }

    public static List<Car> createCars() {
        String[] carModels = { "Model 3", "Civic", "Camry", "F-150", "Prius" };
        String[] manufacturers = { "Tesla", "Honda", "Toyota", "Ford", "Toyota" };
        int[] modelYears = { 2020, 2021, 2022, 2023, 2020 };
        String[] fuelTypes = { "Electric", "Petrol", "Petrol", "Petrol", "Hybrid" };
        double[] emissions = { 0.0, 0.05, 0.04, 0.1, 0.03 };
        int[] prices = { 44000, 25000, 28000, 45000, 30000 };

        List<Car> cars = new ArrayList<>();
        for (int i = 0; i < fuelTypes.length; i++) {
            FuelType carFuelType = FuelType.PETROL;
            switch (fuelTypes[i]) {
                case "Electric" -> carFuelType = FuelType.ELECTRIC;
                case "Hybrid" -> carFuelType = FuelType.HYBRID;
            }
            cars.add(new Car(carModels[i],
                    manufacturers[i],
                    modelYears[i],
                    carFuelType, emissions[i],
                    prices[i]));
        }
        return cars;
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
        double taxRate = BASE_TAX_RATE;

        switch (cars.get(carId).getFuelType()) {
            case FuelType.ELECTRIC -> taxRate += ELECTRIC_ADJUSTMENT;
            case FuelType.HYBRID -> taxRate += HYBRID_ADJUSTMENT;
            case FuelType.PETROL -> taxRate += PETROL_ADJUSTMENT;
        }
        return taxRate;
    }

    private double getEmissionsAdjustedRegistrationTaxRate(int carId, double taxRate) {
        final double EMISSION_MULTIPLIER = 10.0;
        double emissionAdjustment = cars.get(carId).getEmissions() * EMISSION_MULTIPLIER; // For
        taxRate += emissionAdjustment;
        return taxRate;
    }

    private double getModelYearAdjustedRegistrationTaxRate(int carId, int baseYear, double taxRate) {
        final double MODEL_YEAR_MULTIPLIER = 0.2;
        int yearDifference = baseYear - cars.get(carId).getModelYear();
        double modelYearAdjustment = yearDifference * MODEL_YEAR_MULTIPLIER; //
        taxRate -= modelYearAdjustment;
        return taxRate;
    }

    private static double getRoundedRegistrationTaxRate(double taxRate) {
        return Math.round(taxRate * 10.0) / 10.0;
    }

    private double calculateTaxAmount(int carId, double registrationTaxRate) {
        return cars.get(carId).getPrice() * (registrationTaxRate / 100);
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
        double annualTax = BASE_FEE;

        switch (cars.get(carId).getFuelType()) {
            case HYBRID -> annualTax += HYBRID_ADJUSTMENT;
            case PETROL -> annualTax += PETROL_ADJUSTMENT;
            case ELECTRIC -> annualTax += 0;
        }
        return annualTax;
    }

    private double getEmissionsAdjustedAnnualTax(int carId, double annualTax) {
        final double EMISSION_MULTIPLIER = 500.0;
        double emissionAdjustment = cars.get(carId).getEmissions() * EMISSION_MULTIPLIER; // For
        annualTax += emissionAdjustment;
        return annualTax;
    }

    private double getModelYearAdjustedAnnualTax(int carId, int baseYear, double annualTax) {
        final double MODEL_YEAR_MULTIPLIER = 2.0;
        int yearDifference = baseYear - cars.get(carId).getModelYear();
        double modelYearAdjustment = yearDifference * MODEL_YEAR_MULTIPLIER;
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
