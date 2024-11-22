package br.com.bibliaf.service;

import br.com.bibliaf.dto.BookDto;
import br.com.bibliaf.exception.ResourceNotFoundException;
import br.com.bibliaf.mapper.CustomModelMapper;
import br.com.bibliaf.model.AuthorModel;
import br.com.bibliaf.model.BookModel;
import br.com.bibliaf.model.GenreModel;
import br.com.bibliaf.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;

    public BookDto create(BookDto bookDto) {
        BookModel bookModel = CustomModelMapper.parseObject(bookDto, BookModel.class);
        return CustomModelMapper.parseObject(repository.save(bookModel), BookDto.class);
    }

    public BookDto findById(Long id) {
        BookModel found = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Livro não encontrado!"));
        return CustomModelMapper.parseObject(found, BookDto.class);
    }

    public BookDto update(BookDto bookDto) {
        BookModel found = repository.findById(bookDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Livro não encontrado!"));
        found.setImage(bookDto.getImage());
        found.setTitle(bookDto.getTitle());
        found.setAuthor(CustomModelMapper.parseObject(bookDto.getAuthor(), AuthorModel.class));
        found.setGenre(CustomModelMapper.parseObject(bookDto.getGenre(), GenreModel.class));
        found.setIsbn(bookDto.getIsbn());
        found.setSummary(bookDto.getSummary());
        found.setPublicationYear(bookDto.getPublicationYear());
        found.setCopiesAvailable(bookDto.getCopiesAvailable());
        found.setCopies(bookDto.getCopies());
        return CustomModelMapper.parseObject(repository.save(found), BookDto.class);
    }

    public void delete(Long id) {
        BookModel found = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Livro não encontrado!"));
        repository.delete(found);
    }

    public Page<BookDto> findAll(Pageable pageable) {
        var books = repository.findAll(pageable);
        return books.map(book -> CustomModelMapper.parseObject(book, BookDto.class));
    }

    public Page<BookDto> findByTitle(String title, Pageable pageable) {
        var books = repository.findByTitleStartingWithIgnoreCaseOrderByTitle(title, pageable);
        return books.map(book -> CustomModelMapper.parseObject(book, BookDto.class));
    }

    public Page<BookDto> findByAuthor(Long author, Pageable pageable) {
        var books = repository.findByAuthor_IdOrderByTitle(author, pageable);
        return books.map(book -> CustomModelMapper.parseObject(book, BookDto.class));
    }

    public Page<BookDto> findByGenre(Long genre, Pageable pageable) {
        var books = repository.findByGenre_IdOrderByTitle(genre, pageable);
        return books.map(book -> CustomModelMapper.parseObject(book, BookDto.class));
    }
}
