package ee.bcs.carportal.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import ee.bcs.carportal.persistence.Car;
import ee.bcs.carportal.persistence.FuelType;

@Service
public class CarService {
    private static final double BASE_FEE = 50.0;
    public static List<Car> cars = createCars();

    public void deleteCar(int carId) {
        cars.remove(carId);
    }

    public void updateCarPrice(int carId, int price) {
        cars.get(carId).setPrice(price);
    }

    public void replaceCar(int carId, Car car) {
        cars.set(carId, car);
    }

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

    public List<Car> getCarsByRegistrationTaxRange(@RequestParam int from, @RequestParam int to,
            @RequestParam int baseYear) {
        List<Car> filteredCars = new ArrayList<>();
        for (int carId = 0; carId < cars.size(); carId++) {
            double taxRate = calculateRegistrationTaxRate(carId, baseYear);
            double taxAmount = calculateTaxAmount(carId, taxRate);
            if (taxAmount >= from && taxAmount <= to) {
                filteredCars.add(cars.get(carId));
            }
        }
        return filteredCars;
    }

    public List<Car> getCarsByAnnualTaxRange(@RequestParam int from, @RequestParam int to, @RequestParam int baseYear) {
        List<Car> filteredCars = new ArrayList<>();
        for (int carId = 0; carId < cars.size(); carId++) {
            double annualTax = calculateAnnualTax(carId, baseYear);
            if (annualTax >= from && annualTax <= to) {
                filteredCars.add(cars.get(carId));
            }
        }
        return filteredCars;
    }

    public static List<Car> createCars() {
        List<Car> cars = new ArrayList<>();
        try (InputStream is = CarService.class.getClassLoader().getResourceAsStream("files/car_data.csv");
                Scanner myReader = new Scanner(is)) {
            if (myReader.hasNextLine()) {
                myReader.nextLine();
            }
            while (myReader.hasNextLine()) {

                String data = myReader.nextLine();
                String[] splitData = data.split(",");
                FuelType carFuelType = FuelType.PETROL;
                switch (splitData[3]) {
                    case "ELECTRIC" -> carFuelType = FuelType.ELECTRIC;
                    case "HYBRID" -> carFuelType = FuelType.HYBRID;
                }
                Car car = new Car(
                        splitData[0],
                        splitData[1],
                        Integer.parseInt(splitData[2]),
                        carFuelType,
                        Double.parseDouble(splitData[4]),
                        Integer.parseInt(splitData[5]));
                cars.add(car);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read car_data.csv", e);
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
