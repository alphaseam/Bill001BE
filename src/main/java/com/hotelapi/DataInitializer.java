package com.hotelapi;

import com.hotelapi.entity.*;
import com.hotelapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final HotelRepository hotelRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final BillRepository billRepo;
    private final BillItemRepository billItemRepo;

    @Override
    public void run(String... args) throws Exception {
        log.info("Inserting sample data...");

        // Check & insert Hotel
        Hotel taj = hotelRepo.findByMobile("9985947896").orElseGet(() -> {
            Hotel newHotel = Hotel.builder()
                    .hotelName("Hotel Taj")
                    .ownerName("Rajeev")
                    .mobile("9985947896")
                    .email("taj@gmail.com")
                    .gstNumber("GSTT1234")
                    .hotelType("Luxury")
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            log.info("Inserted Hotel Taj");
            return hotelRepo.save(newHotel);
        });

        // Check & insert Admin User
        userRepo.findByEmail("admin12@taj.com").ifPresentOrElse(
                user -> log.info("Admin user already exists."),
                () -> {
                    userRepo.save(User.builder()
                            .email("admin12@taj.com")
                            .password("admin123")
                            .build());
                    log.info("Inserted admin user");
                }
        );

        // Check & insert Tea Product
        productRepo.findByProductNameAndHotel("Tea", taj).ifPresentOrElse(
                p -> log.info("Product 'Tea' already exists."),
                () -> {
                    productRepo.save(Product.builder()
                            .productName("Tea")
                            .productCode("TEA001")
                            .price(10.0)
                            .category("Beverage")
                            .quantity(100)
                            .isActive(true)
                            .hotel(taj)
                            .build());
                    log.info("Inserted product 'Tea'");
                }
        );

        // Check & insert Coffee Product
        productRepo.findByProductNameAndHotel("Coffee", taj).ifPresentOrElse(
                p -> log.info("Product 'Coffee' already exists."),
                () -> {
                    productRepo.save(Product.builder()
                            .productName("Coffee")
                            .productCode("COF001")
                            .price(20.0)
                            .category("Beverage")
                            .quantity(100)
                            .isActive(true)
                            .hotel(taj)
                            .build());
                    log.info("Inserted product 'Coffee'");
                }
        );

        // Always insert a new bill and its items
        Bill bill = Bill.builder()
                .billNumber("INV-1001")
                .createdAt(LocalDateTime.now())
                .subtotal(40.0)
                .tax(7.2)
                .discount(5.0)
                .total(42.2)
                .build();
        bill = billRepo.save(bill);
        log.info("Inserted bill {}", bill.getBillNumber());

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
        log.info("Inserted bill items");

        // SQL Dump
        try {
            log.info("Generating SQL dump...");
            ProcessBuilder pb = new ProcessBuilder(
                    "mysqldump",
                    "-u", "root",
                    "-proot",
                    "hotel_db",
                    "-r", "hotel_dump.sql"
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                log.info("SQL dump generated: hotel_dump.sql");
            } else {
                log.error("Failed to generate SQL dump. Exit code: {}", exitCode);
            }
        } catch (Exception e) {
            log.error("Exception during SQL dump: {}", e.getMessage(), e);
        }
    }
}
