package com.ishan.Ecomm.service;

import com.ishan.Ecomm.model.User;
import com.ishan.Ecomm.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Autowired
    private com.ishan.Ecomm.util.JwtUtil jwtUtil;

    public User registerUser(User user) {
        try {
            if(user.getRole()==null || user.getRole().trim().isEmpty()){
                user.setRole("USER");
            }
            else{
                user.setRole(user.getRole().toUpperCase());
            }
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            User newUser = userRepository.save(user);
            System.out.println("User Added to Database with Encrypted Password");
            return newUser;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String loginUser(String email, String password) {
        // check if user is there or not
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            String token = jwtUtil.generateToken(email, user.getRole());
            return token;
        }
        return "Invalid Credential"; // invalid credentials
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
