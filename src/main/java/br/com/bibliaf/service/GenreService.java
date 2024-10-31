package br.com.bibliaf.service;

import br.com.bibliaf.dto.GenreDto;
import br.com.bibliaf.exception.ResourceNotFoundException;
import br.com.bibliaf.mapper.CustomModelMapper;
import br.com.bibliaf.model.GenreModel;
import br.com.bibliaf.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GenreService {
    
    @Autowired
    private GenreRepository repository;

    public GenreDto create(GenreDto genreDto) {
        GenreModel genreModel = CustomModelMapper.parseObject(genreDto, GenreModel.class);
        return CustomModelMapper.parseObject(repository.save(genreModel), GenreDto.class);
    }

    public GenreDto findById(Long id) {
        GenreModel found = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Gênero não encontrado!"));
        return CustomModelMapper.parseObject(found, GenreDto.class);
    }

    public GenreDto update(GenreDto genreDto) {
        GenreModel found = repository.findById(genreDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Gênero não encontrado!"));
        found.setName(genreDto.getName());
        return CustomModelMapper.parseObject(repository.save(found), GenreDto.class);
    }

    public void delete(Long id) {
        GenreModel found = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Gênero não encontrado!"));
        repository.delete(found);
    }

    public Page<GenreDto> findAll(Pageable pageable) {
        var genres = repository.findAll(pageable);
        return genres.map(genre -> CustomModelMapper.parseObject(genre, GenreDto.class));
    }

    public Page<GenreDto> findByName(String name, Pageable pageable) {
        var genres = repository.findByNameStartingWithIgnoreCaseOrderByName(name, pageable);
        return genres.map(genre -> CustomModelMapper.parseObject(genre, GenreDto.class));
    }
}
