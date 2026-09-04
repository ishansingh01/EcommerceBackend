package com.ishan.Ecomm.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ishan.Ecomm.model.User;
import com.ishan.Ecomm.repo.UserRepository;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found by this email: " + email);
        }
        // Apne database wale 'User' ka data Spring Security ke 'User' format mein daal
        // kar wapas de do
        // Note: Yahan hum org.springframework.security.core.userdetails.User use kar
        // rahe hain

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                // new ArrayList<>()// Yahan user ke roles/authorities aate hain (jaise ADMIN, CUSTOMER). Abhi ke
                //                  // liye hum khali list de rahe hain.
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
}
