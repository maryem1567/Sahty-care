package comsahtycaremodels;

public class UtilisateurExamples {
    public static void main(String[] args) {
        Utilisateur u = new Utilisateur("Dupont", "Jean", "jean@test.com", "jdupont", "pass123", RoleUtilisateur.MEDECIN);
        System.out.println("Nom affiché: " + u.getDisplayName());
        System.out.println("Initiales: " + u.getInitiales());
        System.out.println("Est admin ? " + u.getRole().isAdmin());
    }
}