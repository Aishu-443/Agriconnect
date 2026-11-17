package com.agriconnect.config;

import com.agriconnect.model.Farmer;
import com.agriconnect.repository.FarmerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final FarmerRepository farmerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Farmer farmer = farmerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Farmer not found with email: " + email));

        String role = farmer.getRole() != null ? farmer.getRole().toUpperCase() : "FARMER";
        return new User(
                farmer.getEmail(),
                farmer.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }
}
