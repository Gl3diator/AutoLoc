package tn.esprit.autoloc.service.factory;

import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.service.strategy.ITarificationStrategy;

@Component
public class TarificationStrategyFactory {

    private final Map<String, ITarificationStrategy> strategies;

    public TarificationStrategyFactory(
            List<ITarificationStrategy> strategyList) {

        this.strategies = strategyList.stream()
                .collect(Collectors.toUnmodifiableMap(
                        ITarificationStrategy::getCode,
                        Function.identity()
                ));
    }

    public ITarificationStrategy resoudre(
            Reservation reservation) {

        if (reservation == null
                || reservation.getDateDebut() == null
                || reservation.getDateFin() == null) {
            throw new IllegalArgumentException(
                    "Dates de réservation manquantes");
        }

        long jours = ChronoUnit.DAYS.between(
                reservation.getDateDebut(),
                reservation.getDateFin()
        );

        if (jours < 0) {
            throw new IllegalArgumentException(
                    "Dates de réservation invalides");
        }

        String code;

        if (jours >= 7) {
            code = "LONGUE_DUREE";
        } else if (estWeekend(reservation)) {
            code = "WEEKEND";
        } else {
            code = "STANDARD";
        }

        ITarificationStrategy strategie = strategies.get(code);

        if (strategie == null) {
            throw new IllegalStateException(
                    "Stratégie introuvable : " + code);
        }

        return strategie;
    }

    private boolean estWeekend(Reservation reservation) {

        DayOfWeek jour = reservation
                .getDateDebut()
                .getDayOfWeek();

        return jour == DayOfWeek.SATURDAY
                || jour == DayOfWeek.SUNDAY;
    }
}
