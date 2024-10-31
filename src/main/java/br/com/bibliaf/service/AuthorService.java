package br.com.bibliaf.service;

import br.com.bibliaf.dto.AuthorDto;
import br.com.bibliaf.exception.ResourceNotFoundException;
import br.com.bibliaf.mapper.CustomModelMapper;
import br.com.bibliaf.model.AuthorModel;
import br.com.bibliaf.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    
    @Autowired
    private AuthorRepository repository;

    public AuthorDto create(AuthorDto authorDto) {
        AuthorModel authorModel = CustomModelMapper.parseObject(authorDto, AuthorModel.class);
        return CustomModelMapper.parseObject(repository.save(authorModel), AuthorDto.class);
    }

    public AuthorDto findById(Long id) {
        AuthorModel found = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Autor não encontrado!"));
        return CustomModelMapper.parseObject(found, AuthorDto.class);
    }

    public AuthorDto update(AuthorDto authorDto) {
        AuthorModel found = repository.findById(authorDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Autor não encontrado!"));
        found.setName(authorDto.getName());
        found.setBio(authorDto.getBio());
        return CustomModelMapper.parseObject(repository.save(found), AuthorDto.class);
    }

    public void delete(Long id) {
        AuthorModel found = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Autor não encontrado!"));
        repository.delete(found);
    }

    public Page<AuthorDto> findAll(Pageable pageable) {
        var authors = repository.findAll(pageable);
        return authors.map(author -> CustomModelMapper.parseObject(author, AuthorDto.class));
    }

    public Page<AuthorDto> findByName(String name, Pageable pageable) {
        var authors = repository.findByNameStartingWithIgnoreCaseOrderByName(name, pageable);
        return authors.map(author -> CustomModelMapper.parseObject(author, AuthorDto.class));
    }
}
