package com.library.pocket.bills.pocket_bills.feature.auth.service;

import com.library.pocket.bills.pocket_bills.common.exception.AccountDisabledException;
import com.library.pocket.bills.pocket_bills.common.exception.InvalidCredentialsException;
import com.library.pocket.bills.pocket_bills.common.exception.UserAlreadyExistsException;
import com.library.pocket.bills.pocket_bills.feature.auth.dto.request.LoginRequest;
import com.library.pocket.bills.pocket_bills.feature.auth.dto.request.RegisterRequest;
import com.library.pocket.bills.pocket_bills.feature.auth.dto.response.AuthResponse;
import com.library.pocket.bills.pocket_bills.feature.user.entity.Role;
import com.library.pocket.bills.pocket_bills.feature.user.entity.User;
import com.library.pocket.bills.pocket_bills.feature.user.mapper.UserMapper;
import com.library.pocket.bills.pocket_bills.feature.user.repository.UserRepository;
import com.library.pocket.bills.pocket_bills.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException();
        }

        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .phone(request.phone())
                .role(Role.USER)
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);
        String token = jwtTokenProvider.generateToken(savedUser.getEmail(), savedUser.getId());

        return new AuthResponse(
                token,
                "Bearer",
                jwtTokenProvider.getExpirationSeconds(),
                userMapper.toProfileResponse(savedUser)
        );
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        if (!user.getEnabled()) {
            throw new AccountDisabledException();
        }

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getId());

        return new AuthResponse(
                token,
                "Bearer",
                jwtTokenProvider.getExpirationSeconds(),
                userMapper.toProfileResponse(user)
        );
    }
}
