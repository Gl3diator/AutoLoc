package tn.esprit.autoloc.service.strategy;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;

import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.Reservation;

@Component
public class TarifLongueDureeStrategy implements ITarificationStrategy {

    private static final BigDecimal REMISE_LONGUE_DUREE =
            new BigDecimal("0.80");

    private final TarifStandardStrategy tarifStandardStrategy;

    public TarifLongueDureeStrategy(
            TarifStandardStrategy tarifStandardStrategy) {
        this.tarifStandardStrategy = tarifStandardStrategy;
    }

    @Override
    public BigDecimal calculerTarif(
            Vehicule vehicule,
            Reservation reservation) {

        BigDecimal prixStandard =
                tarifStandardStrategy.calculerTarif(
                    vehicule, reservation);

        return prixStandard.multiply(REMISE_LONGUE_DUREE);
    }

    @Override
    public String getCode() {
        return "LONGUE_DUREE";
    }
}
