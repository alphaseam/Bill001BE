package com.hotelapi;

import com.hotelapi.entity.*;
import com.hotelapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final HotelRepository hotelRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final BillRepository billRepo;
    private final BillItemRepository billItemRepo;

    @Override
    public void run(String... args) {
        // Insert Hotels
        Hotel taj = hotelRepo.save(Hotel.builder()
                .hotelName("Hotel Taj")
                .ownerName("Rajeev")
                .mobile("9999999991")
                .email("taj@gmail.com")
                .gstNumber("GSTT1234")
                .hotelType("Luxury")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());

        // Insert User (admin login)
        User admin = new User(null, "admin@taj.com", "admin123");
        userRepo.save(admin);


        // Insert Products
        Product tea = productRepo.save(Product.builder()
                .name("Tea")
                .price(10.0)
                .category("Beverage")
                .hotel(taj)
                .build());

        Product coffee = productRepo.save(Product.builder()
                .name("Coffee")
                .price(20.0)
                .category("Beverage")
                .hotel(taj)
                .build());

        // Insert Bill
        Bill bill = Bill.builder()
                .billNumber("INV-1001")
                .createdAt(LocalDateTime.now())
                
                .subtotal(40.0)
                .tax(7.2)
                .discount(5.0)
                .total(42.2)
                .build();

        bill = billRepo.save(bill);

        // Insert Bill Items
        BillItem item1 = BillItem.builder()
                .itemName("Tea")
                .quantity(2)
                .unitPrice(10.0)
                .discount(0.0)
                .total(20.0)
                .bill(bill)
                .build();

        BillItem item2 = BillItem.builder()
                .itemName("Coffee")
                .quantity(1)
                .unitPrice(20.0)
                .discount(0.0)
                .total(20.0)
                .bill(bill)
                .build();

        billItemRepo.saveAll(List.of(item1, item2));
    }
}
