package br.com.bibliaf.controller;

import br.com.bibliaf.dto.LoanDto;
import br.com.bibliaf.service.LoanService;
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

@Tag(name = "Empréstimos", description = "Endpoint usado para operações relacionadas a empréstimos.")
@RestController
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private LoanService service;

    @Operation(summary = "Criar um novo empréstimo", description = "Este endpoint cria um novo empréstimo no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empréstimo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida")
    })
    @PostMapping
    public ResponseEntity<LoanDto> create(@RequestBody LoanDto loanDto) {
        LoanDto loan = service.create(loanDto);
        return new ResponseEntity<>(loan, HttpStatus.CREATED);
    }

    @Operation(summary = "Obter empréstimo pelo ID", description = "Este endpoint recupera um empréstimo pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empréstimo encontrado"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LoanDto> findById(@PathVariable(name = "id") Long id) {
        LoanDto loan = service.findById(id);
        this.buildSelfLink(loan);
        return new ResponseEntity<>(loan, HttpStatus.OK);
    }

    @Operation(summary = "Atualizar um empréstimo existente", description = "Este endpoint atualiza um empréstimo existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empréstimo atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida")
    })
    @PutMapping
    public ResponseEntity<LoanDto> update(@RequestBody LoanDto loanDto) {
        LoanDto loan = service.update(loanDto);
        return new ResponseEntity<>(loan, HttpStatus.OK);
    }

    @Operation(summary = "Excluir um empréstimo pelo ID", description = "Este endpoint exclui um empréstimo pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empréstimo excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Obter todos os empréstimos", description = "Este endpoint recupera todos os empréstimos do sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empréstimos recuperados com sucesso"),
    })
    @GetMapping
    public ResponseEntity<CollectionModel<LoanDto>> findAll() {
        CollectionModel<LoanDto> loans = CollectionModel.of(service.findAll());
        for (LoanDto loan : loans) {
            buildSelfLink(loan);
        }
        this.buildCollectionLink(loans);
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }

    @Operation(summary = "Obter empréstimos por usuário", description = "Este endpoint recupera empréstimos baseados no ID do usuário com paginação e ordenação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empréstimos encontrados"),
    })
    @GetMapping("/find/user/{userId}")
    public ResponseEntity<Page<LoanDto>> findByUser(
            @Parameter(description = "Número da página para paginação") @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Número de empréstimos por página") @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(description = "Direção da ordenação (asc/desc)") @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @PathVariable Long userId,
            PagedResourcesAssembler<LoanDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "loanDate"));
        Page<LoanDto> loans = service.findByUser(userId, pageable);
        return new ResponseEntity(assembler.toModel(loans), HttpStatus.OK);
    }

    @Operation(summary = "Obter empréstimos por livro", description = "Este endpoint recupera empréstimos baseados no ID do livro com paginação e ordenação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empréstimos encontrados"),
    })
    @GetMapping("/find/book/{bookId}")
    public ResponseEntity<Page<LoanDto>> findByBook(
            @Parameter(description = "Número da página para paginação") @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Número de empréstimos por página") @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(description = "Direção da ordenação (asc/desc)") @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @PathVariable Long bookId,
            PagedResourcesAssembler<LoanDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "loanDate"));
        Page<LoanDto> loans = service.findByBook(bookId, pageable);
        return new ResponseEntity(assembler.toModel(loans), HttpStatus.OK);
    }

    private void buildSelfLink(LoanDto loanDto) {
        loanDto.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(this.getClass()).findById(loanDto.getId())
                ).withSelfRel()
        );
    }

    public void buildCollectionLink(CollectionModel<LoanDto> loans){
        loans.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(
                                this.getClass()).findAll()
                ).withRel(LinkRelation.of("COLLECTION"))
        );
    }
}
