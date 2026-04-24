package com.sahtycare.examples;

import com.sahtycare.models.Utilisateur;
import com.sahtycare.models.RoleUtilisateur;

/**
 * Exemples d'utilisation de la classe Utilisateur refactorisée
 * Démontre les principales fonctionnalités
 */
public class UtilisateurExamples {

    /**
     * Exemple 1 : Création et initialisation d'utilisateur
     */
    public static void exemple1_CreationUtilisateur() {
        // Créer un nouvel utilisateur
        Utilisateur utilisateur = new Utilisateur(
            "Dupont",
            "Jean",
            "jean.dupont@example.com",
            "jdupont",
            "SecurePassword123",
            RoleUtilisateur.MEDECIN
        );

        // Ajouter des informations supplémentaires
        utilisateur.setTelephone("+33612345678");
        utilisateur.setAdresse("123 Rue de la Paix, 75000 Paris");

        System.out.println("Utilisateur créé:");
        System.out.println(utilisateur.getDisplayName());
        System.out.println("Initiales: " + utilisateur.getInitiales()); // JD
    }

    /**
     * Exemple 2 : Authentification et connexion sécurisée
     */
    public static void exemple2_Authentification() {
        Utilisateur utilisateur = new Utilisateur(
            "Dupont",
            "Jean",
            "jean.dupont@example.com",
            "jdupont",
            "SecurePassword123",
            RoleUtilisateur.MEDECIN
        );

        // Connexion réussie
        if (utilisateur.seConnecter("SecurePassword123")) {
            System.out.println("✓ Connexion réussie");
        }

        // Vérification de la dernière connexion
        System.out.println("Dernière connexion: " + utilisateur.getDateDerniereConnexion());

        // Connexion échouée
        if (!utilisateur.seConnecter("MauvaisMotDePasse")) {
            System.out.println("✗ Mot de passe incorrect");
            System.out.println("Tentatives: " + utilisateur.getNombreTentativesConnexion());
        }
    }

    /**
     * Exemple 3 : Changement de mot de passe
     */
    public static void exemple3_ChangerMotdepasse() {
        Utilisateur utilisateur = new Utilisateur(
            "Dupont",
            "Jean",
            "jean.dupont@example.com",
            "jdupont",
            "AncienMotDePasse123",
            RoleUtilisateur.MEDECIN
        );

        // Changement de mot de passe réussi
        if (utilisateur.changerMotdepasse("AncienMotDePasse123", "NouveauMotDePasse456")) {
            System.out.println("✓ Mot de passe changé avec succès");
        }

        // Vérification du nouveau mot de passe
        if (utilisateur.verifierMotdepasse("NouveauMotDePasse456")) {
            System.out.println("✓ Le nouveau mot de passe est correct");
        }

        // L'ancien mot de passe ne fonctionne plus
        if (!utilisateur.verifierMotdepasse("AncienMotDePasse123")) {
            System.out.println("✓ L'ancien mot de passe ne fonctionne plus");
        }
    }

    /**
     * Exemple 4 : Gestion des rôles et permissions
     */
    public static void exemple4_RolesEtPermissions() {
        // Créer différents utilisateurs avec des rôles
        Utilisateur admin = new Utilisateur("Durand", "Marie", "marie@example.com", "mdurand", "Pass123", RoleUtilisateur.ADMINISTRATEUR);
        Utilisateur medecin = new Utilisateur("Dupont", "Jean", "jean@example.com", "jdupont", "Pass123", RoleUtilisateur.MEDECIN);
        Utilisateur patient = new Utilisateur("Martin", "Pierre", "pierre@example.com", "pmartin", "Pass123", RoleUtilisateur.PATIENT);

        // Vérifier les rôles
        System.out.println("Admin est admin? " + admin.estAdmin());       // true
        System.out.println("Médecin est admin? " + medecin.estAdmin()); // false
        System.out.println("Médecin est médecin? " + medecin.estMedecin()); // true
        System.out.println("Patient est patient? " + patient.estPatient()); // true

        // Vérifier les permissions spécifiques
        System.out.println("Admin a permission ADMINISTRATEUR? " + admin.aLaPermission(RoleUtilisateur.ADMINISTRATEUR)); // true
        System.out.println("Patient a permission MEDECIN? " + patient.aLaPermission(RoleUtilisateur.MEDECIN)); // false
    }

    /**
     * Exemple 5 : Désactivation d'utilisateur (suppression logique)
     */
    public static void exemple5_DesactiverUtilisateur() {
        Utilisateur utilisateur = new Utilisateur(
            "Dupont",
            "Jean",
            "jean.dupont@example.com",
            "jdupont",
            "SecurePassword123",
            RoleUtilisateur.MEDECIN
        );

        System.out.println("Avant désactivation:");
        System.out.println("Actif? " + utilisateur.isActif());
        System.out.println("Est médecin? " + utilisateur.estMedecin());

        // Désactiver l'utilisateur
        utilisateur.desactiver();

        System.out.println("\nAprès désactivation:");
        System.out.println("Actif? " + utilisateur.isActif());
        System.out.println("Est médecin? " + utilisateur.estMedecin()); // false (pas actif)

        // Tentative de connexion avec compte désactivé
        if (!utilisateur.seConnecter("SecurePassword123")) {
            System.out.println("✗ Impossible de se connecter: compte désactivé");
        }
    }

    /**
     * Exemple 6 : Gestion des tentatives de connexion (prévention brute force)
     */
    public static void exemple6_GestionTentativesConnexion() {
        Utilisateur utilisateur = new Utilisateur(
            "Dupont",
            "Jean",
            "jean.dupont@example.com",
            "jdupont",
            "SecurePassword123",
            RoleUtilisateur.MEDECIN
        );

        // Simuler plusieurs tentatives échouées
        for (int i = 0; i < 5; i++) {
            utilisateur.seConnecter("MauvaisMotDePasse");
            System.out.println("Tentative échouée " + (i + 1) + ": " + utilisateur.getNombreTentativesConnexion() + " tentatives");

            // Vérifier si le compte est bloqué
            if (utilisateur.estBloqueParTentatives(5)) {
                System.out.println("⚠️ Compte bloqué! Trop de tentatives de connexion.");
                break;
            }
        }

        // Réinitialiser les tentatives après une action administrative
        utilisateur.reinitialiserTentativesConnexion();
        System.out.println("Tentatives réinitialisées: " + utilisateur.getNombreTentativesConnexion());
    }

    /**
     * Exemple 7 : Comparaison d'utilisateurs (equals/hashCode)
     */
    public static void exemple7_ComparaisonUtilisateurs() {
        // Créer des utilisateurs
        Utilisateur utilisateur1 = new Utilisateur("Dupont", "Jean", "jean@example.com", "jdupont", "Pass123", RoleUtilisateur.MEDECIN);
        utilisateur1.setId(1L); // Simuler un ID persisté

        Utilisateur utilisateur2 = new Utilisateur("Dupont", "Jean", "jean@example.com", "jdupont", "Pass123", RoleUtilisateur.MEDECIN);
        utilisateur2.setId(1L); // Même ID

        Utilisateur utilisateur3 = new Utilisateur("Martin", "Pierre", "pierre@example.com", "pmartin", "Pass123", RoleUtilisateur.PATIENT);
        utilisateur3.setId(2L); // ID différent

        // Comparaison par ID
        System.out.println("utilisateur1.equals(utilisateur2)? " + utilisateur1.equals(utilisateur2)); // true
        System.out.println("utilisateur1.equals(utilisateur3)? " + utilisateur1.equals(utilisateur3)); // false

        // Utilisation en collections
        java.util.Set<Utilisateur> ensemble = new java.util.HashSet<>();
        ensemble.add(utilisateur1);
        ensemble.add(utilisateur2); // Ne sera pas ajouté (même ID)
        ensemble.add(utilisateur3); // Sera ajouté

        System.out.println("Nombre d'utilisateurs uniques: " + ensemble.size()); // 2
    }

    /**
     * Exemple 8 : Affichage et toString
     */
    public static void exemple8_Affichage() {
        Utilisateur utilisateur = new Utilisateur(
            "Dupont",
            "Jean",
            "jean.dupont@example.com",
            "jdupont",
            "SecurePassword123",
            RoleUtilisateur.MEDECIN
        );

        utilisateur.setTelephone("+33612345678");
        utilisateur.setAdresse("123 Rue de la Paix, 75000 Paris");

        // toString() concis
        System.out.println("Format court:");
        System.out.println(utilisateur.toString());

        // toString() détaillé (pour audit/logging)
        System.out.println("\nFormat détaillé (audit):");
        System.out.println(utilisateur.toDetailedString());

        // Affichage pour interface
        System.out.println("\nFormat affichage:");
        System.out.println(utilisateur.getDisplayName()); // Jean Dupont [Médecin]
    }

    /**
     * Exemple 9 : Audit et traçabilité
     */
    public static void exemple9_AuditTracabilite() {
        Utilisateur utilisateur = new Utilisateur(
            "Dupont",
            "Jean",
            "jean.dupont@example.com",
            "jdupont",
            "SecurePassword123",
            RoleUtilisateur.MEDECIN
        );

        // Définir qui a créé/modifié
        utilisateur.setCreatedBy("admin_user");
        utilisateur.setUpdatedBy("admin_user");

        System.out.println("Informations d'audit:");
        System.out.println("Créé par: " + utilisateur.getCreatedBy());
        System.out.println("Créé le: " + utilisateur.getDateCreation());
        System.out.println("Modifié par: " + utilisateur.getUpdatedBy());
        System.out.println("Modifié le: " + utilisateur.getDateModification());
    }

    /**
     * Exemple 10 : Énumération RoleUtilisateur
     */
    public static void exemple10_RolesUtilisateur() {
        // Afficher tous les rôles disponibles
        System.out.println("Rôles disponibles:");
        for (String permission : RoleUtilisateur.getPermissions()) {
            System.out.println("- " + permission);
        }

        // Itérer sur les rôles
        for (RoleUtilisateur role : RoleUtilisateur.values()) {
            System.out.println("\nRôle: " + role.name());
            System.out.println("  Libellé: " + role.getLibelle());
            System.out.println("  Description: " + role.getDescription());
            System.out.println("  Est admin: " + role.isAdmin());
        }
    }

    /**
     * Main pour exécuter les exemples
     */
    public static void main(String[] args) {
        System.out.println("=== EXEMPLES D'UTILISATION DE LA CLASSE UTILISATEUR ===\n");

        System.out.println("--- Exemple 1: Création ---");
        exemple1_CreationUtilisateur();

        System.out.println("\n--- Exemple 2: Authentification ---");
        exemple2_Authentification();

        System.out.println("\n--- Exemple 3: Changement de mot de passe ---");
        exemple3_ChangerMotdepasse();

        System.out.println("\n--- Exemple 4: Rôles et permissions ---");
        exemple4_RolesEtPermissions();

        System.out.println("\n--- Exemple 5: Désactivation ---");
        exemple5_DesactiverUtilisateur();

        System.out.println("\n--- Exemple 6: Tentatives de connexion ---");
        exemple6_GestionTentativesConnexion();

        System.out.println("\n--- Exemple 7: Comparaison d'utilisateurs ---");
        exemple7_ComparaisonUtilisateurs();

        System.out.println("\n--- Exemple 8: Affichage ---");
        exemple8_Affichage();

        System.out.println("\n--- Exemple 9: Audit et traçabilité ---");
        exemple9_AuditTracabilite();

        System.out.println("\n--- Exemple 10: Énumération de rôles ---");
        exemple10_RolesUtilisateur();
    }
}
