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

    private Hotel hotel;
    private Product product;
    private ProductRequest request;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setHotelId(1L);

        product = Product.builder()
                .id(10L).productCode("PTK001").productName("Paneer Tikka")
                .category("Food").quantity(10).price(250.0)
                .isActive(true).hotel(hotel).build();

        request = new ProductRequest();
        request.setProductName("Paneer Tikka");
        request.setProductCode("PTK001");
        request.setCategory("Food");
        request.setQuantity(10);
        request.setPrice(250.0);
        request.setIsActive(true);
    }

    // createProduct - success
    @Test
    void createProduct_Success() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(productRepository.findByProductNameAndHotel("Paneer Tikka", hotel)).thenReturn(Optional.empty());
        when(productRepository.save(any())).thenAnswer(inv -> {
            Product saved = inv.getArgument(0);
            saved.setId(100L);
            return saved;
        });

        ProductResponse response = productService.createProduct(1L, request);
        assertEquals("Paneer Tikka", response.getProductName());
        assertEquals(1L, response.getHotelId());
    }

    // createProduct - hotel not found
    @Test
    void createProduct_HotelNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(InvalidInputException.class, () -> productService.createProduct(1L, request));
    }

    // createProduct - duplicate product name
    @Test
    void createProduct_DuplicateProduct() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(productRepository.findByProductNameAndHotel("Paneer Tikka", hotel)).thenReturn(Optional.of(product));
        assertThrows(InvalidInputException.class, () -> productService.createProduct(1L, request));
    }

    // updateProduct - success
    @Test
    void updateProduct_Success() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(productRepository.findByProductNameAndHotel("Paneer Tikka", hotel)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);

        ProductResponse response = productService.updateProduct(1L, 10L, request);
        assertEquals(10L, response.getId());
    }

    // updateProduct - hotel not found
    @Test
    void updateProduct_HotelNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(InvalidInputException.class, () -> productService.updateProduct(1L, 10L, request));
    }

    // updateProduct - product not found
    @Test
    void updateProduct_ProductNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(productRepository.findById(10L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, 10L, request));
    }

    // updateProduct - duplicate product name with different ID
    @Test
    void updateProduct_DuplicateOtherId() {
        Product another = Product.builder().id(99L).productName("Paneer Tikka").hotel(hotel).build();
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(productRepository.findByProductNameAndHotel("Paneer Tikka", hotel)).thenReturn(Optional.of(another));
        assertThrows(InvalidInputException.class, () -> productService.updateProduct(1L, 10L, request));
    }

    // getProductById - success
    @Test
    void getProductById_Success() {
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        ProductResponse response = productService.getProductById(1L, 10L);
        assertEquals("Paneer Tikka", response.getProductName());
    }

    // getProductById - not found
    @Test
    void getProductById_NotFound() {
        when(productRepository.findById(10L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L, 10L));
    }

    // getAllProducts - success
    @Test
    void getAllProducts_Success() {
        when(productRepository.findByHotelHotelId(1L)).thenReturn(List.of(product));
        List<ProductResponse> responses = productService.getAllProducts(1L);
        assertEquals(1, responses.size());
    }

    // getProductsByUserId - success
    @Test
    void getProductsByUserId_Success() {
        when(productRepository.findByUserId(5L)).thenReturn(List.of(product));
        List<ProductResponse> responses = productService.getProductsByUserId(5L);
        assertEquals("Paneer Tikka", responses.get(0).getProductName());
    }

    // deleteProduct - success
    @Test
    void deleteProduct_Success() {
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        assertDoesNotThrow(() -> productService.deleteProduct(1L, 10L));
        verify(productRepository, times(1)).delete(product);
    }

    // deleteProduct - product not found
    @Test
    void deleteProduct_NotFound() {
        when(productRepository.findById(10L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L, 10L));
    }

    // deleteProduct - hotel mismatch
    @Test
    void deleteProduct_HotelMismatch() {
        Hotel otherHotel = new Hotel();
        otherHotel.setHotelId(2L);
        product.setHotel(otherHotel);

        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L, 10L));
    }
}
