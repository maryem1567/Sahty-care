package comsahtycaremodels;

import java.time.LocalDate;

public class Analyse {
    private int id;
    private String typeAnalyse;
    private LocalDate dateCreation;
    private Patient patient; 
    private String statut;
    private String resultat;

    public Analyse() {}

    public Analyse(int id, String typeAnalyse, LocalDate dateCreation, Patient patient, String statut, String resultat) {
        this.id = id;
        this.typeAnalyse = typeAnalyse;
        this.dateCreation = dateCreation;
        this.patient = patient;
        this.statut = statut;
        this.resultat = resultat;
    }

    public void ajouterResultat(String nouveauResultat) {
        this.resultat = nouveauResultat;
        this.statut = "Terminé";
        System.out.println("Résultat ajouté : " + nouveauResultat);
    }

    // --- GETTERS ---
    public int getId() { return id; }
    public String getTypeAnalyse() { return typeAnalyse; }
    public Patient getPatient() { return patient; }
    public String getResultat() { return resultat; }
    public String getStatut() { return statut; }
    public LocalDate getDateCreation() { return dateCreation; }
}