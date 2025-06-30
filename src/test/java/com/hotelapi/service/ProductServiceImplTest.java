package com.hotelapi.service;

import com.hotelapi.dto.ProductRequest;
import com.hotelapi.dto.ProductResponse;
import com.hotelapi.entity.Hotel;
import com.hotelapi.entity.Product;
import com.hotelapi.exception.InvalidInputException;
import com.hotelapi.exception.ProductNotFoundException;
import com.hotelapi.repository.HotelRepository;
import com.hotelapi.repository.ProductRepository;
import com.hotelapi.service.impl.ProductServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProductServiceImpl with mock repositories
 */
public class ProductServiceImplTest {

    private ProductRepository productRepository;
    private HotelRepository hotelRepository;
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        // initialize mocks
        productRepository = mock(ProductRepository.class);
        hotelRepository = mock(HotelRepository.class);

        // create service with mocked dependencies
        productService = new ProductServiceImpl(productRepository, hotelRepository);
    }

    @Test
    void testCreateProduct_Success() {
        // Setup hotel existence
        Hotel hotel = new Hotel();
        hotel.setHotelId(1L);

        // prepare request
        ProductRequest request = new ProductRequest();
        request.setProductName("Paneer Tikka");
        request.setProductCode("PTK001");
        request.setCategory("Food");
        request.setQuantity(10);
        request.setPrice(250.0);
        request.setIsActive(true);

        // product with same name does not exist
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(productRepository.findByProductNameAndHotel("Paneer Tikka", hotel)).thenReturn(Optional.empty());

        // saved product
        Product savedProduct = Product.builder()
                .id(1L)
                .productName("Paneer Tikka")
                .productCode("PTK001")
                .category("Food")
                .quantity(10)
                .price(250.0)
                .isActive(true)
                .hotel(hotel)
                .build();
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // execute
        ProductResponse response = productService.createProduct(1L, request);

        // verify
        assertNotNull(response);
        assertEquals("Paneer Tikka", response.getProductName());
        assertEquals("PTK001", response.getProductCode());
        verify(productRepository, times(1)).save(any(Product.class));
    }

   

}
