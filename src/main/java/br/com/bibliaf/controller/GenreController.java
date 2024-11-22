package br.com.bibliaf.controller;

import br.com.bibliaf.dto.GenreDto;
import br.com.bibliaf.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Gêneros", description = "Endpoint usado para operações que envolvem Gêneros")
@RestController
@RequestMapping("/genres")
public class GenreController {

    @Autowired
    GenreService service;

    @Operation(summary = "Create a new genre", description = "This endpoint creates a new genre in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Genre created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<GenreDto> create(@RequestBody GenreDto genreDto) {
        GenreDto genre = service.create(genreDto);
        return new ResponseEntity<>(genre, HttpStatus.CREATED);
    }

    @Operation(summary = "Get genre by ID", description = "This endpoint retrieves a genre by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre found"),
            @ApiResponse(responseCode = "404", description = "Genre not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> findById(@PathVariable(name = "id") Long id) {
        GenreDto genre = service.findById(id);
        this.buildSelfLink(genre);
        return new ResponseEntity<>(genre, HttpStatus.OK);
    }

    @Operation(summary = "Update an existing genre", description = "This endpoint updates an existing genre.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping
    public ResponseEntity<GenreDto> update(@RequestBody GenreDto genreDto) {
        GenreDto genre = service.update(genreDto);
        return new ResponseEntity<>(genre, HttpStatus.OK);
    }

    @Operation(summary = "Delete a genre by ID", description = "This endpoint deletes a genre by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Genre deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Genre not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get all genres", description = "This endpoint retrieves all genres in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genres retrieved successfully"),
    })
    @GetMapping
    public ResponseEntity<CollectionModel<GenreDto>> findAll() {
        CollectionModel<GenreDto> genres = CollectionModel.of(service.findAll());
        for (GenreDto genre : genres) {
            buildSelfLink(genre);
        }
        this.buildCollectionLink(genres);
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }

    @Operation(summary = "Get genres with pagination", description = "This endpoint retrieves genres with pagination and sorting options.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genres retrieved successfully"),
    })
    @GetMapping("/paged")
    public ResponseEntity<Page<GenreDto>> findAll(
            @Parameter(description = "Page number for pagination") @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Number of genres per page") @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(value = "direction", defaultValue = "asc") String direction,
            PagedResourcesAssembler<GenreDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));
        Page<GenreDto> genres = service.findAll(pageable);
        genres.forEach(this::buildSelfLink);
        return new ResponseEntity(assembler.toModel(genres), HttpStatus.OK);
    }

    @Operation(summary = "Find genres by name", description = "This endpoint retrieves genres that match the given name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genres found"),
            @ApiResponse(responseCode = "404", description = "No genres found")
    })
    @GetMapping("/find/name/{name}")
    public ResponseEntity<List<GenreDto>> findByName(@PathVariable(name = "name") String name) {
        List<GenreDto> genres = service.findByName(name);
        for (GenreDto genre : genres) {
            buildSelfLink(genre);
        }
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }

    private void buildSelfLink(GenreDto genreDto) {
        genreDto.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(this.getClass()).findById(genreDto.getId())
                ).withSelfRel()
        );
    }

    public void buildCollectionLink(CollectionModel<GenreDto> genres){
        genres.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(
                                this.getClass()).findAll()
                ).withRel(LinkRelation.of("COLLECTION"))
        );
    }
}
