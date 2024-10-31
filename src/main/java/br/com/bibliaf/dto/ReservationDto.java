package br.com.bibliaf.dto;

import br.com.bibliaf.model.ReservationModel;
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
public class ReservationDto extends RepresentationModel<ReservationDto> {
    private long id;
    private BookDto book;
    private UserDto user;
    private Date reservationDate;
    private ReservationModel.ReservationStatus status;
}
