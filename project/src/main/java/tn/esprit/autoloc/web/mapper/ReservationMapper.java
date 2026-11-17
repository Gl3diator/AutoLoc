package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;


@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "id", source = "idReservation")
    @Mapping(target = "clientId", source = "client.idClient")
    @Mapping(target = "vehiculeId", source = "vehicule.idVehicule")
    @Mapping(target = "vehiculeImmatriculation",
             source = "vehicule.immatriculation")
    @Mapping(target = "clientNomComplet",
             expression = "java(clientNomComplet(reservation))")
    @Mapping(target = "tarifEstime", ignore = true)
    ReservationResponseDTO toDto(Reservation reservation);

    default String clientNomComplet(Reservation reservation) {
        if (reservation == null || reservation.getClient() == null) {
            return null;
        }
        Client client = reservation.getClient();
        return client.getPrenom() + " " + client.getNom();
    }

}
