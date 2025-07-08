# API Testing Script for Hotel Bill Management System
# Run this script to test all endpoints

$baseUrl = "http://localhost:8080"
Write-Host "Testing Hotel Bill Management API endpoints..." -ForegroundColor Green

# Function to make HTTP requests
function Test-Endpoint {
    param(
        [string]$Method,
        [string]$Url,
        [string]$Body = $null,
        [hashtable]$Headers = @{}
    )
    
    try {
        $params = @{
            Uri = $Url
            Method = $Method
            ContentType = "application/json"
            Headers = $Headers
        }
        
        if ($Body) {
            $params.Body = $Body
        }
        
        $response = Invoke-RestMethod @params
        Write-Host "✅ SUCCESS: $Method $Url" -ForegroundColor Green
        return $response
    }
    catch {
        Write-Host "❌ FAILED: $Method $Url - $($_.Exception.Message)" -ForegroundColor Red
        return $null
    }
}

Write-Host "`n=== 1. Testing Authentication Endpoints ===" -ForegroundColor Yellow

# Test user registration
$registerData = @{
    email = "testuser@example.com"
    password = "testpass123"
} | ConvertTo-Json

$registerResponse = Test-Endpoint -Method "POST" -Url "$baseUrl/api/auth/register" -Body $registerData

# Test user login
$loginData = @{
    email = "admin@example.com"
    password = "admin123"
} | ConvertTo-Json

$loginResponse = Test-Endpoint -Method "POST" -Url "$baseUrl/api/auth/login" -Body $loginData

$accessToken = $null
if ($loginResponse -and $loginResponse.accessToken) {
    $accessToken = $loginResponse.accessToken
    Write-Host "🔑 Access token obtained successfully" -ForegroundColor Cyan
}

# Headers for authenticated requests
$authHeaders = @{}
if ($accessToken) {
    $authHeaders = @{
        "Authorization" = "Bearer $accessToken"
    }
}

Write-Host "`n=== 2. Testing Hotel Management Endpoints ===" -ForegroundColor Yellow

# Test create hotel
$hotelData = @{
    hotelName = "Test Hotel"
    ownerName = "Test Owner"
    address = "123 Test Street"
    mobile = "9876543210"
    email = "testhotel@example.com"
    gstNumber = "TEST123456789"
    hotelType = "Restaurant"
} | ConvertTo-Json

$hotelResponse = Test-Endpoint -Method "POST" -Url "$baseUrl/api/hotel" -Body $hotelData -Headers $authHeaders

# Test get all hotels
Test-Endpoint -Method "GET" -Url "$baseUrl/api/hotel/all" -Headers $authHeaders

# Test get hotel by ID (assuming hotel ID 1 exists)
Test-Endpoint -Method "GET" -Url "$baseUrl/api/hotel/1" -Headers $authHeaders

Write-Host "`n=== 3. Testing Product Management Endpoints ===" -ForegroundColor Yellow

# Test create product
$productData = @{
    productName = "Test Product"
    category = "Food"
    price = 50.0
    quantity = 100
    productCode = "TP001"
} | ConvertTo-Json

Test-Endpoint -Method "POST" -Url "$baseUrl/api/products?hotelId=1" -Body $productData -Headers $authHeaders

# Test get all products
Test-Endpoint -Method "GET" -Url "$baseUrl/api/products?hotelId=1" -Headers $authHeaders

# Test get product by ID (assuming product ID 1 exists)
Test-Endpoint -Method "GET" -Url "$baseUrl/api/products/1?hotelId=1" -Headers $authHeaders

Write-Host "`n=== 4. Testing Bill Management Endpoints ===" -ForegroundColor Yellow

# Test create bill
$billData = @{
    customerId = 1
    items = @(
        @{
            itemName = "Tea"
            quantity = 2
            unitPrice = 25.0
            total = 50.0
            discount = 0.0
        },
        @{
            itemName = "Coffee"
            quantity = 1
            unitPrice = 30.0
            total = 30.0
            discount = 0.0
        }
    )
    subtotal = 80.0
    tax = 8.0
    discount = 5.0
    total = 83.0
    billDate = "2025-07-06"
    remarks = "Test bill"
} | ConvertTo-Json -Depth 3

Test-Endpoint -Method "POST" -Url "$baseUrl/api/bill" -Body $billData -Headers $authHeaders

# Test get all bills
Test-Endpoint -Method "GET" -Url "$baseUrl/api/bill/all" -Headers $authHeaders

# Test get bill by ID (assuming bill ID 1 exists)
Test-Endpoint -Method "GET" -Url "$baseUrl/api/bill/1" -Headers $authHeaders

# Test mobile bill creation
$mobileBillData = @{
    customerName = "John Doe"
    customerPhone = "9876543210"
    items = @(
        @{
            itemName = "Tea"
            quantity = 1
            unitPrice = 25.0
        }
    )
    hotelId = 1
    discount = 0.0
    remarks = "Mobile order"
} | ConvertTo-Json -Depth 3

Test-Endpoint -Method "POST" -Url "$baseUrl/api/bill/mobile" -Body $mobileBillData -Headers $authHeaders

Write-Host "`n=== 5. Testing Other Endpoints ===" -ForegroundColor Yellow

# Test Swagger documentation
Test-Endpoint -Method "GET" -Url "$baseUrl/v3/api-docs"

Write-Host "`n=== API Testing Complete ===" -ForegroundColor Green
Write-Host "Check the results above to see which endpoints are working correctly." -ForegroundColor Cyan
Write-Host "Access Swagger UI at: http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
