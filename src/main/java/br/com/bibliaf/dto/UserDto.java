package br.com.bibliaf.dto;

import br.com.bibliaf.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto extends RepresentationModel<UserDto> {
    private long id;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String password;
    private UserModel.UserRole role;
}
