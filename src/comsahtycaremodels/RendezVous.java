package com.sahtycare.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Classe RendezVous - Entité JPA
 * 
 * Représente un rendez-vous médical dans le système avec :
 * - Relations Many-to-One vers Medecin et Patient
 * - Date et heure du rendez-vous
 * - Motif de consultation
 * - Statut de vérification
 * - Annotations Lombok pour réduire le boilerplate
 * 
 * @author Sahty Care
 * @version 1.0
 */
@Entity
@Table(
    name = "rendez_vous",
    indexes = {
        @Index(name = "idx_rendezvous_medecin", columnList = "medecin_id"),
        @Index(name = "idx_rendezvous_patient", columnList = "patient_id"),
        @Index(name = "idx_rendezvous_date", columnList = "date_rendezvous"),
        @Index(name = "idx_rendezvous_verified", columnList = "verifie")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RendezVous {

    // ============ ATTRIBUTS ============

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "La date du rendez-vous ne peut pas être null")
    @Future(message = "La date du rendez-vous doit être dans le futur")
    @Column(name = "date_rendezvous", nullable = false)
    private LocalDateTime dateRendezVous;

    @NotBlank(message = "Le motif ne peut pas être vide")
    @Size(min = 5, max = 500, message = "Le motif doit contenir entre 5 et 500 caractères")
    @Column(name = "motif", length = 500, nullable = false)
    private String motif;

    @Column(name = "description", length = 1000)
    @Size(max = 1000, message = "La description ne doit pas dépasser 1000 caractères")
    private String description;

    @Column(name = "verifie", nullable = false)
    private Boolean verifie = false;

    @Column(name = "lieu", length = 255)
    private String lieu;

    @Column(name = "duree_minutes", nullable = false)
    private Integer dureeMinutes = 30;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Version
    @Column(name = "version")
    private Long version;

    // ============ RELATIONS ============

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "medecin_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_rendezvous_medecin")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Medecin medecin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "patient_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_rendezvous_patient")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Patient patient;

    // ============ CONSTRUCTEURS SPÉCIALISÉS ============

    /**
     * Constructeur simplifié pour créer un rendez-vous
     */
    public RendezVous(LocalDateTime dateRendezVous, String motif, Medecin medecin, Patient patient) {
        this.dateRendezVous = dateRendezVous;
        this.motif = motif;
        this.medecin = medecin;
        this.patient = patient;
        this.verifie = false;
        this.dureeMinutes = 30;
        this.dateCreation = LocalDateTime.now();
    }

    /**
     * Constructeur avec tous les détails
     */
    public RendezVous(LocalDateTime dateRendezVous, String motif, String description,
                      Integer dureeMinutes, Medecin medecin, Patient patient) {
        this(dateRendezVous, motif, medecin, patient);
        this.description = description;
        this.dureeMinutes = dureeMinutes != null ? dureeMinutes : 30;
    }

    // ============ CALLBACKS JPA ============

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dateModification = LocalDateTime.now();
    }

    // ============ MÉTHODES MÉTIER ============

    /**
     * Marque le rendez-vous comme vérifié
     */
    public void verifier() {
        this.verifie = true;
        this.dateModification = LocalDateTime.now();
    }

    /**
     * Annule le rendez-vous (suppression logique)
     */
    public void annuler() {
        // Implémentation : marquer comme annulé
        // Ajouter un champ "statut" si nécessaire
        this.dateModification = LocalDateTime.now();
    }

    /**
     * Retourne le nom complet du médecin
     */
    public String getNomMedecin() {
        return this.medecin != null ? this.medecin.getNomComplet() : "N/A";
    }

    /**
     * Retourne la spécialité du médecin
     */
    public String getSpecialiteMedecin() {
        return this.medecin != null ? this.medecin.getSpecialite() : "N/A";
    }

    /**
     * Retourne le nom complet du patient
     */
    public String getNomPatient() {
        return this.patient != null ? this.patient.getNomComplet() : "N/A";
    }

    /**
     * Retourne l'heure de fin du rendez-vous
     */
    public LocalDateTime getHeureFinRendezVous() {
        if (this.dateRendezVous == null || this.dureeMinutes == null) {
            return null;
        }
        return this.dateRendezVous.plusMinutes(this.dureeMinutes);
    }

    /**
     * Vérifie si le rendez-vous est dans le futur
     */
    public boolean estFutur() {
        return this.dateRendezVous != null && this.dateRendezVous.isAfter(LocalDateTime.now());
    }

    /**
     * Vérifie si le rendez-vous est dans le passé
     */
    public boolean estPasse() {
        return this.dateRendezVous != null && this.dateRendezVous.isBefore(LocalDateTime.now());
    }

    /**
     * Retourne la description formatée du rendez-vous
     */
    @Override
    public String toString() {
        return String.format(
            "RendezVous{" +
            "id=%d, " +
            "date=%s, " +
            "medecin='%s', " +
            "patient='%s', " +
            "motif='%s', " +
            "verifie=%s" +
            "}",
            this.id,
            this.dateRendezVous,
            getNomMedecin(),
            getNomPatient(),
            this.motif,
            this.verifie
        );
    }

    /**
     * Retourne une description détaillée du rendez-vous
     */
    public String toDetailedString() {
        return String.format(
            "RendezVous DÉTAILLÉ{" +
            "%nID: %d" +
            "%nDate: %s - %s" +
            "%nMédecin: %s (%s)" +
            "%nPatient: %s" +
            "%nMotif: %s" +
            "%nDescription: %s" +
            "%nDurée: %d minutes" +
            "%nLieu: %s" +
            "%nVérifié: %s" +
            "%nCréé le: %s" +
            "%nModifié le: %s" +
            "%n}",
            this.id,
            this.dateRendezVous,
            getHeureFinRendezVous(),
            getNomMedecin(),
            getSpecialiteMedecin(),
            getNomPatient(),
            this.motif,
            this.description != null ? this.description : "N/A",
            this.dureeMinutes,
            this.lieu != null ? this.lieu : "N/A",
            this.verifie ? "Oui" : "Non",
            this.dateCreation,
            this.dateModification
        );
    }
}
