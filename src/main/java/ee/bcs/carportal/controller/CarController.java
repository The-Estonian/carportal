package ee.bcs.carportal.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringJoiner;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
@CrossOrigin(origins = "https://saarcodes.dev")
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

    private final List<Map<String, Object>> cars = new ArrayList<>();

    public CarController() {

        for (int i = 0; i < carModels.length; i++) {

            Map<String, Object> car = new HashMap<>();

            car.put("id", i + 1);

            car.put("model", carModels[i]);

            car.put("manufacturer", manufacturers[i]);

            car.put("year", modelYears[i]);

            car.put("fuelType", fuelTypes[i]);

            car.put("emission", emissions[i]);

            car.put("price", prices[i]);

            cars.add(car);

        }

    }

    // Mandatory endpoints go to below
    // Please use @Tag annotation as below with all mandatory endpoints:
    // @Tag(name = "Mandatory")

    @GetMapping("/cars/all")
    @Tag(name = "Mandatory")
    // public String getAllCars() {
    // int carCounter = 0;
    // StringBuilder sb = new StringBuilder("All car models: ");
    // for (String model : carModels) {
    // sb.append(model).append(", ");
    // carCounter++;
    // }
    // return sb.substring(0, sb.length() - 2) + "; (number of car models: " +
    // carCounter + ")";
    // }
    public List<Map<String, Object>> getAllCars() {

        System.out.println(cars);

        return cars;

    }
    // ------------------------------------------------------------------------------------------

    @GetMapping("/cars/price-range")
    @Tag(name = "Mandatory")
    public String getCarsInPriceRange(@RequestParam int from, @RequestParam int to) {
        int carCounter = 0;
        StringBuilder sb = new StringBuilder("Cars in price range €" + from + " - €" + to + ": \n\n");
        for (int carId = 0; carId < prices.length; carId++) {
            boolean isWithinPriceRange = prices[carId] >= from && prices[carId] <= to;
            if (isWithinPriceRange) {
                sb.append(getCarDetailedInfoByCarId(carId)).append("\n");
                carCounter++;
            }
        }
        if (carCounter == 0) {
            return String.format("No cars found in price range €%d - €%d", from, to);
        }
        return sb.substring(0, sb.length() - 2) + "\n\nnumber of car models: " + carCounter;
    }

    // ------------------------------------------------------------------------------------------

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

    // ------------------------------------------------------------------------------------------

    @GetMapping("/car/{carId}/registration-tax")
    @Tag(name = "Mandatory")
    public String getCarRegistrationTaxByCarId(@PathVariable int carId, @RequestParam int baseYear) {
        // Calculate tax %
        double taxRate = adjustTaxRate(carId, baseYear);
        // Calculate tax amount
        double taxAmount = calculateTaxAmountForCar(carId, taxRate);

        return "The registration tax rate for " + modelYears[carId] + " " + manufacturers[carId] + " "
                + carModels[carId] + " is " + Math.round(taxRate * 10.0) / 10.0 + "% with total tax amount €"
                + Math.round(taxAmount);
    }

    // ------------------------------------------------------------------------------------------

    @GetMapping("/car/{carId}/annual-tax")
    @Tag(name = "Mandatory")
    public String getCarAnnualTaxByCarId(@PathVariable int carId, @RequestParam int baseYear) {

        double annualTax = calculateAnnualTax(carId, baseYear);

        return String.format("The annual tax for %d %s %s is €%.0f", modelYears[carId], manufacturers[carId],
                carModels[carId], annualTax);
    }

    // ------------------------------------------------------------------------------------------

    @GetMapping("/car/random/basic-info")
    @Tag(name = "Mandatory")
    public String getRandomCarBasicInfo() {
        int carId = new Random().nextInt(0, 5);
        return getCarInfo(manufacturers[carId], carModels[carId], modelYears[carId]);
    }

    // ------------------------------------------------------------------------------------------

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

    // ------------------------------------------------------------------------------------------

    @GetMapping("/car/{carId}/basic-info")
    @Tag(name = "Mandatory")
    public String getCarBasicInfoByCarId(@PathVariable int carId) {
        try {
            return getCarInfo(carId);
        } catch (Exception e) {
            return String.format("No car with id %d exists ", carId);
        }
    }

    // ------------------------------------------------------------------------------------------

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

    // ------------------------------------------------------------------------------------------

    @GetMapping("/cars/registration-tax-range")
    @Tag(name = "Extra practice")
    public String getCarsByRegistrationTaxRange(@RequestParam int from, @RequestParam int to,
            @RequestParam int baseYear) {
        int carCounter = 0;
        StringBuilder sb = new StringBuilder("Cars in tax range €" + from + " - €" + to + ":\n\n");
        for (int carId = 0; carId < prices.length; carId++) {
            double taxRate = adjustTaxRate(carId, baseYear);
            double taxAmount = calculateTaxAmountForCar(carId, taxRate);
            if (taxAmount >= from && taxAmount <= to) {
                sb.append(getCarInfo(manufacturers[carId], carModels[carId], modelYears[carId]));
                sb.append(String.format("Tax rate: %.1f%%\n", taxRate));
                sb.append(String.format("Tax amount: €%.0f\n", taxAmount));
                sb.append("\n");
                carCounter++;
            }
        }
        if (carCounter == 0) {
            return String.format("No cars found in tax range €%d - €%d", from, to);
        }
        return sb.append(String.format("Number of car models: %d", carCounter)).toString();
    }

    // ------------------------------------------------------------------------------------------

    @GetMapping("/cars/annual-tax-range")
    @Tag(name = "Extra practice")
    public String getCarsByAnnualTaxRange(@RequestParam int from, @RequestParam int to,
            @RequestParam int baseYear) {
        int carCounter = 0;
        StringBuilder sb = new StringBuilder("Cars in tax range €" + from + " - €" + to + ":\n\n");
        for (int carId = 0; carId < prices.length; carId++) {
            double taxAmount = calculateAnnualTax(carId, baseYear);
            if (taxAmount >= from && taxAmount <= to) {
                sb.append(getCarInfo(manufacturers[carId], carModels[carId], modelYears[carId]));
                sb.append(String.format("Tax amount: €%.0f\n", taxAmount));
                sb.append("\n");
                carCounter++;
            }
        }
        if (carCounter == 0) {
            return String.format("No cars found in tax range €%d - €%d", from, to);
        }
        return sb.append(String.format("Number of car models: %d", carCounter)).toString();
    }

    // ------------------------------------------------------------------------------------------

    // ALL PRIVATE METHODS go to below

    private String getCarInfo(int carId) throws Exception {
        if (carId > carModels.length - 1 || carId < 0) {
            throw new Exception(String.format("No car with id %d exists", carId));
        }
        return String.format("Make: %s\nModel: %s\nFuel type: %s\nEmissions: %.2f\nPrice: €%d\n", manufacturers[carId],
                carModels[carId], fuelTypes[carId], emissions[carId], prices[carId]);
    }

    // -------------------------------------

    private String getCarInfo(String manufacturer, String carModel, int modelYear) {
        return String.format("Make: %s\nModel: %s\nYear: %d\n", manufacturer, carModel, modelYear);
    }

    // -------------------------------------

    private double adjustTaxRate(int carId, int baseYear) {
        final double BASE_TAX_RATE = 5.0;

        double taxRate = BASE_TAX_RATE; // Base tax rate of 5%

        taxRate = adjustTaxBasedOnFuel(carId, taxRate);
        taxRate = adjustTaxBasedOnEmission(carId, taxRate);
        taxRate = adjustTaxBasedOnModelYear(carId, baseYear, taxRate);

        return taxRate;
    }

    // -------------------------------------

    private double adjustTaxBasedOnFuel(int carId, double taxRate) {
        final double ELECTRIC_ADJUSTMENT = -1.5;
        final double HYBRID_ADJUSTMENT = -0.5;
        final double PETROL_ADJUSTMENT = 1.5;
        // Adjust for fuel type
        switch (fuelTypes[carId]) {
            case FUEL_TYPE_ELECTRIC -> taxRate += ELECTRIC_ADJUSTMENT;
            case FUEL_TYPE_HYBRID -> taxRate += HYBRID_ADJUSTMENT;
            case FUEL_TYPE_PETROL -> taxRate += PETROL_ADJUSTMENT;
        }
        return taxRate;
    }

    // -------------------------------------

    private double adjustTaxBasedOnEmission(int carId, double taxRate) {
        final double EMISSION_MULTIPLIER = 10.0; // For each 0.01 in emissions, increase tax rate by 0.1%
        // Adjust for emissions
        double emissionAdjustment = emissions[carId] * EMISSION_MULTIPLIER; // For each 0.01 in emissions, increase tax
        // rate by 0.1%
        taxRate += emissionAdjustment;
        return taxRate;
    }

    // -------------------------------------

    private double adjustTaxBasedOnModelYear(int carId, int baseYear, double taxRate) {
        final double MODEL_YEAR_MULTIPLIER = 0.2; // Reduce 0.2% for each year older than the base year
        // Adjust for model year
        int yearDifference = baseYear - modelYears[carId];
        double modelYearAdjustment = yearDifference * MODEL_YEAR_MULTIPLIER; // Reduce 0.2% for each year older than the
        // base year
        taxRate -= modelYearAdjustment;

        return taxRate;
    }

    // -------------------------------------

    private double calculateTaxAmountForCar(int carId, double taxRate) {
        double taxAmount = prices[carId] * (taxRate / 100); // Calculate tax based on final rate
        return taxAmount;
    }

    // -------------------------------------

    private double calculateAnnualTax(int carId, int baseYear) {
        final double BASE_FEE = 50.0;
        double annualTax = BASE_FEE; // Base annual tax fee

        annualTax = adjustAnnualTaxByFuel(carId, annualTax);
        annualTax = adjustAnnualTaxByEmissions(carId, annualTax);
        annualTax = adjustAnnualTaxByModelYear(carId, annualTax, baseYear);

        annualTax = applyMinimumTax(annualTax, BASE_FEE);

        return annualTax;
    }

    // -------------------------------------

    private double adjustAnnualTaxByFuel(int carId, double annualTax) {
        final double HYBRID_ADJUSTMENT = 20.0;
        final double PETROL_ADJUSTMENT = 30.0;
        // Adjust for fuel type
        switch (fuelTypes[carId]) {
            case FUEL_TYPE_HYBRID -> annualTax += HYBRID_ADJUSTMENT;
            case FUEL_TYPE_PETROL -> annualTax += PETROL_ADJUSTMENT;
        }
        return annualTax;
    }

    // -------------------------------------

    private double adjustAnnualTaxByEmissions(int carId, double annualTax) {
        final double EMISSION_MULTIPLIER = 500.0; // For each 0.01 in emissions, increase tax by €5
        // Adjust for emissions
        double emissionAdjustment = emissions[carId] * EMISSION_MULTIPLIER; // For each 0.01 in emissions, increase tax
        // by €5
        annualTax += emissionAdjustment;

        return annualTax;
    }

    // -------------------------------------

    private double adjustAnnualTaxByModelYear(int carId, double annualTax, int baseYear) {
        final double MODEL_YEAR_MULTIPLIER = 2.0; // Reduce €2 for each year older than the base year
        // Adjust for model year
        int yearDifference = baseYear - modelYears[carId];
        double modelYearAdjustment = yearDifference * MODEL_YEAR_MULTIPLIER; // Reduce €2 for each year older than the
        // base year
        annualTax -= modelYearAdjustment;

        return annualTax;
    }

    // -------------------------------------

    private double applyMinimumTax(double annualTax, double BASE_FEE) {
        // Ensure the final annual tax is at least the base fee
        boolean annualTaxIsBelowBaseFee = annualTax < BASE_FEE;
        if (annualTaxIsBelowBaseFee) {
            annualTax = BASE_FEE;
        }

        return annualTax;
    }

}
