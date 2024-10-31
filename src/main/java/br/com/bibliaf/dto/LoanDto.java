package br.com.bibliaf.dto;

import br.com.bibliaf.model.LoanModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoanDto extends RepresentationModel<LoanDto> {
    private long id;
    private BookDto book;
    private UserDto user;
    private Date loanDate;
    private Date returnDate;
    private LoanModel.LoanStatus status;

}
