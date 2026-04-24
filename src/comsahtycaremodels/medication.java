package com.sahtycare.models;

public class Medication {
    private String nom;
    private String dosage;

    public Medication(String nom, String dosage) {
        this.nom = nom;
        this.dosage = dosage;
    }

    public String getNom() { return nom; }
    public String getDosage() { return dosage; }
}