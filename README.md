# Application de Gestion des Relevés

Application Android pour la gestion des agents, clients et relevés de consommation.

## Structure du Projet

```
com.master.base/
├── dao/
│   ├── AgentDAO.java
│   ├── ClientDAO.java
│   └── ReleveDAO.java
├── database/
│   └── DatabaseManager.java
├── models/
│   ├── Agent.java
│   ├── Client.java
│   └── Releve.java
├── utils/
│   └── DataInitializer.java
├── activities/
│   ├── MainActivity.java
│   ├── ClientActivity.java
│   └── ReleveActivity.java
```

## Fonctionnalités

### Gestion des Agents
- Liste des agents
- Ajout d'un nouvel agent
- Modification d'un agent existant
- Suppression d'un agent

### Gestion des Clients
- Liste des clients par agent
- Ajout d'un nouveau client
- Modification d'un client existant
- Suppression d'un client

### Gestion des Relevés
- Liste des relevés par client
- Ajout d'un nouveau relevé
- Modification d'un relevé existant
- Suppression d'un relevé
- Calcul automatique des montants
- Prix du kWh fixé à 0.2516€

## Base de Données

### Tables
- `agent` : id, nom
- `client` : id, nom, agent_id
- `releve` : id, date, valeur, client_id

### Relations
- Un agent peut avoir plusieurs clients
- Un client appartient à un seul agent
- Un client peut avoir plusieurs relevés

## Validation des Données

### Relevés
- La valeur d'un nouveau relevé doit être supérieure à la dernière valeur enregistrée
- La date d'un nouveau relevé doit être postérieure à la dernière date enregistrée
- Un seul relevé par jour autorisé

## Interface Utilisateur

- Navigation hiérarchique : Agents → Clients → Relevés
- Boutons de retour pour la navigation
- Dialogues de confirmation pour les suppressions
- Interface de saisie adaptée (DatePicker pour les dates)
