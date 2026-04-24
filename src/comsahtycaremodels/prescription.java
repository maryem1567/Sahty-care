package com.sahtycare.models;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Prescription {
    private int id;
    private LocalDate date;
    private String nomMedecin;
    private String instructionsDosage;
    private List<Medication> medicaments;

    public Prescription() {
        this.medicaments = new ArrayList<>();
    }

    public Prescription(int id, LocalDate date, String nomMedecin, String instructionsDosage, List<Medication> medicaments) {
        this.id = id;
        this.date = date;
        this.nomMedecin = nomMedecin;
        this.instructionsDosage = instructionsDosage;
        this.medicaments = medicaments != null ? medicaments : new ArrayList<>();
    }

    public String formaterPourImpression() {
        StringBuilder sb = new StringBuilder();
        sb.append("Prescription ID: ").append(id).append("\n");
        sb.append("Date: ").append(date).append("\n");
        sb.append("Médecin: ").append(nomMedecin).append("\n");
        sb.append("Instructions de dosage: ").append(instructionsDosage).append("\n");
        sb.append("Médicaments:\n");
        for (Medication med : medicaments) {
            sb.append("- ").append(med.getNom()).append(" : ").append(med.getDosage()).append("\n");
        }
        return sb.toString();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getNomMedecin() { return nomMedecin; }
    public void setNomMedecin(String nomMedecin) { this.nomMedecin = nomMedecin; }

    public String getInstructionsDosage() { return instructionsDosage; }
    public void setInstructionsDosage(String instructionsDosage) { this.instructionsDosage = instructionsDosage; }

    public List<Medication> getMedicaments() { return medicaments; }
    public void setMedicaments(List<Medication> medicaments) { this.medicaments = medicaments; }
}