package br.com.bibliaf.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomExceptionResponse extends RuntimeException {

    private Date timestamp;
    private String message;
    private String details;

}
