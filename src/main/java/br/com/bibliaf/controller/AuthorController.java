package br.com.bibliaf.controller;

import br.com.bibliaf.dto.AuthorDto;
import br.com.bibliaf.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "Autores", description = "Endpoint usado para operações que envolvem Autores")
@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    AuthorService service;

    @Operation(summary = "Cria um novo autor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Autor criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping
    public ResponseEntity<AuthorDto> create(@RequestBody AuthorDto authorDto) {
        AuthorDto author = service.create(authorDto);
        return new ResponseEntity<>(author, HttpStatus.CREATED);
    }

    @Operation(summary = "Busca um autor pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> findById(@PathVariable(name = "id") Long id) {
        AuthorDto author = service.findById(id);
        this.buildSelfLink(author);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @Operation(summary = "Atualiza um autor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PutMapping
    public ResponseEntity<AuthorDto> update(@RequestBody AuthorDto authorDto) {
        AuthorDto author = service.update(authorDto);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @Operation(summary = "Deleta um autor pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Autor deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") long id){
        service.delete(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Lista todos os autores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de autores retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<AuthorDto>> findAll() {
        CollectionModel<AuthorDto> authors = CollectionModel.of(service.findAll());
        for(AuthorDto author : authors) {
            buildSelfLink(author);
        }
        buildCollectionLink(authors);
        return new ResponseEntity<CollectionModel<AuthorDto>>(authors, HttpStatus.OK);
    }

    @Operation(summary = "Lista autores paginados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de autores paginada retornada com sucesso")
    })
    @GetMapping("/paged")
    public ResponseEntity<Page<AuthorDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            PagedResourcesAssembler<AuthorDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));
        Page<AuthorDto> authors = service.findAll(pageable);
        authors.forEach(this::buildSelfLink);
        return new ResponseEntity(assembler.toModel(authors), HttpStatus.OK);
    }

    @Operation(summary = "Busca autores pelo nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de autores encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum autor encontrado")
    })
    @GetMapping("/find/name/{name}")
    public ResponseEntity<List<AuthorDto>> findByName(@PathVariable(name = "name") String name) {
        var authors = service.findByName(name);
        return new ResponseEntity<List<AuthorDto>>(authors, HttpStatus.OK);
    }

    private void buildSelfLink(AuthorDto authorDto) {
        authorDto.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(this.getClass()).findById(authorDto.getId())
                ).withSelfRel()
        );
    }

    public void buildCollectionLink(CollectionModel<AuthorDto> authors){
        authors.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(
                                this.getClass()).findAll()
                ).withRel(LinkRelation.of("COLLECTION"))
        );
    }
}
