package com.agriconnect;

import com.agriconnect.service.AuthService;
import com.agriconnect.service.CropService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AgriConnectApplicationTests {

    @Autowired(required = false)
    private AuthService authService;

    @Autowired(required = false)
    private CropService cropService;

    @Test
    void contextLoads() {
        assertNotNull(authService, "AuthService bean should be present in context");
        assertNotNull(cropService, "CropService bean should be present in context");
    }
}
