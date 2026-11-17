package tn.esprit.autoloc.web.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import tn.esprit.autoloc.domain.enums.ModePaiement;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaiementDTO {

    private Long id;
    private BigDecimal montant;
    private LocalDate datePaiement;
    private ModePaiement modePaiement;
    private Long contratId;
}
