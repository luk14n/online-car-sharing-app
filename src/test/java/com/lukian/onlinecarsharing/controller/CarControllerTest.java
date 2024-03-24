package com.lukian.onlinecarsharing.controller;

import static org.junit.Assert.assertThrows;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukian.onlinecarsharing.dto.car.CreateCarRequestDto;
import com.lukian.onlinecarsharing.exception.EntityNotFoundException;
import com.lukian.onlinecarsharing.model.Car;
import com.lukian.onlinecarsharing.service.CarService;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarControllerTest {
    private static final String INSERT_CARS_SCRIPT =
            "database/cars/insert-cars.sql";
    private static final String CLEAN_UP_CARS_DATA_SCRIPT =
            "database/cars/clean-up-car-data.sql";
    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CarService carService;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext,
                          @Autowired DataSource dataSource) {
        teardown(dataSource);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) throws SQLException {
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_CARS_SCRIPT));
        }
    }

    @Test
    @DisplayName("Create car with valid data")
    @WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
    void create_CreateBookWithValidData_ShouldReturnBookDto() throws Exception {
        CreateCarRequestDto requestDto = new CreateCarRequestDto(
                "Test Model",
                "Test Brand",
                Car.Type.SUV,
                100,
                BigDecimal.valueOf(20.00)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model")
                        .value(requestDto.model()))
                .andExpect(jsonPath("$.brand")
                        .value(requestDto.brand()))
                .andExpect(jsonPath("$.type")
                        .value(requestDto.type().toString()))
                .andExpect(jsonPath("$.inventory")
                        .value(requestDto.inventory()))
                .andExpect(jsonPath("$.dailyFee")
                        .value(requestDto.dailyFee()));
    }

    @Test
    @DisplayName("Get all cars as user with pagination")
    @WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
    void get_GetAllCarsAsUserWithPagination_ShouldReturnCarDtoList() throws Exception {
        int page = 0;
        int size = 10;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cars")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].model").value("Model1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].brand").value("Brand1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value("SEDAN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].inventory").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dailyFee").value(50.00))

                .andExpect(MockMvcResultMatchers.jsonPath("$[1].model").value("Model2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].brand").value("Brand2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].type").value("SUV"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].inventory").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].dailyFee").value(70.00))

                .andExpect(MockMvcResultMatchers.jsonPath("$[2].model").value("Model3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].brand").value("Brand3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].type").value("HATCHBACK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].inventory").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].dailyFee").value(40.00))

                .andExpect(MockMvcResultMatchers.jsonPath("$[3].model").value("Model4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].brand").value("Brand4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].type").value("UNIVERSAL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].inventory").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].dailyFee").value(60.00));
    }

    @Test
    @DisplayName("Delete car by ID as admin")
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void delete_DeleteCarByIdAsAdmin_ShouldReturnNoContent() throws Exception {
        Long carIdToDelete = 4L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cars/{id}", carIdToDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertThrows(EntityNotFoundException.class,
                () -> carService.finById(carIdToDelete));
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(CLEAN_UP_CARS_DATA_SCRIPT));
        }
    }
}
