package com.curootest.clientback.persistence.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.curootest.clientback.domain.UserDTO;
import com.curootest.clientback.persistence.entity.User;

@Mapper(componentModel = "spring", uses = { ClientMapper.class })
public interface UserMapper {

    @Mappings({
            // Map fields from User entity to User domain object
            @Mapping(target = "id", source = "user.userId"),
            @Mapping(target = "name", source = "user.name"),
            @Mapping(target = "surname", source = "user.surname"),
            @Mapping(target = "email", source = "user.email"),
            @Mapping(target = "password", source = "user.password"),
            @Mapping(target = "mobilePhone", source = "user.mobilePhone")
    })

    UserDTO toUserDTO(User user);

    List<UserDTO> toUserDTOs(List<User> users);

    List<User> toUsers(List<UserDTO> userDTOs);

    @Mappings({
        @Mapping(target = "userId", source = "userDTO.id"),
        @Mapping(target = "name", source = "userDTO.name"),
        @Mapping(target = "surname", source = "userDTO.surname"),
        @Mapping(target = "email", source = "userDTO.email"),
        @Mapping(target = "password", source = "userDTO.password"),
        @Mapping(target = "mobilePhone", source = "userDTO.mobilePhone"),
        @Mapping(target = "creationDate", ignore = true),
        @Mapping(target = "modificationDate", ignore = true),
        @Mapping(target = "clients", ignore = true)
})
    User toUser(UserDTO userDTO);

}
