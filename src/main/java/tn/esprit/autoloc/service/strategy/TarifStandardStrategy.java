package tn.esprit.autoloc.service.strategy;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.Reservation;

@Component
public class TarifStandardStrategy implements ITarificationStrategy {

    @Override
    public BigDecimal calculerTarif(
            Vehicule vehicule,
            Reservation reservation) {

        if (vehicule == null
                || vehicule.getTarifJournalier() == null
                || reservation == null
                || reservation.getDateDebut() == null
                || reservation.getDateFin() == null) {
            throw new IllegalArgumentException(
                "Données de tarification manquantes");
        }

        long jours = ChronoUnit.DAYS.between(
            reservation.getDateDebut(),
            reservation.getDateFin()
        );

        if (jours < 0) {
            throw new IllegalArgumentException(
                "Dates de réservation invalides");
        }

        // Une location le même jour est facturée 1 jour.
        jours = Math.max(1, jours);

        return vehicule.getTarifJournalier()
                .multiply(BigDecimal.valueOf(jours));
    }

    @Override
    public String getCode() {
        return "STANDARD";
    }
}
