package com.curootest.clientback.domain.repository;

import java.util.List;
import java.util.Optional;

import com.curootest.clientback.domain.UserDTO;

public interface UserDTORepository {
    // Method to find a user by ID
    Optional<UserDTO> getById(String id);

    // Method to find all users
    List<UserDTO> getAll();

    // Method to save a user
    UserDTO saveUser(UserDTO userDTO);

    // Method to delete a user by ID
    void deleteUserById(String id);

    Optional<UserDTO> findByEmail(String email);
}
