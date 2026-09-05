package com.special.todolist.controller;

import com.special.todolist.dto.request.LoginRequest;
import com.special.todolist.dto.request.SignupRequest;
import com.special.todolist.dto.response.TokenResponse;
import com.special.todolist.entity.RefreshToken;
import com.special.todolist.entity.User;
import com.special.todolist.security.JwtUtils;
import com.special.todolist.service.AuthService;
import com.special.todolist.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public AuthController(AuthService authService, UserRepository userRepository, JwtUtils jwtUtils,
                          AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
                          UserDetailsService userDetailsService) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest req) {
        User user = authService.signup(req.getUsername(), req.getPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        String access = jwtUtils.generateAccessToken(req.getUsername());
        String refresh = authService.createRefreshToken((User) userRepository.findByUsername(req.getUsername()).orElseThrow()).getToken();
        return ResponseEntity.ok(new TokenResponse(access, refresh));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestParam("refreshToken") String refreshToken) {
        RefreshToken rt = authService.findByToken(refreshToken).orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        if (rt.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }
        String access = jwtUtils.generateAccessToken(rt.getUser().getUsername());
        // rotate refresh token
        authService.deleteByUser(rt.getUser());
        RefreshToken newRt = authService.createRefreshToken(rt.getUser());
        return ResponseEntity.ok(new TokenResponse(access, newRt.getToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam("username") String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        authService.deleteByUser(user);
        return ResponseEntity.ok().build();
    }
}
