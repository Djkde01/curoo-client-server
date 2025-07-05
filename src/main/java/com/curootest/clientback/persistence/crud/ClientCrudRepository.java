package com.curootest.clientback.persistence.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.curootest.clientback.persistence.entity.Client;

/**
 * JPA Repository for Client entity with user-specific queries
 * All queries include user filtering for data isolation and security
 */
public interface ClientCrudRepository extends CrudRepository<Client, Integer> {

    /**
     * Get all clients for a specific user
     */
    @Query("SELECT c FROM Client c WHERE c.userId.email = :userEmail")
    List<Client> findByUserEmail(@Param("userEmail") String userEmail);

    /**
     * Get clients by ID type and ID number for a specific user
     */
    @Query("SELECT c FROM Client c WHERE c.idType = :idType AND c.idNumber = :idNumber AND c.userId.email = :userEmail")
    Optional<Client> findByIdTypeAndIdNumberAndUserEmail(@Param("idType") String idType,
            @Param("idNumber") String idNumber, @Param("userEmail") String userEmail);

    /**
     * Get client by ID for a specific user
     */
    @Query("SELECT c FROM Client c WHERE c.clientId = :clientId AND c.userId.email = :userEmail")
    Optional<Client> findByClientIdAndUserEmail(@Param("clientId") Integer clientId,
            @Param("userEmail") String userEmail);

    /**
     * Get clients by name (case insensitive) for a specific user
     */
    @Query("SELECT c FROM Client c WHERE UPPER(c.name) LIKE UPPER(CONCAT('%', :name, '%')) AND c.userId.email = :userEmail")
    List<Client> findByNameContainingIgnoreCaseAndUserEmail(@Param("name") String name,
            @Param("userEmail") String userEmail);

    /**
     * Get clients by surname (case insensitive) for a specific user
     */
    @Query("SELECT c FROM Client c WHERE UPPER(c.surname) LIKE UPPER(CONCAT('%', :surname, '%')) AND c.userId.email = :userEmail")
    List<Client> findBySurnameContainingIgnoreCaseAndUserEmail(@Param("surname") String surname,
            @Param("userEmail") String userEmail);
}
