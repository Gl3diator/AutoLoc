package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.domain.enums.*;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ServiceCrudTests {

    private final IAgenceService agences;
    private final IClientService clients;
    private final IVehiculeService vehicules;
    private final IEmployeService employes;
    private final IEquipementService equipements;
    private final IContratService contrats;
    private final IPaiementService paiements;

    @Autowired
    ServiceCrudTests(
            IAgenceService agences,
            IClientService clients,
            IVehiculeService vehicules,
            IEmployeService employes,
            IEquipementService equipements,
            IContratService contrats,
            IPaiementService paiements) {
        this.agences = agences;
        this.clients = clients;
        this.vehicules = vehicules;
        this.employes = employes;
        this.equipements = equipements;
        this.contrats = contrats;
        this.paiements = paiements;
    }

    private Agence createAgence() {
        Agence a = new Agence();
        a.setNom("Agence TP4");
        a.setVille("Tunis");
        a.setAdresse("Centre-ville");
        a.setTelephone("00000000");
        return agences.create(a);
    }

    @Test
    void testAgenceService() {
        long initial = agences.count();

        Agence a = createAgence();
        Long id = a.getIdAgence();

        assertNotNull(id);
        assertEquals(initial + 1, agences.count());
        assertTrue(agences.existsById(id));
        assertTrue(agences.findAll().stream()
                .anyMatch(x -> id.equals(x.getIdAgence())));
        assertEquals("Tunis", agences.findById(id).getVille());

        Agence update = new Agence();
        update.setNom("Agence Modifiee");
        update.setVille("Sfax");
        agences.update(id, update);

        assertEquals("Sfax", agences.findById(id).getVille());

        agences.deleteById(id);

        assertFalse(agences.existsById(id));
        assertThrows(ResourceNotFoundException.class,
                () -> agences.findById(id));

        Agence invalid = new Agence();
        invalid.setNom(" ");
        assertThrows(IllegalArgumentException.class,
                () -> agences.create(invalid));

        System.out.println("PASS: AgenceService CRUD");
    }

    @Test
    void testClientService() {
        long initial = clients.count();

        Client a = new Client();
        a.setNom("ClientA");
        a.setPrenom("Alice");
        a.setEmail("alice@example.invalid");
        a.setDateInscription(LocalDate.now());

        Client b = new Client();
        b.setNom("ClientB");
        b.setPrenom("Bob");
        b.setEmail("bob@example.invalid");

        a = clients.create(a);
        b = clients.create(b);

        Long idA = a.getIdClient();
        Long idB = b.getIdClient();

        assertEquals(initial + 2, clients.count());
        assertEquals("ClientA", clients.findById(idA).getNom());
        assertTrue(clients.findAll().stream()
                .anyMatch(x -> idB.equals(x.getIdClient())));

        Client update = new Client();
        update.setNom("ClientUpdated");
        update.setEmail("updated@example.invalid");
        clients.update(idA, update);

        assertEquals("ClientUpdated", clients.findById(idA).getNom());

        clients.deleteById(idA);
        clients.deleteById(idB);

        assertFalse(clients.existsById(idA));
        assertFalse(clients.existsById(idB));

        System.out.println("PASS: ClientService CRUD");
    }

    @Test
    void testVehiculeService() {
        Agence agence = createAgence();

        Vehicule v = new Vehicule();
        v.setImmatriculation("TEST-SERVICE-001");
        v.setMarque("Toyota");
        v.setModele("Yaris");
        v.setCategorie(CategorieVehicule.CITADINE);
        v.setStatut(StatutVehicule.DISPONIBLE);
        v.setTarifJournalier(new BigDecimal("100"));
        v.setAgence(agence);

        v = vehicules.create(v);
        Long id = v.getIdVehicule();

        assertNotNull(id);
        assertTrue(vehicules.existsById(id));
        assertTrue(vehicules.findAll().stream()
                .anyMatch(x -> id.equals(x.getIdVehicule())));

        Vehicule update = new Vehicule();
        update.setMarque("Renault");
        update.setTarifJournalier(new BigDecimal("120"));
        vehicules.update(id, update);

        assertEquals("Renault", vehicules.findById(id).getMarque());
        assertEquals(agence.getIdAgence(),
                vehicules.findById(id).getAgence().getIdAgence());

        Vehicule invalid = new Vehicule();
        invalid.setTarifJournalier(new BigDecimal("-50"));

        assertThrows(IllegalArgumentException.class,
                () -> vehicules.create(invalid));

        vehicules.deleteById(id);
        assertFalse(vehicules.existsById(id));

        System.out.println("PASS: VehiculeService CRUD");
    }

    @Test
    void testEmployeService() {
        Agence agence = createAgence();

        Employe e = new Employe();
        e.setNom("Karim");
        e.setPrenom("Test");
        e.setRole(RoleEmploye.AGENT);
        e.setAgence(agence);

        e = employes.create(e);
        Long id = e.getIdEmploye();

        assertNotNull(id);
        assertTrue(employes.existsById(id));
        assertEquals("Karim", employes.findById(id).getNom());

        Employe update = new Employe();
        update.setNom("Ahmed");
        update.setRole(RoleEmploye.MANAGER);
        employes.update(id, update);

        assertEquals(RoleEmploye.MANAGER,
                employes.findById(id).getRole());

        assertEquals(agence.getIdAgence(),
                employes.findById(id).getAgence().getIdAgence());

        employes.deleteById(id);
        assertFalse(employes.existsById(id));

        System.out.println("PASS: EmployeService CRUD");
    }

    @Test
    void testEquipementService() {
        Equipement e = new Equipement();
        e.setLibelle("GPS");

        e = equipements.create(e);
        Long id = e.getIdEquipement();

        assertTrue(equipements.existsById(id));
        assertEquals("GPS", equipements.findById(id).getLibelle());

        Equipement update = new Equipement();
        update.setLibelle("GPS Premium");
        equipements.update(id, update);

        assertEquals("GPS Premium",
                equipements.findById(id).getLibelle());

        Equipement invalid = new Equipement();
        invalid.setLibelle("");

        assertThrows(IllegalArgumentException.class,
                () -> equipements.create(invalid));

        equipements.deleteById(id);
        assertFalse(equipements.existsById(id));

        System.out.println("PASS: EquipementService CRUD");
    }

    @Test
    void testContratServiceCascade() {
        Contrat c = new Contrat();
        c.setDateSignature(LocalDate.now());
        c.setMontantTotal(new BigDecimal("480"));
        c.setValide(true);

        Paiement p = new Paiement();
        p.setMontant(new BigDecimal("150"));
        p.setDatePaiement(LocalDate.now());
        p.setModePaiement(ModePaiement.CARTE);

        c.getPaiements().add(p);

        c = contrats.create(c);
        Long contratId = c.getIdContrat();
        Long paiementId = p.getIdPaiement();

        assertNotNull(contratId);
        assertNotNull(paiementId);
        assertTrue(contrats.existsById(contratId));
        assertEquals(paiementId,
                paiements.findById(paiementId).getIdPaiement());

        assertTrue(paiements.findAll().stream()
                .anyMatch(x -> paiementId.equals(x.getIdPaiement())));

        Contrat update = new Contrat();
        update.setDateSignature(LocalDate.now());
        update.setMontantTotal(new BigDecimal("520"));
        update.setValide(true);

        contrats.update(contratId, update);

        assertEquals(0, contrats.findById(contratId)
                .getMontantTotal().compareTo(new BigDecimal("520")));

        assertEquals(1,
                contrats.findById(contratId).getPaiements().size());

        Contrat invalid = new Contrat();
        invalid.setMontantTotal(new BigDecimal("-100"));

        assertThrows(IllegalArgumentException.class,
                () -> contrats.create(invalid));

        contrats.deleteById(contratId);

        assertFalse(contrats.existsById(contratId));
        assertThrows(ResourceNotFoundException.class,
                () -> paiements.findById(paiementId));

        System.out.println("PASS: ContratService cascade");
    }
}
