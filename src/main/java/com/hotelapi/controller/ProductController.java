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

/**
 * TICKET 2: HOTEL PRODUCT MANAGEMENT CONTROLLER
 * This controller handles all CRUD operations and filtering for products
 * as per the requirements specified in Ticket 2.
 * 
 * Required Endpoints:
 * - POST /api/hotel/products - Create a new product
 * - GET /api/hotel/products?userId=123 - Get products by user ID
 * - GET /api/hotel/products/{id} - Get product by ID
 * - PUT /api/hotel/products/{id} - Update product
 * - DELETE /api/hotel/products/{id} - Delete product
 */
@RestController
@RequestMapping("/api/hotel/products")
@RequiredArgsConstructor
@Tag(name = "Hotel Product Management", description = "CRUD operations and filtering for hotel products")
public class ProductController {

    private final ProductService productService;

    /**
     * CREATE PRODUCT - POST /api/hotel/products
     * Creates a new product for a specific hotel.
     * Validates that the hotel exists and product name is unique within the hotel.
     */
    @Operation(summary = "Create a new product", 
               description = "Creates a new product for a hotel with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product created successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request - validation failed"),
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
     * UPDATE PRODUCT - PUT /api/hotel/products/{id}
     * Updates an existing product by its ID.
     * Validates product existence and hotel ownership before updating.
     */
    @Operation(summary = "Update an existing product by its ID", 
               description = "Updates product details with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request - validation failed"),
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
        ProductResponse existing = productService.getProductById(hotelId, id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        ProductResponse response = productService.updateProduct(hotelId, id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET PRODUCT BY ID - GET /api/hotel/products/{id}
     * Retrieves a product by its ID using hotel and product information.
     * Confirms ownership by the specified hotel.
     */
    @Operation(summary = "Get details of a product by its ID", 
               description = "Retrieves product information using hotel validation")
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
     * GET PRODUCTS BY USER - GET /api/hotel/products?userId=123
     * Retrieves all products available to a specific user.
     * Utilizes user ID for filtering.
     */
    @Operation(summary = "Get a list of all products for a user", 
               description = "Retrieves products by user ID for personalized access")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(
            @Parameter(description = "User ID", example = "123")
            @RequestParam Long userId
    ) {
        List<ProductResponse> products = productService.getProductsByUserId(userId);
        return ResponseEntity.ok(products);
    }

    /**
     * DELETE PRODUCT - DELETE /api/hotel/products/{id}
     * Deletes a product using its ID from a specified hotel.
     * Checks the product's existence and ownership before deletion.
     */
    @Operation(summary = "Delete a product by its ID", 
               description = "Removes a product ensuring proper validation")
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
