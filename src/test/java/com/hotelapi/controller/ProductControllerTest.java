package com.hotelapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelapi.dto.ProductRequest;
import com.hotelapi.dto.ProductResponse;
import com.hotelapi.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for ProductController using MockMvc + Mockito
 */
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;  // to simulate HTTP requests to the controller

    @MockBean
    private ProductService productService;  // mock the service layer

    @Autowired
    private ObjectMapper objectMapper; // for JSON serialization

    private ProductResponse sampleProductResponse;

    @BeforeEach
    void setUp() {
        // Create a reusable sample product response for mocking
        sampleProductResponse = new ProductResponse();
        sampleProductResponse.setId(1L);
        sampleProductResponse.setProductName("Paneer Tikka");
        sampleProductResponse.setProductCode("PTK001");
        sampleProductResponse.setCategory("Food");
        sampleProductResponse.setQuantity(10);
        sampleProductResponse.setPrice(250.0);
        sampleProductResponse.setIsActive(true);
        sampleProductResponse.setHotelId(1L);
    }

    @Test
    void testCreateProduct() throws Exception {
        // Build a sample product request
        ProductRequest request = new ProductRequest();
        request.setProductName("Paneer Tikka");
        request.setProductCode("PTK001");
        request.setCategory("Food");
        request.setQuantity(10);
        request.setPrice(250.0);
        request.setIsActive(true);

        // Mock the service to return a prepared response
        Mockito.when(productService.createProduct(eq(1L), any(ProductRequest.class)))
                .thenReturn(sampleProductResponse);

        // Simulate HTTP POST call to /api/products with JSON body
        mockMvc.perform(post("/api/products?hotelId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // expect HTTP 200
                .andExpect(jsonPath("$.productName").value("Paneer Tikka")); // verify product name in response
    }

    // More tests can be added in the same way for update, get, delete
}
