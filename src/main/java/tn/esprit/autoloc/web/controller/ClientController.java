package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.service.IClientService;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Clients")
public class ClientController {

    private final IClientService service;

    @GetMapping
    @Operation(summary = "Lister Clients")
    @ApiResponse(responseCode = "200", description = "Liste retournée")
    public List<Client> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter Client")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ressource trouvée"),
        @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    })
    public Client findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer Client")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Ressource créée"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public Client create(@RequestBody Client entity) {
        return service.create(entity);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier Client")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ressource modifiée"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    })
    public Client update(
            @PathVariable Long id,
            @RequestBody Client entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer Client")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Ressource supprimée"),
        @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    })
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }
}
