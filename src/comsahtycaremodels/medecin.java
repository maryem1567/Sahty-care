package comsahtycaremodels;
import java.util.ArrayList;
import java.util.List;

public class Medecin extends Utilisateur {
    private String specialite;
    private boolean disponible;
    private List<RendezVous> rendezVousList;

    public Medecin() {
        this.rendezVousList = new ArrayList<>();
        this.disponible = true;
    }

    public Medecin(int id, String nom, String specialite) {
        super(id, nom);
        this.specialite = specialite;
        this.disponible = true;
        this.rendezVousList = new ArrayList<>();
    }

    public Medecin(String nom, String prenom, String email, String username, String password, String specialite) {
        super(nom, prenom, email, username, password, RoleUtilisateur.MEDECIN);
        this.specialite = specialite;
        this.disponible = true;
        this.rendezVousList = new ArrayList<>();
    }

    public void consulterPlanning() {
        System.out.println("=== Planning du Dr. " + getNom() + " (" + specialite + ") ===");
        System.out.println("Nombre de rendez-vous: " + rendezVousList.size());
    }

    public void ajouterRendezVous(RendezVous rdv) {
        if (rdv != null) this.rendezVousList.add(rdv);
    }

    public String getSpecialite() { return specialite; } 
    public void setSpecialite(String s) { this.specialite = s; }
    public boolean isDisponible() { return disponible; } 
    public void setDisponible(boolean d) { this.disponible = d; }
}