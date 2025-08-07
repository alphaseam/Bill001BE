#!/usr/bin/env python3
import requests
import json

# Configuration
BASE_URL = "http://localhost:8080/api"
HOTEL_ID = 1  # Test with hotel ID 1

def test_force_delete_hotel():
    """Test the force delete hotel endpoint"""
    print("Testing Force Delete Hotel API...")
    
    # Get hotel details first
    print(f"\n1. Getting hotel details for ID {HOTEL_ID}...")
    response = requests.get(f"{BASE_URL}/hotel/{HOTEL_ID}")
    
    if response.status_code == 200:
        hotel_data = response.json()
        print(f"Hotel found: {hotel_data}")
    elif response.status_code == 404:
        print(f"Hotel with ID {HOTEL_ID} not found. Let's create a test hotel first.")
        create_test_hotel()
        return
    else:
        print(f"Error getting hotel: {response.status_code} - {response.text}")
        return
    
    # Check associated products
    print(f"\n2. This hotel might have associated products. Force delete will remove them all.")
    
    # Perform force delete
    print(f"\n3. Performing force delete for hotel ID {HOTEL_ID}...")
    response = requests.delete(f"{BASE_URL}/hotel/{HOTEL_ID}/force")
    
    print(f"Response Status: {response.status_code}")
    print(f"Response Body: {response.text}")
    
    if response.status_code == 200:
        result = response.json()
        print(f"✅ Success: {result['message']}")
        return True
    else:
        print(f"❌ Failed: {response.text}")
        return False

def create_test_hotel():
    """Create a test hotel for testing"""
    print("\nCreating a test hotel...")
    
    test_hotel = {
        "hotelName": "Test Hotel for Force Delete",
        "ownerName": "Test Owner",
        "mobile": "9999999999",
        "email": "test@test.com",
        "address": "Test Address"
    }
    
    response = requests.post(f"{BASE_URL}/hotel", json=test_hotel)
    
    if response.status_code == 200:
        hotel_data = response.json()
        print(f"✅ Test hotel created: {hotel_data}")
        hotel_id = hotel_data['data']['hotelId']
        
        # Now test force delete on this new hotel
        print(f"\n4. Testing force delete on new hotel ID {hotel_id}...")
        delete_response = requests.delete(f"{BASE_URL}/hotel/{hotel_id}/force")
        
        print(f"Force Delete Response Status: {delete_response.status_code}")
        print(f"Force Delete Response Body: {delete_response.text}")
        
        if delete_response.status_code == 200:
            result = delete_response.json()
            print(f"✅ Force delete successful: {result['message']}")
            return True
        else:
            print(f"❌ Force delete failed: {delete_response.text}")
            return False
    else:
        print(f"❌ Failed to create test hotel: {response.text}")
        return False

if __name__ == "__main__":
    print("=== Hotel Force Delete API Test ===")
    
    try:
        test_force_delete_hotel()
    except requests.exceptions.ConnectionError:
        print("❌ Error: Cannot connect to the server. Make sure the application is running on http://localhost:8080")
    except Exception as e:
        print(f"❌ Unexpected error: {e}")
    
    print("\n=== Test completed ===")
