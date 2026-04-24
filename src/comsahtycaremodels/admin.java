package comsahtycaremodels;

public class Admin extends Utilisateur {
    private int niveauAcces;

    public Admin() {}

    public Admin(int id, String nom, int niveauAcces) {
        super(id, nom);
        this.niveauAcces = niveauAcces;
        this.setRole(RoleUtilisateur.ADMINISTRATEUR);
    }

    public boolean gererUtilisateur(Utilisateur cible, String action) {
        if (this.niveauAcces <= 1) {
            System.out.println("Accès refusé: niveau insuffisant.");
            return false;
        }
        if (action.equalsIgnoreCase("bloquer")) {
            cible.setActif(false);
            System.out.println("L'admin a bloqué " + cible.getNom());
            return true;
        }
        return false;
    }

    public int getNiveauAcces() { return niveauAcces; }
    public void setNiveauAcces(int n) { this.niveauAcces = n; }
}