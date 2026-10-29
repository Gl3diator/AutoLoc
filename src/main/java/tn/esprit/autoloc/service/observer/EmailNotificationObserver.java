package tn.esprit.autoloc.service.observer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import tn.esprit.autoloc.domain.Reservation;

@Slf4j
@Component
public class EmailNotificationObserver implements IReservationObserver {

    @Override
    public void onReservationConfirmee(Reservation reservation) {
        log.info(
            "[EMAIL] Confirmation envoyee au client {} pour la reservation #{}",
            reservation.getClient().getEmail(),
            reservation.getIdReservation()
        );
    }

    @Override
    public void onReservationAnnulee(Reservation reservation) {
        log.info(
            "[EMAIL] Annulation notifiee au client {} pour la reservation #{}",
            reservation.getClient().getEmail(),
            reservation.getIdReservation()
        );
    }
}
