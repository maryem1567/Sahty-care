package com.sahtycare.models;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== SahtyCare Sprint 1 : Démonstration V1 ===\n");

        // 1. Authentification (SC)
        System.out.println("--- 1. Authentification (SC) ---");
        Patient patient = new Patient(1, "Ali Ben Salah", "123456789");
        patient.sInscrire();
        patient.seConnecter();
        
        Medecin medecin = new Medecin(2, "Dr. Karray", "Cardiologie");
        System.out.println();

        // 2. Rendez-vous (MS)
        System.out.println("--- 2. Gestion des Rendez-vous (MS) ---");
        medecin.consulterPlanning();
        RendezVous rdv = new RendezVous("25 Avril 2026 à 10h00");
        rdv.confirmerRdv();
        System.out.println();

        // 3. Gestion des Patients (BA2)
        System.out.println("--- 3. Gestion des Patients (BA2) ---");
        DossierMedical dossier = new DossierMedical("Allergie à la pénicilline");
        dossier.mettreAJour();
        System.out.println();

        // 4. Analyses et Prescriptions (BA1)
        System.out.println("--- 4. Gestion des Analyses (BA1) ---");
        Analyse analyse = new Analyse("Glycémie normale (0.9 g/L)");
        analyse.ajouterResultat();
        Prescription ordonnance = new Prescription("Paracétamol 1g, Vitamine C");
        ordonnance.genererOrdonnance();
        System.out.println();

        // 5. Gestion des Utilisateurs (HT)
        System.out.println("--- 5. Tableau de bord Admin (HT) ---");
        Admin admin = new Admin(3, "SuperAdmin", 1);
        admin.seConnecter();
        admin.gererUtilisateur();
        
        System.out.println("\n=== Fin de la démonstration Sprint 1 ===");
    }
}