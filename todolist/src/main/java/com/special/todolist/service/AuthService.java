package com.special.todolist.service;

import com.special.todolist.entity.RefreshToken;
import com.special.todolist.entity.User;
import com.special.todolist.repository.RefreshTokenRepository;
import com.special.todolist.repository.UserRepository;
import com.special.todolist.security.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final long refreshTokenMs;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils,
                       @Value("${security.jwt.refresh-token-ms}") long refreshTokenMs) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.refreshTokenMs = refreshTokenMs;
    }

    public User signup(String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already taken");
        }
        User user = new User(username, passwordEncoder.encode(rawPassword));
        return userRepository.save(user);
    }

    public String loginAndCreateRefreshToken(String username) {
        String refresh = UUID.randomUUID().toString();
        RefreshToken rt = new RefreshToken();
        rt.setToken(refresh);
        rt.setUser(userRepository.findByUsername(username).orElseThrow());
        rt.setExpiryDate(Instant.now().plusMillis(refreshTokenMs));
        refreshTokenRepository.save(rt);
        return refresh;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(User user) {
        RefreshToken rt = new RefreshToken();
        rt.setToken(UUID.randomUUID().toString());
        rt.setUser(user);
        rt.setExpiryDate(Instant.now().plusMillis(refreshTokenMs));
        return refreshTokenRepository.save(rt);
    }

    public int deleteByUser(User user) {
        return refreshTokenRepository.deleteByUser(user);
    }
}
