package com.hotelapi.service;

import com.hotelapi.dto.ProductRequest;
import com.hotelapi.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(Long hotelId, ProductRequest request);

    ProductResponse updateProduct(Long hotelId, Long productId, ProductRequest request);

    ProductResponse getProductById(Long hotelId, Long productId);

    List<ProductResponse> getAllProducts(Long hotelId);

    void deleteProduct(Long hotelId, Long productId);
}
