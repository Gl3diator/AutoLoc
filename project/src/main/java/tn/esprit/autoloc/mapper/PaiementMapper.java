package tn.esprit.autoloc.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.dto.PaiementDTO;


@Mapper(componentModel = "spring")
public interface PaiementMapper {

    @Mapping(target = "id", source = "idPaiement")
    @Mapping(target = "contratId", source = "contrat.idContrat")
    PaiementDTO toDto(Paiement entity);

    @Mapping(target = "idPaiement", source = "id")
    @Mapping(target = "contrat", ignore = true)
    Paiement toEntity(PaiementDTO dto);

}
