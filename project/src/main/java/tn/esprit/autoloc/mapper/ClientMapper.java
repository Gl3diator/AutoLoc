package tn.esprit.autoloc.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.dto.ClientDTO;


@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "id", source = "idClient")
    ClientDTO toDto(Client entity);

    @Mapping(target = "idClient", source = "id")
    @Mapping(target = "reservations", ignore = true)
    Client toEntity(ClientDTO dto);

}
