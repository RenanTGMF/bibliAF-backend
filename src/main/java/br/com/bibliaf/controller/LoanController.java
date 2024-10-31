package br.com.bibliaf.controller;

import br.com.bibliaf.dto.LoanDto;
import br.com.bibliaf.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private LoanService service;

    @PostMapping
    public ResponseEntity<LoanDto> create(@RequestBody LoanDto loanDto) {
        LoanDto loan = service.create(loanDto);
        return new ResponseEntity<>(loan, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDto> findById(@PathVariable(name = "id") Long id) {
        LoanDto loan = service.findById(id);
        return new ResponseEntity<>(loan, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<LoanDto> update(@RequestBody LoanDto loanDto) {
        LoanDto loan = service.update(loanDto);
        return new ResponseEntity<>(loan, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<Page<LoanDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            PagedResourcesAssembler<LoanDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "loanDate"));
        Page<LoanDto> loans = service.findAll(pageable);
        return new ResponseEntity(assembler.toModel(loans), HttpStatus.OK);
    }

    @GetMapping("/find/user/{userId}")
    public ResponseEntity<Page<LoanDto>> findByUser(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @PathVariable Long userId,
            PagedResourcesAssembler<LoanDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "loanDate"));
        Page<LoanDto> loans = service.findByUser(userId, pageable);
        return new ResponseEntity(assembler.toModel(loans), HttpStatus.OK);
    }

    @GetMapping("/find/book/{bookId}")
    public ResponseEntity<Page<LoanDto>> findByBook(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @PathVariable Long bookId,
            PagedResourcesAssembler<LoanDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "loanDate"));
        Page<LoanDto> loans = service.findByBook(bookId, pageable);
        return new ResponseEntity(assembler.toModel(loans), HttpStatus.OK);
    }
}
