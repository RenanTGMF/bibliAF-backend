package br.com.bibliaf.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto extends RepresentationModel<AuthorDto> {

    private Long id;
    private String name;
    private String bio;
}
