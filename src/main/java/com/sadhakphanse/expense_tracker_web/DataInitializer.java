package com.sadhakphanse.expense_tracker_web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        if (userRepository.findByUsername("sadhak123").isEmpty()) {
            
            User demoUser = new User();
            demoUser.setUsername("sadhak123");
            
            
            demoUser.setPassword(passwordEncoder.encode("sphanse123"));
            
            userRepository.save(demoUser);
            
            
            System.out.println(">>> Created demo user 'sadhak123' with password 'sphanse123'");
        }
    }
}