package ee.bcs.carportal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.bcs.carportal.service.car.dto.CarDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Enable ordering with @Order
class CarControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    // // Add Car Test
    @Test
    @Order(1) // This test will run first
    public void shouldAddCar() throws Exception {
        CarDto newCar = new CarDto(1, 1, "Model S", 2022, new BigDecimal("0.00"),
                55000);
        String json = new ObjectMapper().writeValueAsString(newCar);

        mockMvc.perform(post("/api/v1/car")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    // Find Car Info Test
    @Test
    @Order(2) // This test will run second
    public void shouldReturnCarInfo() throws Exception {
        // Load the expected JSON from the file
        Path path = Paths.get(ResourceUtils.getFile("classpath:files/expected_car_info.json").toURI());
        String expectedJson = Files.readString(path);

        mockMvc.perform(get("/api/v1/car/{carId}", 1)) // Replace 1 with a valid car ID
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));
    }

    // // Find Car Detailed Info Test
    @Test
    @Order(3) // This test will run third
    public void shouldReturnCarDetailedInfo() throws Exception {
        // Load the expected JSON from the file
        Path path = Paths.get(ResourceUtils.getFile("classpath:files/expected_car_detailed_info.json").toURI());
        String expectedJson = Files.readString(path);

        mockMvc.perform(get("/api/v1/car/detailed-info/{carId}", 1)) // Replace 1 with a valid car ID
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));
    }

    // @Test
    @Order(4) // This test will run fourth
    public void shouldReturnAllCars() throws Exception {
        // Load the expected JSON from the file
        Path path = Paths.get(ResourceUtils.getFile("classpath:files/expected_all_cars.json").toURI());
        String expectedJson = Files.readString(path);

        // Assert: Perform the GET request and check the response
        mockMvc.perform(get("/api/v1/cars/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));
    }

    // // Update Car Test
    @Test
    @Order(5) // This test will run fifth
    public void shouldUpdateCar() throws Exception {
        CarDto updatedCar = new CarDto(3, 2, "Model 3", 2020, new BigDecimal("5.55"), 3000);
        String json = new ObjectMapper().writeValueAsString(updatedCar);

        mockMvc.perform(put("/api/v1/car/{carId}", 1) // Replace ID with a valid car ID
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    // // Delete Car Test
    @Test
    @Order(6) // This test will run last
    public void shouldDeleteCar() throws Exception {
        mockMvc.perform(delete("/api/v1/car/{carId}", 6)) // Replace ID with a valid car ID
                .andExpect(status().isOk());
    }

}