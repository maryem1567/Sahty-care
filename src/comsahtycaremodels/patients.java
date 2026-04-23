package com.sahtycare.models;

public class Patient extends Utilisateur {
    private String numeroSecu;

    public Patient() {}

    public Patient(int id, String nom, String numeroSecu) {
        super(id, nom);
        this.numeroSecu = numeroSecu;
    }

    public void sInscrire() {
        System.out.println("Le patient " + getNom() + " s'est inscrit avec succès.");
    }

    public String getNumeroSecu() { return numeroSecu; }
    public void setNumeroSecu(String numeroSecu) { this.numeroSecu = numeroSecu; }
}