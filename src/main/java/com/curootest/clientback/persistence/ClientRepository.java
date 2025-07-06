package com.curootest.clientback.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.curootest.clientback.domain.ClientDTO;
import com.curootest.clientback.domain.repository.ClientDTORepository;
import com.curootest.clientback.persistence.crud.ClientCrudRepository;
import com.curootest.clientback.persistence.crud.UserCrudRepository;
import com.curootest.clientback.persistence.entity.Client;
import com.curootest.clientback.persistence.entity.User;
import com.curootest.clientback.persistence.mapper.ClientMapper;

/**
 * Repository implementation for client data operations
 * All operations are user-specific for data isolation
 */
@Repository
public class ClientRepository implements ClientDTORepository {

    private static final Logger logger = LoggerFactory.getLogger(ClientRepository.class);

    @Autowired
    private ClientCrudRepository clientCrudRepository;

    @Autowired
    private UserCrudRepository userCrudRepository;

    @Autowired
    private ClientMapper mapper;

    @Override
    public List<ClientDTO> getAllByUserEmail(String userEmail) {
        List<Client> clients = clientCrudRepository.findByUserEmail(userEmail);
        return mapper.toClientDTOs(clients);
    }

    @Override
    public Optional<ClientDTO> getByIdNumberAndUserEmail(String idType, String idNumber, String userEmail) {
        return clientCrudRepository.findByIdTypeAndIdNumberAndUserEmail(idType, idNumber, userEmail)
                .map(mapper::toClientDTO);
    }

    @Override
    public ClientDTO saveClient(ClientDTO clientDTO, String userEmail) {
        logger.info("Saving client for user: {}", userEmail);
        Client client = mapper.toClient(clientDTO);

        // Find and set the user
        Optional<User> userOpt = userCrudRepository.findByEmail(userEmail);
        if (userOpt.isEmpty()) {
            logger.error("User not found with email: {}", userEmail);
            throw new RuntimeException("User not found with email: " + userEmail);
        }

        User user = userOpt.get();
        logger.info("Found user with ID: {} for email: {}", user.getUserId(), userEmail);

        client.setUserId(user);
        client.setCreationDate(LocalDateTime.now());
        client.setModificationDate(LocalDateTime.now());

        logger.info("Client before save - user set: {}", client.getUserId() != null);

        Client savedClient = clientCrudRepository.save(client);
        logger.info("Client saved with ID: {} and user_id: {}", savedClient.getClientId(),
                savedClient.getUserId() != null ? savedClient.getUserId().getUserId() : "NULL");

        return mapper.toClientDTO(savedClient);
    }

    @Override
    public boolean existsByIdAndUserEmail(Integer clientId, String userEmail) {
        logger.info("Checking if client ID: {} exists for user: {}", clientId, userEmail);
        return clientCrudRepository.existsByIdAndUserEmail(clientId, userEmail);
    }

    @Override
    public ClientDTO updateClientByUserEmail(Integer clientId, ClientDTO clientDTO, String userEmail) {
        logger.info("Updating client ID: {} for user: {}", clientId, userEmail);

        Optional<Client> existingClientOpt = clientCrudRepository.findByIdAndUserEmail(clientId, userEmail);
        if (existingClientOpt.isEmpty()) {
            logger.warn("Client ID: {} not found for user: {}", clientId, userEmail);
            return null;
        }

        Client existingClient = existingClientOpt.get();

        // Update fields
        existingClient.setName(clientDTO.getName());
        existingClient.setSurname(clientDTO.getSurname());
        existingClient.setIdType(clientDTO.getIdType());
        existingClient.setIdNumber(clientDTO.getIdNumber());
        existingClient.setModificationDate(LocalDateTime.now());

        Client updatedClient = clientCrudRepository.save(existingClient);
        logger.info("Client ID: {} updated successfully for user: {}", clientId, userEmail);

        return mapper.toClientDTO(updatedClient);
    }

    @Override
    public boolean deleteClientByUserEmail(Integer clientId, String userEmail) {
        logger.info("Deleting client ID: {} for user: {}", clientId, userEmail);

        Optional<Client> clientOpt = clientCrudRepository.findByIdAndUserEmail(clientId, userEmail);
        if (clientOpt.isEmpty()) {
            logger.warn("Client ID: {} not found for user: {}", clientId, userEmail);
            return false;
        }

        clientCrudRepository.delete(clientOpt.get());
        logger.info("Client ID: {} deleted successfully for user: {}", clientId, userEmail);
        return true;
    }
}
