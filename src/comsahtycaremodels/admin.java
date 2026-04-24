package com.sahtycare.models;

public class Admin extends Utilisateur {
    private int niveauAcces;

    public Admin() {}

    public Admin(int id, String nom, int niveauAcces) {
        super(id, nom);
        this.niveauAcces = niveauAcces;
    }

    /**
     * Gère un utilisateur cible avec une action spécifique.
     * @param cible L'utilisateur à gérer
     * @param action L'action à effectuer ("bloquer" ou "supprimer")
     * @return true si l'action a été effectuée, false sinon
     */
    public boolean gererUtilisateur(Utilisateur cible, String action) {
        if (cible == null || action == null) {
            return false;
        }

        // Vérifier que l'admin a le niveau d'accès suffisant
        if (this.niveauAcces <= 1) {
            System.out.println("Accès refusé: niveau d'accès insuffisant (requis: > 1)");
            return false;
        }

        switch (action.toLowerCase()) {
            case "bloquer":
                return bloquerUtilisateur(cible);
            case "supprimer":
                return supprimerUtilisateur(cible);
            default:
                System.out.println("Action inconnue: " + action + ". Actions valides: bloquer, supprimer");
                return false;
        }
    }

    /**
     * Bloque un utilisateur cible.
     */
    private boolean bloquerUtilisateur(Utilisateur cible) {
        if (cible instanceof Admin) {
            System.out.println("Impossible de bloquer un autre administrateur.");
            return false;
        }
        System.out.println("L'admin " + getNom() + " a bloqué l'utilisateur " + cible.getNom());
        return true;
    }

    /**
     * Supprime un utilisateur cible.
     */
    private boolean supprimerUtilisateur(Utilisateur cible) {
        if (cible instanceof Admin) {
            System.out.println("Impossible de supprimer un autre administrateur.");
            return false;
        }
        System.out.println("L'admin " + getNom() + " a supprimé l'utilisateur " + cible.getNom());
        return true;
    }

    public int getNiveauAcces() { return niveauAcces; }
    public void setNiveauAcces(int niveauAcces) { this.niveauAcces = niveauAcces; }
}