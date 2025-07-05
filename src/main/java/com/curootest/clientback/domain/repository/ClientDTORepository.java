package com.curootest.clientback.domain.repository;

import java.util.List;
import java.util.Optional;

import com.curootest.clientback.domain.ClientDTO;

/**
 * Repository interface for client data operations
 * All operations are user-specific for data isolation
 */
public interface ClientDTORepository {

    /**
     * Get all clients for a specific user
     */
    List<ClientDTO> getAllByUserEmail(String userEmail);

    /**
     * Get a client by ID type and number for a specific user
     */
    Optional<ClientDTO> getByIdNumberAndUserEmail(String idType, String idNumber, String userEmail);

    /**
     * Save a client for a specific user
     */
    ClientDTO saveClient(ClientDTO clientDTO, String userEmail);

    /**
     * Delete a client by ID for a specific user
     */
    boolean deleteClientByUserEmail(Integer clientId, String userEmail);
}
