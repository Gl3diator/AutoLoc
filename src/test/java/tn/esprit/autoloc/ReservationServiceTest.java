package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.service.IReservationService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ExtendWith(OutputCaptureExtension.class)
class ReservationServiceTest {

    @Autowired
    private IReservationService reservationService;

    @Autowired
    private IClientRepository clientRepository;

    private Client creerClient() {
        Client client = new Client();
        client.setNom("TestObserver");
        client.setEmail("observer@example.com");
        client.setTelephone("12345678");

        return clientRepository.save(client);
    }

    private Reservation creerReservation(
            Client client,
            StatutReservation statut) {

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setDateDebut(LocalDate.now());
        reservation.setDateFin(LocalDate.now().plusDays(3));
        reservation.setStatut(statut);

        return reservationService.create(reservation);
    }

    @Test
    void testConfirmation(CapturedOutput output) {

        Client client = creerClient();

        Reservation reservation = creerReservation(
                client, StatutReservation.CONFIRMEE);

        assertNotNull(reservation.getIdReservation());

        String logs = output.getAll();

        assertTrue(logs.contains(
                "[EMAIL] Confirmation envoyee au client observer@example.com"));

        assertTrue(logs.contains(
                "[SMS] Confirmation envoyee au 12345678"));
    }

    @Test
    void testAnnulation(CapturedOutput output) {

        Client client = creerClient();

        Reservation reservation = creerReservation(
                client, StatutReservation.EN_ATTENTE);

        Long id = reservation.getIdReservation();

        Reservation annulee =
                reservationService.annulerReservation(id);

        assertEquals(
                StatutReservation.ANNULEE,
                annulee.getStatut());

        String logs = output.getAll();

        assertTrue(logs.contains(
                "[EMAIL] Annulation notifiee au client observer@example.com"));

        assertTrue(logs.contains(
                "[SMS] Annulation notifiee au 12345678"));

        // Annuler une deuxième fois ne doit pas notifier à nouveau.
        reservationService.annulerReservation(id);

        String tousLesLogs = output.getAll();

        assertEquals(1, compterOccurrences(
                tousLesLogs,
                "[EMAIL] Annulation notifiee au client observer@example.com"));

        assertEquals(1, compterOccurrences(
                tousLesLogs,
                "[SMS] Annulation notifiee au 12345678"));
    }

    private int compterOccurrences(String texte, String motif) {
        return (texte.length()
                - texte.replace(motif, "").length())
                / motif.length();
    }
}
