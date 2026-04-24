package com.sahtycare.examples;

import com.sahtycare.models.Medecin;
import com.sahtycare.models.Patient;
import com.sahtycare.models.RendezVous;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Exemples d'utilisation de la classe Medecin refactorisée
 * Démontre l'intégration Spring Boot avec JPA, validation et Lombok
 */
public class MedecinExamples {

    /**
     * Exemple 1 : Créer un médecin
     */
    public static void exemple1_CreerMedecin() {
        System.out.println("\n=== Exemple 1: Créer un Médecin ===");

        // Créer un médecin avec le constructeur simplifié
        Medecin medecin = new Medecin(
            "Dupont",
            "Jean",
            "jean.dupont@example.com",
            "jdupont",
            "SecurePassword123",
            "Cardiologie"
        );

        // Ajouter des informations supplémentaires
        medecin.setNumeroOrdre("12345678");
        medecin.setTarificationConsultations(new BigDecimal("150.00"));
        medecin.setTelephone("+33612345678");
        medecin.setAdresse("123 Rue de la Paix, 75000 Paris");

        System.out.println(medecin.afficherProfil());
    }

    /**
     * Exemple 2 : Créer un médecin avec le Builder Lombok
     */
    public static void exemple2_CreerMedecinAvecBuilder() {
        System.out.println("\n=== Exemple 2: Créer un Médecin avec Builder ===");

        Medecin medecin = Medecin.builder()
            .nom("Martin")
            .prenom("Marie")
            .email("marie.martin@example.com")
            .username("mmartin")
            .specialite("Pneumologie")
            .numeroOrdre("87654321")
            .tarificationConsultations(new BigDecimal("180.00"))
            .disponible(true)
            .nombrePatients(0)
            .dateDebutExercice(LocalDateTime.now())
            .build();

        System.out.println("Médecin créée avec Builder:");
        System.out.println(medecin.getDisplayNameMedecin());
    }

    /**
     * Exemple 3 : Créer un patient
     */
    public static void exemple3_CreerPatient() {
        System.out.println("\n=== Exemple 3: Créer un Patient ===");

        Patient patient = new Patient(
            "Durand",
            "Pierre",
            "pierre.durand@example.com",
            "pdurand",
            "SecurePassword123",
            "DOSS001",
            LocalDate.of(1985, 3, 15)
        );

        patient.setGroupeSanguin("O+");
        patient.setAllergies("Pénicilline");
        patient.setPoidsKg(new BigDecimal("75.5"));
        patient.setTailleCm(180);

        System.out.println(patient.afficherProfilMedical());
    }

    /**
     * Exemple 4 : Créer un rendez-vous
     */
    public static void exemple4_CreerRendezVous() {
        System.out.println("\n=== Exemple 4: Créer un Rendez-vous ===");

        Medecin medecin = new Medecin("Dupont", "Jean", "jean@example.com", "jdupont", "Pass123", "Cardiologie");
        Patient patient = new Patient("Durand", "Pierre", "pierre@example.com", "pdurand", "Pass123", "DOSS001", LocalDate.of(1985, 3, 15));

        RendezVous rendezVous = new RendezVous(
            LocalDateTime.now().plusDays(7),
            "Consultation de suivi cardiaque",
            medecin,
            patient
        );

        rendezVous.setDureeMinutes(45);
        rendezVous.setDescription("Suivi de la tension artérielle et du cœur");
        rendezVous.setLieu("Cabinet cardiologue, 123 Rue de la Paix");

        System.out.println("Rendez-vous créé:");
        System.out.println(rendezVous.toDetailedString());
    }

    /**
     * Exemple 5 : Ajouter des rendez-vous au médecin et au patient
     */
    public static void exemple5_GestionRendezVous() {
        System.out.println("\n=== Exemple 5: Gestion des Rendez-vous ===");

        Medecin medecin = new Medecin("Dupont", "Jean", "jean@example.com", "jdupont", "Pass123", "Cardiologie");
        Patient patient = new Patient("Durand", "Pierre", "pierre@example.com", "pdurand", "Pass123", "DOSS001", LocalDate.of(1985, 3, 15));

        // Créer plusieurs rendez-vous
        RendezVous rdv1 = new RendezVous(
            LocalDateTime.now().plusDays(7),
            "Consultation initiale",
            medecin,
            patient
        );

        RendezVous rdv2 = new RendezVous(
            LocalDateTime.now().plusDays(30),
            "Suivi post-consultation",
            medecin,
            patient
        );

        // Ajouter les rendez-vous
        medecin.ajouterRendezVous(rdv1);
        medecin.ajouterRendezVous(rdv2);
        patient.ajouterRendezVous(rdv1);
        patient.ajouterRendezVous(rdv2);

        System.out.println("Nombre de rendez-vous du médecin: " + medecin.getNombreRendezVous());
        System.out.println("Nombre de rendez-vous du patient: " + patient.getNombreRendezVous());

        // Afficher le planning du médecin
        medecin.consulterPlanning();
    }

    /**
     * Exemple 6 : Gestion de la disponibilité
     */
    public static void exemple6_DisponibilitesMedecin() {
        System.out.println("\n=== Exemple 6: Gestion de la Disponibilité ===");

        Medecin medecin = new Medecin("Dupont", "Jean", "jean@example.com", "jdupont", "Pass123", "Cardiologie");

        System.out.println("Médecin disponible? " + medecin.getDisponible());
        System.out.println("Peut accepter RDV? " + medecin.peutAccepterRendezVous());

        // Marquer comme indisponible
        medecin.marquerIndisponible();
        System.out.println("\nAprès indisponibilité:");
        System.out.println("Médecin disponible? " + medecin.getDisponible());
        System.out.println("Peut accepter RDV? " + medecin.peutAccepterRendezVous());

        // Rémarquer comme disponible
        medecin.marquerDisponible();
        System.out.println("\nAprès disponibilité:");
        System.out.println("Médecin disponible? " + medecin.getDisponible());
    }

    /**
     * Exemple 7 : Gestion des patients du médecin
     */
    public static void exemple7_GestionPatients() {
        System.out.println("\n=== Exemple 7: Gestion des Patients ===");

        Medecin medecin = new Medecin("Dupont", "Jean", "jean@example.com", "jdupont", "Pass123", "Cardiologie");

        System.out.println("Nombre initial de patients: " + medecin.getNombrePatients());

        // Ajouter des patients
        medecin.incrementerNombrePatients();
        medecin.incrementerNombrePatients();
        medecin.incrementerNombrePatients();

        System.out.println("Après ajout de 3 patients: " + medecin.getNombrePatients());

        // Retirer un patient
        medecin.decrementerNombrePatients();
        System.out.println("Après retrait d'1 patient: " + medecin.getNombrePatients());
    }

    /**
     * Exemple 8 : Calculs médicaux du patient (IMC, âge)
     */
    public static void exemple8_CalculsMedicaux() {
        System.out.println("\n=== Exemple 8: Calculs Médicaux ===");

        Patient patient = new Patient("Durand", "Pierre", "pierre@example.com", "pdurand", "Pass123", "DOSS001", LocalDate.of(1980, 5, 15));
        patient.setPoidsKg(new BigDecimal("75"));
        patient.setTailleCm(180);

        System.out.println("Âge du patient: " + patient.calculerAge() + " ans");
        System.out.println("IMC: " + String.format("%.2f", patient.calculerIMC()));
        System.out.println("Interprétation IMC: " + patient.getInterpretationIMC());
    }

    /**
     * Exemple 9 : Vérification des rendez-vous
     */
    public static void exemple9_VerificationRendezVous() {
        System.out.println("\n=== Exemple 9: Vérification des Rendez-vous ===");

        Medecin medecin = new Medecin("Dupont", "Jean", "jean@example.com", "jdupont", "Pass123", "Cardiologie");
        Patient patient = new Patient("Durand", "Pierre", "pierre@example.com", "pdurand", "Pass123", "DOSS001", LocalDate.of(1985, 3, 15));

        RendezVous rdv1 = new RendezVous(LocalDateTime.now().plusDays(7), "Consultation 1", medecin, patient);
        RendezVous rdv2 = new RendezVous(LocalDateTime.now().plusDays(14), "Consultation 2", medecin, patient);

        medecin.ajouterRendezVous(rdv1);
        medecin.ajouterRendezVous(rdv2);

        System.out.println("Rendez-vous en attente de vérification: " + medecin.getRendezVousEnAttente().size());

        // Vérifier un rendez-vous
        rdv1.verifier();
        System.out.println("Après vérification d'un RDV: " + medecin.getRendezVousEnAttente().size());

        // Vérifier tous les rendez-vous
        medecin.verifierTousLesRendezVous();
        System.out.println("Après vérification de tous: " + medecin.getRendezVousEnAttente().size());
    }

    /**
     * Exemple 10 : Historique du patient
     */
    public static void exemple10_HistoriquePatient() {
        System.out.println("\n=== Exemple 10: Historique du Patient ===");

        Medecin medecin = new Medecin("Dupont", "Jean", "jean@example.com", "jdupont", "Pass123", "Cardiologie");
        Patient patient = new Patient("Durand", "Pierre", "pierre@example.com", "pdurand", "Pass123", "DOSS001", LocalDate.of(1985, 3, 15));

        // Ajouter des rendez-vous futurs et passés
        RendezVous rdvFutur = new RendezVous(LocalDateTime.now().plusDays(7), "Consultation à venir", medecin, patient);
        RendezVous rdvPasse = new RendezVous(LocalDateTime.now().minusDays(7), "Consultation passée", medecin, patient);

        patient.ajouterRendezVous(rdvFutur);
        patient.ajouterRendezVous(rdvPasse);

        System.out.println("Total rendez-vous: " + patient.getNombreRendezVous());
        System.out.println("À venir: " + patient.getRendezVousAvenir().size());
        System.out.println("Passés: " + patient.getHistoriqueRendezVous().size());

        patient.afficherHistorique();
    }

    /**
     * Exemple 11 : Affichage complet
     */
    public static void exemple11_AffichageComplet() {
        System.out.println("\n=== Exemple 11: Affichage Complet ===");

        Medecin medecin = new Medecin("Dupont", "Jean", "jean@example.com", "jdupont", "Pass123", "Cardiologie");
        Patient patient = new Patient("Durand", "Pierre", "pierre@example.com", "pdurand", "Pass123", "DOSS001", LocalDate.of(1985, 3, 15));

        patient.setGroupeSanguin("O+");
        patient.setAllergies("Pénicilline");
        patient.setPoidsKg(new BigDecimal("75"));
        patient.setTailleCm(180);
        patient.setMedecinTraitant("Dr. Dupont");

        RendezVous rdv = new RendezVous(LocalDateTime.now().plusDays(7), "Consultation cardiaque", medecin, patient);
        rdv.setDureeMinutes(45);
        rdv.setDescription("Suivi complet");
        rdv.setLieu("Cabinet médical");

        medecin.ajouterRendezVous(rdv);
        patient.ajouterRendezVous(rdv);

        System.out.println("=== Profil du Médecin ===");
        System.out.println(medecin.afficherProfil());

        System.out.println("\n=== Profil Médical du Patient ===");
        System.out.println(patient.afficherProfilMedical());

        System.out.println("\n=== Rendez-vous Détaillé ===");
        System.out.println(rdv.toDetailedString());
    }

    /**
     * Exemple 12 : Utilisation avec toString Lombok
     */
    public static void exemple12_ToStringLombok() {
        System.out.println("\n=== Exemple 12: toString() Lombok ===");

        Medecin medecin = new Medecin("Dupont", "Jean", "jean@example.com", "jdupont", "Pass123", "Cardiologie");
        medecin.setNumeroOrdre("12345678");

        // toString() généré par Lombok (inclut les parents)
        System.out.println(medecin.toString());

        // getDisplayNameMedecin() personnalisé
        System.out.println("\nDisplay Name: " + medecin.getDisplayNameMedecin());
    }

    /**
     * Exemple 13 : Comparaison d'objets (equals/hashCode Lombok)
     */
    public static void exemple13_EqualsHashCode() {
        System.out.println("\n=== Exemple 13: equals/hashCode Lombok ===");

        Medecin medecin1 = new Medecin("Dupont", "Jean", "jean@example.com", "jdupont", "Pass123", "Cardiologie");
        Medecin medecin2 = new Medecin("Dupont", "Jean", "jean@example.com", "jdupont", "Pass123", "Cardiologie");

        System.out.println("medecin1.equals(medecin2)? " + medecin1.equals(medecin2));
        System.out.println("hashCode égaux? " + (medecin1.hashCode() == medecin2.hashCode()));

        // Utilisation en Set
        java.util.Set<Medecin> ensemble = new java.util.HashSet<>();
        ensemble.add(medecin1);
        ensemble.add(medecin2); // Même objet logique
        System.out.println("Taille du Set: " + ensemble.size());
    }

    /**
     * Main pour exécuter les exemples
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("EXEMPLES D'UTILISATION - MEDECIN");
        System.out.println("========================================");

        exemple1_CreerMedecin();
        exemple2_CreerMedecinAvecBuilder();
        exemple3_CreerPatient();
        exemple4_CreerRendezVous();
        exemple5_GestionRendezVous();
        exemple6_DisponibilitesMedecin();
        exemple7_GestionPatients();
        exemple8_CalculsMedicaux();
        exemple9_VerificationRendezVous();
        exemple10_HistoriquePatient();
        exemple11_AffichageComplet();
        exemple12_ToStringLombok();
        exemple13_EqualsHashCode();

        System.out.println("\n========================================");
        System.out.println("TOUS LES EXEMPLES EXÉCUTÉS");
        System.out.println("========================================");
    }
}
