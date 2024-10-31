package br.com.bibliaf.repository;

import br.com.bibliaf.model.BookModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;

@Repository
public interface BookRepository extends JpaRepository<BookModel, Long> {

    public Page<BookModel> findByTitleStartingWithIgnoreCaseOrderByTitle(String title, Pageable pageable);

    public Page<BookModel> findAll(Pageable pageable);

    public Page<BookModel> findByAuthorStartingWithIgnoreCaseOrderByTitle(String author, Pageable pageable);

    public Page<BookModel> findByGenreStartingWithIgnoreCaseOrderByTitle(String genre, Pageable pageable);
}
