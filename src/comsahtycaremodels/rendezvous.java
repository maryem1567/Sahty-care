package comsahtycaremodels;
import java.time.LocalDateTime;

public class RendezVous {
    private int id;
    private Patient patient;
    private Medecin medecin;
    private LocalDateTime date;
    private String statut;
    private String motif;
    private double prixDT;

    public RendezVous() {}

    // Constructeur utilisé par Main.java
    public RendezVous(int id, Patient patient, LocalDateTime date, String motif, double prixDT) {
        this.id = id; this.patient = patient; this.date = date; 
        this.motif = motif; this.prixDT = prixDT; this.statut = "EN_ATTENTE";
    }

    // Constructeur utilisé par MedecinExamples
    public RendezVous(LocalDateTime date, String motif, Medecin medecin, Patient patient) {
        this.date = date; this.motif = motif; this.medecin = medecin;
        this.patient = patient; this.statut = "EN_ATTENTE";
    }

    public void confirmerRdv() {
        this.statut = "CONFIRMÉ";
        System.out.println("Rendez-vous du " + date + " confirmé.");
    }

    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public LocalDateTime getDate() { return date; } public void setDate(LocalDateTime d) { this.date = d; }
    public String getStatut() { return statut; } public void setStatut(String s) { this.statut = s; }
    public Patient getPatient() { return patient; } public void setPatient(Patient p) { this.patient = p; }
    public Medecin getMedecin() { return medecin; } public void setMedecin(Medecin m) { this.medecin = m; }

    // --- GETTERS ---
    public String getMotif() { 
        return motif; 
    }
    
    // Note : j'ai mis "double" ici car c'est un prix. 
    // Si tu avais déclaré "private int prixDT;", remplace simplement "double" par "int" ci-dessous !
    public double getPrixDT() { 
        return prixDT; 
    }

    // --- SETTERS (optionnels, pour enlever les alertes "can be final") ---
    public void setMotif(String motif) { 
        this.motif = motif; 
    }
    
    public void setPrixDT(double prixDT) { 
        this.prixDT = prixDT; 
    }
}