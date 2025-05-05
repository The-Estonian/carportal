package ee.bcs.carportal.controller;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
class CarControllerMvcTest {

    @Resource
    private MockMvc mockMvc;

    @Test
    public void shouldReturnAllModels() throws Exception {
        mockMvc.perform(get("/api/v1/cars/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(
                        "[{\"emission\":0.0,\"fuelType\":\"Electric\",\"year\":2020,\"price\":44000,\"model\":\"Model 3\",\"id\":1,\"manufacturer\":\"Tesla\"},{\"emission\":0.05,\"fuelType\":\"Petrol\",\"year\":2021,\"price\":25000,\"model\":\"Civic\",\"id\":2,\"manufacturer\":\"Honda\"},{\"emission\":0.04,\"fuelType\":\"Petrol\",\"year\":2022,\"price\":28000,\"model\":\"Camry\",\"id\":3,\"manufacturer\":\"Toyota\"},{\"emission\":0.1,\"fuelType\":\"Petrol\",\"year\":2023,\"price\":45000,\"model\":\"F-150\",\"id\":4,\"manufacturer\":\"Ford\"},{\"emission\":0.03,\"fuelType\":\"Hybrid\",\"year\":2020,\"price\":30000,\"model\":\"Prius\",\"id\":5,\"manufacturer\":\"Toyota\"}]"));
    }

    @Test
    public void shouldReturn3ModelsInRange28000To44000() throws Exception {
        mockMvc.perform(get("/api/v1/cars/price-range")
                .param("from", "28000")
                .param("to", "44000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(
                        "Cars in price range €28000 - €44000:\n\nMake: Tesla\nModel: Model 3\nFuel type: Electric\nEmission: 0.0\nPrice: €44000\n\nMake: Toyota\nModel: Camry\nFuel type: Petrol\nEmission: 0.04\nPrice: €28000\n\nMake: Toyota\nModel: Prius\nFuel type: Hybrid\nEmission: 0.03\nPrice: €30000\n\nNumber of car models: 3"));
    }

    @Test
    public void shouldReturn2ModelsInRange35000To45000() throws Exception {
        mockMvc.perform(get("/api/v1/cars/price-range")
                .param("from", "35000")
                .param("to", "45000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(
                        "Cars in price range €35000 - €45000:\n\nMake: Tesla\nModel: Model 3\nFuel type: Electric\nEmission: 0.0\nPrice: €44000\n\nMake: Ford\nModel: F-150\nFuel type: Petrol\nEmission: 0.1\nPrice: €45000\n\nNumber of car models: 2"));
    }

    @Test
    public void shouldReturnNoModelsInRange50000To60000() throws Exception {
        mockMvc.perform(get("/api/v1/cars/price-range")
                .param("from", "50000")
                .param("to", "60000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("No cars found in price range €50000 - €60000"));
    }

    @Test
    public void shouldReturnGreen2ModelsInRange28000To44000() throws Exception {
        mockMvc.perform(get("/api/v1/cars/green/price-range")
                .param("from", "28000")
                .param("to", "44000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(
                        "Green cars in price range €28000 - €44000: Model 3 (Electric), Prius (Hybrid); (number of car models: 2)"));
    }

    @Test
    public void shouldReturnNoGreenCarsInRange50000To60000() throws Exception {
        mockMvc.perform(get("/api/v1/cars/green/price-range")
                .param("from", "50000")
                .param("to", "60000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Green cars in price range €50000 - €60000; (number of car models: 0)"));
    }

    @Test
    public void shouldReturnCorrectTeslaModel3RegistrationTaxRateAndAmount() throws Exception {
        mockMvc.perform(get("/api/v1/car/0/registration-tax")
                .param("baseYear", "2025"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(
                        "The registration tax rate for 2020 Tesla Model 3 is 2.5% with total tax amount €1100"));
    }

    @Test
    public void shouldReturnCorrectHondaCivicRegistrationTaxRateAndAmount() throws Exception {
        mockMvc.perform(get("/api/v1/car/1/registration-tax")
                .param("baseYear", "2025"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content()
                        .string("The registration tax rate for 2021 Honda Civic is 6.2% with total tax amount €1550"));
    }

    @Test
    public void shouldReturnCorrectToyotaCamryRegistrationTaxRateAndAmount() throws Exception {
        mockMvc.perform(get("/api/v1/car/2/registration-tax")
                .param("baseYear", "2025"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content()
                        .string("The registration tax rate for 2022 Toyota Camry is 6.3% with total tax amount €1764"));
    }

    @Test
    public void shouldReturnCorrectFordF150RegistrationTaxRateAndAmount() throws Exception {
        mockMvc.perform(get("/api/v1/car/3/registration-tax")
                .param("baseYear", "2025"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content()
                        .string("The registration tax rate for 2023 Ford F-150 is 7.1% with total tax amount €3195"));
    }

    @Test
    public void shouldReturnCorrectToyotaPriusRegistrationTaxRateAndAmount() throws Exception {
        mockMvc.perform(get("/api/v1/car/4/registration-tax")
                .param("baseYear", "2025"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content()
                        .string("The registration tax rate for 2020 Toyota Prius is 3.8% with total tax amount €1140"));
    }

    @Test
    public void shouldReturnCorrectTeslaModel3AnnualTaxFee() throws Exception {
        mockMvc.perform(get("/api/v1/car/0/annual-tax")
                .param("baseYear", "2025"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("The annual tax for 2020 Tesla Model 3 is €50"));
    }

    @Test
    public void shouldReturnCorrectHondaCivicAnnualTaxFee() throws Exception {
        mockMvc.perform(get("/api/v1/car/1/annual-tax")
                .param("baseYear", "2025"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("The annual tax for 2021 Honda Civic is €97"));
    }

    @Test
    public void shouldReturnCorrectToyotaCamryAnnualTaxFee() throws Exception {
        mockMvc.perform(get("/api/v1/car/2/annual-tax")
                .param("baseYear", "2025"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("The annual tax for 2022 Toyota Camry is €94"));
    }

    @Test
    public void shouldReturnCorrectFordF150AnnualTaxFee() throws Exception {
        mockMvc.perform(get("/api/v1/car/3/annual-tax")
                .param("baseYear", "2025"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("The annual tax for 2023 Ford F-150 is €126"));
    }

    @Test
    public void shouldReturnCorrectToyotaPriusAnnualTaxFee() throws Exception {
        mockMvc.perform(get("/api/v1/car/4/annual-tax")
                .param("baseYear", "2025"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("The annual tax for 2020 Toyota Prius is €75"));
    }

    @Test
    public void shouldReturnRandomCarBasicInfo() throws Exception {
        mockMvc.perform(get("/api/v1/car/random/basic-info"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(containsString("Make:")))
                .andExpect(content().string(containsString("Model:")))
                .andExpect(content().string(containsString("Year:")));
    }

    @Test
    public void shouldReturnRandomCarDetailedInfo() throws Exception {
        mockMvc.perform(get("/api/v1/car/random/detailed-info"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(containsString("Make:")))
                .andExpect(content().string(containsString("Model:")))
                .andExpect(content().string(containsString("Fuel type:")))
                .andExpect(content().string(containsString("Emission:")))
                .andExpect(content().string(containsString("Price:")));
    }

    @Test
    public void shouldReturnCarDetailedInfoById() throws Exception {
        mockMvc.perform(get("/api/v1/car/0/detailed-info"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content()
                        .string("Make: Tesla\nModel: Model 3\nFuel type: Electric\nEmission: 0.0\nPrice: €44000"));
    }

    @Test
    public void shouldHandleNonExistentCarIdForBasicInfo() throws Exception {
        mockMvc.perform(get("/api/v1/car/5/basic-info"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("No car with id 5 exists"));
    }

    @Test
    public void shouldHandleNonExistentCarIdForDetailedInfo() throws Exception {
        mockMvc.perform(get("/api/v1/car/5/detailed-info"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("No car with id 5 exists"));
    }
}
