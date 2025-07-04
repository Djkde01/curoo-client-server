package com.curootest.clientback.persistence.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.curootest.clientback.persistence.entity.User;

public interface UserCrudRepository extends CrudRepository<User, String> {

    // READ - Get all users
    List<User> findAll();

    // READ - Get user by ID (inherited from CrudRepository: findById(String id))
    Optional<User> findById(String id);

}
