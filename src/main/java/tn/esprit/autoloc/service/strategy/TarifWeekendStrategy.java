package tn.esprit.autoloc.service.strategy;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;

import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.Reservation;

@Component
public class TarifWeekendStrategy implements ITarificationStrategy {

    private static final BigDecimal REMISE_WEEKEND =
            new BigDecimal("0.90");

    private final TarifStandardStrategy tarifStandardStrategy;

    public TarifWeekendStrategy(
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

        return prixStandard.multiply(REMISE_WEEKEND);
    }

    @Override
    public String getCode() {
        return "WEEKEND";
    }
}
