package com.sahtycare.models;

public class Medecin extends Utilisateur {
    private String specialite;

    public Medecin() {}

    public Medecin(int id, String nom, String specialite) {
        super(id, nom);
        this.specialite = specialite;
    }

    public void consulterPlanning() {
        System.out.println("Affichage du planning pour le Dr. " + getNom());
    }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
}