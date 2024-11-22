package br.com.bibliaf.service;

import br.com.bibliaf.dto.UserDto;
import br.com.bibliaf.exception.ResourceNotFoundException;
import br.com.bibliaf.mapper.CustomModelMapper;
import br.com.bibliaf.model.UserModel;
import br.com.bibliaf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public UserDto create(UserDto userDto) {
        UserModel userModel = CustomModelMapper.parseObject(userDto, UserModel.class);
        return CustomModelMapper.parseObject(repository.save(userModel), UserDto.class);
    }

    public UserDto findById(Long id) {
        UserModel found = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Usuário não encontrado!"));
        return CustomModelMapper.parseObject(found, UserDto.class);
    }

    public UserDto update(UserDto userDto) {
        UserModel found = repository.findById(userDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Usuário não encontrado!"));
        if (!found.getPassword().equals(userDto.getPassword())) {
            String encodedPassword = new BCryptPasswordEncoder().encode(userDto.getPassword());
            found.setPassword(encodedPassword);
        } else {
            found.setPassword(userDto.getPassword());
        }
        found.setUsername(userDto.getUsername());
        found.setEmail(userDto.getEmail());
        found.setPhone(userDto.getPhone());
        found.setAddress(userDto.getAddress());
        found.setRole(userDto.getRole());
        return CustomModelMapper.parseObject(repository.save(found), UserDto.class);
    }

    public void delete(Long id) {
        UserModel found = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Usuário não encontrado!"));
        repository.delete(found);
    }

    public Page<UserDto> findAll(Pageable pageable) {
        var users = repository.findAll(pageable);
        return users.map(user -> CustomModelMapper.parseObject(user, UserDto.class));
    }

    public Page<UserDto> findByUsername(String username, Pageable pageable) {
        var users = repository.findByUsernameStartingWithIgnoreCaseOrderByUsername(username, pageable);
        return users.map(user -> CustomModelMapper.parseObject(user, UserDto.class));
    }

    public Page<UserDto> findByEmail(String email, Pageable pageable) {
        var users = repository.findByEmail(email, pageable);
        return users.map(user -> CustomModelMapper.parseObject(user, UserDto.class));
    }
}
