package com.hotelapi.repository;

import com.hotelapi.entity.Hotel;
import com.hotelapi.entity.Product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for ProductRepository
 * using H2 in-memory database
 */
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private HotelRepository hotelRepository;

    private Hotel hotel;

    @BeforeEach
    void setUp() {
        // set up a test hotel entity in H2
        hotel = new Hotel();
        hotel.setHotelName("Test Hotel");
        hotel.setAddress("Test Address");
        hotel = hotelRepository.save(hotel);
    }

    @Test
    void testFindByProductNameAndHotel() {
        // given
        Product product = Product.builder()
                .productName("Paneer Tikka")
                .productCode("PTK001")
                .category("Food")
                .quantity(5)
                .price(200.0)
                .isActive(true)
                .hotel(hotel)
                .build();
        productRepository.save(product);

        // when
        Optional<Product> found = productRepository.findByProductNameAndHotel("Paneer Tikka", hotel);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getProductCode()).isEqualTo("PTK001");
    }

    @Test
    void testFindByProductCodeAndHotel() {
        // given
        Product product = Product.builder()
                .productName("Veg Biryani")
                .productCode("VBY001")
                .category("Food")
                .quantity(8)
                .price(180.0)
                .isActive(true)
                .hotel(hotel)
                .build();
        productRepository.save(product);

        // when
        Optional<Product> found = productRepository.findByProductCodeAndHotel("VBY001", hotel);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getProductName()).isEqualTo("Veg Biryani");
    }
}
