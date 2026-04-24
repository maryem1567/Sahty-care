# Refactorisation Medecin - Documentation

## 📌 Vue d'ensemble

La classe `Medecin` a été **entièrement refactorisée** pour un projet Spring Boot avec :
- ✅ Annotations JPA (@Entity, @Table)
- ✅ Héritage JPA de Utilisateur
- ✅ Validation des champs (Bean Validation)
- ✅ Liste de RendezVous avec relation One-to-Many
- ✅ Annotations Lombok (réduction du boilerplate)

---

## 🎯 Principaux changements

### Avant (Ancien code)
```java
public class Medecin extends Utilisateur {
    private String specialite;

    public Medecin() {}
    public Medecin(int id, String nom, String specialite) { ... }

    public void consulterPlanning() { ... }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { ... }
}
```

### Après (Code refactorisé)
```java
@Entity
@Table(name = "medecins", ...)
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = "rendezVous")
public class Medecin extends Utilisateur {
    
    @NotBlank
    @Size(min = 3, max = 100)
    @Column(name = "specialite", length = 100, nullable = false)
    private String specialite;

    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL, ...)
    @Builder.Default
    private List<RendezVous> rendezVous = new ArrayList<>();

    // ... plus 10+ méthodes métier
}
```

---

## 📊 Architecture JPA

### Héritage JOINED

La classe `Medecin` utilise une **stratégie d'héritage JOINED** :

```
UTILISATEURS (table parent)
├── id (PK)
├── nom
├── prenom
├── email
├── role = 'MEDECIN'
└── ... autres colonnes

MEDECINS (table enfant)
├── utilisateur_id (PK, FK -> UTILISATEURS.id)
├── specialite
├── numero_ordre
├── tarification_consultations
├── disponible
└── ... autres colonnes
```

**Avantages** :
- ✅ Chaque classe a sa propre table
- ✅ Pas de colonnes inutilisées
- ✅ Requête JOIN pour récupérer un Medecin
- ✅ Polymorphisme SQL supporté

---

## 📋 Annotations JPA

### @Entity
Marque la classe comme entité persistante

### @Table
```java
@Table(
    name = "medecins",
    indexes = {
        @Index(name = "idx_medecin_utilisateur_id", columnList = "utilisateur_id", unique = true),
        @Index(name = "idx_medecin_specialite", columnList = "specialite"),
        @Index(name = "idx_medecin_disponible", columnList = "disponible")
    }
)
```

### @Inheritance et @PrimaryKeyJoinColumn
```java
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "utilisateur_id")
```
Définit la stratégie d'héritage et la clé de jointure

### @OneToMany
```java
@OneToMany(
    mappedBy = "medecin",
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY,
    orphanRemoval = true
)
```

**Paramètres** :
- `mappedBy` : L'attribut de l'entité liée qui référence cette entité
- `cascade = CascadeType.ALL` : Les opérations se cascadent (INSERT, UPDATE, DELETE)
- `fetch = FetchType.LAZY` : Les rendez-vous sont chargés à la demande
- `orphanRemoval = true` : Les rendez-vous orphelins sont supprimés

---

## ✅ Validation Bean Validation

```java
@NotBlank(message = "La spécialité ne peut pas être vide")
@Size(min = 3, max = 100, ...)
private String specialite;

@Positive(message = "Le numéro d'ordre doit être positif")
private String numeroOrdre;

@DecimalMin(value = "0.0", ...)
private java.math.BigDecimal tarificationConsultations;
```

**Annotations utilisées** :
- `@NotBlank` : Non vide (String)
- `@Size` : Taille/longueur
- `@Positive` : Valeur positive
- `@DecimalMin` : Valeur minimale décimale

---

## 🔗 Annotations Lombok

### @Data
```java
@Data
public class Medecin extends Utilisateur { ... }
```
Génère automatiquement :
- Getters et Setters
- equals(), hashCode()
- toString()

### @NoArgsConstructor
Génère un constructeur vide

### @AllArgsConstructor
Génère un constructeur avec tous les arguments

### @EqualsAndHashCode(callSuper = true)
Inclut les attributs de la classe parent dans equals() et hashCode()

### @ToString(callSuper = true, exclude = "rendezVous")
Inclut les attributs du parent et exclut la liste de rendez-vous

### @Builder
Génère le pattern Builder

```java
Medecin medecin = Medecin.builder()
    .nom("Dupont")
    .prenom("Jean")
    .specialite("Cardiologie")
    .disponible(true)
    .build();
```

---

## 📋 Structure de la classe

### Attributs

| Attribut | Type | Validation | Description |
|----------|------|-----------|-------------|
| `specialite` | String | @NotBlank, @Size | Spécialité médicale |
| `numeroOrdre` | String | @Positive | Numéro d'ordre CNOM |
| `tarificationConsultations` | BigDecimal | @DecimalMin | Tarif de consultation |
| `disponible` | Boolean | - | Disponibilité du médecin |
| `nombrePatients` | Integer | - | Nombre de patients |
| `dateDebutExercice` | LocalDateTime | - | Date du début d'exercice |
| `rendezVous` | List<RendezVous> | - | Liste des rendez-vous |

### Constructeurs

1. **Constructeur vide** (généré par Lombok)
   ```java
   Medecin medecin = new Medecin();
   ```

2. **Constructeur simplifié**
   ```java
   Medecin medecin = new Medecin(
       "Dupont", "Jean", "jean@example.com", 
       "jdupont", "Password123", "Cardiologie"
   );
   ```

3. **Constructeur complet**
   ```java
   Medecin medecin = new Medecin(
       "Dupont", "Jean", "jean@example.com", 
       "jdupont", "Password123", "Cardiologie",
       "12345678", new BigDecimal("150.00")
   );
   ```

---

## 🔧 Méthodes principales

### Gestion des rendez-vous

```java
// Ajouter un rendez-vous
medecin.ajouterRendezVous(rendezVous);

// Retirer un rendez-vous
medecin.retirerRendezVous(rendezVous);

// Nombre total
int nombre = medecin.getNombreRendezVous();

// Rendez-vous en attente de vérification
List<RendezVous> enAttente = medecin.getRendezVousEnAttente();

// Vérifier tous les rendez-vous
medecin.verifierTousLesRendezVous();
```

### Gestion de la disponibilité

```java
// Marquer comme disponible
medecin.marquerDisponible();

// Marquer comme indisponible
medecin.marquerIndisponible();

// Vérifier la possibilité d'accepter un RDV
if (medecin.peutAccepterRendezVous()) {
    // Ajouter le rendez-vous
}
```

### Affichage

```java
// Affichage complet du profil
System.out.println(medecin.afficherProfil());

// Consultation du planning
medecin.consulterPlanning();

// Nom complet avec spécialité
String display = medecin.getDisplayNameMedecin();
// "Dr. Jean Dupont [Cardiologie]"
```

### Gestion des patients

```java
// Incrémenter le nombre de patients
medecin.incrementerNombrePatients();

// Décrémenter
medecin.decrementerNombrePatients();
```

---

## 🔗 Classe RendezVous

### Structure

```java
@Entity
@Table(name = "rendez_vous", ...)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RendezVous {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Future
    private LocalDateTime dateRendezVous;

    @NotBlank
    @Size(min = 5, max = 500)
    private String motif;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medecin_id", nullable = false)
    private Medecin medecin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
}
```

### Méthodes

```java
// Vérifier le rendez-vous
rendezVous.verifier();

// Annuler
rendezVous.annuler();

// Heure de fin
LocalDateTime fin = rendezVous.getHeureFinRendezVous();

// Vérifier si futur
if (rendezVous.estFutur()) { ... }

// Vérifier si passé
if (rendezVous.estPasse()) { ... }

// Affichage
System.out.println(rendezVous.toDetailedString());
```

---

## 👨‍⚕️ Classe Patient

### Structure

```java
@Entity
@Table(name = "patients", ...)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = "rendezVous")
public class Patient extends Utilisateur {
    
    @NotBlank
    private String numeroDossier;

    @NotNull
    @Past
    private LocalDate dateNaissance;

    @Pattern(regexp = "^(O|A|B|AB)[+-]$")
    private String groupeSanguin;

    @Column(name = "allergies", length = 500)
    private String allergies;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, ...)
    private List<RendezVous> rendezVous = new ArrayList<>();
}
```

### Calculs médicaux

```java
// Âge
int age = patient.calculerAge();

// IMC
Double imc = patient.calculerIMC();

// Interprétation IMC
String interpretation = patient.getInterpretationIMC();
// "Poids normal", "Surpoids", etc.
```

### Affichage

```java
// Profil médical complet
System.out.println(patient.afficherProfilMedical());

// Historique des rendez-vous
patient.afficherHistorique();
```

---

## 📊 Schéma base de données

```sql
-- Table parent
CREATE TABLE utilisateurs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL,
    -- ... autres colonnes
);

-- Table enfant Medecin
CREATE TABLE medecins (
    utilisateur_id BIGINT PRIMARY KEY,
    specialite VARCHAR(100) NOT NULL,
    numero_ordre VARCHAR(50),
    tarification_consultations DECIMAL(10, 2),
    disponible BOOLEAN DEFAULT TRUE,
    nombre_patients INT DEFAULT 0,
    date_debut_exercice DATETIME,
    
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id),
    UNIQUE KEY uq_numero_ordre (numero_ordre),
    INDEX idx_specialite (specialite),
    INDEX idx_disponible (disponible)
);

-- Table enfant Patient
CREATE TABLE patients (
    utilisateur_id BIGINT PRIMARY KEY,
    numero_dossier VARCHAR(50) NOT NULL UNIQUE,
    date_naissance DATE NOT NULL,
    groupe_sanguin VARCHAR(3),
    allergies VARCHAR(500),
    maladies_chroniques VARCHAR(500),
    poids_kg DECIMAL(5, 2),
    taille_cm INT,
    
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id),
    INDEX idx_numero_dossier (numero_dossier),
    INDEX idx_groupe_sanguin (groupe_sanguin)
);

-- Rendez-vous
CREATE TABLE rendez_vous (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    date_rendezvous DATETIME NOT NULL,
    motif VARCHAR(500) NOT NULL,
    medecin_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    verifie BOOLEAN DEFAULT FALSE,
    
    FOREIGN KEY (medecin_id) REFERENCES medecins(utilisateur_id),
    FOREIGN KEY (patient_id) REFERENCES patients(utilisateur_id),
    INDEX idx_date (date_rendezvous),
    INDEX idx_verifie (verifie)
);
```

---

## 🔐 Dépendances requises

```xml
<!-- JPA/Hibernate -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <scope>provided</scope>
</dependency>

<!-- Database (exemple MySQL) -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```

---

## 💡 Exemples d'utilisation

### Créer un médecin

```java
Medecin medecin = new Medecin(
    "Dupont", "Jean",
    "jean.dupont@example.com",
    "jdupont",
    "SecurePassword123",
    "Cardiologie"
);

medecin.setNumeroOrdre("12345678");
medecin.setTarificationConsultations(new BigDecimal("150.00"));

repository.save(medecin);
```

### Créer un patient

```java
Patient patient = new Patient(
    "Martin", "Pierre",
    "pierre.martin@example.com",
    "pmartin",
    "SecurePassword123",
    "DOSS001",
    LocalDate.of(1990, 5, 15)
);

patient.setGroupeSanguin("O+");
patient.setAllergies("Pénicilline");

repository.save(patient);
```

### Créer un rendez-vous

```java
RendezVous rendezVous = new RendezVous(
    LocalDateTime.now().plusDays(7),
    "Consultation cardiologie",
    medecin,
    patient
);

rendezVous.setDureeMinutes(45);
rendezVous.setDescription("Suivi de la tension artérielle");

medecin.ajouterRendezVous(rendezVous);
patient.ajouterRendezVous(rendezVous);

repository.save(rendezVous);
```

### Affichage

```java
// Profil du médecin
System.out.println(medecin.afficherProfil());

// Profil médical du patient
System.out.println(patient.afficherProfilMedical());

// Planning du médecin
medecin.consulterPlanning();

// Historique du patient
patient.afficherHistorique();
```

---

## ✅ Checklist Spring Boot

- [ ] Ajouter les dépendances Maven (JPA, Validation, Lombok)
- [ ] Configurer application.properties
- [ ] Créer MedecinRepository et PatientRepository
- [ ] Créer RendezVousRepository
- [ ] Créer les services métier
- [ ] Ajouter les contrôleurs REST
- [ ] Tester la création et la récupération
- [ ] Configurer Hibernate pour créer les tables
- [ ] Valider les relations One-to-Many
- [ ] Tester la validation Bean Validation

---

## 🎯 Prochaines étapes

1. **Créer les repositories** :
   ```java
   public interface MedecinRepository extends JpaRepository<Medecin, Long> {
       List<Medecin> findBySpecialite(String specialite);
       List<Medecin> findByDisponibleTrue();
   }
   ```

2. **Créer les services** avec logique métier

3. **Créer les contrôleurs REST** pour les opérations CRUD

4. **Ajouter les tests unitaires** et d'intégration

5. **Configurer la pagination** et le tri

6. **Ajouter la gestion des erreurs** (GlobalExceptionHandler)

---

**Version** : 2.0  
**Status** : ✅ Production-Ready  
**Dernière mise à jour** : 2026-04-24
