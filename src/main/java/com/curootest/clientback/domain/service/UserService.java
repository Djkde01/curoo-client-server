package com.curootest.clientback.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.curootest.clientback.config.CustomUserDetails;
import com.curootest.clientback.domain.UserDTO;
import com.curootest.clientback.domain.repository.UserDTORepository;
import com.curootest.clientback.exception.InvalidPasswordException;
import com.curootest.clientback.exception.UserAlreadyExistsException;

@Service
public class UserService {

    @Autowired
    private UserDTORepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    // Method to get a user by ID
    public Optional<UserDTO> getUserById(String id) {
        return userRepository.getById(id);
    }

    // Method to get all users
    public List<UserDTO> getAllUsers() {
        return userRepository.getAll();
    }

    // Method to save a user
    public UserDTO saveUser(UserDTO userDTO) {
        return userRepository.saveUser(userDTO);
    }

    // Method to delete a user by ID
    public void deleteUserById(String id) {
        userRepository.deleteUserById(id);
        return;
    }

    public UserDTO register(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists.");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().length() < 6) {
            throw new InvalidPasswordException("Password must be at least 6 characters long.");
        }
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.saveUser(userDTO);
    }

    public Optional<String> login(String email, String password) {
        Optional<UserDTO> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            UserDTO user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(jwtService.generateToken(new CustomUserDetails(user)));
            }
        }
        return Optional.empty();
    }

    public Optional<UserDTO> update(int userId, UserDTO userDTO) {
        return userRepository.getById(String.valueOf(userId))
                .map(existingUser -> {
                    if (userDTO.getPassword() != null && userDTO.getPassword().length() < 6) {
                        throw new InvalidPasswordException("Password must be at least 6 characters long.");
                    }
                    if (userDTO.getPassword() != null) {
                        existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                    }
                    existingUser.setName(userDTO.getName());
                    existingUser.setSurname(userDTO.getSurname());
                    existingUser.setEmail(userDTO.getEmail());
                    existingUser.setMobilePhone(userDTO.getMobilePhone());
                    return userRepository.saveUser(existingUser);
                });
    }
}
