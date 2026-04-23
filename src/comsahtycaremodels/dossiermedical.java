package com.sahtycare.models;

public class DossierMedical {
    private String antecedents;

    public DossierMedical() {}

    public DossierMedical(String antecedents) {
        this.antecedents = antecedents;
    }

    public void mettreAJour() {
        System.out.println("Dossier médical mis à jour avec les antécédents : " + antecedents);
    }

    public String getAntecedents() { return antecedents; }
    public void setAntecedents(String antecedents) { this.antecedents = antecedents; }
}