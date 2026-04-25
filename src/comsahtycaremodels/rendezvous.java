package comsahtycaremodels;

import java.time.LocalDateTime;

public class RendezVous {
    private int id;
    private LocalDateTime dateRendezVous;
    private String motif;
    private double prixDT;
    private String statut = "En attente";
    
    // Ajout des liaisons
    private Patient patient;
    private Medecin medecin;

    public RendezVous() {}

    // Constructeur mis à jour pour accepter le Patient
    public RendezVous(int id, Patient patient, LocalDateTime dateRendezVous, String motif, double prixDT) {
        this.id = id;
        this.patient = patient;
        this.dateRendezVous = dateRendezVous;
        this.motif = motif;
        this.prixDT = prixDT;
    }

    // --- GETTERS ---
    public int getId() { return id; }
    public LocalDateTime getDateRendezVous() { return dateRendezVous; }
    public String getMotif() { return motif; }
    public double getPrixDT() { return prixDT; }
    public String getStatut() { return statut; }
    public Patient getPatient() { return patient; }
    public Medecin getMedecin() { return medecin; }

    // --- SETTERS ---
    public void setId(int id) { this.id = id; }
    public void setDateRendezVous(LocalDateTime date) { this.dateRendezVous = date; }
    public void setMotif(String motif) { this.motif = motif; }
    public void setPrixDT(double prix) { this.prixDT = prix; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public void setMedecin(Medecin medecin) { this.medecin = medecin; }
}