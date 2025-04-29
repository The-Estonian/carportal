package ee.bcs.carportal.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // Mandatory endpoints go to below
    // Please use @Tag annotation as below with all mandatory endpoints:
    // @Tag(name = "Mandatory")

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

    @GetMapping("/cars/price-range/from/to")
    @Tag(name = "Mandatory")
    public String getCarsInPriceRange() {
        int from = 28000;
        int to = 44000;
        int carCounter = 0;
        StringBuilder sb = new StringBuilder("Cars in price range €" + from + " - €" + to + ": ");
        for (int carId = 0; carId < prices.length; carId++) {
            boolean isWithinPriceRange = prices[carId] >= from && prices[carId] <= to;
            if (isWithinPriceRange) {
                sb.append(carModels[carId]).append(", ");
                carCounter++;
            }
        }
        return sb.substring(0, sb.length() - 2) + "; (number of car models: " + carCounter + ")";
    }

    @GetMapping("/cars/green/price-range/from/to")
    @Tag(name = "Mandatory")
    public String getGreenCarsInPriceRange() {
        int from = 28000;
        int to = 44000;
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

    @GetMapping("/car/id/registration-tax")
    @Tag(name = "Mandatory")
    public String getCarRegistrationTaxByCarId() {
        int carId = 0;
        int baseYear = 2025;
        final double BASE_TAX_RATE = 5.0;
        final double ELECTRIC_ADJUSTMENT = -1.5;
        final double HYBRID_ADJUSTMENT = -0.5;
        final double PETROL_ADJUSTMENT = 1.5;
        final double EMISSION_MULTIPLIER = 10.0; // For each 0.01 in emissions, increase tax rate by 0.1%
        final double MODEL_YEAR_MULTIPLIER = 0.2; // Reduce 0.2% for each year older than the base year

        double taxRate = BASE_TAX_RATE; // Base tax rate of 5%

        // Adjust for fuel type
        switch (fuelTypes[carId]) {
            case FUEL_TYPE_ELECTRIC -> taxRate += ELECTRIC_ADJUSTMENT;
            case FUEL_TYPE_HYBRID -> taxRate += HYBRID_ADJUSTMENT;
            case FUEL_TYPE_PETROL -> taxRate += PETROL_ADJUSTMENT;
        }

        // Adjust for emissions
        double emissionAdjustment = emissions[carId] * EMISSION_MULTIPLIER; // For each 0.01 in emissions, increase tax
                                                                            // rate by 0.1%
        taxRate += emissionAdjustment;

        // Adjust for model year
        int yearDifference = baseYear - modelYears[carId];
        double modelYearAdjustment = yearDifference * MODEL_YEAR_MULTIPLIER; // Reduce 0.2% for each year older than the
                                                                             // base year
        taxRate -= modelYearAdjustment;

        // Calculate final tax
        double taxAmount = prices[carId] * (taxRate / 100); // Calculate tax based on final rate

        return "The registration tax rate for " + modelYears[carId] + " " + manufacturers[carId] + " "
                + carModels[carId] + " is " + Math.round(taxRate * 10.0) / 10.0 + "% with total tax amount €"
                + Math.round(taxAmount);
    }

    @GetMapping("/car/id/annual-tax")
    @Tag(name = "Mandatory")
    public String getCarAnnualTaxByCarId() {
        int carId = 0; // Example carId, you can modify it as needed
        int baseYear = 2025;
        final double BASE_FEE = 50.0;
        final double HYBRID_ADJUSTMENT = 20.0;
        final double PETROL_ADJUSTMENT = 30.0;
        final double EMISSION_MULTIPLIER = 500.0; // For each 0.01 in emissions, increase tax by €5
        final double MODEL_YEAR_MULTIPLIER = 2.0; // Reduce €2 for each year older than the base year

        double annualTax = BASE_FEE; // Base annual tax fee

        // Adjust for fuel type
        switch (fuelTypes[carId]) {
            case FUEL_TYPE_HYBRID -> annualTax += HYBRID_ADJUSTMENT;
            case FUEL_TYPE_PETROL -> annualTax += PETROL_ADJUSTMENT;
        }

        // Adjust for emissions
        double emissionAdjustment = emissions[carId] * EMISSION_MULTIPLIER; // For each 0.01 in emissions, increase tax
                                                                            // by €5
        annualTax += emissionAdjustment;

        // Adjust for model year
        int yearDifference = baseYear - modelYears[carId];
        double modelYearAdjustment = yearDifference * MODEL_YEAR_MULTIPLIER; // Reduce €2 for each year older than the
                                                                             // base year
        annualTax -= modelYearAdjustment;

        // Ensure the final annual tax is at least the base fee
        boolean annualTaxIsBelowBaseFee = annualTax < BASE_FEE;
        if (annualTaxIsBelowBaseFee) {
            annualTax = BASE_FEE;
        }

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
        return getCarInfo(carId);
    }

    private String getCarInfo(int carId) {
        return String.format("Make: %s\nModel: %s\nFuel type: %s\nEmissions: %.2f\nPrice: €%d\n", manufacturers[carId],
                carModels[carId], fuelTypes[carId], emissions[carId], prices[carId]);
    }

    private String getCarInfo(String manufacturer, String carModel, int modelYear) {
        return String.format("Make: %s\nModel: %s\nYear: %d\n", manufacturer, carModel, modelYear);
    }

    // Extra practice endpoints go to below
    // Please use @Tag annotation as below with all extra practice endpoints:
    // @Tag(name = "Extra practice")

    // ALL PRIVATE METHODS go to below

}
