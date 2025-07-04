package com.curootest.clientback.persistence.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.curootest.clientback.domain.ClientDTO;
import com.curootest.clientback.persistence.entity.Client;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface ClientMapper {

    @Mappings({
            @Mapping(target = "id", source = "client.id"),
            @Mapping(target = "name", source = "client.name"),
            @Mapping(target = "surname", source = "client.surname"),
            @Mapping(target = "idType", source = "client.idType"),
            @Mapping(target = "idNumber", source = "client.idNumber")
    })

    ClientDTO toClientDTO(Client client);

    List<ClientDTO> toClientDTOs(List<Client> clients);

    List<Client> toClients(List<ClientDTO> clientDTOs);

    Client toClient(ClientDTO clientDTO);
}
