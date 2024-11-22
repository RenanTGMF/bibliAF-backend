package br.com.bibliaf.controller;

import br.com.bibliaf.dto.AuthenticationDto;
import br.com.bibliaf.dto.LoginResponseDto;
import br.com.bibliaf.dto.UserDto;
import br.com.bibliaf.model.UserModel;
import br.com.bibliaf.repository.UserRepository;
import br.com.bibliaf.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação", description = "Endpoint utilizado para operações de login e registro de usuários")
@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Realiza o login do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated AuthenticationDto data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var user = (UserModel) auth.getPrincipal();
        var token = tokenService.generateToken((UserModel) auth.getPrincipal());
        var role = user.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null);
        var id = user.getId();
        return ResponseEntity.ok(new LoginResponseDto(token, role, id));
    }


    @Operation(summary = "Realiza o registro de um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Email já cadastrado")
    })
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Validated UserDto data){
        if (this.repository.findByEmail(data.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email já cadastrado.");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());

        UserModel newUser = new UserModel();
        newUser.setUsername(data.getUsername());
        newUser.setEmail(data.getEmail());
        newUser.setPhone(data.getPhone());
        newUser.setAddress(data.getAddress());
        newUser.setPassword(encryptedPassword);
        newUser.setRole(data.getRole());

        this.repository.save(newUser);

        return ResponseEntity.ok("Usuário cadastrado com sucesso.");
    }
}
