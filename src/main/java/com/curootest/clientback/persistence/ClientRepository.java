package com.curootest.clientback.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.curootest.clientback.domain.ClientDTO;
import com.curootest.clientback.domain.repository.ClientDTORepository;
import com.curootest.clientback.persistence.crud.ClientCrudRepository;
import com.curootest.clientback.persistence.entity.Client;
import com.curootest.clientback.persistence.mapper.ClientMapper;

@Repository
public class ClientRepository implements ClientDTORepository {

    @Autowired
    private ClientCrudRepository clientCrudRepository;

    @Autowired
    private ClientMapper mapper;

    @Override
    public List<ClientDTO> getAll() {
        List<Client> clients = (List<Client>) clientCrudRepository.findAll();

        return mapper.toClientDTOs(clients);
    }

    @Override
    public Optional<ClientDTO> getByIdNumber(String idType, String idNumber) {
        Client client = clientCrudRepository.findByIdTypeAndIdNumber(idType, idNumber);
        return Optional.of(mapper.toClientDTO(client));
    }

    @Override
    public ClientDTO saveClient(ClientDTO clientDTO) {
        Client client = mapper.toClient(clientDTO);
        Client savedClient = clientCrudRepository.save(client);
        return mapper.toClientDTO(savedClient);
    }

    @Override
    public void deleteClient(Integer clientId) {
        clientCrudRepository.deleteById(clientId);
    }

}
