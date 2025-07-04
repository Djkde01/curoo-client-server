package com.curootest.clientback.persistence.crud;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.curootest.clientback.persistence.entity.User;

public interface UserCrudRepository extends CrudRepository<User, String> {

    Optional<User> findByEmail(String email);
}
