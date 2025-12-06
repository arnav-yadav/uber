package com.arnav.uber.service;

import com.arnav.uber.dto.JwtResponse;
import com.arnav.uber.dto.LoginRequest;
import com.arnav.uber.dto.RegisterRequest;
import com.arnav.uber.exception.BadRequestException;
import com.arnav.uber.exception.UnauthorizedException;
import com.arnav.uber.model.User;
import com.arnav.uber.repository.UserRepository;
import com.arnav.uber.config.JwtUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepo,
                       PasswordEncoder encoder,
                       JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterRequest req) {
        if (userRepo.findByUsername(req.getUsername()).isPresent()) {
            throw new BadRequestException("Username already exists");
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole(req.getRole());
        userRepo.save(user);
    }

    public JwtResponse login(LoginRequest req) {
        User user = userRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return new JwtResponse(token, user.getUsername(), user.getRole());
    }

    public User getCurrentUser() {
        String username = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }
}
