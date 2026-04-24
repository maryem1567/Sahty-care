package com.sahtycare.models;

import jakarta.persistence.*;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Configuration pour l'utilisation de BCrypt en production
 * Décommentez et configurez avec Spring Security
 * import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 * private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
 */

/**
 * Énumération des rôles utilisateur
 * Définit les différents rôles et leurs permissions dans le système
 */
public enum RoleUtilisateur {
    ADMINISTRATEUR("Admin", "Accès complet au système", true),
    MEDECIN("Médecin", "Gestion des patients et analyses", false),
    PATIENT("Patient", "Consultation de ses analyses", false),
    SECRETAIRE("Secrétaire", "Gestion administrative", false),
    LABORANTIN("Laborantin", "Gestion des analyses médicales", false);

    private final String libelle;
    private final String description;
    private final boolean estAdmin;

    RoleUtilisateur(String libelle, String description, boolean estAdmin) {
        this.libelle = libelle;
        this.description = description;
        this.estAdmin = estAdmin;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAdmin() {
        return estAdmin;
    }

    /**
     * Retourne tous les rôles disponibles avec leur description
     */
    public static String[] getPermissions() {
        return java.util.Arrays.stream(values())
                .map(r -> r.getLibelle() + ": " + r.getDescription())
                .toArray(String[]::new);
    }
}

/**
 * Classe Utilisateur refactorisée pour usage réel en production
 * Inclut : JPA/Hibernate, validation robuste, sécurité avancée, encapsulation, audit
 * 
 * Annotations JPA principales :
 * - @Entity : Marque comme entité persistante
 * - @Table : Configuration de la table avec indexes
 * - @PrePersist/@PreUpdate : Callbacks pour les timestamps
 * 
 * Validation (Bean Validation) :
 * - @NotBlank, @Email, @Pattern, @Size, etc.
 * 
 * Sécurité :
 * - Mot de passe hashé avec SHA-256 (utiliser BCrypt en production)
 * - Vérification d'activation avant connexion
 * - Traçabilité des modifications
 */
@Entity
@Table(
    name = "utilisateurs",
    indexes = {
        @Index(name = "idx_email", columnList = "email", unique = true),
        @Index(name = "idx_username", columnList = "username", unique = true),
        @Index(name = "idx_actif", columnList = "actif"),
        @Index(name = "idx_role", columnList = "role")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_email", columnNames = "email"),
        @UniqueConstraint(name = "uq_username", columnNames = "username")
    }
)
public class Utilisateur {

    // ============ CONSTANTES DE VALIDATION ============

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 128;
    private static final String REGEX_USERNAME = "^[a-zA-Z0-9_-]{3,50}$";
    private static final String REGEX_PHONE = "^[+]?[0-9]{10,}$|^$";

    // ============ ATTRIBUTS ============

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Le nom ne peut pas être vide")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Column(name = "nom", length = 100, nullable = false)
    private String nom;

    @NotBlank(message = "Le prénom ne peut pas être vide")
    @Size(min = 2, max = 100, message = "Le prénom doit contenir entre 2 et 100 caractères")
    @Column(name = "prenom", length = 100, nullable = false)
    private String prenom;

    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "L'email doit être valide")
    @Column(name = "email", length = 150, nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Le nom d'utilisateur ne peut pas être vide")
    @Size(min = 3, max = 50, message = "Le username doit contenir entre 3 et 50 caractères")
    @Pattern(regexp = REGEX_USERNAME, message = "Le username ne peut contenir que des lettres, chiffres, - et _")
    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    @Column(name = "motdepasse_hash", nullable = false, length = 256)
    private String motdepasseHash;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le rôle ne peut pas être null")
    @Column(name = "role", nullable = false, length = 50)
    private RoleUtilisateur role;

    @Column(name = "actif", nullable = false)
    private Boolean actif = true;

    @Column(name = "telephone", length = 20)
    @Pattern(regexp = REGEX_PHONE, message = "Le téléphone doit être au format valide")
    private String telephone;

    @Column(name = "adresse", length = 255)
    private String adresse;

    // ============ COLONNES D'AUDIT ============

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "date_derniere_connexion")
    private LocalDateTime dateDerniereConnexion;

    @Column(name = "nombre_tentatives_connexion", nullable = false)
    private Integer nombreTentativesConnexion = 0;

    @Version
    @Column(name = "version")
    private Long version;

    // ============ CONSTRUCTEURS ============

    /**
     * Constructeur par défaut (requis par JPA/Hibernate)
     */
    public Utilisateur() {
        this.actif = true;
        this.nombreTentativesConnexion = 0;
    }

    /**
     * Constructeur complet pour la création d'utilisateur
     * Utilise un mot de passe en clair qui sera hashé automatiquement
     */
    public Utilisateur(String nom, String prenom, String email, String username, 
                       String motdepasse, RoleUtilisateur role) {
        this();
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.username = username;
        this.motdepasseHash = hashPassword(motdepasse);
        this.role = role;
    }

    /**
     * Constructeur minimal pour les tests
     */
    public Utilisateur(String email, String username, String motdepasse) {
        this();
        this.email = email;
        this.username = username;
        this.motdepasseHash = hashPassword(motdepasse);
    }

    // ============ CALLBACKS JPA POUR L'AUDIT ============

    /**
     * Appelé automatiquement avant une insertion en base de données
     */
    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
        if (this.createdBy == null) {
            this.createdBy = "SYSTEM";
        }
        if (this.updatedBy == null) {
            this.updatedBy = "SYSTEM";
        }
    }

    /**
     * Appelé automatiquement avant une mise à jour en base de données
     */
    @PreUpdate
    protected void onUpdate() {
        this.dateModification = LocalDateTime.now();
        if (this.updatedBy == null) {
            this.updatedBy = "SYSTEM";
        }
    }

    // ============ MÉTHODES DE SÉCURITÉ ============

    /**
     * Hash le mot de passe avec SHA-256
     * ⚠️ EN PRODUCTION : Utiliser BCryptPasswordEncoder de Spring Security
     * 
     * Pour activer BCrypt :
     * 1. Ajouter la dépendance : spring-security-crypto
     * 2. Décommenter l'import en haut du fichier
     * 3. Remplacer hashPassword() par : passwordEncoder.encode(motdepasse)
     * 4. Remplacer verifierMotdepasse() par : passwordEncoder.matches(motdepasseClair, motdepasseHash)
     * 
     * @param motdepasse le mot de passe en clair à hasher
     * @return le hash du mot de passe en SHA-256
     * @throws RuntimeException si l'algorithme n'est pas disponible
     */
    private static String hashPassword(String motdepasse) {
        if (motdepasse == null || motdepasse.isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide");
        }
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(motdepasse.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Vérifie si le mot de passe fourni correspond au hash stocké
     * 
     * @param motdepasseClair le mot de passe en clair à vérifier
     * @return true si le mot de passe est correct, false sinon
     */
    public boolean verifierMotdepasse(String motdepasseClair) {
        if (motdepasseClair == null || motdepasseClair.isEmpty()) {
            return false;
        }
        return this.motdepasseHash.equals(hashPassword(motdepasseClair));
    }

    /**
     * Modifie le mot de passe de l'utilisateur
     * Vérifie l'ancien mot de passe avant d'effectuer le changement
     * 
     * @param ancienMotdepasse l'ancien mot de passe pour vérification
     * @param nouveauMotdepasse le nouveau mot de passe (minimum 8 caractères)
     * @return true si le changement a réussi, false sinon
     */
    public boolean changerMotdepasse(String ancienMotdepasse, String nouveauMotdepasse) {
        if (!verifierMotdepasse(ancienMotdepasse)) {
            return false;
        }
        if (nouveauMotdepasse == null || nouveauMotdepasse.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }
        this.motdepasseHash = hashPassword(nouveauMotdepasse);
        this.nombreTentativesConnexion = 0;
        return true;
    }

    /**
     * Définit l'état d'activation de l'utilisateur
     * 
     * @param actif true pour activer, false pour désactiver
     */
    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    /**
     * Désactive l'utilisateur (utile pour les suppressions logiques)
     */
    public void desactiver() {
        this.actif = false;
        this.dateModification = LocalDateTime.now();
    }

    // ============ MÉTHODES DE CONNEXION ============

    /**
     * Effectue la connexion de l'utilisateur
     * Vérifie l'activation, le mot de passe et met à jour la date de dernière connexion
     * 
     * @param motdepasse le mot de passe saisi
     * @return true si la connexion a réussi, false sinon
     */
    public boolean seConnecter(String motdepasse) {
        if (!actif) {
            return false;
        }

        if (!verifierMotdepasse(motdepasse)) {
            this.nombreTentativesConnexion++;
            return false;
        }

        this.dateDerniereConnexion = LocalDateTime.now();
        this.nombreTentativesConnexion = 0;
        return true;
    }

    /**
     * Déconnecte l'utilisateur
     */
    public void seDeconnecter() {
        // Logique de déconnexion (peut être enrichie)
    }

    /**
     * Réinitialise le compteur de tentatives de connexion échouées
     */
    public void reinitialiserTentativesConnexion() {
        this.nombreTentativesConnexion = 0;
    }

    /**
     * Vérifiez si le compte est bloqué après trop de tentatives
     * (Logique métier à adapter selon vos besoins)
     * 
     * @param maxTentatives le nombre maximum de tentatives autorisées
     * @return true si le compte est bloqué
     */
    public boolean estBloqueParTentatives(int maxTentatives) {
        return this.nombreTentativesConnexion >= maxTentatives;
    }

    // ============ MÉTHODES MÉTIER ============

    /**
     * Retourne le nom complet de l'utilisateur
     * 
     * @return le prénom et nom combinés
     */
    public String getNomComplet() {
        return this.prenom + " " + this.nom;
    }

    /**
     * Retourne les initiales de l'utilisateur (ex: "JD" pour John Doe)
     * 
     * @return les deux premières lettres du prénom et du nom
     */
    public String getInitiales() {
        return (this.prenom.charAt(0) + "" + this.nom.charAt(0)).toUpperCase();
    }

    /**
     * Vérifie si l'utilisateur possède un rôle spécifique
     * 
     * @param role le rôle à vérifier
     * @return true si l'utilisateur a ce rôle et est actif
     */
    public boolean aLaPermission(RoleUtilisateur role) {
        return this.role == role && this.actif;
    }

    /**
     * Vérifie si l'utilisateur est administrateur
     * 
     * @return true si le rôle est ADMINISTRATEUR et l'utilisateur est actif
     */
    public boolean estAdmin() {
        return this.role == RoleUtilisateur.ADMINISTRATEUR && this.actif;
    }

    /**
     * Vérifie si l'utilisateur est médecin
     * 
     * @return true si le rôle est MEDECIN et l'utilisateur est actif
     */
    public boolean estMedecin() {
        return this.role == RoleUtilisateur.MEDECIN && this.actif;
    }

    /**
     * Vérifie si l'utilisateur est patient
     * 
     * @return true si le rôle est PATIENT et l'utilisateur est actif
     */
    public boolean estPatient() {
        return this.role == RoleUtilisateur.PATIENT && this.actif;
    }

    /**
     * Convertit l'utilisateur en format lisible pour affichage
     * 
     * @return une représentation textuelle simple
     */
    public String getDisplayName() {
        return this.getNomComplet() + " [" + this.role.getLibelle() + "]";
    }

    // ============ GETTERS ET SETTERS ============

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RoleUtilisateur getRole() {
        return role;
    }

    public void setRole(RoleUtilisateur role) {
        this.role = role;
    }

    public Boolean isActif() {
        return actif;
    }

    public Boolean getActif() {
        return actif;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public LocalDateTime getDateDerniereConnexion() {
        return dateDerniereConnexion;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getNombreTentativesConnexion() {
        return nombreTentativesConnexion;
    }

    public void setNombreTentativesConnexion(Integer nombreTentativesConnexion) {
        this.nombreTentativesConnexion = nombreTentativesConnexion;
    }

    public Long getVersion() {
        return version;
    }

    // ============ EQUALS, HASHCODE, TOSTRING ============

    /**
     * Méthode equals pour comparaison d'utilisateurs
     * Basée sur l'ID (si présent) ou l'email (unique)
     * 
     * @param o l'objet à comparer
     * @return true si les deux objets représentent le même utilisateur
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Utilisateur utilisateur = (Utilisateur) o;

        // Comparaison par ID si les deux ont un ID
        if (this.id != null && utilisateur.id != null) {
            return Objects.equals(this.id, utilisateur.id);
        }

        // Sinon, comparaison par email (unique en base)
        return Objects.equals(this.email, utilisateur.email);
    }

    /**
     * Méthode hashCode pour utilisation en collections (Set, HashMap, etc.)
     * Cohérente avec equals()
     * 
     * @return le code de hash basé sur l'ID ou l'email
     */
    @Override
    public int hashCode() {
        // Si l'ID existe, l'utiliser
        if (this.id != null) {
            return Objects.hash(this.id);
        }
        // Sinon, utiliser l'email (qui est unique en base)
        return Objects.hash(this.email);
    }

    /**
     * Méthode toString pour affichage lisible et debugging
     * Affiche les informations principales sans données sensibles
     * 
     * @return une représentation textuelle de l'utilisateur
     */
    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", role=" + (role != null ? role.getLibelle() : "N/A") +
                ", actif=" + actif +
                ", dateCreation=" + dateCreation +
                ", dateModification=" + dateModification +
                ", dateDerniereConnexion=" + dateDerniereConnexion +
                '}';
    }

    /**
     * Retourne une représentation détaillée de l'utilisateur (pour logging/audit)
     * 
     * @return une chaîne avec tous les détails (sans le hash du mot de passe)
     */
    public String toDetailedString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", telephone='" + telephone + '\'' +
                ", adresse='" + adresse + '\'' +
                ", role=" + (role != null ? role.getLibelle() : "N/A") +
                ", actif=" + actif +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", dateCreation=" + dateCreation +
                ", dateModification=" + dateModification +
                ", dateDerniereConnexion=" + dateDerniereConnexion +
                ", nombreTentativesConnexion=" + nombreTentativesConnexion +
                ", version=" + version +
                '}';
    }
}