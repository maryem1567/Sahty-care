package comsahtycaremodels;
import java.time.LocalDateTime;

public class DossierMedical {
    private int id;
    private String antecedents;
    private String groupeSanguin;
    private LocalDateTime updatedAt;

    public DossierMedical() { 
        this.updatedAt = LocalDateTime.now(); 
    }

    public void mettreAJour(String nouveauxAntecedents) {
        this.antecedents = nouveauxAntecedents;
        this.updatedAt = LocalDateTime.now();
        System.out.println("Dossier mis à jour.");
    }
    
    public void setGroupeSanguin(String g) { 
        this.groupeSanguin = g; 
    }

    // --- GETTERS ---
    public int getId() { return id; }
    public String getAntecedents() { return antecedents; }
    public String getGroupeSanguin() { return groupeSanguin; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}