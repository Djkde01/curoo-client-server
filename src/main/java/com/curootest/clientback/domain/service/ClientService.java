package com.curootest.clientback.domain.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curootest.clientback.domain.ClientDTO;
import com.curootest.clientback.domain.repository.ClientDTORepository;

/**
 * Service for client business operations
 * All operations are user-specific based on JWT authentication
 */
@Service
public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientDTORepository clientRepository;

    @Autowired
    private SecurityService securityService;

    /**
     * Get all clients for the authenticated user
     */
    public List<ClientDTO> getAllClients() {
        String userEmail = securityService.getCurrentUserEmail();
        logger.info("Getting all clients for user: {}", userEmail);
        List<ClientDTO> clients = clientRepository.getAllByUserEmail(userEmail);
        logger.info("Found {} clients for user: {}", clients.size(), userEmail);
        return clients;
    }

    /**
     * Get a client by ID type and number for the authenticated user
     */
    public Optional<ClientDTO> getClientByIdNumber(String idType, String idNumber) {
        String userEmail = securityService.getCurrentUserEmail();
        logger.info("Getting client by idType: {} and idNumber: {} for user: {}", idType, idNumber, userEmail);
        Optional<ClientDTO> client = clientRepository.getByIdNumberAndUserEmail(idType, idNumber, userEmail);
        logger.info("Client found: {} for user: {}", client.isPresent(), userEmail);
        return client;
    }

    /**
     * Save a client for the authenticated user
     */
    public ClientDTO saveClient(ClientDTO clientDTO) {
        String userEmail = securityService.getCurrentUserEmail();
        logger.info("Saving client for user: {}", userEmail);
        ClientDTO savedClient = clientRepository.saveClient(clientDTO, userEmail);
        logger.info("Client saved with ID: {} for user: {}", savedClient.getId(), userEmail);
        return savedClient;
    }

    /**
     * Update a client for the authenticated user
     */
    public Optional<ClientDTO> updateClient(Integer clientId, ClientDTO clientDTO) {
        String userEmail = securityService.getCurrentUserEmail();
        logger.info("Attempting to update client ID: {} for user: {}", clientId, userEmail);

        // First check if client exists and belongs to user
        boolean exists = clientRepository.existsByIdAndUserEmail(clientId, userEmail);
        logger.info("Client ID: {} exists for user {}: {}", clientId, userEmail, exists);

        if (!exists) {
            logger.warn("Client ID: {} not found or doesn't belong to user: {}", clientId, userEmail);
            return Optional.empty();
        }

        ClientDTO updatedClient = clientRepository.updateClientByUserEmail(clientId, clientDTO, userEmail);
        if (updatedClient != null) {
            logger.info("Client ID: {} updated successfully for user: {}", clientId, userEmail);
            return Optional.of(updatedClient);
        } else {
            logger.error("Failed to update client ID: {} for user: {}", clientId, userEmail);
            return Optional.empty();
        }
    }

    /**
     * Delete a client by ID for the authenticated user
     */
    public boolean deleteClient(Integer clientId) {
        String userEmail = securityService.getCurrentUserEmail();
        logger.info("Attempting to delete client ID: {} for user: {}", clientId, userEmail);

        boolean deleted = clientRepository.deleteClientByUserEmail(clientId, userEmail);
        logger.info("Client ID: {} deletion result for user {}: {}", clientId, userEmail, deleted);
        return deleted;
    }
}
