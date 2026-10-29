package tn.esprit.autoloc.service.impl;

import java.math.BigDecimal;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.service.observer.ReservationSubject;
import tn.esprit.autoloc.service.factory.TarificationStrategyFactory;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository repository;
    private final ReservationSubject reservationSubject;
    private final TarificationStrategyFactory tarificationStrategyFactory;

    @Override
    @Transactional
    public Reservation create(Reservation entity) {
        if (entity.getIdReservation() != null) {
            throw new IllegalArgumentException(
                "Un nouvel objet ne doit pas avoir d'identifiant");
        }

        validate(entity);

        Reservation saved = repository.save(entity);

        if (saved.getStatut() == StatutReservation.CONFIRMEE) {
            reservationSubject.notifierConfirmation(saved);
        }

        return saved;
    }

    @Transactional(readOnly = true)
    public BigDecimal calculerTarif(Long id) {
        Reservation reservation = findById(id);

        if (reservation.getVehicule() == null) {
            throw new IllegalArgumentException(
                "Aucun véhicule associé à la réservation");
        }

        return tarificationStrategyFactory
                .resoudre(reservation)
                .calculerTarif(
                    reservation.getVehicule(),
                    reservation
                );
    }

    @Override
    public Reservation findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Reservation", id));
    }

    @Override
    public List<Reservation> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Reservation update(Long id, Reservation entity) {
        Reservation existing = findById(id);
        validate(entity);

        StatutReservation ancienStatut = existing.getStatut();

        existing.setDateDebut(entity.getDateDebut());
        existing.setDateFin(entity.getDateFin());
        existing.setStatut(entity.getStatut());

        Reservation saved = repository.save(existing);

        if (saved.getStatut() != ancienStatut) {
            if (saved.getStatut() == StatutReservation.CONFIRMEE) {
                reservationSubject.notifierConfirmation(saved);
            } else if (saved.getStatut() == StatutReservation.ANNULEE) {
                reservationSubject.notifierAnnulation(saved);
            }
        }

        return saved;
    }

    @Transactional
    public Reservation annulerReservation(Long id) {
        Reservation reservation = findById(id);

        if (reservation.getStatut() == StatutReservation.ANNULEE) {
            return reservation;
        }

        reservation.setStatut(StatutReservation.ANNULEE);

        Reservation saved = repository.save(reservation);

        log.info("Reservation #{} annulee",
                saved.getIdReservation());

        reservationSubject.notifierAnnulation(saved);

        return saved;
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", id);
        }
        repository.deleteById(id);
    }

    private void validate(Reservation e) {
        if (e.getDateDebut() != null && e.getDateFin() != null
                && e.getDateFin().isBefore(e.getDateDebut())) {
            throw new IllegalArgumentException(
                "Dates réservation invalides");
        }
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
