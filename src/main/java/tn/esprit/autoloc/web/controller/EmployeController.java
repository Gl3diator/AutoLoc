package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.service.IEmployeService;

import java.util.List;

@RestController
@RequestMapping("/employes")
@RequiredArgsConstructor
@Tag(name = "Employés")
public class EmployeController {

    private final IEmployeService service;

    @GetMapping
    @Operation(summary = "Lister Employés")
    @ApiResponse(responseCode = "200", description = "Liste retournée")
    public List<Employe> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter Employe")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ressource trouvée"),
        @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    })
    public Employe findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer Employe")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Ressource créée"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public Employe create(@RequestBody Employe entity) {
        return service.create(entity);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier Employe")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ressource modifiée"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    })
    public Employe update(
            @PathVariable Long id,
            @RequestBody Employe entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer Employe")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Ressource supprimée"),
        @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    })
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }
}
