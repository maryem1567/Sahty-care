package com.sahtycare.models;

public class Admin extends Utilisateur {
    private int niveauAcces;

    public Admin() {}

    public Admin(int id, String nom, int niveauAcces) {
        super(id, nom);
        this.niveauAcces = niveauAcces;
    }

    public void gererUtilisateur() {
        System.out.println("L'admin " + getNom() + " gère les comptes utilisateurs.");
    }

    public int getNiveauAcces() { return niveauAcces; }
    public void setNiveauAcces(int niveauAcces) { this.niveauAcces = niveauAcces; }
}