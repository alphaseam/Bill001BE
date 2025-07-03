package com.hotelapi.service.impl;

import com.hotelapi.dto.ProductRequest;
import com.hotelapi.dto.ProductResponse;
import com.hotelapi.entity.Hotel;
import com.hotelapi.entity.Product;
import com.hotelapi.exception.InvalidInputException;
import com.hotelapi.exception.ProductNotFoundException;
import com.hotelapi.repository.HotelRepository;
import com.hotelapi.repository.ProductRepository;
import com.hotelapi.service.ProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final HotelRepository hotelRepository;

    @Override
    public ProductResponse createProduct(Long hotelId, ProductRequest request) {
        // validate if the hotel exists
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new InvalidInputException("Hotel not found"));

        // check if a product with the same name already exists in this hotel
        productRepository.findByProductNameAndHotel(request.getProductName(), hotel)
                .ifPresent(existing -> {
                    throw new InvalidInputException("Product with this name already exists in this hotel");
                });

        // create and save the new product
        Product product = Product.builder()
                .productName(request.getProductName())
                .productCode(request.getProductCode())
                .category(request.getCategory())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .isActive(request.getIsActive())
                .hotel(hotel)
                .build();

        product = productRepository.save(product);

        // convert entity to response DTO
        return mapToResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Long hotelId, Long productId, ProductRequest request) {
        // validate the hotel exists
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new InvalidInputException("Hotel not found"));

        // validate the product exists and belongs to this hotel
        Product product = productRepository.findById(productId)
                .filter(p -> p.getHotel().getHotelId().equals(hotelId))
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        // check if another product with the same name already exists
        productRepository.findByProductNameAndHotel(request.getProductName(), hotel)
                .ifPresent(existing -> {
                    if (!existing.getId().equals(productId)) {
                        throw new InvalidInputException("Another product with this name already exists");
                    }
                });

        // update product details
        product.setProductName(request.getProductName());
        product.setProductCode(request.getProductCode());
        product.setCategory(request.getCategory());
        product.setQuantity(request.getQuantity());
        product.setPrice(request.getPrice());
        product.setIsActive(request.getIsActive());

        product = productRepository.save(product);

        return mapToResponse(product);
    }

    @Override
    public ProductResponse getProductById(Long hotelId, Long productId) {
        // find the product and validate ownership
        Product product = productRepository.findById(productId)
                .filter(p -> p.getHotel().getHotelId().equals(hotelId))
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        return mapToResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts(Long hotelId) {
        // fetch all products belonging to this hotel
        List<Product> products = productRepository.findByHotelHotelId(hotelId);

        return products.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProduct(Long hotelId, Long productId) {
        // validate product existence and ownership
        Product product = productRepository.findById(productId)
                .filter(p -> p.getHotel().getHotelId().equals(hotelId))
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        // Option 1: permanently delete the product
        productRepository.delete(product);

        // Option 2: mark as inactive instead of hard delete
        // product.setIsActive(false);
        // productRepository.save(product);
    }

    /**
     * Converts Product entity to ProductResponse DTO
     */
    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setProductName(product.getProductName());
        response.setProductCode(product.getProductCode());
        response.setCategory(product.getCategory());
        response.setQuantity(product.getQuantity());
        response.setPrice(product.getPrice());
        response.setIsActive(product.getIsActive());
        response.setHotelId(product.getHotel().getHotelId());
        return response;
    }
}
