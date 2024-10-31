package br.com.bibliaf.controller;

import br.com.bibliaf.dto.ReservationDto;
import br.com.bibliaf.service.ReservationService;
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
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService service;

    @PostMapping
    public ResponseEntity<ReservationDto> create(@RequestBody ReservationDto reservationDto) {
        ReservationDto reservation = service.create(reservationDto);
        return new ResponseEntity<>(reservation, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> findById(@PathVariable(name = "id") Long id) {
        ReservationDto reservation = service.findById(id);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ReservationDto> update(@RequestBody ReservationDto reservationDto) {
        ReservationDto reservation = service.update(reservationDto);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<Page<ReservationDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            PagedResourcesAssembler<ReservationDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "reservationDate"));
        Page<ReservationDto> reservations = service.findAll(pageable);
        return new ResponseEntity(assembler.toModel(reservations), HttpStatus.OK);
    }

    @GetMapping("/find/user/{userId}")
    public ResponseEntity<Page<ReservationDto>> findByUser(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @PathVariable Long userId,
            PagedResourcesAssembler<ReservationDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "reservationDate"));
        Page<ReservationDto> reservations = service.findByUser(userId, pageable);
        return new ResponseEntity(assembler.toModel(reservations), HttpStatus.OK);
    }

    @GetMapping("/find/book/{bookId}")
    public ResponseEntity<Page<ReservationDto>> findByBook(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @PathVariable Long bookId,
            PagedResourcesAssembler<ReservationDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "reservationDate"));
        Page<ReservationDto> reservations = service.findByBook(bookId, pageable);
        return new ResponseEntity(assembler.toModel(reservations), HttpStatus.OK);
    }
}
