# 🏥 SahtyCare - Gestion Hospitalière Intelligente

### 🚀 Pitch
SahtyCare est une plateforme innovante de gestion hospitalière conçue pour centraliser les interactions entre patients, médecins et administrateurs [cite: 54-55]. ]Notre solution permet de simplifier la prise de rendez-vous, de sécuriser le suivi des dossiers médicaux et d'optimiser la gestion des analyses biologiques au sein des cliniques tunisiennes[cite: 56].

---

## 👥 L'Équipe Scrum (Startup)
[cite_start]Conformément au principe "1 membre = 1 fonctionnalité"[cite: 7, 35], voici la répartition :

| Membre | Rôle Scrum | Fonctionnalité Gérée | Branche Git |
| :--- | :--- | :--- | :--- |
| **[chahed sassi]** | Product Owner | Inscription & Authentification | `feature/authentification` |
| **[Maryem sokker]** | Scrum Master | Gestion des Rendez-vous | `feature/rendez-vous` |
| **[Taha Hasnaoui]** | Développeur | Dashboard Administrateur | `feature/admin-users` |
| **[Ameni Bouguerra]** | Développeur | Gestion des Analyses | `feature/analyses` |
| **[Arij Hamida]** | Développeur | Dossiers Patients | `feature/patients` |

---

## 🎨 Design & Maquettes
Le prototype interactif de l'application est disponible sur Figma. Il comprend au moins 2 écrans par fonctionnalité (10 écrans au total) avec une navigation cliquable [cite: 97-98, 115].

🔗 **[Lien Figma : Cliquez ici pour voir les maquettes](https://lift-fairy-10437464.figma.site/)**

---

## 📂 Structure du Dépôt (Standard AGL)
Le projet suit l'arborescence exigée par le cahier des charges [cite: 120-131, 208] :

* `src/com/sahtycare/models/` : Code source Java (Squelette V1 & Logique V2)[cite: 129, 208].
* `diagrammes/` : Modélisation UML (DCU, DC, DS) au format PlantUML[cite: 124, 208].
* `docs/captures/` : Preuves visuelles (Maquettes Figma, Kanban Jira, Reviews de code)[cite: 115, 208].
* `docs/rapport.pdf` : Rapport final consolidé (Livrable 7)[cite: 208].

---

## 🛠️ Instructions d'Exécution (V1)
Pour tester le prototype console du Sprint 1[cite: 155]:

1. **Compilation :**
   ```bash
   javac -encoding UTF-8 -d out src/com/sahtycare/models/*.java
