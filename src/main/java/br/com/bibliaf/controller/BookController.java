package br.com.bibliaf.controller;

import br.com.bibliaf.dto.BookDto;
import br.com.bibliaf.dto.GenreDto;
import br.com.bibliaf.service.BookService;
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
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService service;

    @PostMapping
    public ResponseEntity<BookDto> create(@RequestBody BookDto bookDto) {
        BookDto book = service.create(bookDto);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> findById(@PathVariable(name = "id") Long id) {
        BookDto book = service.findById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<BookDto> update(@RequestBody BookDto bookDto) {
        BookDto book = service.update(bookDto);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<Page<BookDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            PagedResourcesAssembler<BookDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
        Page<BookDto> books = service.findAll(pageable);
        return new ResponseEntity(assembler.toModel(books), HttpStatus.OK);
    }

    @GetMapping("/find/name/{name}")
    public ResponseEntity<List<BookDto>> findByName(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @PathVariable String name,
            PagedResourcesAssembler<BookDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "fullName"));
        Page<BookDto> books = service.findByName(name, pageable);
        return new ResponseEntity(assembler.toModel(books), HttpStatus.OK);
    }

    @GetMapping("/find/author/{author}")
    public ResponseEntity<List<BookDto>> findByAuthor(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @PathVariable String author,
            PagedResourcesAssembler<BookDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
        Page<BookDto> books = service.findByAuthor(author, pageable);
        return new ResponseEntity(assembler.toModel(books), HttpStatus.OK);
    }

    @GetMapping("/find/genre/{genre}")
    public ResponseEntity<List<BookDto>> findByGenre(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @PathVariable String genre,
            PagedResourcesAssembler<BookDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
        Page<BookDto> books = service.findByGenre(genre, pageable);
        return new ResponseEntity(assembler.toModel(books), HttpStatus.OK);
    }
}
