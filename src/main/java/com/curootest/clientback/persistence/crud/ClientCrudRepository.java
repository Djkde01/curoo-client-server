package com.curootest.clientback.persistence.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.curootest.clientback.persistence.entity.Client;

public interface ClientCrudRepository extends CrudRepository<Client, Integer> {

    // READ - Get all clients
    List<Client> findAll();

    // READ - Get clients by ID type and ID number
    Client findByIdTypeAndIdNumber(String idType, String idNumber);

    // READ - Get client by exact ID type and ID number (single result)
    Optional<Client> findFirstByIdTypeAndIdNumber(String idType, String idNumber);

    // READ - Get clients by name (case insensitive)
    List<Client> findByNameContainingIgnoreCase(String name);

    // READ - Get clients by surname (case insensitive)
    List<Client> findBySurnameContainingIgnoreCase(String surname);

    // UPDATE - Custom update method for specific fields
    @Modifying
    @Transactional
    @Query("UPDATE Client c SET c.name = :name, c.surname = :surname, c.modificationDate = CURRENT_TIMESTAMP WHERE c.clientId = :clientId")
    int updateClientNameAndSurname(@Param("clientId") Integer clientId, @Param("name") String name,
            @Param("surname") String surname);

    @Modifying
    @Transactional
    @Query("UPDATE Client c SET c.idType = :idType, c.idNumber = :idNumber, c.modificationDate = CURRENT_TIMESTAMP WHERE c.clientId = :clientId")
    int updateClientIdTypeAndNumber(@Param("clientId") Integer clientId, @Param("idType") String idType,
            @Param("idNumber") String idNumber);
}
