package com.curootest.clientback.domain.service;

import java.util.List;
import java.util.Optional;

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

    @Autowired
    private ClientDTORepository clientRepository;

    @Autowired
    private SecurityService securityService;

    /**
     * Get all clients for the authenticated user
     */
    public List<ClientDTO> getAllClients() {
        String userEmail = securityService.getCurrentUserEmail();
        return clientRepository.getAllByUserEmail(userEmail);
    }

    /**
     * Get a client by ID type and number for the authenticated user
     */
    public Optional<ClientDTO> getClientByIdNumber(String idType, String idNumber) {
        String userEmail = securityService.getCurrentUserEmail();
        return clientRepository.getByIdNumberAndUserEmail(idType, idNumber, userEmail);
    }

    /**
     * Save a client for the authenticated user
     */
    public ClientDTO saveClient(ClientDTO clientDTO) {
        String userEmail = securityService.getCurrentUserEmail();
        return clientRepository.saveClient(clientDTO, userEmail);
    }

    /**
     * Update a client for the authenticated user
     */
    public Optional<ClientDTO> updateClient(Integer clientId, ClientDTO clientDTO) {
        String userEmail = securityService.getCurrentUserEmail();

        // First check if the client exists and belongs to the user
        Optional<ClientDTO> existingClient = clientRepository.getAllByUserEmail(userEmail)
                .stream()
                .filter(client -> clientId.toString().equals(client.getId()))
                .findFirst();

        if (existingClient.isPresent()) {
            clientDTO.setId(clientId.toString());
            return Optional.of(clientRepository.saveClient(clientDTO, userEmail));
        }

        return Optional.empty();
    }

    /**
     * Delete a client by ID for the authenticated user
     */
    public boolean deleteClient(Integer clientId) {
        String userEmail = securityService.getCurrentUserEmail();
        return clientRepository.deleteClientByUserEmail(clientId, userEmail);
    }
}
