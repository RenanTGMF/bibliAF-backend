package br.com.bibliaf.controller;

import br.com.bibliaf.dto.UserDto;
import br.com.bibliaf.service.UserService;
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

@Tag(name = "Users", description = "Endpoint usado para operações que envolvem usuários")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    @Operation(summary = "Cria um novo usuário", description = "Cria um novo usuário na base de dados com as informações fornecidas.")
    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(schema = @Schema(implementation = UserDto.class)))
    @ApiResponse(responseCode = "400", description = "Erro na validação dos dados")
    public ResponseEntity<UserDto> create(@RequestBody @Parameter(description = "Objeto contendo as informações do usuário a ser criado") UserDto userDto) {
        UserDto user = service.create(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um usuário pelo ID", description = "Retorna as informações do usuário com o ID especificado.")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado", content = @Content(schema = @Schema(implementation = UserDto.class)))
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<UserDto> findById(@PathVariable(name = "id") @Parameter(description = "ID do usuário a ser buscado") Long id) {
        UserDto user = service.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    @Operation(summary = "Atualiza as informações de um usuário", description = "Atualiza um usuário existente com as novas informações fornecidas.")
    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso", content = @Content(schema = @Schema(implementation = UserDto.class)))
    @ApiResponse(responseCode = "400", description = "Erro na validação dos dados")
    public ResponseEntity<UserDto> update(@RequestBody @Parameter(description = "Objeto contendo as informações do usuário a ser atualizado") UserDto userDto) {
        UserDto user = service.update(userDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um usuário", description = "Deleta o usuário com o ID especificado.")
    @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") @Parameter(description = "ID do usuário a ser deletado") Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @Operation(summary = "Busca todos os usuários com paginação", description = "Retorna uma lista de usuários com suporte a paginação e ordenação.")
    @ApiResponse(responseCode = "200", description = "Lista de usuários", content = @Content(schema = @Schema(implementation = UserDto.class)))
    public ResponseEntity<Page<UserDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") @Parameter(description = "Número da página para paginação") int page,
            @RequestParam(value = "size", defaultValue = "10") @Parameter(description = "Número de itens por página") int size,
            @RequestParam(value = "direction", defaultValue = "asc") @Parameter(description = "Direção da ordenação (asc ou desc)") String direction,
            PagedResourcesAssembler<UserDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "username"));
        Page<UserDto> users = service.findAll(pageable);
        return new ResponseEntity(assembler.toModel(users), HttpStatus.OK);
    }

    @GetMapping("/find/username/{username}")
    @Operation(summary = "Busca usuários pelo nome de usuário", description = "Retorna uma lista de usuários que correspondem ao nome de usuário fornecido.")
    @ApiResponse(responseCode = "200", description = "Lista de usuários encontrados", content = @Content(schema = @Schema(implementation = UserDto.class)))
    public ResponseEntity<Page<UserDto>> findByUsername(
            @RequestParam(value = "page", defaultValue = "0") @Parameter(description = "Número da página para paginação") int page,
            @RequestParam(value = "size", defaultValue = "10") @Parameter(description = "Número de itens por página") int size,
            @RequestParam(value = "direction", defaultValue = "asc") @Parameter(description = "Direção da ordenação (asc ou desc)") String direction,
            @PathVariable String username,
            PagedResourcesAssembler<UserDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "username"));
        Page<UserDto> users = service.findByUsername(username, pageable);
        return new ResponseEntity(assembler.toModel(users), HttpStatus.OK);
    }

    @GetMapping("/find/email/{email}")
    @Operation(summary = "Busca usuários por e-mail", description = "Retorna uma lista de usuários que correspondem ao e-mail fornecido.")
    @ApiResponse(responseCode = "200", description = "Lista de usuários encontrados", content = @Content(schema = @Schema(implementation = UserDto.class)))
    public ResponseEntity<Page<UserDto>> findByEmail(
            @RequestParam(value = "page", defaultValue = "0") @Parameter(description = "Número da página para paginação") int page,
            @RequestParam(value = "size", defaultValue = "10") @Parameter(description = "Número de itens por página") int size,
            @RequestParam(value = "direction", defaultValue = "asc") @Parameter(description = "Direção da ordenação (asc ou desc)") String direction,
            @PathVariable String email,
            PagedResourcesAssembler<UserDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "username"));
        Page<UserDto> users = service.findByEmail(email, pageable);
        return new ResponseEntity(assembler.toModel(users), HttpStatus.OK);
    }
}
