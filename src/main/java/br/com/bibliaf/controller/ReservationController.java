package br.com.bibliaf.controller;

import br.com.bibliaf.dto.ReservationDto;
import br.com.bibliaf.service.ReservationService;
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

@Tag(name = "Reservas", description = "Endpoint usado para operações relacionadas a reservas de livros.")
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService service;

    @Operation(summary = "Criar uma nova reserva", description = "Este endpoint cria uma nova reserva de livro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida")
    })
    @PostMapping
    public ResponseEntity<ReservationDto> create(@RequestBody ReservationDto reservationDto) {
        ReservationDto reservation = service.create(reservationDto);
        return new ResponseEntity<>(reservation, HttpStatus.CREATED);
    }

    @Operation(summary = "Obter reserva pelo ID", description = "Este endpoint recupera uma reserva pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> findById(@PathVariable(name = "id") Long id) {
        ReservationDto reservation = service.findById(id);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @Operation(summary = "Atualizar uma reserva existente", description = "Este endpoint atualiza uma reserva existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida")
    })
    @PutMapping
    public ResponseEntity<ReservationDto> update(@RequestBody ReservationDto reservationDto) {
        ReservationDto reservation = service.update(reservationDto);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @Operation(summary = "Excluir uma reserva pelo ID", description = "Este endpoint exclui uma reserva pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reserva excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Obter todas as reservas", description = "Este endpoint recupera todas as reservas do sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservas recuperadas com sucesso"),
    })
    @GetMapping
    public ResponseEntity<CollectionModel<ReservationDto>> findAll() {
        CollectionModel<ReservationDto> reservations = CollectionModel.of(service.findAll());
        for (ReservationDto reservation : reservations) {
            buildSelfLink(reservation);
        }
        this.buildCollectionLink(reservations);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @Operation(summary = "Obter reservas por usuário", description = "Este endpoint recupera reservas baseadas no ID do usuário com paginação e ordenação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservas encontradas"),
    })
    @GetMapping("/find/user/{userId}")
    public ResponseEntity<Page<ReservationDto>> findByUser(
            @Parameter(description = "Número da página para paginação") @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Número de reservas por página") @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(description = "Direção da ordenação (asc/desc)") @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @PathVariable Long userId,
            PagedResourcesAssembler<ReservationDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "reservationDate"));
        Page<ReservationDto> reservations = service.findByUser(userId, pageable);
        return new ResponseEntity(assembler.toModel(reservations), HttpStatus.OK);
    }

    @Operation(summary = "Obter reservas por livro", description = "Este endpoint recupera reservas baseadas no ID do livro com paginação e ordenação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservas encontradas"),
    })
    @GetMapping("/find/book/{bookId}")
    public ResponseEntity<Page<ReservationDto>> findByBook(
            @Parameter(description = "Número da página para paginação") @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Número de reservas por página") @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(description = "Direção da ordenação (asc/desc)") @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @PathVariable Long bookId,
            PagedResourcesAssembler<ReservationDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "reservationDate"));
        Page<ReservationDto> reservations = service.findByBook(bookId, pageable);
        return new ResponseEntity(assembler.toModel(reservations), HttpStatus.OK);
    }

    private void buildSelfLink(ReservationDto reservationDto) {
        reservationDto.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(this.getClass()).findById(reservationDto.getId())
                ).withSelfRel()
        );
    }

    public void buildCollectionLink(CollectionModel<ReservationDto> reservations){
        reservations.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(
                                this.getClass()).findAll()
                ).withRel(LinkRelation.of("COLLECTION"))
        );
    }
}
