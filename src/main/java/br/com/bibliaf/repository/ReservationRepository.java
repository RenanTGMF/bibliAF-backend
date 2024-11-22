package br.com.bibliaf.repository;

import br.com.bibliaf.model.ReservationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationModel, Long> {

    Page<ReservationModel> findByUserIdAndStatus(Long userId, ReservationModel.ReservationStatus status, Pageable pageable);

    Page<ReservationModel> findByBookIdAndStatus(Long bookId, ReservationModel.ReservationStatus status, Pageable pageable);

    ReservationModel findByBookIdAndUserId(Long bookId, Long userId);
}
