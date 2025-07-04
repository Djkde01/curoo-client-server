package com.curootest.clientback.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.curootest.clientback.domain.UserDTO;
import com.curootest.clientback.domain.repository.UserDTORepository;
import com.curootest.clientback.persistence.crud.UserCrudRepository;
import com.curootest.clientback.persistence.entity.User;
import com.curootest.clientback.persistence.mapper.UserMapper;

@Repository
public class UserRepository implements UserDTORepository {

    @Autowired
    private UserCrudRepository userCrudRepository;

    @Autowired
    private UserMapper mapper;

    @Override
    public Optional<UserDTO> getById(String id) {
        Optional<User> user = userCrudRepository.findById(id);

        return user.map(mapper::toUserDTO);
    }

    @Override
    public List<UserDTO> getAll() {
        List<User> users = (List<User>) userCrudRepository.findAll();
        return mapper.toUserDTOs(users);
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        User user = mapper.toUser(userDTO);
        User savedUser = userCrudRepository.save(user);
        return mapper.toUserDTO(savedUser);
    }

    @Override
    public void deleteUserById(String id) {
        userCrudRepository.deleteById(id);
    }

    @Override
    public Optional<UserDTO> findByEmail(String email) {
        Optional<User> user = userCrudRepository.findByEmail(email);
        return user.map(mapper::toUserDTO);
    }
}
