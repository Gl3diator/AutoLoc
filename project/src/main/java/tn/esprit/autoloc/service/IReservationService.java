package tn.esprit.autoloc.service;

import java.math.BigDecimal;
import java.util.List;
import tn.esprit.autoloc.domain.Reservation;

public interface IReservationService {

    Reservation create(Reservation entity);

    Reservation findById(Long id);

    List<Reservation> findAll();

    Reservation update(Long id, Reservation entity);

    Reservation annulerReservation(Long id);

    BigDecimal calculerTarif(Long id);

    void deleteById(Long id);

    long count();

    boolean existsById(Long id);
}
