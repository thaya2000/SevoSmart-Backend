package org.sevosmart.com.sevosmartbackend.controller;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.dto.request.AuthenticationRequest;
import org.sevosmart.com.sevosmartbackend.dto.request.PasswordUpdateRequest;
import org.sevosmart.com.sevosmartbackend.dto.request.RegisterRequest;
import org.sevosmart.com.sevosmartbackend.dto.response.AuthenticationResponse;
import org.sevosmart.com.sevosmartbackend.model.User;
import org.sevosmart.com.sevosmartbackend.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(
            @RequestHeader("Authorization") String authorizationHeader
    ) throws IOException {
        return authenticationService.refreshToken(authorizationHeader);
    }

    @PostMapping("/user-info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authorizationHeader) throws IOException {
        return authenticationService.getUserInfo(authorizationHeader);
    }

//    @GetMapping("/auth-check")
//    public ResponseEntity<?> authCheck(@RequestHeader("Authorization") String authorizationHeader) {
//        System.out.println("Authorization Header: " + authorizationHeader);
//        return authenticationService.authCheck(authorizationHeader);
//    }
//
//    @GetMapping("/admin-check")
//    public ResponseEntity<?> adminCheck(@RequestHeader("Authorization") String authorizationHeader) {
//        System.out.println("Authorization Header: " + authorizationHeader);
//        return authenticationService.adminCheck(authorizationHeader);
//    }


    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            User user = authenticationService.getUserById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User userUpdates) {
        try {
            User updatedUser = authenticationService.updateUser(id, userUpdates);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/users/password/{id}")
    public ResponseEntity<String> updateUserPassword(@PathVariable String id, @RequestBody PasswordUpdateRequest request) {
        try {
            String message = authenticationService.updateUserPassword(id, request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            String message = authenticationService.deleteUser(id);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = authenticationService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
