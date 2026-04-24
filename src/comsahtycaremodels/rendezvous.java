package com.sahtycare.models;

import java.time.LocalDateTime;

public class RendezVous {
    private int id;
    private int idPatient;
    private LocalDateTime date;
    private String statut;
    private String motif;
    private double prixDT;

    public RendezVous() {}

    public RendezVous(int id, int idPatient, LocalDateTime date, String motif, double prixDT) {
        this.id = id;
        this.idPatient = idPatient;
        this.date = date;
        this.motif = motif;
        this.prixDT = prixDT;
        this.statut = "EN_ATTENTE";
    }

    public void confirmerRdv() {
        this.statut = "CONFIRMÉ";
        System.out.println("Le rendez-vous du " + date + " a été confirmé. Statut: " + statut);
    }

    public void annulerRdv() {
        this.statut = "ANNULÉ";
    }

    public void completrerRdv() {
        this.statut = "COMPLÉTÉ";
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPatient() { return idPatient; }
    public void setIdPatient(int idPatient) { this.idPatient = idPatient; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public double getPrixDT() { return prixDT; }
    public void setPrixDT(double prixDT) { this.prixDT = prixDT; }

    @Override
    public String toString() {
        return "RendezVous{" +
                "id=" + id +
                ", idPatient=" + idPatient +
                ", date=" + date +
                ", statut='" + statut + '\'' +
                ", motif='" + motif + '\'' +
                ", prixDT=" + prixDT +
                '}';
    }
}