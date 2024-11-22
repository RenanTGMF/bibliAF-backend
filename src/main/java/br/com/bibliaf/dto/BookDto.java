package br.com.bibliaf.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookDto extends RepresentationModel<BookDto> {
    private long id;
    private String title;
    private AuthorDto author;
    private GenreDto genre;
    private String isbn;
    private String summary;
    private String image;
    private int publicationYear;
    private int copiesAvailable;
    private int copies;
}
