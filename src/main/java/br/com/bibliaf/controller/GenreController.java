package br.com.bibliaf.controller;

import br.com.bibliaf.dto.GenreDto;
import br.com.bibliaf.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    
    @Autowired
    GenreService service;

    @PostMapping
    public ResponseEntity<GenreDto> create(@RequestBody GenreDto genreDto) {
        GenreDto genre = service.create(genreDto);
        this.buildSelfLink(genre);
        return new ResponseEntity<>(genre, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> findById(@PathVariable(name = "id") Long id) {
        GenreDto genre = service.findById(id);
        this.buildSelfLink(genre);
        return new ResponseEntity<>(genre, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<GenreDto> update(@RequestBody GenreDto genreDto) {
        GenreDto genre = service.update(genreDto);
        this.buildSelfLink(genre);
        return new ResponseEntity<>(genre, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") long id){
        service.delete(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<Page<GenreDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            PagedResourcesAssembler<GenreDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));
        Page<GenreDto> genres = service.findAll(pageable);
        genres.forEach(this::buildSelfLink);
        return new ResponseEntity(assembler.toModel(genres), HttpStatus.OK);
    }

    @GetMapping("/find/name/{name}")
    public ResponseEntity<List<GenreDto>> findByName(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @PathVariable String name,
            PagedResourcesAssembler<GenreDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));
        Page<GenreDto> genres = service.findByName(name, pageable);
        genres.forEach(this::buildSelfLink);
        return new ResponseEntity(assembler.toModel(genres), HttpStatus.OK);
    }

    private void buildSelfLink(GenreDto genreDto) {
        genreDto.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(
                                this.getClass()).findById(genreDto.getId())
                ).withSelfRel()
        );
    }
}
