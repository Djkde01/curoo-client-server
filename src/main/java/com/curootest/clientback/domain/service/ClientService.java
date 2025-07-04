package com.curootest.clientback.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curootest.clientback.domain.ClientDTO;
import com.curootest.clientback.domain.repository.ClientDTORepository;

@Service
public class ClientService {

    @Autowired
    private ClientDTORepository clientRepository;

    // Method to get all clients
    public List<ClientDTO> getAllClients() {
        return clientRepository.getAll();
    }

    // Method to get a client by ID type and ID number
    public Optional<ClientDTO> getClientByIdNumber(String idType, String idNumber) {
        return clientRepository.getByIdNumber(idType, idNumber);
    }

    // Method to save a client
    public ClientDTO saveClient(ClientDTO clientDTO) {
        return clientRepository.saveClient(clientDTO);
    }

    // Method to delete a client by ID
    public boolean deleteClient(Integer clientId) {
        try {
            clientRepository.deleteClient(clientId);
            return true;
        } catch (Exception e) {
            return false; // Handle the exception as needed
        }
    }

}
