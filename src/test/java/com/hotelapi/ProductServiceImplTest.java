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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Hotel sampleHotel;
    private Product sampleProduct;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleHotel = Hotel.builder().hotelId(1L).hotelName("Hotel Royal").build();
        sampleProduct = Product.builder()
                .id(1L)
                .productName("Paneer Tikka")
                .productCode("PT001")
                .category("Food")
                .quantity(10)
                .price(250.0)
                .isActive(true)
                .hotel(sampleHotel)
                .build();

        productRequest = new ProductRequest();
        productRequest.setProductName("Paneer Tikka");
        productRequest.setProductCode("PT001");
        productRequest.setCategory("Food");
        productRequest.setQuantity(10);
        productRequest.setPrice(250.0);
        productRequest.setIsActive(true);
    }

    @Test
    void testCreateProduct_success() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(sampleHotel));
        when(productRepository.findByProductNameAndHotel("Paneer Tikka", sampleHotel)).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductResponse response = productService.createProduct(1L, productRequest);

        assertNotNull(response);
        assertEquals("Paneer Tikka", response.getProductName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testCreateProduct_duplicateName() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(sampleHotel));
        when(productRepository.findByProductNameAndHotel("Paneer Tikka", sampleHotel))
                .thenReturn(Optional.of(sampleProduct));

        assertThrows(InvalidInputException.class, () -> productService.createProduct(1L, productRequest));
    }

    @Test
    void testUpdateProduct_success() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(sampleHotel));
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.findByProductNameAndHotel("Paneer Tikka", sampleHotel)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductResponse response = productService.updateProduct(1L, 1L, productRequest);

        assertNotNull(response);
        assertEquals("Paneer Tikka", response.getProductName());
    }

    @Test
    void testUpdateProduct_notFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(sampleHotel));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, 1L, productRequest));
    }

    @Test
    void testGetProductById_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        ProductResponse response = productService.getProductById(1L, 1L);
        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void testGetProductById_notFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L, 1L));
    }

    @Test
    void testGetAllProducts_success() {
        when(productRepository.findAll(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(sampleProduct)));

        List<ProductResponse> products = productService.getAllProducts(1L, 0, 10);
        assertEquals(1, products.size());
        assertEquals("Paneer Tikka", products.get(0).getProductName());
    }

    @Test
    void testDeleteProduct_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        doNothing().when(productRepository).delete(sampleProduct);

        assertDoesNotThrow(() -> productService.deleteProduct(1L, 1L));
        verify(productRepository).delete(sampleProduct);
    }

    @Test
    void testDeleteProduct_notFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L, 1L));
    }
}
