package br.com.bibliaf.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "books")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private AuthorModel author;

    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private GenreModel genre;

    @Column(name = "publication_year")
    private int publicationYear;

    @Column(unique = true)
    private String isbn;

    @Column(name = "copies_available")
    private int copiesAvailable;

    @Column(columnDefinition = "TEXT")
    private String summary;
}
