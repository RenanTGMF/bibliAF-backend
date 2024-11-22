package br.com.bibliaf.service;

import br.com.bibliaf.dto.LoanDto;
import br.com.bibliaf.exception.ResourceNotFoundException;
import br.com.bibliaf.mapper.CustomModelMapper;
import br.com.bibliaf.model.BookModel;
import br.com.bibliaf.model.LoanModel;
import br.com.bibliaf.model.ReservationModel;
import br.com.bibliaf.model.UserModel;
import br.com.bibliaf.repository.BookRepository;
import br.com.bibliaf.repository.LoanRepository;
import br.com.bibliaf.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository repository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public LoanDto create(LoanDto loanDto) {
        BookModel book = bookRepository.findById(loanDto.getBook().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado!"));

        if (book.getCopiesAvailable() <= 0) {
            throw new IllegalStateException("Não há cópias disponíveis para empréstimo.");
        }

        book.setCopiesAvailable(book.getCopiesAvailable() - 1);
        bookRepository.save(book);

        LoanModel loanModel = CustomModelMapper.parseObject(loanDto, LoanModel.class);
        loanModel.setBook(book);

        ReservationModel reservation = reservationRepository.findByBookIdAndUserId(loanDto.getBook().getId(), loanDto.getUser().getId());
        if (reservation != null) {
            reservationRepository.delete(reservation);
        }


        return CustomModelMapper.parseObject(repository.save(loanModel), LoanDto.class);
    }


    public LoanDto findById(Long id) {
        LoanModel found = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Empréstimo não encontrado!"));
        return CustomModelMapper.parseObject(found, LoanDto.class);
    }

    public LoanDto update(LoanDto loanDto) {
        LoanModel found = repository.findById(loanDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Empréstimo não encontrado!"));

        if (LoanModel.LoanStatus.RETURNED.equals(loanDto.getStatus()) && !LoanModel.LoanStatus.RETURNED.equals(found.getStatus())) {
            BookModel book = bookRepository.findById(loanDto.getBook().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado!"));

            book.setCopiesAvailable(book.getCopiesAvailable() + 1);

            BookModel updatedBook = bookRepository.save(book);
            if (updatedBook == null) {
                throw new IllegalStateException("Erro ao salvar o livro atualizado.");
            }
        } else if(LoanModel.LoanStatus.RETURNED.equals(found.getStatus()) && !LoanModel.LoanStatus.RETURNED.equals(loanDto.getStatus())) {
            BookModel book = bookRepository.findById(loanDto.getBook().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado!"));

            if (book.getCopiesAvailable() == 0) {
                throw new IllegalStateException("Não há cópias disponíveis para empréstimo.");
            }

            book.setCopiesAvailable(book.getCopiesAvailable() - 1);
            BookModel updatedBook = bookRepository.save(book);
            if (updatedBook == null) {
                throw new IllegalStateException("Erro ao salvar o livro atualizado.");
            }
        }


        found.setBook(CustomModelMapper.parseObject(loanDto.getBook(), BookModel.class));
        found.setUser(CustomModelMapper.parseObject(loanDto.getUser(), UserModel.class));
        found.setLoanDate(loanDto.getLoanDate());
        found.setReturnDate(loanDto.getReturnDate());
        found.setStatus(loanDto.getStatus());

        return CustomModelMapper.parseObject(repository.save(found), LoanDto.class);
    }

    public void delete(Long id) {
        LoanModel found = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Empréstimo não encontrado!"));

        if(LoanModel.LoanStatus.PENDING == found.getStatus() || LoanModel.LoanStatus.OVERDUE == found.getStatus()) {
            BookModel book = bookRepository.findById(found.getBook().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado!"));

            book.setCopiesAvailable(book.getCopiesAvailable() + 1);
            bookRepository.save(book);
        }

        repository.delete(found);
    }

    public List<LoanDto> findAll() {
        var loans = repository.findAll();
        return CustomModelMapper.parseObjectList(loans, LoanDto.class);
    }

    public Page<LoanDto> findByUser(Long userId, Pageable pageable) {
        var loans = repository.findByUserId(userId, pageable);
        return loans.map(loan -> CustomModelMapper.parseObject(loan, LoanDto.class));
    }

    public Page<LoanDto> findByBook(Long bookId, Pageable pageable) {
        var loans = repository.findByBookId(bookId, pageable);
        return loans.map(loan -> CustomModelMapper.parseObject(loan, LoanDto.class));
    }
}
