package com.sahtycare.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Patient - Entité JPA héritant de Utilisateur
 * 
 * Représente un patient dans le système avec :
 * - Héritage JPA de la classe Utilisateur (stratégie JOINED)
 * - Informations médicales (groupe sanguin, allergies, maladies chroniques)
 * - Dossier médical
 * - Liste de rendez-vous
 * - Validation des champs
 * - Annotations Lombok pour réduire le boilerplate
 * 
 * @author Sahty Care
 * @version 1.0
 */
@Entity
@Table(
    name = "patients",
    indexes = {
        @Index(name = "idx_patient_utilisateur_id", columnList = "utilisateur_id", unique = true),
        @Index(name = "idx_patient_numero_dossier", columnList = "numero_dossier", unique = true),
        @Index(name = "idx_patient_groupe_sanguin", columnList = "groupe_sanguin")
    }
)
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "utilisateur_id")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = "rendezVous")
public class Patient extends Utilisateur {

    // ============ ATTRIBUTS ============

    @NotBlank(message = "Le numéro de dossier ne peut pas être vide")
    @Column(name = "numero_dossier", length = 50, nullable = false, unique = true)
    private String numeroDossier;

    @NotNull(message = "La date de naissance ne peut pas être null")
    @Past(message = "La date de naissance doit être dans le passé")
    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @Pattern(regexp = "^(O|A|B|AB)[+-]$", message = "Groupe sanguin invalide. Format : O+, O-, A+, A-, B+, B-, AB+, AB-")
    @Column(name = "groupe_sanguin", length = 3)
    private String groupeSanguin;

    @Column(name = "allergies", length = 500)
    private String allergies;

    @Column(name = "maladies_chroniques", length = 500)
    private String maladiesChroniques;

    @Column(name = "numero_secu", length = 15, unique = true)
    @Pattern(regexp = "^[0-9]{15}$|^$", message = "Numéro de sécurité sociale invalide")
    private String numeroSecu;

    @Column(name = "assurance", length = 100)
    private String assurance;

    @Column(name = "medecin_traitant", length = 100)
    private String medecinTraitant;

    @Column(name = "poids_kg", precision = 5, scale = 2)
    @Positive(message = "Le poids doit être positif")
    private java.math.BigDecimal poidsKg;

    @Column(name = "taille_cm")
    @Positive(message = "La taille doit être positive")
    private Integer tailleCm;

    @Column(name = "groupe_sanguin_compatible_avec")
    private String groupeSanguinCompatibleAvec;

    @Column(name = "derniere_visite_medicale")
    private LocalDateTime derniereVisiteMedicale;

    // ============ RELATIONS ============

    @OneToMany(
        mappedBy = "patient",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    @Builder.Default
    private List<RendezVous> rendezVous = new ArrayList<>();

    // ============ CONSTRUCTEURS SPÉCIALISÉS ============

    /**
     * Constructeur pour créer un patient avec les informations principales
     */
    public Patient(String nom, String prenom, String email, String username,
                   String motdepasse, String numeroDossier, LocalDate dateNaissance) {
        super(nom, prenom, email, username, motdepasse, RoleUtilisateur.PATIENT);
        this.numeroDossier = numeroDossier;
        this.dateNaissance = dateNaissance;
    }

    /**
     * Constructeur avec informations médicales complètes
     */
    public Patient(String nom, String prenom, String email, String username,
                   String motdepasse, String numeroDossier, LocalDate dateNaissance,
                   String groupeSanguin, String allergies) {
        this(nom, prenom, email, username, motdepasse, numeroDossier, dateNaissance);
        this.groupeSanguin = groupeSanguin;
        this.allergies = allergies;
    }

    // ============ MÉTHODES MÉTIER ============

    /**
     * Ajoute un rendez-vous à la liste du patient
     * 
     * @param rendezVous le rendez-vous à ajouter
     * @return true si l'ajout a réussi
     */
    public boolean ajouterRendezVous(RendezVous rendezVous) {
        if (rendezVous == null) {
            return false;
        }
        rendezVous.setPatient(this);
        return this.rendezVous.add(rendezVous);
    }

    /**
     * Retire un rendez-vous de la liste du patient
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
     * Retourne le nombre de rendez-vous du patient
     */
    public int getNombreRendezVous() {
        return this.rendezVous != null ? this.rendezVous.size() : 0;
    }

    /**
     * Retourne les rendez-vous à venir du patient
     */
    public List<RendezVous> getRendezVousAvenir() {
        if (this.rendezVous == null) {
            return new ArrayList<>();
        }
        return this.rendezVous.stream()
                .filter(RendezVous::estFutur)
                .toList();
    }

    /**
     * Retourne l'historique des rendez-vous passés
     */
    public List<RendezVous> getHistoriqueRendezVous() {
        if (this.rendezVous == null) {
            return new ArrayList<>();
        }
        return this.rendezVous.stream()
                .filter(RendezVous::estPasse)
                .toList();
    }

    /**
     * Calcule l'âge du patient
     */
    public int calculerAge() {
        if (this.dateNaissance == null) {
            return 0;
        }
        LocalDate today = LocalDate.now();
        return today.getYear() - this.dateNaissance.getYear() -
               (today.getMonthValue() < this.dateNaissance.getMonthValue() ||
                (today.getMonthValue() == this.dateNaissance.getMonthValue() &&
                 today.getDayOfMonth() < this.dateNaissance.getDayOfMonth()) ? 1 : 0);
    }

    /**
     * Calcule l'IMC (Indice de Masse Corporelle)
     */
    public Double calculerIMC() {
        if (this.poidsKg == null || this.tailleCm == null || this.tailleCm == 0) {
            return null;
        }
        double tailleMetre = this.tailleCm / 100.0;
        return this.poidsKg.doubleValue() / (tailleMetre * tailleMetre);
    }

    /**
     * Retourne l'interprétation de l'IMC
     */
    public String getInterpretationIMC() {
        Double imc = calculerIMC();
        if (imc == null) {
            return "IMC non calculable";
        }
        if (imc < 18.5) return "Poids insuffisant";
        if (imc < 25.0) return "Poids normal";
        if (imc < 30.0) return "Surpoids";
        return "Obésité";
    }

    /**
     * Met à jour la date de dernière visite médicale
     */
    public void mettreAJourDerniereVisiteMedicale() {
        this.derniereVisiteMedicale = LocalDateTime.now();
    }

    /**
     * Affiche les informations médicales du patient
     */
    public String afficherProfilMedical() {
        return String.format(
            "Patient: %s %s%nNuméro de dossier: %s%nDate de naissance: %s (Age: %d ans)%n" +
            "Groupe sanguin: %s%nTaille: %d cm | Poids: %.2f kg | IMC: %.2f (%s)%n" +
            "Allergies: %s%nMaladies chroniques: %s%nMédecin traitant: %s%n" +
            "Dernière visite: %s",
            getNom(), getPrenom(),
            this.numeroDossier,
            this.dateNaissance,
            calculerAge(),
            this.groupeSanguin != null ? this.groupeSanguin : "N/A",
            this.tailleCm != null ? this.tailleCm : 0,
            this.poidsKg != null ? this.poidsKg : 0,
            calculerIMC() != null ? calculerIMC() : 0,
            getInterpretationIMC(),
            this.allergies != null ? this.allergies : "Aucune",
            this.maladiesChroniques != null ? this.maladiesChroniques : "Aucune",
            this.medecinTraitant != null ? this.medecinTraitant : "N/A",
            this.derniereVisiteMedicale != null ? this.derniereVisiteMedicale : "N/A"
        );
    }

    /**
     * Affiche l'historique complet du patient
     */
    public void afficherHistorique() {
        System.out.println("\n=== Historique du Patient: " + getNomComplet() + " ===");
        System.out.println("Numéro de dossier: " + this.numeroDossier);
        System.out.println("Nombre total de rendez-vous: " + getNombreRendezVous());

        List<RendezVous> futurs = getRendezVousAvenir();
        System.out.println("\nRendez-vous à venir (" + futurs.size() + "):");
        futurs.forEach(rv -> System.out.println("  - " + rv));

        List<RendezVous> passes = getHistoriqueRendezVous();
        System.out.println("\nRendez-vous passés (" + passes.size() + "):");
        passes.forEach(rv -> System.out.println("  - " + rv));
    }

    /**
     * Retourne une description courte du patient
     */
    public String getDisplayNamePatient() {
        return getNomComplet() + " (Dossier: " + this.numeroDossier + ")";
    }
}
