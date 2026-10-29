package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.domain.enums.*;
import tn.esprit.autoloc.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RepositoryCrudTests {

    private final IAgenceRepository agences;
    private final IClientRepository clients;
    private final IVehiculeRepository vehicules;
    private final IEmployeRepository employes;
    private final IEquipementRepository equipements;
    private final IContratRepository contrats;
    private final IPaiementRepository paiements;

    @Autowired
    RepositoryCrudTests(
            IAgenceRepository agences,
            IClientRepository clients,
            IVehiculeRepository vehicules,
            IEmployeRepository employes,
            IEquipementRepository equipements,
            IContratRepository contrats,
            IPaiementRepository paiements) {

        this.agences = agences;
        this.clients = clients;
        this.vehicules = vehicules;
        this.employes = employes;
        this.equipements = equipements;
        this.contrats = contrats;
        this.paiements = paiements;
    }

    private Agence nouvelleAgence() {
        Agence a = new Agence();
        a.setNom("Agence Test JPA");
        a.setVille("Tunis");
        a.setAdresse("Centre Ville");
        a.setTelephone("00000000");
        return agences.saveAndFlush(a);
    }

    @Test
    void testAgenceRepository() {
        long avant = agences.count();

        Agence a = nouvelleAgence();
        Long id = a.getIdAgence();

        assertNotNull(id);
        assertEquals(avant + 1, agences.count());
        assertTrue(agences.existsById(id));
        assertTrue(agences.findById(id).isPresent());
        assertTrue(agences.findAll().stream()
                .anyMatch(x -> id.equals(x.getIdAgence())));

        agences.deleteById(id);
        agences.flush();

        assertFalse(agences.existsById(id));
        System.out.println("PASS: AgenceRepository CRUD");
    }

    @Test
    void testClientRepository() {
        long avant = clients.count();

        Client a = new Client();
        a.setNom("TestClientA");
        a.setPrenom("Alice");
        a.setEmail("alice@example.invalid");
        a.setDateInscription(LocalDate.now());

        Client b = new Client();
        b.setNom("TestClientB");
        b.setPrenom("Bob");
        b.setEmail("bob@example.invalid");
        b.setDateInscription(LocalDate.now());

        a = clients.saveAndFlush(a);
        b = clients.saveAndFlush(b);

        Long idA = a.getIdClient();
        Long idB = b.getIdClient();

        assertNotNull(idA);
        assertNotNull(idB);
        assertEquals(avant + 2, clients.count());
        assertTrue(clients.findById(idA).isPresent());
        assertTrue(clients.findAll().stream()
                .anyMatch(x -> idB.equals(x.getIdClient())));
        assertTrue(clients.existsById(idA));
        assertFalse(clients.existsById(-1L));

        clients.deleteById(idA);
        clients.deleteById(idB);
        clients.flush();

        assertFalse(clients.existsById(idA));
        assertFalse(clients.existsById(idB));
        System.out.println("PASS: ClientRepository CRUD");
    }

    @Test
    void testVehiculeRepository() {
        long avant = vehicules.count();
        Agence agence = nouvelleAgence();

        Vehicule v = new Vehicule();
        v.setImmatriculation("TEST-JPA-001");
        v.setMarque("Toyota");
        v.setModele("Yaris");
        v.setCategorie(CategorieVehicule.CITADINE);
        v.setStatut(StatutVehicule.DISPONIBLE);
        v.setTarifJournalier(new BigDecimal("100.00"));
        v.setAgence(agence);

        v = vehicules.saveAndFlush(v);
        Long id = v.getIdVehicule();

        assertNotNull(id);
        assertEquals(avant + 1, vehicules.count());
        assertTrue(vehicules.findById(id).isPresent());
        assertTrue(vehicules.findAll().stream()
                .anyMatch(x -> id.equals(x.getIdVehicule())));

        v.setMarque("Renault");
        vehicules.saveAndFlush(v);

        assertEquals("Renault",
                vehicules.findById(id).orElseThrow().getMarque());

        vehicules.deleteById(id);
        vehicules.flush();

        assertFalse(vehicules.existsById(id));
        System.out.println("PASS: VehiculeRepository CRUD");
    }

    @Test
    void testEmployeRepository() {
        Agence agence = nouvelleAgence();

        Employe e = new Employe();
        e.setNom("TestEmploye");
        e.setPrenom("Karim");
        e.setRole(RoleEmploye.AGENT);
        e.setAgence(agence);

        e = employes.saveAndFlush(e);
        Long id = e.getIdEmploye();

        assertNotNull(id);
        assertTrue(employes.existsById(id));
        assertTrue(employes.findById(id).isPresent());
        assertEquals(agence.getIdAgence(),
                employes.findById(id).orElseThrow()
                        .getAgence().getIdAgence());

        employes.deleteById(id);
        employes.flush();

        assertFalse(employes.existsById(id));
        System.out.println("PASS: EmployeRepository CRUD");
    }

    @Test
    void testEquipementRepository() {
        Equipement e = new Equipement();
        e.setLibelle("GPS Test");

        e = equipements.saveAndFlush(e);
        Long id = e.getIdEquipement();

        assertNotNull(id);
        assertTrue(equipements.existsById(id));
        assertTrue(equipements.findAll().stream()
                .anyMatch(x -> id.equals(x.getIdEquipement())));

        equipements.deleteById(id);
        equipements.flush();

        assertFalse(equipements.existsById(id));
        System.out.println("PASS: EquipementRepository CRUD");
    }

    @Test
    void testContratRepository() {
        Contrat c = new Contrat();
        c.setDateSignature(LocalDate.now());
        c.setMontantTotal(new BigDecimal("480.00"));
        c.setValide(true);

        Paiement p = new Paiement();
        p.setMontant(new BigDecimal("150.00"));
        p.setDatePaiement(LocalDate.now());
        p.setModePaiement(ModePaiement.CARTE);
        p.setContrat(c);

        c.getPaiements().add(p);

        c = contrats.saveAndFlush(c);
        Long contratId = c.getIdContrat();
        Long paiementId = p.getIdPaiement();

        assertNotNull(contratId);
        assertNotNull(paiementId);
        assertTrue(contrats.existsById(contratId));
        assertTrue(paiements.existsById(paiementId));

        Contrat lu = contrats.findById(contratId).orElseThrow();
        assertEquals(new BigDecimal("480.00"), lu.getMontantTotal());

        contrats.deleteById(contratId);
        contrats.flush();

        assertFalse(contrats.existsById(contratId));
        assertFalse(paiements.existsById(paiementId));

        System.out.println("PASS: Contrat/Paiement CASCADE");
    }
}
