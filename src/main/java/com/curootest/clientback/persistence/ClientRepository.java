package com.curootest.clientback.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        Client client = mapper.toClient(clientDTO);

        // Find and set the user
        User user = userCrudRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        client.setUserId(user);
        client.setCreationDate(LocalDateTime.now());
        client.setModificationDate(LocalDateTime.now());

        Client savedClient = clientCrudRepository.save(client);
        return mapper.toClientDTO(savedClient);
    }

    @Override
    public boolean deleteClientByUserEmail(Integer clientId, String userEmail) {
        Optional<Client> client = clientCrudRepository.findByClientIdAndUserEmail(clientId, userEmail);
        if (client.isPresent()) {
            clientCrudRepository.deleteById(clientId);
            return true;
        }
        return false;
    }
}
