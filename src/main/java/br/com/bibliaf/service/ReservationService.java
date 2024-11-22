package br.com.bibliaf.service;

import br.com.bibliaf.dto.LoanDto;
import br.com.bibliaf.dto.ReservationDto;
import br.com.bibliaf.exception.ResourceNotFoundException;
import br.com.bibliaf.mapper.CustomModelMapper;
import br.com.bibliaf.model.BookModel;
import br.com.bibliaf.model.ReservationModel;
import br.com.bibliaf.model.UserModel;
import br.com.bibliaf.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository repository;

    public ReservationDto create(ReservationDto reservationDto) {
        ReservationModel reservationModel = CustomModelMapper.parseObject(reservationDto, ReservationModel.class);
        return CustomModelMapper.parseObject(repository.save(reservationModel), ReservationDto.class);
    }

    public ReservationDto findById(Long id) {
        ReservationModel found = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Reserva não encontrada!"));
        return CustomModelMapper.parseObject(found, ReservationDto.class);
    }

    public ReservationDto update(ReservationDto reservationDto) {
        ReservationModel found = repository.findById(reservationDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Reserva não encontrada!"));
        found.setBook(CustomModelMapper.parseObject(reservationDto.getBook(), BookModel.class));
        found.setUser(CustomModelMapper.parseObject(reservationDto.getUser(), UserModel.class));
        found.setReservationDate(reservationDto.getReservationDate());
        found.setStatus(reservationDto.getStatus());
        return CustomModelMapper.parseObject(repository.save(found), ReservationDto.class);
    }

    public void delete(Long id) {
        ReservationModel found = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Reserva não encontrada!"));
        repository.delete(found);
    }

    public List<ReservationDto> findAll() {
        var reservations = repository.findAll();
        return CustomModelMapper.parseObjectList(reservations, ReservationDto.class);
    }

    public Page<ReservationDto> findByUser(Long userId, Pageable pageable) {
        var reservations = repository.findByUserIdAndStatus(userId, ReservationModel.ReservationStatus.ACTIVE, pageable);
        return reservations.map(reservation -> CustomModelMapper.parseObject(reservation, ReservationDto.class));
    }

    public Page<ReservationDto> findByBook(Long bookId, Pageable pageable) {
        var reservations = repository.findByBookIdAndStatus(bookId, ReservationModel.ReservationStatus.ACTIVE, pageable);
        return reservations.map(reservation -> CustomModelMapper.parseObject(reservation, ReservationDto.class));
    }

    public ReservationDto findByBookAndUser(Long bookId, Long userId) {
        var reservations = repository.findByBookIdAndUserId(bookId, userId);
        return CustomModelMapper.parseObject(reservations, ReservationDto.class);
    }
}
