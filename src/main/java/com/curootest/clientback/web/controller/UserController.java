package com.curootest.clientback.web.controller;

import com.curootest.clientback.domain.UserDTO;
import com.curootest.clientback.domain.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "Operations for user authentication and management")
public class UserController {

        @Autowired
        private UserService userService;

        @Operation(summary = "Register a new user", description = "Creates a new user account in the system")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input data - validation errors", content = @Content),
                        @ApiResponse(responseCode = "409", description = "Conflict - User with this email already exists", content = @Content)
        })
        @PostMapping("/register")
        public ResponseEntity<UserDTO> register(
                        @Parameter(description = "User registration data", required = true) @RequestBody UserDTO userDTO) {
                return new ResponseEntity<>(userService.register(userDTO), HttpStatus.CREATED);
        }

        @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Login successful - JWT token returned", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."))),
                        @ApiResponse(responseCode = "401", description = "Invalid credentials - wrong email or password", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Invalid credentials"))),
                        @ApiResponse(responseCode = "400", description = "Bad request - missing email or password", content = @Content)
        })
        @PostMapping("/login")
        public ResponseEntity<String> login(
                        @Parameter(description = "User email address", required = true, example = "user@example.com") @RequestParam String email,
                        @Parameter(description = "User password", required = true, example = "password123") @RequestParam String password) {
                return userService.login(email, password)
                                .map(jwt -> new ResponseEntity<>(jwt, HttpStatus.OK))
                                .orElse(new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED));
        }

        @Operation(summary = "Update user information", description = "Updates user profile information (requires authentication)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                        @ApiResponse(responseCode = "404", description = "User not found with the specified ID", content = @Content),
                        @ApiResponse(responseCode = "400", description = "Invalid input data - validation errors", content = @Content),
                        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Forbidden - Cannot update other user's information", content = @Content)
        })
        @SecurityRequirement(name = "Bearer Authentication")
        @PutMapping("/{userId}")
        public ResponseEntity<UserDTO> update(
                        @Parameter(description = "User ID to update", required = true, example = "1") @PathVariable("userId") int userId,
                        @Parameter(description = "Updated user data", required = true) @RequestBody UserDTO userDTO) {
                return userService.update(userId, userDTO)
                                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
}
