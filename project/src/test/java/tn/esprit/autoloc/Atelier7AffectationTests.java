package tn.esprit.autoloc;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.domain.enums.*;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.*;
import tn.esprit.autoloc.service.*;
import tn.esprit.autoloc.web.controller.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class Atelier7AffectationTests {

    @Autowired private IAgenceRepository agences;
    @Autowired private IVehiculeRepository vehiculeRepo;
    @Autowired private IEmployeRepository employeRepo;
    @Autowired private IVehiculeService vehicules;
    @Autowired private IEmployeService employes;
    @Autowired private EntityManager em;

    private Agence nouvelleAgence(String nom) {
        Agence a = new Agence();
        a.setNom(nom);
        a.setVille("Tunis");
        return agences.saveAndFlush(a);
    }

    private Vehicule nouveauVehicule() {
        Vehicule v = new Vehicule();
        v.setImmatriculation("TEST-A7-001");
        v.setMarque("Toyota");
        v.setModele("Yaris");
        v.setCategorie(CategorieVehicule.CITADINE);
        v.setStatut(StatutVehicule.DISPONIBLE);
        v.setTarifJournalier(new BigDecimal("100"));
        return vehiculeRepo.saveAndFlush(v);
    }

    private Employe nouvelEmploye() {
        Employe e = new Employe();
        e.setNom("EmployeTest");
        e.setPrenom("Atelier7");
        e.setRole(RoleEmploye.AGENT);
        return employeRepo.saveAndFlush(e);
    }

    private void verifierVehicule(Long id, Long agenceId) {
        em.flush();
        em.clear();

        Vehicule v = vehiculeRepo.findById(id).orElseThrow();

        if (agenceId == null) {
            assertNull(v.getAgence());
        } else {
            assertEquals(agenceId, v.getAgence().getIdAgence());
        }
    }

    private void verifierEmploye(Long id, Long agenceId) {
        em.flush();
        em.clear();

        Employe e = employeRepo.findById(id).orElseThrow();

        if (agenceId == null) {
            assertNull(e.getAgence());
        } else {
            assertEquals(agenceId, e.getAgence().getIdAgence());
        }
    }

    @Test
    void affecterTransfererDesaffecterVehicule() {
        Agence a = nouvelleAgence("Agence A");
        Agence b = nouvelleAgence("Agence B");
        Long id = nouveauVehicule().getIdVehicule();

        vehicules.affecterVehiculeAAgence(id, a.getIdAgence());
        verifierVehicule(id, a.getIdAgence());

        vehicules.affecterVehiculeAAgence(id, b.getIdAgence());
        verifierVehicule(id, b.getIdAgence());

        vehicules.desaffecterVehiculeDeAgence(id);
        verifierVehicule(id, null);
    }

    @Test
    void affecterTransfererDesaffecterEmploye() {
        Agence a = nouvelleAgence("Agence A");
        Agence b = nouvelleAgence("Agence B");
        Long id = nouvelEmploye().getIdEmploye();

        employes.affecterEmployeAAgence(id, a.getIdAgence());
        verifierEmploye(id, a.getIdAgence());

        employes.affecterEmployeAAgence(id, b.getIdAgence());
        verifierEmploye(id, b.getIdAgence());

        employes.desaffecterEmployeDeAgence(id);
        verifierEmploye(id, null);
    }

    @Test
    void refuserIdentifiantsInexistants() {
        Long agenceId = nouvelleAgence("Agence Test").getIdAgence();
        Long vehiculeId = nouveauVehicule().getIdVehicule();
        Long employeId = nouvelEmploye().getIdEmploye();

        assertThrows(ResourceNotFoundException.class,
            () -> vehicules.affecterVehiculeAAgence(-1L, agenceId));

        assertThrows(ResourceNotFoundException.class,
            () -> vehicules.affecterVehiculeAAgence(vehiculeId, -1L));

        assertThrows(ResourceNotFoundException.class,
            () -> employes.affecterEmployeAAgence(-1L, agenceId));

        assertThrows(ResourceNotFoundException.class,
            () -> employes.affecterEmployeAAgence(employeId, -1L));

        assertNull(vehiculeRepo.findById(vehiculeId)
            .orElseThrow().getAgence());

        assertNull(employeRepo.findById(employeId)
            .orElseThrow().getAgence());
    }

    @Test
    void desaffectationEstIdempotente() {
        Long vehiculeId = nouveauVehicule().getIdVehicule();
        Long employeId = nouvelEmploye().getIdEmploye();

        assertDoesNotThrow(
            () -> vehicules.desaffecterVehiculeDeAgence(vehiculeId));

        assertDoesNotThrow(
            () -> employes.desaffecterEmployeDeAgence(employeId));

        assertThrows(ResourceNotFoundException.class,
            () -> vehicules.desaffecterVehiculeDeAgence(-1L));

        assertThrows(ResourceNotFoundException.class,
            () -> employes.desaffecterEmployeDeAgence(-1L));
    }

    @Test
    void endpointsRestPut() throws Exception {
        Agence agence = nouvelleAgence("Agence REST");
        Long agenceId = agence.getIdAgence();
        Long vehiculeId = nouveauVehicule().getIdVehicule();
        Long employeId = nouvelEmploye().getIdEmploye();

        MockMvc mvc = MockMvcBuilders.standaloneSetup(
                new VehiculeController(vehicules),
                new EmployeController(employes))
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

        mvc.perform(put("/vehicules/{id}/agence/{agenceId}",
                vehiculeId, agenceId))
            .andExpect(status().isNoContent());

        mvc.perform(put("/employes/{id}/agence/{agenceId}",
                employeId, agenceId))
            .andExpect(status().isNoContent());

        verifierVehicule(vehiculeId, agenceId);
        verifierEmploye(employeId, agenceId);

        mvc.perform(put("/vehicules/{id}/agence", vehiculeId))
            .andExpect(status().isNoContent());

        mvc.perform(put("/employes/{id}/agence", employeId))
            .andExpect(status().isNoContent());

        verifierVehicule(vehiculeId, null);
        verifierEmploye(employeId, null);

        mvc.perform(put("/vehicules/{id}/agence/{agenceId}",
                -1L, agenceId))
            .andExpect(status().isNotFound());
    }
}
