package org.sevosmart.com.sevosmartbackend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sevosmart.com.sevosmartbackend.dto.response.AuthenticationResponse;
import org.sevosmart.com.sevosmartbackend.model.User;
import org.sevosmart.com.sevosmartbackend.service.AuthenticationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {
    @Mock
    private AuthenticationService authenticationService;
    @InjectMocks
    private AuthenticationController authenticationController;
    private MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void register() throws Exception {
        // RegisterRequest request = new RegisterRequest("John", "Doe",
        // "john@example.com", "password123", Role.CUSTOMER);
        User user = new User();
        user.setEmail("john@example.com");
        AuthenticationResponse response = new AuthenticationResponse("token123", "Success", user);
        given(authenticationService.register(any())).willReturn(response);
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"firstname\":\"John\",\"lastname\":\"Doe\",\"email\":\"john@example.com\",\"password\":\"password123\",\"role\":\"CUSTOMER\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.user.email").value("john@example.com"))
                .andDo(print());
    }

    @Test
    void authenticate() throws Exception {
        // Given
        // AuthenticationRequest request = new AuthenticationRequest("john@example.com",
        // "password123");
        User user = new User();
        user.setEmail("john@example.com");
        AuthenticationResponse response = new AuthenticationResponse("validToken123", "Logged In", user);
        given(authenticationService.authenticate(any())).willReturn(response);

        // When
        mockMvc.perform(post("/api/v1/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"john@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logged In"))
                .andExpect(jsonPath("$.token").value("validToken123"))
                .andExpect(jsonPath("$.user.email").value("john@example.com"))
                .andDo(print());

        // Then
        verify(authenticationService).authenticate(
                argThat(arg -> arg.getEmail().equals("john@example.com") && arg.getPassword().equals("password123")));
    }

    @Test
    void authCheck() throws Exception {
        String token = "Bearer validToken123";
        given(authenticationService.authCheck(anyString())).willReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/api/v1/auth/auth-check")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
