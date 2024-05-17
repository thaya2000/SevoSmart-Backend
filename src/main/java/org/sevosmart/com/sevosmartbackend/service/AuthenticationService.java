package org.sevosmart.com.sevosmartbackend.service;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.dto.request.AuthenticationRequest;
import org.sevosmart.com.sevosmartbackend.dto.request.RegisterRequest;
import org.sevosmart.com.sevosmartbackend.dto.response.AuthenticationResponse;
import org.sevosmart.com.sevosmartbackend.enums.Role;
import org.sevosmart.com.sevosmartbackend.model.Admin;
import org.sevosmart.com.sevosmartbackend.model.Customer;
import org.sevosmart.com.sevosmartbackend.model.User;
import org.sevosmart.com.sevosmartbackend.repository.AdminRepository;
import org.sevosmart.com.sevosmartbackend.repository.CustomerRepository;
import org.sevosmart.com.sevosmartbackend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        Optional<User> existingUserOptional = userRepository.findByEmail(request.getEmail());
        if (existingUserOptional.isPresent()) {
            return AuthenticationResponse.builder()
                    .message("Email already exists")
                    .build();
        }

        User user;
        switch (request.getRole()) {
            case USER:
                user = createUser(request);
                userRepository.save(user);
                break;
            case ADMIN:
                user = createAdmin(request);
                adminRepository.save((Admin) user);
                break;
            case CUSTOMER:
                user = createCustomer(request);
                customerRepository.save((Customer) user);
                break;
            default:
                return AuthenticationResponse.builder()
                        .message("Unknown role")
                        .build();
        }

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .user(user)
                .token(jwtToken)
                .build();
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

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        if (request.getEmail().isEmpty()) {
            return AuthenticationResponse.builder()
                    .message("Email is required")
                    .build();
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .user(user)
                .token(jwtToken)
                .build();
    }

    public ResponseEntity<?> authCheck(String token) {
        String userEmail = jwtService.extractUsername(token);
        if (userEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not Sign in");

        }
        var user = userRepository.findByEmail(userEmail)
                .orElseThrow();

        if (jwtService.isTokenValid(token, user)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token is expired");
        }
    }

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

}
