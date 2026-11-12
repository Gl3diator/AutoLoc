package tn.esprit.autoloc.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequestDTO {

    private Long clientId;
    private Long vehiculeId;
    private LocalDate dateDebut;
    private LocalDate dateFin;
}
