package org.sevosmart.com.sevosmartbackend.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.dto.request.AuthenticationRequest;
import org.sevosmart.com.sevosmartbackend.dto.request.RegisterRequest;
import org.sevosmart.com.sevosmartbackend.dto.response.AuthenticationResponse;
import org.sevosmart.com.sevosmartbackend.dto.response.UserInfoResponse;
import org.sevosmart.com.sevosmartbackend.enums.Role;
import org.sevosmart.com.sevosmartbackend.enums.TokenType;
import org.sevosmart.com.sevosmartbackend.exception.BadRequestException;
import org.sevosmart.com.sevosmartbackend.model.Admin;
import org.sevosmart.com.sevosmartbackend.model.Customer;
import org.sevosmart.com.sevosmartbackend.model.Token;
import org.sevosmart.com.sevosmartbackend.model.User;
import org.sevosmart.com.sevosmartbackend.repository.TokenRepository;
import org.sevosmart.com.sevosmartbackend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        });

        User user = createUserByRole(request);
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        if (request.getEmail().isEmpty()) {
            throw new BadRequestException("Email is required");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (!validUserTokens.isEmpty()) {
            validUserTokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
            tokenRepository.saveAll(validUserTokens);
        }
    }

    public ResponseEntity<?> refreshToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authorization header is missing or does not start with Bearer");
        }

        final String refreshToken = authorizationHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid refresh token");
        }

        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired refresh token");
        }

        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return ResponseEntity.ok(authResponse);
    }

    public ResponseEntity<?> getUserInfo(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authorization header is missing or does not start with Bearer");
        }

        String token = authorizationHeader.substring(7);
        try {
            String userEmail = jwtService.extractUsername(token);
            if (userEmail == null || userEmail.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not signed in");
            }

            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            if (!jwtService.isTokenValid(token, user)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid or expired token");
            }

            return ResponseEntity.ok(UserInfoResponse.builder()
                    .userId(user.getId())
                    .firstName(user.getFirstname())
                    .lastName(user.getLastname())
                    .build());

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("JWT token is expired");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("JWT token is invalid");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred processing your request");
        }
    }


//    public ResponseEntity<String> authCheck(String authorizationHeader) {
//        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Authorization header is missing or does not start with Bearer");
//        }
//
//        String token = authorizationHeader.substring(7);
//        try {
//            String userEmail = jwtService.extractUsername(token);
//            if (userEmail == null || userEmail.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body("User not signed in");
//            }
//
//            var user = userRepository.findByEmail(userEmail)
//                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
//
//            if (jwtService.isTokenValid(token, user)) {
//                return ResponseEntity.ok().build();
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body("Token is expired or invalid");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("An error occurred processing your request");
//        }
//    }

//    public ResponseEntity<String> adminCheck(String authorizationHeader) {
//        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Authorization header is missing or does not start with Bearer");
//        }
//
//        String token = authorizationHeader.substring(7);
//        try {
//            String userEmail = jwtService.extractUsername(token);
//            if (userEmail == null || userEmail.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body("User not signed in");
//            }
//
//            var user = userRepository.findByEmail(userEmail)
//                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
//
//            if (user.getRole() != Role.ADMIN) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                        .body("Access denied: User does not have admin privileges");
//            }
//
//            if (jwtService.isTokenValid(token, user)) {
//                return ResponseEntity.ok().build();
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body("Token is expired or invalid");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("An error occurred processing your request");
//        }
//    }

    public Optional<User> getUserById(String id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching the user with id: " + id, e);
        }
    }

    public User updateUser(String id, User userUpdates) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
            user.setFirstname(userUpdates.getFirstname());
            user.setLastname(userUpdates.getLastname());
            user.setEmail(userUpdates.getEmail());
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while updating the user with id: " + id, e);
        }
    }

    public String updateUserPassword(String id, String oldPassword, String newPassword) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                return "Old password is incorrect";
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return "Password updated successfully";
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while updating the password for user with id: " + id, e);
        }
    }

    public String deleteUser(String id) {
        try {
            userRepository.deleteById(id);
            return "User deleted successfully";
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while deleting the user with id: " + id, e);
        }
    }

    public List<User> getAllUsers() {
        try {
            return userRepository.findByRole(Role.CUSTOMER);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching all users", e);
        }
    }

    private User createUserByRole(RegisterRequest request) {
        switch (request.getRole()) {
            case GUEST:
                return createUser(request);
            case ADMIN:
                return createAdmin(request);
            case CUSTOMER:
                return createCustomer(request);
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown role");
        }
    }

    private User createUser(RegisterRequest request) {
        return User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
    }

    private Admin createAdmin(RegisterRequest request) {
        return Admin.adminBuilder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
    }

    private Customer createCustomer(RegisterRequest request) {
        return Customer.customerBuilder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
    }
}
