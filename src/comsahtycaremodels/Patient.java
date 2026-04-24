package comsahtycaremodels;

import java.util.ArrayList;
import java.util.List;

public class Patient {
    private int id;
    private String nom;
    private String telephone;
    
    // ATTENTION ICI : le nom de la variable commence par un "r" minuscule !
    private final List<RendezVous> rendezVous = new ArrayList<>();

    public Patient() {}

    public Patient(int id, String nom, String telephone) {
        this.id = id;
        this.nom = nom;
        this.telephone = telephone;
    }

    public void ajouterRendezVous(RendezVous rdv) {
        if (rdv != null) {
            this.rendezVous.add(rdv); // Maintenant ça correspond parfaitement !
        }
    }

    // --- GETTERS & SETTERS ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
}