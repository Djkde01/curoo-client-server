package com.curootest.clientback.persistence.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.curootest.clientback.persistence.entity.Client;

public interface ClientCrudRepository extends CrudRepository<Client, Integer> {

    // READ - Get clients by ID type and ID number
    Client findByIdTypeAndIdNumber(String idType, String idNumber);

    // READ - Get client by exact ID type and ID number (single result)
    Optional<Client> findFirstByIdTypeAndIdNumber(String idType, String idNumber);

    // READ - Get clients by name (case insensitive)
    List<Client> findByNameContainingIgnoreCase(String name);

    // READ - Get clients by surname (case insensitive)
    List<Client> findBySurnameContainingIgnoreCase(String surname);
}
