package br.com.bibliaf.service;

import br.com.bibliaf.dto.LoanDto;
import br.com.bibliaf.exception.ResourceNotFoundException;
import br.com.bibliaf.mapper.CustomModelMapper;
import br.com.bibliaf.model.BookModel;
import br.com.bibliaf.model.LoanModel;
import br.com.bibliaf.model.UserModel;
import br.com.bibliaf.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

    @Autowired
    private LoanRepository repository;

    public LoanDto create(LoanDto loanDto) {
        LoanModel loanModel = CustomModelMapper.parseObject(loanDto, LoanModel.class);
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
        repository.delete(found);
    }

    public Page<LoanDto> findAll(Pageable pageable) {
        var loans = repository.findAll(pageable);
        return loans.map(loan -> CustomModelMapper.parseObject(loan, LoanDto.class));
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
