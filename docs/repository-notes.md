# Atelier 3 — Spring Data JPA et Repository

## A. Choix des interfaces

Toutes les interfaces héritent de JpaRepository<Entite, Long>.

| Interface | Entité | Justification |
|---|---|---|
| IAgenceRepository | Agence | CRUD, listes et comptage |
| IEmployeRepository | Employe | CRUD des employés |
| IVehiculeRepository | Vehicule | CRUD et mise à jour |
| IEquipementRepository | Equipement | CRUD des équipements |
| IClientRepository | Client | CRUD des clients |
| IReservationRepository | Reservation | Gestion des réservations |
| IContratRepository | Contrat | CRUD et cascade vers Paiement |
| IPaiementRepository | Paiement | Lecture des paiements |
| IMaintenanceRepository | Maintenance | Gestion des maintenances |

JpaRepository fournit les opérations CRUD, les listes,
le tri, la pagination et les méthodes JPA comme flush().

Spring Data JPA génère les implémentations à l'exécution.
Aucune classe RepositoryImpl n'est nécessaire.

## B. Opérations CRUD

- save() : insertion ou mise à jour.
- findAll() : récupération de toutes les entités.
- findById() : recherche retournant Optional.
- existsById() : vérification d'existence.
- count() : nombre total d'entités.
- deleteById() : suppression par identifiant.
- saveAndFlush() : sauvegarde et synchronisation SQL.

Un identifiant généré est renseigné après persistance.
Une entité existante peut être mise à jour avec save().

Les suppressions en lot contournent les cascades JPA :
elles ne doivent pas remplacer aveuglément deleteById().

## C. Tests réalisés

Classe : RepositoryCrudTests

Tests :
1. AgenceRepository
2. ClientRepository
3. VehiculeRepository
4. EmployeRepository
5. EquipementRepository
6. ContratRepository et cascade Paiement

Résultat observé le 27/10/2026 :

Tests run: 6
Failures: 0
Errors: 0

Suite Maven complète :

Tests run: 7
Failures: 0
Errors: 0
BUILD SUCCESS

Les tests utilisent @SpringBootTest et @Transactional.
Spring effectue le rollback des transactions de test.

La suppression d'un Contrat entraîne la suppression
de ses Paiements via CascadeType.ALL.

## D. SonarQube for IDE

Analyse à réaliser dans IntelliJ IDEA.

| Anomalie réelle | Règle | Correction |
|---|---|---|
| À relever | À relever | À documenter |
| À relever | À relever | À documenter |
| À relever | À relever | À documenter |

Ne pas déclarer d'anomalie corrigée sans vérification.

## E. Validation

- 9 entités JPA : OK
- 9 repositories : OK
- Démarrage Spring Boot : OK
- 9 repositories détectés : OK
- 6 tests CRUD : OK
- Suite Maven : 7/7 PASS
- Analyse SonarQube : à compléter
