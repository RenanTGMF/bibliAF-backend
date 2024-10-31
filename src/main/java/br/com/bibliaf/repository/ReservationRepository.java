package br.com.bibliaf.repository;

import br.com.bibliaf.model.ReservationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationModel, Long> {

    Page<ReservationModel> findByUserId(Long userId, Pageable pageable);

    Page<ReservationModel> findByBookId(Long bookId, Pageable pageable);
}
