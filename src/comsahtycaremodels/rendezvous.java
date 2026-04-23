package com.sahtycare.models;

public class RendezVous {
    private String date;

    public RendezVous() {}

    public RendezVous(String date) {
        this.date = date;
    }

    public void confirmerRdv() {
        System.out.println("Le rendez-vous du " + date + " est confirmé.");
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}