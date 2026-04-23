package com.sahtycare.models;

public class Utilisateur {
    private int id;
    private String nom;

    public Utilisateur() {}

    public Utilisateur(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public boolean seConnecter() {
        System.out.println("Connexion réussie pour l'utilisateur : " + nom);
        return true;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}