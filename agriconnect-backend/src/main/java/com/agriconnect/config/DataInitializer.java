package com.agriconnect.config;

import com.agriconnect.model.Farmer;
import com.agriconnect.repository.FarmerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final FarmerRepository farmerRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.password:}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (adminPassword == null || adminPassword.isBlank()) {
            log.warn("ADMIN_PASSWORD is not set. The admin account will not be created or updated.");
            return;
        }
        farmerRepository.findByEmail("admin@agriconnect.com").ifPresentOrElse(
                admin -> {
                    admin.setPassword(passwordEncoder.encode(adminPassword));
                    admin.setRole("ADMIN");
                    farmerRepository.save(admin);
                    log.info("Admin user verified and password initialized for admin@agriconnect.com");
                },
                () -> {
                    Farmer admin = Farmer.builder()
                            .name("Admin")
                            .email("admin@agriconnect.com")
                            .mobile("9876543210")
                            .password(passwordEncoder.encode(adminPassword))
                            .state("Telangana")
                            .district("Hyderabad")
                            .village("City Center")
                            .farmSize(BigDecimal.ZERO)
                            .primaryCrop("General")
                            .role("ADMIN")
                            .build();
                    farmerRepository.save(admin);
                    log.info("Admin user seeded successfully with email admin@agriconnect.com");
                }
        );
    }
}
