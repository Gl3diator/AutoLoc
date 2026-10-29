package tn.esprit.autoloc.service.observer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import tn.esprit.autoloc.domain.Reservation;

@Component
public class ReservationSubject {

    private final List<IReservationObserver> observers;

    public ReservationSubject(List<IReservationObserver> observers) {
        this.observers = new ArrayList<>(observers);
    }

    public void notifierConfirmation(Reservation reservation) {
        observers.forEach(
            observer -> observer.onReservationConfirmee(reservation)
        );
    }

    public void notifierAnnulation(Reservation reservation) {
        observers.forEach(
            observer -> observer.onReservationAnnulee(reservation)
        );
    }
}
