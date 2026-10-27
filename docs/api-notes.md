# Atelier 5 — Spring MVC, Postman et Swagger

## A. Endpoints

Préfixe commun : `/api` (`server.servlet.context-path=/api`).

| Contrôleur | Verbe et route | Service | Succès | Erreurs |
|---|---|---|---|---|
| AgenceController | GET `/api/agences` | `IAgenceService.findAll()` | 200 | — |
| AgenceController | GET `/api/agences/{id}` | `IAgenceService.findById(id)` | 200 | 404 |
| AgenceController | POST `/api/agences` | `IAgenceService.create(entity)` | 201 | 400 |
| AgenceController | PUT `/api/agences/{id}` | `IAgenceService.update(id, entity)` | 200 | 400, 404 |
| AgenceController | DELETE `/api/agences/{id}` | `IAgenceService.deleteById(id)` | 204 | 404 |
| ClientController | GET `/api/clients` | `IClientService.findAll()` | 200 | — |
| ClientController | GET `/api/clients/{id}` | `IClientService.findById(id)` | 200 | 404 |
| ClientController | POST `/api/clients` | `IClientService.create(entity)` | 201 | 400 |
| ClientController | PUT `/api/clients/{id}` | `IClientService.update(id, entity)` | 200 | 400, 404 |
| ClientController | DELETE `/api/clients/{id}` | `IClientService.deleteById(id)` | 204 | 404 |
| EquipementController | GET `/api/equipements` | `IEquipementService.findAll()` | 200 | — |
| EquipementController | GET `/api/equipements/{id}` | `IEquipementService.findById(id)` | 200 | 404 |
| EquipementController | POST `/api/equipements` | `IEquipementService.create(entity)` | 201 | 400 |
| EquipementController | PUT `/api/equipements/{id}` | `IEquipementService.update(id, entity)` | 200 | 400, 404 |
| EquipementController | DELETE `/api/equipements/{id}` | `IEquipementService.deleteById(id)` | 204 | 404 |
| VehiculeController | GET `/api/vehicules` | `IVehiculeService.findAll()` | 200 | — |
| VehiculeController | GET `/api/vehicules/{id}` | `IVehiculeService.findById(id)` | 200 | 404 |
| VehiculeController | POST `/api/vehicules` | `IVehiculeService.create(entity)` | 201 | 400 |
| VehiculeController | PUT `/api/vehicules/{id}` | `IVehiculeService.update(id, entity)` | 200 | 400, 404 |
| VehiculeController | DELETE `/api/vehicules/{id}` | `IVehiculeService.deleteById(id)` | 204 | 404 |
| EmployeController | GET `/api/employes` | `IEmployeService.findAll()` | 200 | — |
| EmployeController | GET `/api/employes/{id}` | `IEmployeService.findById(id)` | 200 | 404 |
| EmployeController | POST `/api/employes` | `IEmployeService.create(entity)` | 201 | 400 |
| EmployeController | PUT `/api/employes/{id}` | `IEmployeService.update(id, entity)` | 200 | 400, 404 |
| EmployeController | DELETE `/api/employes/{id}` | `IEmployeService.deleteById(id)` | 204 | 404 |
| ReservationController | GET `/api/reservations` | `IReservationService.findAll()` | 200 | — |
| ReservationController | GET `/api/reservations/{id}` | `IReservationService.findById(id)` | 200 | 404 |
| ReservationController | POST `/api/reservations` | `IReservationService.create(entity)` | 201 | 400 |
| ReservationController | PUT `/api/reservations/{id}` | `IReservationService.update(id, entity)` | 200 | 400, 404 |
| ReservationController | DELETE `/api/reservations/{id}` | `IReservationService.deleteById(id)` | 204 | 404 |
| MaintenanceController | GET `/api/maintenances` | `IMaintenanceService.findAll()` | 200 | — |
| MaintenanceController | GET `/api/maintenances/{id}` | `IMaintenanceService.findById(id)` | 200 | 404 |
| MaintenanceController | POST `/api/maintenances` | `IMaintenanceService.create(entity)` | 201 | 400 |
| MaintenanceController | PUT `/api/maintenances/{id}` | `IMaintenanceService.update(id, entity)` | 200 | 400, 404 |
| MaintenanceController | DELETE `/api/maintenances/{id}` | `IMaintenanceService.deleteById(id)` | 204 | 404 |
| ContratController | GET `/api/contrats` | `IContratService.findAll()` | 200 | — |
| ContratController | GET `/api/contrats/{id}` | `IContratService.findById(id)` | 200 | 404 |
| ContratController | POST `/api/contrats` | `IContratService.create(entity)` | 201 | 400 |
| ContratController | PUT `/api/contrats/{id}` | `IContratService.update(id, entity)` | 200 | 400, 404 |
| ContratController | DELETE `/api/contrats/{id}` | `IContratService.deleteById(id)` | 204 | 404 |
| PaiementController | GET `/api/paiements` | `IPaiementService.findAll()` | 200 | — |
| PaiementController | GET `/api/paiements/{id}` | `IPaiementService.findById(id)` | 200 | 404 |
| PaiementController | POST `/api/paiements` | Non exposé | — | 405 |

## B. Annotations Jackson

- `Contrat.paiements` : `@JsonManagedReference("contrat-paiements")` conserve les paiements dans le JSON.
- `Paiement.contrat` : `@JsonBackReference("contrat-paiements")` coupe la boucle et conserve le lien parent/enfant lors de la désérialisation.
- `@JsonIgnore` masque les collections inverses : Agence.employes, Agence.vehicules, Client.reservations, Vehicule.reservations, Vehicule.maintenances, Equipement.vehicules et Reservation.contrat.
- `@JsonBackReference` est associé à `@JsonManagedReference` ; `@JsonIgnore` masque une propriété isolée.
- Ne pas masquer Reservation.client : le POST doit conserver le client.
- Les DTO du chapitre 10 supprimeront le couplage direct entre JSON et entités JPA.

## C. Postman et Swagger

- Collection : `postman/AutoLoc.postman_collection.json`.
- Environnement : `postman/AutoLoc-local.postman_environment.json`.
- Le dossier `00 Scenario E2E` contient les opérations ordonnées, validations d'erreurs et cascade.
- Exécution du scénario dans Postman : à confirmer.
- Les tests Python/curl ont validé les sept CRUD, les erreurs 400/404, les suppressions 204, le POST Paiement 405 et la cascade Contrat/Paiement.
- Swagger UI `/api/swagger-ui.html` et OpenAPI JSON `/api/v3/api-docs` : HTTP 200 vérifiés.
- Postman sert à envoyer, rejouer et tester les requêtes ; Swagger sert à documenter et explorer l'API.

## D. Anomalies SonarQube for IDE

Analyse réelle à effectuer dans IntelliJ : relever au moins deux anomalies, leurs règles et leurs corrections.

| Règle | Explication | Correction vérifiée |
|---|---|---|
| À relever | À relever | À relever |
| À relever | À relever | À relever |

## E. Questions de compréhension

1. **DispatcherServlet** reçoit les requêtes ; les HandlerMapping utilisent le chemin, le verbe HTTP et les annotations pour sélectionner la méthode du contrôleur.
2. ContratController dépend de IContratService pour préserver les couches, éviter le couplage à l'implémentation et faciliter les tests.
3. POST retourne 201 après création, DELETE retourne 204 sans corps. ResourceNotFoundException est convertie en 404 par GlobalExceptionHandler.
4. Paiement est créé et supprimé via la cascade de Contrat ; POST /api/paiements retourne 405.
5. L'exposition des entités JPA risque les boucles JSON, le chargement LAZY et le couplage au modèle. JsonManagedReference garde les paiements visibles ; JsonIgnore masque les associations inverses non nécessaires.

## Limites du modèle actuel

Equipement ne possède pas de quantité et Maintenance ne possède pas de coût. Ces validations du TP ne sont donc pas couvertes par les attributs actuellement implémentés.
