package br.com.bibliaf.controller;

import br.com.bibliaf.dto.BookDto;
import br.com.bibliaf.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Livros", description = "Endpoint usado para operações que envolvem Livros")
@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService service;

    @PostMapping
    @Operation(summary = "Cria um novo livro", description = "Cria um novo livro na base de dados com as informações fornecidas.")
    @ApiResponse(responseCode = "201", description = "Livro criado com sucesso", content = @Content(schema = @Schema(implementation = BookDto.class)))
    @ApiResponse(responseCode = "400", description = "Erro na validação dos dados")
    public ResponseEntity<BookDto> create(@RequestBody @Parameter(description = "Objeto contendo as informações do livro a ser criado") BookDto bookDto) {
        BookDto book = service.create(bookDto);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um livro pelo ID", description = "Retorna as informações do livro com o ID especificado.")
    @ApiResponse(responseCode = "200", description = "Livro encontrado", content = @Content(schema = @Schema(implementation = BookDto.class)))
    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    public ResponseEntity<BookDto> findById(@PathVariable(name = "id") @Parameter(description = "ID do livro a ser buscado") Long id) {
        BookDto book = service.findById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PutMapping
    @Operation(summary = "Atualiza as informações de um livro", description = "Atualiza um livro existente com as novas informações fornecidas.")
    @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso", content = @Content(schema = @Schema(implementation = BookDto.class)))
    @ApiResponse(responseCode = "400", description = "Erro na validação dos dados")
    public ResponseEntity<BookDto> update(@RequestBody @Parameter(description = "Objeto contendo as informações do livro a ser atualizado") BookDto bookDto) {
        BookDto book = service.update(bookDto);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um livro", description = "Deleta o livro com o ID especificado.")
    @ApiResponse(responseCode = "204", description = "Livro deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") @Parameter(description = "ID do livro a ser deletado") Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @Operation(summary = "Busca todos os livros com paginação", description = "Retorna uma lista de livros com suporte a paginação e ordenação.")
    @ApiResponse(responseCode = "200", description = "Lista de livros", content = @Content(schema = @Schema(implementation = BookDto.class)))
    public ResponseEntity<Page<BookDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") @Parameter(description = "Número da página para paginação") int page,
            @RequestParam(value = "size", defaultValue = "10") @Parameter(description = "Número de itens por página") int size,
            @RequestParam(value = "direction", defaultValue = "asc") @Parameter(description = "Direção da ordenação (asc ou desc)") String direction,
            PagedResourcesAssembler<BookDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
        Page<BookDto> books = service.findAll(pageable);
        return new ResponseEntity(assembler.toModel(books), HttpStatus.OK);
    }

    @GetMapping("/find/title/{title}")
    @Operation(summary = "Busca livros pelo título", description = "Retorna uma lista de livros que correspondem ao título fornecido.")
    @ApiResponse(responseCode = "200", description = "Lista de livros encontrados", content = @Content(schema = @Schema(implementation = BookDto.class)))
    public ResponseEntity<List<BookDto>> findByTitle(
            @RequestParam(value = "page", defaultValue = "0") @Parameter(description = "Número da página para paginação") int page,
            @RequestParam(value = "size", defaultValue = "10") @Parameter(description = "Número de itens por página") int size,
            @RequestParam(value = "direction", defaultValue = "asc") @Parameter(description = "Direção da ordenação (asc ou desc)") String direction,
            @PathVariable String title,
            PagedResourcesAssembler<BookDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
        Page<BookDto> books = service.findByTitle(title, pageable);
        return new ResponseEntity(assembler.toModel(books), HttpStatus.OK);
    }

    @GetMapping("/find/author/{author}")
    @Operation(summary = "Busca livros por autor", description = "Retorna uma lista de livros que correspondem ao autor fornecido.")
    @ApiResponse(responseCode = "200", description = "Lista de livros encontrados", content = @Content(schema = @Schema(implementation = BookDto.class)))
    public ResponseEntity<List<BookDto>> findByAuthor(
            @RequestParam(value = "page", defaultValue = "0") @Parameter(description = "Número da página para paginação") int page,
            @RequestParam(value = "size", defaultValue = "10") @Parameter(description = "Número de itens por página") int size,
            @RequestParam(value = "direction", defaultValue = "asc") @Parameter(description = "Direção da ordenação (asc ou desc)") String direction,
            @PathVariable Long author,
            PagedResourcesAssembler<BookDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
        Page<BookDto> books = service.findByAuthor(author, pageable);
        return new ResponseEntity(assembler.toModel(books), HttpStatus.OK);
    }

    @GetMapping("/find/genre/{genre}")
    @Operation(summary = "Busca livros por gênero", description = "Retorna uma lista de livros que correspondem ao gênero fornecido.")
    @ApiResponse(responseCode = "200", description = "Lista de livros encontrados", content = @Content(schema = @Schema(implementation = BookDto.class)))
    public ResponseEntity<List<BookDto>> findByGenre(
            @RequestParam(value = "page", defaultValue = "0") @Parameter(description = "Número da página para paginação") int page,
            @RequestParam(value = "size", defaultValue = "10") @Parameter(description = "Número de itens por página") int size,
            @RequestParam(value = "direction", defaultValue = "asc") @Parameter(description = "Direção da ordenação (asc ou desc)") String direction,
            @PathVariable Long genre,
            PagedResourcesAssembler<BookDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
        Page<BookDto> books = service.findByGenre(genre, pageable);
        return new ResponseEntity(assembler.toModel(books), HttpStatus.OK);
    }
}
