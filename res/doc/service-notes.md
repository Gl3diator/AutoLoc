# Atelier 4 — Couche Service, IoC et Injection des dépendances

## 1. Architecture

L'application AutoLoc utilise une architecture en couches :

Controller -> Service -> Repository -> MariaDB

La couche Service centralise les règles métier et
isole les futurs contrôleurs REST de la persistance.

## 2. Services implémentés

| Interface | Implémentation | Opérations |
|---|---|---|
| IAgenceService | AgenceServiceImpl | CRUD, count, existsById |
| IEmployeService | EmployeServiceImpl | CRUD, count, existsById |
| IVehiculeService | VehiculeServiceImpl | CRUD, count, existsById |
| IEquipementService | EquipementServiceImpl | CRUD, count, existsById |
| IClientService | ClientServiceImpl | CRUD, count, existsById |
| IReservationService | ReservationServiceImpl | CRUD, count, existsById |
| IContratService | ContratServiceImpl | CRUD, count, existsById |
| IPaiementService | PaiementServiceImpl | Lecture uniquement |
| IMaintenanceService | MaintenanceServiceImpl | CRUD, count, existsById |

## 3. IoC et injection des dépendances

@Service permet à Spring de détecter les implémentations.

@RequiredArgsConstructor de Lombok génère un constructeur
pour les champs final.

Spring injecte automatiquement les repositories dans
les services par injection de constructeur.

Avantages :
- Couplage réduit
- Séparation des responsabilités
- Testabilité
- Maintenance facilitée

## 4. Gestion des exceptions

ResourceNotFoundException est levée lorsqu'une ressource
demandée n'existe pas.

IllegalArgumentException est utilisée pour les
données invalides.

Une traduction de ces exceptions en réponses HTTP
sera ajoutée dans l'Atelier 5.

## 5. Validations métier implémentées

- Agence : nom obligatoire
- Client : nom et email obligatoires
- Employe : nom et rôle obligatoires
- Equipement : libellé obligatoire
- Vehicule : tarif journalier non négatif
- Reservation : dateFin >= dateDebut
- Maintenance : dateFin >= dateDebut
- Contrat : montant total non négatif

Les mises à jour ordinaires conservent les associations
existantes. La gestion explicite du changement
d'associations reste à traiter.

PaiementService expose uniquement findById et findAll.

## 6. Tests d'intégration

Classe : ServiceCrudTests

Six scénarios :
1. AgenceService
2. ClientService
3. VehiculeService
4. EmployeService
5. EquipementService
6. ContratService et cascade Paiement

Résultats observés le 27/10/2026 :

ServiceCrudTests :
Tests run: 6
Failures: 0
Errors: 0

Suite complète :
Tests run: 13
Failures: 0
Errors: 0
BUILD SUCCESS

Les tests utilisent @SpringBootTest et @Transactional.

## 7. SonarQube

Analyse statique à compléter.

Ne pas déclarer de problèmes corrigés sans les
avoir effectivement vérifiés.
