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

    @Column(columnDefinition = "TEXT")
    private String image;

    @Column
    private int copies;

    public void setCopies(int newCopies) {
        int difference = newCopies - this.copies;

        this.copiesAvailable += difference;

        this.copies = newCopies;

        if (this.copiesAvailable < 0) {
            this.copiesAvailable = 0;
        } else if (this.copiesAvailable > this.copies) {
            this.copiesAvailable = this.copies;
        }
    }
}
