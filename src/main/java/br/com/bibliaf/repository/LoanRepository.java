package br.com.bibliaf.repository;

import br.com.bibliaf.model.LoanModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<LoanModel, Long> {

    Page<LoanModel> findByUserId(Long userId, Pageable pageable);

    Page<LoanModel> findByBookId(Long bookId, Pageable pageable);
}
