package tn.esprit.autoloc.web.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContratDTO {

    private Long id;
    private LocalDate dateSignature;
    private BigDecimal montantTotal;
    private boolean valide;
    private Long reservationId;
}
