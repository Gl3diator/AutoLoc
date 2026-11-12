package tn.esprit.autoloc.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.dto.ContratDTO;


@Mapper(componentModel = "spring")
public interface ContratMapper {

    @Mapping(target = "id", source = "idContrat")
    @Mapping(target = "reservationId", source = "reservation.idReservation")
    ContratDTO toDto(Contrat entity);

    @Mapping(target = "idContrat", source = "id")
    @Mapping(target = "reservation", ignore = true)
    @Mapping(target = "paiements", ignore = true)
    Contrat toEntity(ContratDTO dto);

}
