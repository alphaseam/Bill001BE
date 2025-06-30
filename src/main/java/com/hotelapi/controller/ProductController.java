package com.hotelapi.controller;

import com.hotelapi.dto.ProductRequest;
import com.hotelapi.dto.ProductResponse;
import com.hotelapi.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "CRUD operations for products")
public class ProductController {

    private final ProductService productService;

    /**
     * Create a new product
     */
    @Operation(summary = "Create a new product")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product created successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Parameter(description = "Hotel ID to which the product belongs", example = "1")
            @RequestParam Long hotelId,
            @RequestBody ProductRequest request
    ) {
        ProductResponse response = productService.createProduct(hotelId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing product
     */
    @Operation(summary = "Update an existing product by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "403", description = "Access denied"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @Parameter(description = "Hotel ID", example = "1")
            @RequestParam Long hotelId,
            @Parameter(description = "Product ID", example = "10")
            @PathVariable Long id,
            @RequestBody ProductRequest request
    ) {
        // check if product exists
        ProductResponse existing = productService.getProductById(hotelId, id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        ProductResponse response = productService.updateProduct(hotelId, id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get product by ID
     */
    @Operation(summary = "Get details of a product by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product found"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @Parameter(description = "Hotel ID", example = "1")
            @RequestParam Long hotelId,
            @Parameter(description = "Product ID", example = "10")
            @PathVariable Long id
    ) {
        ProductResponse response = productService.getProductById(hotelId, id);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Get all products (with pagination)
     */
    @Operation(summary = "Get a list of all products with pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(
            @Parameter(description = "Hotel ID", example = "1")
            @RequestParam Long hotelId,
            @Parameter(description = "Page number", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        List<ProductResponse> products = productService.getAllProducts(hotelId, page, size);
        return ResponseEntity.ok(products);
    }

    /**
     * Delete a product
     */
    @Operation(summary = "Delete a product by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Hotel ID", example = "1")
            @RequestParam Long hotelId,
            @Parameter(description = "Product ID", example = "10")
            @PathVariable Long id
    ) {
        ProductResponse existing = productService.getProductById(hotelId, id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        productService.deleteProduct(hotelId, id);
        return ResponseEntity.noContent().build();
    }
}
