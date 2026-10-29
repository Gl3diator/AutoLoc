package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.service.IReservationService;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(name = "Réservations")
public class ReservationController {

    private final IReservationService service;

    @GetMapping
    @Operation(summary = "Lister Réservations")
    @ApiResponse(responseCode = "200", description = "Liste retournée")
    public List<Reservation> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter Reservation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ressource trouvée"),
        @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    })
    public Reservation findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer Reservation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Ressource créée"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public Reservation create(@RequestBody Reservation entity) {
        return service.create(entity);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier Reservation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ressource modifiée"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    })
    public Reservation update(
            @PathVariable Long id,
            @RequestBody Reservation entity) {
        return service.update(id, entity);
    }

    @GetMapping("/{id}/tarif")
    @Operation(summary = "Calculer le tarif d'une réservation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tarif calculé"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    public java.math.BigDecimal calculerTarif(@PathVariable Long id) {
        return service.calculerTarif(id);
    }

    @PatchMapping("/{id}/annuler")
    @Operation(summary = "Annuler une réservation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Réservation annulée"),
        @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    public Reservation annulerReservation(@PathVariable Long id) {
        return service.annulerReservation(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer Reservation")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Ressource supprimée"),
        @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    })
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }
}
