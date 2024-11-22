package br.com.bibliaf.dto;

import br.com.bibliaf.model.LoanModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoanDto extends RepresentationModel<LoanDto> {
    private long id;
    private BookDto book;
    private UserDto user;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private LoanModel.LoanStatus status;
}
