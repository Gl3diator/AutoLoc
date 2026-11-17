package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.web.dto.*;
import tn.esprit.autoloc.web.mapper.*;

import static org.junit.jupiter.api.Assertions.*;

class DtoMapperTests {

    @Test
    void agenceMapping() {
        Agence agence = new Agence();
        agence.setIdAgence(10L);
        agence.setNom("AutoLoc Tunis");

        AgenceMapper mapper = Mappers.getMapper(AgenceMapper.class);
        AgenceDTO dto = mapper.toDto(agence);

        assertEquals(10L, dto.getId());
        assertEquals("AutoLoc Tunis", dto.getNom());
        assertEquals(10L, mapper.toEntity(dto).getIdAgence());
    }

    @Test
    void clientMapping() {
        Client client = new Client();
        client.setIdClient(20L);
        client.setNom("Ben Salah");

        ClientMapper mapper = Mappers.getMapper(ClientMapper.class);
        ClientDTO dto = mapper.toDto(client);

        assertEquals(20L, dto.getId());
        assertEquals(20L, mapper.toEntity(dto).getIdClient());
    }

    @Test
    void contratMapping() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation(30L);

        Contrat contrat = new Contrat();
        contrat.setIdContrat(40L);
        contrat.setReservation(reservation);

        ContratMapper mapper = Mappers.getMapper(ContratMapper.class);
        ContratDTO dto = mapper.toDto(contrat);

        assertEquals(40L, dto.getId());
        assertEquals(30L, dto.getReservationId());
        assertNull(mapper.toEntity(dto).getReservation());
    }

    @Test
    void paiementMapping() {
        Contrat contrat = new Contrat();
        contrat.setIdContrat(40L);

        Paiement paiement = new Paiement();
        paiement.setIdPaiement(50L);
        paiement.setContrat(contrat);

        PaiementMapper mapper = Mappers.getMapper(PaiementMapper.class);
        PaiementDTO dto = mapper.toDto(paiement);

        assertEquals(50L, dto.getId());
        assertEquals(40L, dto.getContratId());
        assertNull(mapper.toEntity(dto).getContrat());
    }

    @Test
    void vehiculeMapping() {
        Agence agence = new Agence();
        agence.setIdAgence(10L);

        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(60L);
        vehicule.setAgence(agence);

        VehiculeMapper mapper = Mappers.getMapper(VehiculeMapper.class);
        VehiculeDTO dto = mapper.toDto(vehicule);

        assertEquals(60L, dto.getId());
        assertEquals(10L, dto.getAgenceId());
        assertNull(mapper.toEntity(dto).getAgence());
    }

    @Test
    void reservationMapping() {
        Client client = new Client();
        client.setIdClient(20L);
        client.setPrenom("Ghaith");
        client.setNom("Ben Salah");

        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(60L);
        vehicule.setImmatriculation("123-TU-456");

        Reservation reservation = new Reservation();
        reservation.setIdReservation(70L);
        reservation.setClient(client);
        reservation.setVehicule(vehicule);

        ReservationMapper mapper =
                Mappers.getMapper(ReservationMapper.class);

        ReservationResponseDTO dto = mapper.toDto(reservation);

        assertEquals(70L, dto.getId());
        assertEquals(20L, dto.getClientId());
        assertEquals("Ghaith Ben Salah", dto.getClientNomComplet());
        assertEquals(60L, dto.getVehiculeId());
        assertEquals("123-TU-456", dto.getVehiculeImmatriculation());
        assertNull(dto.getTarifEstime());
    }
}
