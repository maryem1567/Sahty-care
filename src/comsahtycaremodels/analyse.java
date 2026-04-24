package com.sahtycare.models;

public class Analyse {
    private String resultat;

    public Analyse() {}

    public Analyse(String resultat) {
        this.resultat = resultat;
    }

    public void ajouterResultat() {
        System.out.println("Résultat d'analyse ajouté : " + resultat);
    }

    public String getResultat() { return resultat; }
    public void setResultat(String resultat) { this.resultat = resultat; }
}