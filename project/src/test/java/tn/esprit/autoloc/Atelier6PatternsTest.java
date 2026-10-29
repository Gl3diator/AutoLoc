package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.service.strategy.*;
import tn.esprit.autoloc.service.factory.*;
import tn.esprit.autoloc.service.observer.*;

import static org.junit.jupiter.api.Assertions.*;

class Atelier6PatternsTest {

    private final TarifStandardStrategy standard =
            new TarifStandardStrategy();

    private final TarifWeekendStrategy weekend =
            new TarifWeekendStrategy(standard);

    private final TarifLongueDureeStrategy longueDuree =
            new TarifLongueDureeStrategy(standard);

    private final TarificationStrategyFactory factory =
            new TarificationStrategyFactory(
                List.of(standard, weekend, longueDuree)
            );

    private Vehicule vehicule() {
        Vehicule v = new Vehicule();
        v.setTarifJournalier(new BigDecimal("150"));
        return v;
    }

    private Reservation reservation(LocalDate debut, int jours) {
        Reservation r = new Reservation();
        r.setDateDebut(debut);
        r.setDateFin(debut.plusDays(jours));
        return r;
    }

    @Test
    void testTarifStandard() {
        Reservation r = reservation(
                LocalDate.of(2026, 11, 2), 4);

        BigDecimal prix = standard.calculerTarif(vehicule(), r);

        assertEquals(0, prix.compareTo(new BigDecimal("600")));
    }

    @Test
    void testTarifWeekend() {
        Reservation r = reservation(
                LocalDate.of(2026, 10, 31), 4);

        BigDecimal prix = weekend.calculerTarif(vehicule(), r);

        assertEquals(0, prix.compareTo(new BigDecimal("540")));
    }

    @Test
    void testTarifLongueDuree() {
        Reservation r = reservation(
                LocalDate.of(2026, 11, 2), 10);

        BigDecimal prix = longueDuree.calculerTarif(vehicule(), r);

        assertEquals(0, prix.compareTo(new BigDecimal("1200")));
    }

    @Test
    void testSelectionFactory() {
        assertEquals("STANDARD", factory.resoudre(
            reservation(LocalDate.of(2026, 11, 2), 4)
        ).getCode());

        assertEquals("WEEKEND", factory.resoudre(
            reservation(LocalDate.of(2026, 10, 31), 2)
        ).getCode());

        assertEquals("LONGUE_DUREE", factory.resoudre(
            reservation(LocalDate.of(2026, 11, 2), 10)
        ).getCode());
    }

    @Test
    void testDatesInvalides() {
        Reservation r = reservation(
                LocalDate.of(2026, 11, 2), -2);

        assertThrows(IllegalArgumentException.class,
                () -> factory.resoudre(r));

        assertThrows(IllegalArgumentException.class,
                () -> standard.calculerTarif(vehicule(), r));
    }

    @Test
    void testNotificationFactory() {
        EmailNotificationObserver email =
                new EmailNotificationObserver();

        SmsNotificationObserver sms =
                new SmsNotificationObserver();

        NotificationFactory notificationFactory =
                new NotificationFactory(email, sms);

        assertSame(email,
                notificationFactory.creerObservateur("EMAIL"));

        assertSame(sms,
                notificationFactory.creerObservateur("SMS"));

        assertThrows(IllegalArgumentException.class,
                () -> notificationFactory.creerObservateur("FAX"));
    }
}
