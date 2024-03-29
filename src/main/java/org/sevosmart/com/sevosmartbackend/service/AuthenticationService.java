package org.sevosmart.com.sevosmartbackend.service;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.dto.request.AuthenticationRequest;
import org.sevosmart.com.sevosmartbackend.dto.request.RegisterRequest;
import org.sevosmart.com.sevosmartbackend.dto.response.AuthenticationResponse;
import org.sevosmart.com.sevosmartbackend.model.User;
import org.sevosmart.com.sevosmartbackend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        Optional<User> existingUserOptional = repository.findByEmail(request.getEmail());
        if (existingUserOptional.isPresent()) {
            return AuthenticationResponse.builder()
                    .message("Email already exists")
                    .build();
        }

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .user(user)
                .token(jwtToken)
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
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .user(user)
                .token(jwtToken)
                .build();
    }

    public ResponseEntity<?> authCheck(String token){
        System.out.println("step1");
        String userEmail = jwtService.extractUsername(token);
        System.out.println("step2");
        if (userEmail.isEmpty()){
            System.out.println("step3");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not Sign in");

        }
        System.out.println("step4");
        var user = repository.findByEmail(userEmail)
                .orElseThrow();

        System.out.println("User : " + user);
        if(jwtService.isTokenValid(token, user)){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token is expired");
        }
    }
}
