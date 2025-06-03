package ee.bcs.carportal.controller;


import ee.bcs.carportal.repository.CarRepositoryImpl;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CarControllerMvcTest {

    @Resource
    private CarRepositoryImpl carRepository;  // Inject the real service

    @Resource
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Reset the cars data
        carRepository.resetData();
    }

    @Test
    public void shouldReturnAllCars() throws Exception {
        // Load the expected JSON from the file
        Path path = Paths.get(ResourceUtils.getFile("classpath:files/expected_cars.json").toURI());
        String expectedJson = Files.readString(path);

        // Assert: Perform the GET request and check the response
        mockMvc.perform(get("/api/v1/cars/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void shouldReturnCarsInPriceRange() throws Exception {
        // Load the expected JSON from the file
        Path path = Paths.get(ResourceUtils.getFile("classpath:files/expected_cars_price_range.json").toURI());
        String expectedJson = Files.readString(path);

        // Assert: Perform the GET request and check the response
        mockMvc.perform(get("/api/v1/cars/price-range")
                        .param("from", "28000")
                        .param("to", "45000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void shouldReturnGreenCarsInPriceRange() throws Exception {
        // Load the expected JSON from the file
        Path path = Paths.get(ResourceUtils.getFile("classpath:files/expected_green_cars_price_range.json").toURI());
        String expectedJson = Files.readString(path);

        // Assert: Perform the GET request and check the response
        mockMvc.perform(get("/api/v1/cars/green/price-range")
                        .param("from", "28000")
                        .param("to", "45000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void shouldReturnRandomCarBasicInfo() throws Exception {
        mockMvc.perform(get("/api/v1/car/random/basic-info"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(containsString("\"carModel\":")))
                .andExpect(content().string(containsString("\"manufacturer\":")))
                .andExpect(content().string(containsString("\"modelYear\":")))
                .andExpect(content().string(containsString("\"fuelType\":")))
                .andExpect(content().string(containsString("\"emissions\":")))
                .andExpect(content().string(containsString("\"price\":")));
    }

    @Test
    public void shouldReturnRandomCarDetailedInfo() throws Exception {
        mockMvc.perform(get("/api/v1/car/random/detailed-info"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(containsString("\"carModel\":")))
                .andExpect(content().string(containsString("\"manufacturer\":")))
                .andExpect(content().string(containsString("\"modelYear\":")))
                .andExpect(content().string(containsString("\"fuelType\":")))
                .andExpect(content().string(containsString("\"emissions\":")))
                .andExpect(content().string(containsString("\"price\":")));
    }

    @Test
    public void shouldReturnCarBasicInfoById() throws Exception {
        // Load the expected JSON from the file
        Path path = Paths.get(ResourceUtils.getFile("classpath:files/expected_car_basic_info_by_id.json").toURI());
        String expectedJson = Files.readString(path);

        // Assert: Perform the GET request and check the response
        mockMvc.perform(get("/api/v1/car/1/basic-info"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void shouldReturnCarDetailedInfoById() throws Exception {
        // Load the expected JSON from the file
        Path path = Paths.get(ResourceUtils.getFile("classpath:files/expected_car_detailed_info_by_id.json").toURI());
        String expectedJson = Files.readString(path);

        // Assert: Perform the GET request and check the response
        mockMvc.perform(get("/api/v1/car/1/detailed-info"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void shouldReturnCarsByRegistrationTaxRange() throws Exception {
        // Load the expected JSON from the file
        Path path = Paths.get(ResourceUtils.getFile("classpath:files/expected_cars_registration_tax_range.json").toURI());
        String expectedJson = Files.readString(path);

        // Assert: Perform the GET request and check the response
        mockMvc.perform(get("/api/v1/tax/cars/registration-tax-range")
                        .param("from", "1100")
                        .param("to", "1200")
                        .param("baseYear", "2025"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void shouldReturnCarsByAnnualTaxRange() throws Exception {
        // Load the expected JSON from the file
        Path path = Paths.get(ResourceUtils.getFile("classpath:files/expected_cars_annual_tax_range.json").toURI());
        String expectedJson = Files.readString(path);

        // Assert: Perform the GET request and check the response
        mockMvc.perform(get("/api/v1/tax/cars/annual-tax-range")
                        .param("from", "50")
                        .param("to", "126")
                        .param("baseYear", "2025"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));
    }


    // new tests
    @Test
    public void shouldAddNewCar() throws Exception {
        // Load the request JSON from the file
        Path requestPath = Paths.get(ResourceUtils.getFile("classpath:files/request_add_car.json").toURI());
        String requestJson = Files.readString(requestPath);

        // Perform the POST request to add a new car
        mockMvc.perform(post("/api/v1/car")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isOk());

        // Load the expected JSON from the file
        Path expectedPath = Paths.get(ResourceUtils.getFile("classpath:files/expected_cars_after_add.json").toURI());
        String expectedJson = Files.readString(expectedPath);

        // Assert that the car list is updated
        mockMvc.perform(get("/api/v1/cars/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void shouldUpdateCar() throws Exception {
        // Load the request JSON from the file
        Path requestPath = Paths.get(ResourceUtils.getFile("classpath:files/request_update_car.json").toURI());
        String requestJson = Files.readString(requestPath);

        // Perform the PUT request to update the car
        mockMvc.perform(put("/api/v1/car/4")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isOk());

        // Load the expected JSON from the file
        Path expectedPath = Paths.get(ResourceUtils.getFile("classpath:files/expected_cars_after_update.json").toURI());
        String expectedJson = Files.readString(expectedPath);

        // Assert that the car list is updated
        mockMvc.perform(get("/api/v1/cars/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void shouldUpdateCarPrice() throws Exception {
        // Perform the PATCH request to update the car price
        mockMvc.perform(patch("/api/v1/car/3")
                        .param("price", "30000"))
                .andExpect(status().isOk());

        // Load the expected JSON from the file
        Path expectedPath = Paths.get(ResourceUtils.getFile("classpath:files/expected_cars_after_price_update.json").toURI());
        String expectedJson = Files.readString(expectedPath);

        // Assert that the car list is updated
        mockMvc.perform(get("/api/v1/cars/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void shouldDeleteCar() throws Exception {
        // Perform the DELETE request to delete a car
        mockMvc.perform(delete("/api/v1/car/3"))
                .andExpect(status().isOk());

        // Load the expected JSON from the file
        Path expectedPath = Paths.get(ResourceUtils.getFile("classpath:files/expected_cars_after_delete.json").toURI());
        String expectedJson = Files.readString(expectedPath);

        // Assert that the car list is updated
        mockMvc.perform(get("/api/v1/cars/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));
    }
}
