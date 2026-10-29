package tn.esprit.autoloc.service.strategy;

import java.math.BigDecimal;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.Reservation;

public interface ITarificationStrategy {

    BigDecimal calculerTarif(
        Vehicule vehicule,
        Reservation reservation
    );

    String getCode();
}
