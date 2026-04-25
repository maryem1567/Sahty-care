package comsahtycaremodels;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main - Application Console Interactive
 * Point d'entrée pour la démonstration du Sprint 2
 */
public class Main {
    
    // Listes en mémoire agissant comme notre base de données
    private static final List<Patient> patients = new ArrayList<>();
    private static final List<Medecin> medecins = new ArrayList<>();
    private static final List<Admin> admins = new ArrayList<>();
    private static final List<RendezVous> rendezVousList = new ArrayList<>();
    private static final List<Analyse> analyses = new ArrayList<>();
    
    private static final Scanner scanner = new Scanner(System.in);
    private static int idCounter = 1;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   🏥 BIENVENUE DANS SAHTYCARE - SPRINT 2 🏥");
        System.out.println("==================================================");
        
        boolean running = true;
        while (running) {
            afficherMenu();
            int choix = lireEntier();

            switch (choix) {
                case 1: ajouterPatient(); break;
                case 2: ajouterMedecin(); break;
                case 3: ajouterAdmin(); break;
                case 4: ajouterRendezVous(); break;
                case 5: ajouterAnalyse(); break;
                case 6: afficherBdd(); break;
                case 0: 
                    running = false; 
                    System.out.println("Fermeture de l'application. Merci d'avoir utilisé SahtyCare !");
                    break;
                default:
                    System.out.println("❌ Choix invalide. Veuillez réessayer.");
            }
        }
        scanner.close();
    }

    private static void afficherMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Ajouter un Patient (Feature BA2)");
        System.out.println("2. Ajouter un Médecin (Feature MS)");
        System.out.println("3. Ajouter un Administrateur (Feature HT)");
        System.out.println("4. Programmer un Rendez-vous (Liaison MS & BA2)");
        System.out.println("5. Saisir une Analyse (Feature BA1)");
        System.out.println("6. 👁️ Afficher toute la base de données");
        System.out.println("0. Quitter");
        System.out.print("👉 Votre choix : ");
    }

    // ==========================================
    // MÉTHODES D'AJOUT (Logique Métier Sprint 2)
    // ==========================================

    private static void ajouterPatient() {
        System.out.println("\n--- NOUVEAU PATIENT ---");
        System.out.print("Nom du patient : ");
        String nom = scanner.nextLine();
        System.out.print("Numéro de téléphone : ");
        String tel = scanner.nextLine();
        
        Patient p = new Patient(idCounter++, nom, tel);
        patients.add(p);
        System.out.println("✅ Patient ajouté avec succès ! (ID attribué: " + p.getId() + ")");
    }

    private static void ajouterMedecin() {
        System.out.println("\n--- NOUVEAU MÉDECIN ---");
        System.out.print("Nom du médecin : Dr. ");
        String nom = scanner.nextLine();
        System.out.print("Spécialité : ");
        String spec = scanner.nextLine();
        
        Medecin m = new Medecin(idCounter++, "Dr. " + nom, spec);
        medecins.add(m);
        System.out.println("✅ Médecin ajouté avec succès ! (ID attribué: " + m.getId() + ")");
    }

    private static void ajouterAdmin() {
        System.out.println("\n--- NOUVEL ADMIN ---");
        System.out.print("Nom de l'administrateur : ");
        String nom = scanner.nextLine();
        System.out.print("Niveau d'accès (1 à 3) : ");
        int niveau = lireEntier();
        
        Admin a = new Admin(idCounter++, nom, niveau);
        admins.add(a);
        System.out.println("✅ Admin ajouté avec succès ! (ID attribué: " + a.getId() + ")");
    }

    private static void ajouterRendezVous() {
        System.out.println("\n--- NOUVEAU RENDEZ-VOUS ---");
        if (patients.isEmpty() || medecins.isEmpty()) {
            System.out.println("❌ Erreur : Il faut au moins un Patient et un Médecin enregistrés dans le système.");
            return;
        }

        System.out.print("ID du Patient : ");
        int idPat = lireEntier();
        Patient p = trouverPatient(idPat);

        System.out.print("ID du Médecin : ");
        int idMed = lireEntier();
        Medecin m = trouverMedecin(idMed);

        if (p == null || m == null) {
            System.out.println("❌ Erreur : Patient ou Médecin introuvable.");
            return;
        }

        System.out.print("Motif de la consultation : ");
        String motif = scanner.nextLine();

        // On simule un RDV pour demain
        LocalDateTime dateRdv = LocalDateTime.now().plusDays(1);
        
        // Création du RDV
        RendezVous rdv = new RendezVous(idCounter++, p, dateRdv, motif, 60.0);
        rdv.setMedecin(m);
        rendezVousList.add(rdv);
        
        // Mise à jour des plannings (Liaison des classes)
        p.ajouterRendezVous(rdv);
        m.ajouterRendezVous(rdv);

        System.out.println("✅ Rendez-vous programmé et assigné avec succès !");
    }

    private static void ajouterAnalyse() {
        System.out.println("\n--- NOUVELLE ANALYSE ---");
        if (patients.isEmpty()) {
            System.out.println("❌ Erreur : Aucun patient enregistré.");
            return;
        }

        System.out.print("ID du Patient concerné : ");
        int idPat = lireEntier();
        Patient p = trouverPatient(idPat);

        if (p == null) {
            System.out.println("❌ Erreur : Patient introuvable.");
            return;
        }

        System.out.print("Type d'analyse (ex: Prise de sang, IRM) : ");
        String type = scanner.nextLine();
        System.out.print("Résultat : ");
        String resultat = scanner.nextLine();

        Analyse analyse = new Analyse(idCounter++, type, LocalDate.now(), p, "Terminé", resultat);
        analyses.add(analyse);

        System.out.println("✅ Analyse ajoutée au dossier du patient " + p.getNom() + " !");
    }

    // ==========================================
    // MÉTHODES UTILITAIRES
    // ==========================================

    private static void afficherBdd() {
        System.out.println("\n======== ÉTAT DE LA BASE DE DONNÉES ========");
        
        System.out.println("👥 PATIENTS (" + patients.size() + ") :");
        patients.forEach(p -> System.out.println("   [ID:" + p.getId() + "] " + p.getNom() + " - Tel: " + p.getTelephone()));
        
        System.out.println("\n⚕️ MÉDECINS (" + medecins.size() + ") :");
        medecins.forEach(m -> System.out.println("   [ID:" + m.getId() + "] " + m.getNom() + " - Spécialité: " + m.getSpecialite()));
        
        System.out.println("\n📅 RENDEZ-VOUS (" + rendezVousList.size() + ") :");
        rendezVousList.forEach(r -> System.out.println("   [ID:" + r.getId() + "] Patient: " + r.getPatient().getNom() + " | Médecin: " + r.getMedecin().getNom() + " | Statut: " + r.getStatut()));
        
        System.out.println("\n🔬 ANALYSES (" + analyses.size() + ") :");
        analyses.forEach(a -> System.out.println("   [ID:" + a.getId() + "] Patient: " + a.getPatient().getNom() + " | Type: " + a.getTypeAnalyse() + " | Résultat: " + a.getResultat()));
        
        System.out.println("============================================");
    }

    private static int lireEntier() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // Retourne -1 si l'utilisateur tape des lettres au lieu d'un chiffre
        }
    }

    private static Patient trouverPatient(int id) {
        return patients.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    private static Medecin trouverMedecin(int id) {
        return medecins.stream().filter(m -> m.getId() == id).findFirst().orElse(null);
    }
}