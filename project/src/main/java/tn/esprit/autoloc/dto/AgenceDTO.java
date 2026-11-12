package tn.esprit.autoloc.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgenceDTO {

    private Long id;
    private String nom;
    private String ville;
    private String adresse;
    private String telephone;
}
