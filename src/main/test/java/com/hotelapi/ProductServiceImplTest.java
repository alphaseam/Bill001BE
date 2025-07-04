package com.hotelapi;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private HotelRepository hotelRepository;

    private Hotel sampleHotel;
    private Product sampleProduct;
    private ProductRequest sampleRequest;

    // Setup common data before each test
    @BeforeEach
    void setUp() {
        sampleHotel = new Hotel();
        sampleHotel.setHotelId(1L);
        sampleHotel.setHotelName("Sample Hotel");

        sampleProduct = Product.builder()
                .id(10L)
                .productCode("PTK001")
                .productName("Paneer Tikka")
                .category("Food")
                .quantity(10)
                .price(250.0)
                .isActive(true)
                .hotel(sampleHotel)
                .build();

        sampleRequest = new ProductRequest();
        sampleRequest.setProductName("Paneer Tikka");
        sampleRequest.setProductCode("PTK001");
        sampleRequest.setCategory("Food");
        sampleRequest.setQuantity(10);
        sampleRequest.setPrice(250.0);
        sampleRequest.setIsActive(true);
    }

    // Test: Successfully create a product
    @Test
    void testCreateProduct_Success() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(sampleHotel));
        when(productRepository.findByProductNameAndHotel("Paneer Tikka", sampleHotel)).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId(100L);
            return p;
        });

        ProductResponse response = productService.createProduct(1L, sampleRequest);

        assertNotNull(response);
        assertEquals("Paneer Tikka", response.getProductName());
        assertEquals(1L, response.getHotelId());
    }

    // Test: Create product fails due to missing hotel
    @Test
    void testCreateProduct_HotelNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InvalidInputException.class, () -> productService.createProduct(1L, sampleRequest));
    }

    // Test: Create product fails due to duplicate product name
    @Test
    void testCreateProduct_DuplicateName() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(sampleHotel));
        when(productRepository.findByProductNameAndHotel("Paneer Tikka", sampleHotel))
                .thenReturn(Optional.of(sampleProduct));

        assertThrows(InvalidInputException.class, () -> productService.createProduct(1L, sampleRequest));
    }

    // Test: Successfully update an existing product
    @Test
    void testUpdateProduct_Success() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(sampleHotel));
        when(productRepository.findById(10L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.findByProductNameAndHotel("Paneer Tikka", sampleHotel)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductResponse response = productService.updateProduct(1L, 10L, sampleRequest);

        assertNotNull(response);
        assertEquals("Paneer Tikka", response.getProductName());
        assertEquals(10L, response.getId());
    }

    // Test: Update product fails due to missing hotel
    @Test
    void testUpdateProduct_HotelNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InvalidInputException.class, () -> productService.updateProduct(1L, 10L, sampleRequest));
    }

    // Test: Update product fails due to missing product
    @Test
    void testUpdateProduct_ProductNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(sampleHotel));
        when(productRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, 10L, sampleRequest));
    }

    // Test: Successfully retrieve a product by ID
    @Test
    void testGetProductById_Success() {
        when(productRepository.findById(10L)).thenReturn(Optional.of(sampleProduct));

        ProductResponse response = productService.getProductById(1L, 10L);

        assertNotNull(response);
        assertEquals("Paneer Tikka", response.getProductName());
    }

    // Test: Get product by ID fails if product doesn't exist
    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L, 10L));
    }

    // Test: Retrieve all products for a hotel
    @Test
    void testGetAllProducts() {
        when(productRepository.findByHotelHotelId(1L)).thenReturn(List.of(sampleProduct, sampleProduct));

        List<ProductResponse> responses = productService.getAllProducts(1L);

        assertEquals(2, responses.size());
    }

    // Test: Successfully delete a product
    @Test
    void testDeleteProduct_Success() {
        when(productRepository.findById(10L)).thenReturn(Optional.of(sampleProduct));

        assertDoesNotThrow(() -> productService.deleteProduct(1L, 10L));
        verify(productRepository, times(1)).delete(sampleProduct);
    }
}
