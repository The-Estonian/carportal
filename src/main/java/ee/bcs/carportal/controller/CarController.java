package ee.bcs.carportal.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import ee.bcs.carportal.persistence.Car;
import ee.bcs.carportal.persistence.FuelType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RequestMapping("/api/v1")
@RestController
public class CarController {

    private final String[] carModels = { "Model 3", "Civic", "Camry", "F-150", "Prius" };
    private final String[] manufacturers = { "Tesla", "Honda", "Toyota", "Ford", "Toyota" };
    private final int[] modelYears = { 2020, 2021, 2022, 2023, 2020 };
    private final String[] fuelTypes = { "Electric", "Petrol", "Petrol", "Petrol", "Hybrid" };
    private final double[] emissions = { 0.0, 0.05, 0.04, 0.1, 0.03 };
    private final int[] prices = { 44000, 25000, 28000, 45000, 30000 };

    // Fuel type constants
    private static final String FUEL_TYPE_ELECTRIC = "Electric";
    private static final String FUEL_TYPE_HYBRID = "Hybrid";
    private static final String FUEL_TYPE_PETROL = "Petrol";
    private static final double BASE_FEE = 50.0;

    public static List<Car> cars = createCars();

    // Mandatory endpoints go to below
    // Please use @Tag annotation as below with all mandatory endpoints:
    // @Tag(name = "Mandatory")

    @GetMapping("/cars/all/class")
    @Tag(name = "Mandatory")
    public List<Car> getCarClassEndpoint() {
        return cars;
    }

    @GetMapping("/cars/all")
    @Tag(name = "Mandatory")
    public String getAllCars() {
        int carCounter = 0;
        StringBuilder sb = new StringBuilder("All car models: ");
        for (String model : carModels) {
            sb.append(model).append(", ");
            carCounter++;
        }
        return sb.substring(0, sb.length() - 2) + "; (number of car models: " + carCounter + ")";
    }

    @GetMapping("/cars/price-range")
    @Tag(name = "Mandatory")
    public String getCarsInPriceRange(@RequestParam int from, @RequestParam int to) {
        int carCounter = 0;
        StringBuilder sb = new StringBuilder("Cars in price range €" + from + " - €" + to + ":");
        for (int carId = 0; carId < prices.length; carId++) {
            boolean isWithinPriceRange = prices[carId] >= from && prices[carId] <= to;
            if (isWithinPriceRange) {
                sb.append("\n\n").append(getCarDetailedInfoByCarId(carId));
                carCounter++;
            }
        }
        if (carCounter == 0) {
            return "No cars found in price range €" + from + " - €" + to;
        }
        return sb + "\n\nNumber of car models: " + carCounter;
    }

    @GetMapping("/cars/green/price-range")
    @Tag(name = "Mandatory")
    public String getGreenCarsInPriceRange(@RequestParam int from, @RequestParam int to) {
        int carCounter = 0;
        StringBuilder sb = new StringBuilder("Green cars in price range €" + from + " - €" + to + ": ");
        for (int carId = 0; carId < prices.length; carId++) {
            boolean isWithinPriceRange = prices[carId] >= from && prices[carId] <= to;
            boolean isGreenCar = fuelTypes[carId].equals(FUEL_TYPE_ELECTRIC)
                    || fuelTypes[carId].equals(FUEL_TYPE_HYBRID);
            if (isWithinPriceRange && isGreenCar) {
                sb.append(carModels[carId]).append(" (").append(fuelTypes[carId]).append("), ");
                carCounter++;
            }
        }
        return sb.substring(0, sb.length() - 2) + "; (number of car models: " + carCounter + ")";
    }

    @GetMapping("/car/{carId}/registration-tax")
    @Tag(name = "Mandatory")
    public String getCarRegistrationTaxByCarId(@PathVariable int carId, @RequestParam int baseYear) {
        double registrationTaxRate = calculateRegistrationTaxRate(carId, baseYear);
        double taxAmount = calculateTaxAmount(carId, registrationTaxRate);
        return "The registration tax rate for " + modelYears[carId] + " " + manufacturers[carId] + " "
                + carModels[carId] + " is " + registrationTaxRate + "% with total tax amount €"
                + String.format("%.0f", taxAmount);
    }

    @GetMapping("/car/{carId}/annual-tax")
    @Tag(name = "Mandatory")
    public String getCarAnnualTaxByCarId(@PathVariable int carId, @RequestParam int baseYear) {
        double annualTax = calculateAnnualTax(carId, baseYear);
        return String.format("The annual tax for %d %s %s is €%.0f", modelYears[carId], manufacturers[carId],
                carModels[carId], annualTax);
    }

    @GetMapping("/car/random/basic-info")
    @Tag(name = "Mandatory")
    public String getRandomCarBasicInfo() {
        int carId = new Random().nextInt(0, 5);
        return getCarInfo(manufacturers[carId], carModels[carId], modelYears[carId]);
    }

    @GetMapping("/car/random/detailed-info")
    @Tag(name = "Mandatory")
    public String getRandomCarDetailedInfo() {
        int carId = new Random().nextInt(0, 5);
        try {
            return getCarInfo(carId);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/car/{carId}/basic-info")
    @Tag(name = "Mandatory")
    public String getCarBasicInfoByCarId(@PathVariable int carId) {
        try {
            return getCarInfo(manufacturers[carId], carModels[carId], modelYears[carId]);
        } catch (IndexOutOfBoundsException e) {
            return "No car with id " + carId + " exists";
        }
    }

    @GetMapping("/car/{carId}/detailed-info")
    @Tag(name = "Mandatory")
    public String getCarDetailedInfoByCarId(@PathVariable int carId) {
        try {
            return getCarInfo(carId);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // Extra practice endpoints go to below
    // Please use @Tag annotation as below with all extra practice endpoints:
    // @Tag(name = "Extra practice")

    // ALL PRIVATE METHODS go to below

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
        double taxRate = BASE_TAX_RATE; // Base tax rate of 5%

        switch (fuelTypes[carId]) {
            case FUEL_TYPE_ELECTRIC -> taxRate += ELECTRIC_ADJUSTMENT;
            case FUEL_TYPE_HYBRID -> taxRate += HYBRID_ADJUSTMENT;
            case FUEL_TYPE_PETROL -> taxRate += PETROL_ADJUSTMENT;
        }
        return taxRate;
    }

    private double getEmissionsAdjustedRegistrationTaxRate(int carId, double taxRate) {
        final double EMISSION_MULTIPLIER = 10.0; // For each 0.01 in emissions, increase tax rate by 0.1%
        double emissionAdjustment = emissions[carId] * EMISSION_MULTIPLIER; // For each 0.01 in emissions, increase tax
                                                                            // rate by 0.1%
        taxRate += emissionAdjustment;
        return taxRate;
    }

    private double getModelYearAdjustedRegistrationTaxRate(int carId, int baseYear, double taxRate) {
        final double MODEL_YEAR_MULTIPLIER = 0.2; // Reduce 0.2% for each year older than the base year
        int yearDifference = baseYear - modelYears[carId];
        double modelYearAdjustment = yearDifference * MODEL_YEAR_MULTIPLIER; // Reduce 0.2% for each year older than the
                                                                             // base year
        taxRate -= modelYearAdjustment;
        return taxRate;
    }

    private static double getRoundedRegistrationTaxRate(double taxRate) {
        return Math.round(taxRate * 10.0) / 10.0;
    }

    private double calculateTaxAmount(int carId, double registrationTaxRate) {
        return prices[carId] * (registrationTaxRate / 100);
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

        switch (fuelTypes[carId]) {
            case FUEL_TYPE_HYBRID -> annualTax += HYBRID_ADJUSTMENT;
            case FUEL_TYPE_PETROL -> annualTax += PETROL_ADJUSTMENT;
        }
        return annualTax;
    }

    private double getEmissionsAdjustedAnnualTax(int carId, double annualTax) {
        final double EMISSION_MULTIPLIER = 500.0; // For each 0.01 in emissions, increase tax by €5
        double emissionAdjustment = emissions[carId] * EMISSION_MULTIPLIER; // For each 0.01 in emissions, increase tax
                                                                            // by €5
        annualTax += emissionAdjustment;
        return annualTax;
    }

    private double getModelYearAdjustedAnnualTax(int carId, int baseYear, double annualTax) {
        final double MODEL_YEAR_MULTIPLIER = 2.0; // Reduce €2 for each year older than the base year
        int yearDifference = baseYear - modelYears[carId];
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

    private String getCarInfo(String manufacturer, String carModel, int modelYear) {
        return "Make: " + manufacturer + "\n" +
                "Model: " + carModel + "\n" +
                "Year: " + modelYear;
    }

    private String getCarInfo(int carId) throws Exception {
        boolean carIdExists = carId >= 0 && carId < carModels.length;
        if (!carIdExists) {
            throw new Exception("No car with id " + carId + " exists");
        }
        return "Make: " + manufacturers[carId] + "\n" +
                "Model: " + carModels[carId] + "\n" +
                "Fuel type: " + fuelTypes[carId] + "\n" +
                "Emission: " + emissions[carId] + "\n" +
                "Price: €" + prices[carId];
    }

}
