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
            @Mapping(target = "id", source = "user.id"),
            @Mapping(target = "name", source = "user.name"),
            @Mapping(target = "surname", source = "user.surname"),
            @Mapping(target = "email", source = "user.email")
    })

    UserDTO toUserDTO(User user);

    List<UserDTO> toUserDTOs(List<User> users);

    List<User> toUsers(List<UserDTO> userDTOs);

    User toUser(UserDTO userDTO);

}
