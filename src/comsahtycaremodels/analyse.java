package com.sahtycare.models;

import java.time.LocalDate;

public class Analyse {
    private int id;
    private String typeAnalyse;
    private LocalDate dateCreation;
    private Patient patient;
    private String statut;
    private String resultat;

    public Analyse() {}

    public Analyse(int id, String typeAnalyse, LocalDate dateCreation, Patient patient, String statut, String resultat) {
        this.id = id;
        this.typeAnalyse = typeAnalyse;
        this.dateCreation = dateCreation;
        this.patient = patient;
        this.statut = statut;
        this.resultat = resultat;
    }

    public void ajouterResultat(String nouveauResultat) {
        this.resultat = nouveauResultat;
        this.statut = "Terminé";
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTypeAnalyse() { return typeAnalyse; }
    public void setTypeAnalyse(String typeAnalyse) { this.typeAnalyse = typeAnalyse; }

    public LocalDate getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getResultat() { return resultat; }
    public void setResultat(String resultat) { this.resultat = resultat; }
}