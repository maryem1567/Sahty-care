package com.sahtycare.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Medecin - Entité JPA héritant de Utilisateur
 * 
 * Représente un médecin dans le système avec :
 * - Héritage JPA de la classe Utilisateur (stratégie JOINED)
 * - Spécialité médicale
 * - Liste de rendez-vous
 * - Validation des champs
 * - Annotations Lombok pour réduire le boilerplate
 * 
 * @author Sahty Care
 * @version 2.0
 */
@Entity
@Table(
    name = "medecins",
    indexes = {
        @Index(name = "idx_medecin_utilisateur_id", columnList = "utilisateur_id", unique = true),
        @Index(name = "idx_medecin_specialite", columnList = "specialite"),
        @Index(name = "idx_medecin_disponible", columnList = "disponible")
    }
)
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "utilisateur_id")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = "rendezVous")
public class Medecin extends Utilisateur {

    // ============ ATTRIBUTS ============

    @NotBlank(message = "La spécialité ne peut pas être vide")
    @Size(min = 3, max = 100, message = "La spécialité doit contenir entre 3 et 100 caractères")
    @Column(name = "specialite", length = 100, nullable = false)
    private String specialite;

    @Positive(message = "Le numéro d'ordre doit être positif")
    @Column(name = "numero_ordre", unique = true)
    private String numeroOrdre;

    @DecimalMin(value = "0.0", inclusive = true, message = "La tarification doit être >= 0")
    @Column(name = "tarification_consultations", precision = 10, scale = 2)
    private java.math.BigDecimal tarificationConsultations;

    @Column(name = "disponible", nullable = false)
    private Boolean disponible = true;

    @Column(name = "nombre_patients", nullable = false)
    private Integer nombrePatients = 0;

    @Column(name = "date_debut_exercice")
    private LocalDateTime dateDebutExercice;

    // ============ RELATIONS ============

    @OneToMany(
        mappedBy = "medecin",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    @Builder.Default
    private List<RendezVous> rendezVous = new ArrayList<>();

    // ============ CONSTRUCTEURS SPÉCIALISÉS ============

    /**
     * Constructeur pour créer un médecin avec les informations principales
     */
    public Medecin(String nom, String prenom, String email, String username,
                   String motdepasse, String specialite) {
        super(nom, prenom, email, username, motdepasse, RoleUtilisateur.MEDECIN);
        this.specialite = specialite;
        this.disponible = true;
        this.nombrePatients = 0;
        this.dateDebutExercice = LocalDateTime.now();
    }

    /**
     * Constructeur avec toutes les informations
     */
    public Medecin(String nom, String prenom, String email, String username,
                   String motdepasse, String specialite, String numeroOrdre,
                   java.math.BigDecimal tarification) {
        this(nom, prenom, email, username, motdepasse, specialite);
        this.numeroOrdre = numeroOrdre;
        this.tarificationConsultations = tarification;
    }

    // ============ MÉTHODES MÉTIER ============

    /**
     * Ajoute un rendez-vous à la liste du médecin
     * 
     * @param rendezVous le rendez-vous à ajouter
     * @return true si l'ajout a réussi
     */
    public boolean ajouterRendezVous(RendezVous rendezVous) {
        if (rendezVous == null) {
            return false;
        }
        rendezVous.setMedecin(this);
        return this.rendezVous.add(rendezVous);
    }

    /**
     * Retire un rendez-vous de la liste du médecin
     * 
     * @param rendezVous le rendez-vous à retirer
     * @return true si le retrait a réussi
     */
    public boolean retirerRendezVous(RendezVous rendezVous) {
        if (rendezVous == null) {
            return false;
        }
        return this.rendezVous.remove(rendezVous);
    }

    /**
     * Retourne le nombre de rendez-vous du médecin
     * 
     * @return le nombre total de rendez-vous
     */
    public int getNombreRendezVous() {
        return this.rendezVous != null ? this.rendezVous.size() : 0;
    }

    /**
     * Retourne les rendez-vous non vérifiés
     * 
     * @return liste des rendez-vous en attente
     */
    public List<RendezVous> getRendezVousEnAttente() {
        if (this.rendezVous == null) {
            return new ArrayList<>();
        }
        return this.rendezVous.stream()
                .filter(rv -> !rv.getVerifie())
                .toList();
    }

    /**
     * Marque le médecin comme disponible
     */
    public void marquerDisponible() {
        this.disponible = true;
    }

    /**
     * Marque le médecin comme indisponible
     */
    public void marquerIndisponible() {
        this.disponible = false;
    }

    /**
     * Consulte le planning du médecin (affichage des rendez-vous)
     */
    public void consulterPlanning() {
        if (this.rendezVous == null || this.rendezVous.isEmpty()) {
            System.out.println("Aucun rendez-vous pour le Dr. " + getNomComplet());
            return;
        }

        System.out.println("\n=== Planning du Dr. " + getNomComplet() + " ===");
        System.out.println("Spécialité : " + this.specialite);
        System.out.println("Nombre de rendez-vous : " + getNombreRendezVous());

        for (RendezVous rv : this.rendezVous) {
            System.out.println("- " + rv);
        }
    }

    /**
     * Affiche les informations du médecin
     */
    public String afficherProfil() {
        return String.format(
            "Dr. %s %s%nSpécialité : %s%nNuméro d'ordre : %s%nDisponible : %s%nNombre de patients : %d",
            getNom(), getPrenom(),
            this.specialite,
            this.numeroOrdre != null ? this.numeroOrdre : "N/A",
            this.disponible ? "Oui" : "Non",
            this.nombrePatients
        );
    }

    /**
     * Incrémente le nombre de patients du médecin
     */
    public void incrementerNombrePatients() {
        if (this.nombrePatients == null) {
            this.nombrePatients = 0;
        }
        this.nombrePatients++;
    }

    /**
     * Décrémente le nombre de patients du médecin
     */
    public void decrementerNombrePatients() {
        if (this.nombrePatients != null && this.nombrePatients > 0) {
            this.nombrePatients--;
        }
    }

    /**
     * Vérifie si le médecin peut accepter un nouveau rendez-vous
     * 
     * @return true si disponible et actif
     */
    public boolean peutAccepterRendezVous() {
        return this.disponible && this.isActif();
    }

    /**
     * Marque tous les rendez-vous en attente comme vérifiés
     */
    public void verifierTousLesRendezVous() {
        if (this.rendezVous != null) {
            this.rendezVous.stream()
                    .filter(rv -> !rv.getVerifie())
                    .forEach(rv -> rv.setVerifie(true));
        }
    }

    /**
     * Retourne une description courte du médecin
     * 
     * @return format : "Dr. Nom [Spécialité]"
     */
    public String getDisplayNameMedecin() {
        return "Dr. " + getNomComplet() + " [" + this.specialite + "]";
    }
}