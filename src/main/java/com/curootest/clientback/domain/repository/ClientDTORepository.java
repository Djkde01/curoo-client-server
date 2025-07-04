package com.curootest.clientback.domain.repository;

import java.util.List;
import java.util.Optional;

import com.curootest.clientback.domain.ClientDTO;

public interface ClientDTORepository {
    Optional<ClientDTO> getByIdNumber(String idType, String idNumber);

    List<ClientDTO> getAll();

    ClientDTO saveClient(ClientDTO clientDTO);

    void deleteClient(Integer clientId);
}
