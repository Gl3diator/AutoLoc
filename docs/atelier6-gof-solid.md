# Atelier 6 — Patrons GoF et principes SOLID

**Projet :** AutoLoc\
**Cours :** Architecture des Systèmes d'Information\
**Date :** 29/10/2026\
**Environnement :** Fedora, Java 17, Maven, Spring Boot, MariaDB, Zed

## 1. Objectifs

Refactorer AutoLoc avec les principes SOLID et les patrons GoF :

- SRP : Single Responsibility Principle
- OCP : Open/Closed Principle
- DIP : Dependency Inversion Principle
- Strategy : calcul dynamique des tarifs
- Factory : sélection des stratégies et notifications
- Observer : notifications de confirmation et d'annulation

## 2. Partie 1 — Analyse de AutoLoc v0

Fichier : `legacy/AutoLocManager.java`

### Question 1 — Combien de responsabilités possède AutoLocManager ?

Cinq responsabilités principales :

1. Calculer le prix de location.
2. Créer les contrats.
3. Envoyer les notifications email/SMS.
4. Enregistrer les paiements.
5. Sauvegarder les contrats.

### Question 2 — Quel principe SOLID n'est pas respecté ?

Le principe SRP (Single Responsibility Principle).

La classe AutoLocManager possède plusieurs responsabilités
indépendantes et plusieurs raisons de changer.

### Question 3 — Que se passe-t-il si AutoLoc ajoute PleineSaison ?

Il faut modifier les conditions if/else de AutoLocManager.

Principe violé : OCP (Open/Closed Principle).

Une architecture extensible doit permettre d'ajouter
une stratégie tarifaire sans modifier les stratégies existantes.

## 3. Partie 2 — Strategy Pattern

Package : `tn.esprit.autoloc.service.strategy`

### Classes créées

- ITarificationStrategy.java
- TarifStandardStrategy.java
- TarifWeekendStrategy.java
- TarifLongueDureeStrategy.java

### Interface commune

```java
BigDecimal calculerTarif(Vehicule vehicule, Reservation reservation);
String getCode();
```

### Calculs

| Stratégie | Calcul |
|---|---|
| STANDARD | Tarif journalier × jours |
| WEEKEND | Prix standard × 0.90 |
| LONGUE_DUREE | Prix standard × 0.80 |

Les stratégies utilisent BigDecimal pour les montants.

Les remises réutilisent TarifStandardStrategy pour éviter
la duplication du calcul de base.

## 4. Partie 3 — Factory Pattern

Package : `tn.esprit.autoloc.service.factory`

### TarificationStrategyFactory

Sélectionne automatiquement une stratégie avec :

```java
ITarificationStrategy resoudre(Reservation reservation);
```

Règles adoptées pour cet atelier :

- Durée >= 7 jours : LONGUE_DUREE
- Sinon, début samedi/dimanche : WEEKEND
- Sinon : STANDARD

Ces règles de sélection sont des hypothèses
d'implémentation : le PDF ne précise pas les seuils.

Spring injecte automatiquement les stratégies disponibles.

### NotificationFactory

```java
IReservationObserver creerObservateur(String canal);
```

Canaux disponibles :

- EMAIL
- SMS

Une valeur inconnue produit une IllegalArgumentException.

La Factory retourne les observateurs gérés par Spring.

## 5. Partie 4 — Observer Pattern

Package : `tn.esprit.autoloc.service.observer`

### Classes créées

- IReservationObserver.java
- ReservationSubject.java
- EmailNotificationObserver.java
- SmsNotificationObserver.java

### Méthodes

```java
void onReservationConfirmee(Reservation reservation);
void onReservationAnnulee(Reservation reservation);
```

ReservationSubject maintient la liste des observateurs.

Lors d'une confirmation ou annulation :

1. Le service déclenche l'événement.
2. ReservationSubject reçoit cet événement.
3. EmailNotificationObserver est notifié.
4. SmsNotificationObserver est notifié.

Les notifications sont simulées avec les logs SLF4J.

## 6. Partie 5 — Intégration Spring Boot

### Fichiers modifiés

- service/IReservationService.java
- service/impl/ReservationServiceImpl.java
- web/controller/ReservationController.java

### Méthodes ajoutées

```java
Reservation annulerReservation(Long id);
BigDecimal calculerTarif(Long id);
```

### Endpoints REST

| Méthode | Endpoint | Fonction |
|---|---|---|
| GET | /api/reservations/{id}/tarif | Calcul du prix |
| PATCH | /api/reservations/{id}/annuler | Annulation |

La tarification suit :

Controller → Service → Factory → Strategy

Les notifications suivent :

Controller → Service → ReservationSubject → Observers

Une réservation déjà annulée n'envoie pas de nouvelles
notifications lorsqu'on appelle à nouveau annulerReservation.

## 7. Tests automatisés

### Atelier6PatternsTest.java

Six tests :

1. Tarif STANDARD
2. Tarif WEEKEND
3. Tarif LONGUE_DUREE
4. Sélection automatique Factory
5. Validation des dates
6. NotificationFactory

### ReservationServiceTest.java

Deux tests :

1. Confirmation avec EMAIL et SMS
2. Annulation avec EMAIL et SMS, sans doublons

### Commandes

```bash
cd ~/Documents/autoloc

mvn -Dtest=Atelier6PatternsTest test
mvn -Dtest=ReservationServiceTest test

mvn clean test
```

### Résultats vérifiés le 29/10/2026

```text
Atelier6PatternsTest     6 PASS
ReservationServiceTest   2 PASS
RepositoryCrudTests      6 PASS
ServiceCrudTests         6 PASS
AutolocApplicationTests  1 PASS

Tests run: 21
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

## 8. Tests REST réalisés

### Infrastructure

```bash
mvn spring-boot:run
```

Swagger :

http://localhost:8080/api/swagger-ui.html

OpenAPI :

http://localhost:8080/api/v3/api-docs

### Données de test utilisées

Client : ID 37\
Véhicule : ID 18\
Tarif journalier : 150 DT

Ces données ont été supprimées après validation.

### Résultats

| Réservation | Stratégie | Prix obtenu |
|---|---|---|
| 7 | STANDARD | 600.00 DT |
| 8 | WEEKEND | 540.0000 DT |
| 9 | LONGUE_DUREE | 1200.0000 DT |

Toutes les requêtes de tarification ont retourné HTTP 200.

### Confirmation

Réservation 7 : EN_ATTENTE → CONFIRMEE

Logs observés :

```text
[EMAIL] Confirmation envoyee au client atelier6@example.invalid pour la reservation #7
[SMS] Confirmation envoyee au 12345678 pour la reservation #7
```

### Annulation

```http
PATCH /api/reservations/7/annuler
```

Résultat : HTTP 200, statut ANNULEE.

Logs observés :

```text
Reservation #7 annulee
[EMAIL] Annulation notifiee au client atelier6@example.invalid pour la reservation #7
[SMS] Annulation notifiee au 12345678 pour la reservation #7
```

Une seconde annulation a conservé le statut ANNULEE
avec HTTP 200. L'absence de notifications répétées
a été vérifiée par le test automatisé.

### Nettoyage

- DELETE réservation 7 : HTTP 204
- DELETE réservation 8 : HTTP 204
- DELETE réservation 9 : HTTP 204
- DELETE véhicule 18 : HTTP 204
- DELETE client 37 : HTTP 204

## 9. Démonstration pour l'instructeur

### Étape 1 — Démarrer AutoLoc

Terminal 1 :

```bash
cd ~/Documents/autoloc
mvn spring-boot:run
```

Attendu : Started AutolocApplication.

### Étape 2 — Vérifier les endpoints

Terminal 2 :

```bash
curl -sS -o /dev/null -w 'Reservations: %{http_code}\n' \
  http://localhost:8080/api/reservations

curl -sS -o /dev/null -w 'Swagger: %{http_code}\n' \
  http://localhost:8080/api/v3/api-docs
```

Attendu : HTTP 200.

### Étape 3 — Montrer Strategy et Factory

```bash
mvn -Dtest=Atelier6PatternsTest test
```

Attendu : 6 tests réussis.

Montrer dans Zed :

- ITarificationStrategy
- TarifStandardStrategy
- TarifWeekendStrategy
- TarifLongueDureeStrategy
- TarificationStrategyFactory

Expliquer la sélection STANDARD / WEEKEND / LONGUE_DUREE.

### Étape 4 — Montrer Observer

```bash
mvn -Dtest=ReservationServiceTest test
```

Attendu : 2 tests réussis.

Montrer dans Zed :

- IReservationObserver
- ReservationSubject
- EmailNotificationObserver
- SmsNotificationObserver
- ReservationServiceImpl

Les tests vérifient les logs EMAIL/SMS de confirmation
et d'annulation.

### Étape 5 — Montrer les endpoints REST

Dans Swagger UI, ouvrir ReservationController.

Montrer :

- GET /api/reservations/{id}/tarif
- PATCH /api/reservations/{id}/annuler

Pour une démonstration dynamique, créer d'abord
un client, un véhicule et une réservation de test.

Utiliser les IDs réellement retournés par POST.
Ne pas réutiliser les anciens IDs 7, 8 et 9,
car les enregistrements correspondants ont été supprimés.

Afficher le tarif, confirmer la réservation avec PUT,
puis l'annuler avec PATCH.

Observer les logs dans le terminal Spring Boot.

Supprimer uniquement les données créées pour la démonstration.

### Étape 6 — Vérification finale

```bash
cd ~/Documents/autoloc

mvn clean test
git status --short
git diff --check
```

Attendu : 21 tests réussis, BUILD SUCCESS.

## 10. Questions de soutenance

**Pourquoi Strategy ?**\
Pour remplacer les conditions tarifaires par des
algorithmes interchangeables.

**Pourquoi Factory ?**\
Pour centraliser la sélection des implémentations.

**Pourquoi Observer ?**\
Pour notifier plusieurs composants lorsqu'un
événement métier se produit.

**Quel principe SOLID est corrigé principalement ?**\
SRP : séparation des responsabilités.

**Quel principe permet d'ajouter des stratégies ?**\
OCP : ouverture à l'extension.

**Pourquoi utiliser une interface ?**\
Pour réduire le couplage et faciliter les tests.

**Quel est le rôle de @Component ?**\
Déclarer une classe comme bean Spring.

**Quel est le rôle de @RequiredArgsConstructor ?**\
Générer un constructeur pour les champs final.

**Pourquoi BigDecimal ?**\
Pour éviter les imprécisions usuelles de double
dans les calculs monétaires.

## 11. État final

- Analyse SOLID réalisée
- Strategy implémenté et testé
- Factory implémenté et testé
- Observer implémenté et testé
- Tarification REST vérifiée
- Confirmation et annulation vérifiées
- Nettoyage des données de test effectué
- 21 tests Maven réussis
- Aucun échec de compilation

Le code a été vérifié localement sur Fedora.
La documentation sera versionnée avec le projet AutoLoc.
