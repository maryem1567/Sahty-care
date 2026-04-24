package com.sahtycare.models;

public class Prescription {
    private String medicaments;

    public Prescription() {}

    public Prescription(String medicaments) {
        this.medicaments = medicaments;
    }

    public void genererOrdonnance() {
        System.out.println("Ordonnance générée pour les médicaments : " + medicaments);
    }

    public String getMedicaments() { return medicaments; }
    public void setMedicaments(String medicaments) { this.medicaments = medicaments; }
}