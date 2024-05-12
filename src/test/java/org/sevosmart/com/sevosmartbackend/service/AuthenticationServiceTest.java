package org.sevosmart.com.sevosmartbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sevosmart.com.sevosmartbackend.dto.request.AuthenticationRequest;
import org.sevosmart.com.sevosmartbackend.dto.request.RegisterRequest;
import org.sevosmart.com.sevosmartbackend.dto.response.AuthenticationResponse;
import org.sevosmart.com.sevosmartbackend.enums.Role;
import org.sevosmart.com.sevosmartbackend.model.Admin;
import org.sevosmart.com.sevosmartbackend.model.Customer;
import org.sevosmart.com.sevosmartbackend.model.Seller;
import org.sevosmart.com.sevosmartbackend.model.User;
import org.sevosmart.com.sevosmartbackend.repository.AdminRepository;
import org.sevosmart.com.sevosmartbackend.repository.CustomerRepository;
import org.sevosmart.com.sevosmartbackend.repository.SellerRepository;
import org.sevosmart.com.sevosmartbackend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    }

    @Test
    void register_EmailExists_ReturnsEmailExistsResponse() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "john@example.com", "password123", Role.USER);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        AuthenticationResponse response = authenticationService.register(request);

        assertEquals("Email already exists", response.getMessage());
        assertNull(response.getUser());
        assertNull(response.getToken());
    }

    @Test
    void register_NewUser_ReturnsSuccessfulRegistration() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "newuser@example.com", "password123", Role.USER);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        doAnswer(invocation -> invocation.getArgument(0)).when(userRepository).save(any(User.class));
        when(jwtService.generateToken(any(User.class))).thenReturn("token123");

        AuthenticationResponse response = authenticationService.register(request);

        assertEquals("newuser@example.com", response.getUser().getEmail());
        assertEquals("token123", response.getToken());
        assertNotNull(response.getUser());
    }

    @Test
    void authenticate_InvalidEmail_ThrowsException() {
        AuthenticationRequest request = new AuthenticationRequest("", "password123");

        Exception exception = assertThrows(RuntimeException.class, () -> authenticationService.authenticate(request));

        assertEquals("Email is required", exception.getMessage());
    }

    @Test
    void authenticate_ValidCredentials_ReturnsAuthenticatedUser() {
        AuthenticationRequest request = new AuthenticationRequest("john@example.com", "password123");
        User user = new User();
        user.setEmail(request.getEmail());
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("validToken");
        doNothing().when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertEquals("john@example.com", response.getUser().getEmail());
        assertEquals("validToken", response.getToken());
        assertNotNull(response.getUser());
    }

    @Test
    void authCheck_InvalidToken_ReturnsUnauthorized() {
        when(jwtService.extractUsername("invalidToken")).thenReturn("");
        ResponseEntity<?> response = authenticationService.authCheck("invalidToken");

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("User not Sign in", response.getBody());
    }

    @Test
    void authCheck_ValidToken_ReturnsOk() {
        String token = "validToken";
        User user = new User();
        user.setEmail("john@example.com");
        when(jwtService.extractUsername(token)).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(token, user)).thenReturn(true);

        ResponseEntity<?> response = authenticationService.authCheck(token);

        assertEquals(200, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}