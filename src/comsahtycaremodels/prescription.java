package comsahtycaremodels;

import java.time.LocalDate;

public class Prescription {
    private int id;
    private LocalDate date; // Si tu avais utilisé "String" avant, remplace "LocalDate" par "String" partout
    private String nomMedecin;
    private String instructionsDosage;

    // Constructeur par défaut
    public Prescription() {
    }

    // Constructeur avec paramètres
    public Prescription(int id, LocalDate date, String nomMedecin, String instructionsDosage) {
        this.id = id;
        this.date = date;
        this.nomMedecin = nomMedecin;
        this.instructionsDosage = instructionsDosage;
    }

    // --- GETTERS (Pour lire les variables et enlever les avertissements "never read") ---
    public int getId() { 
        return id; 
    }
    
    public LocalDate getDate() { 
        return date; 
    }
    
    public String getNomMedecin() { 
        return nomMedecin; 
    }
    
    public String getInstructionsDosage() { 
        return instructionsDosage; 
    }

    // --- SETTERS (Pour modifier les variables et enlever les avertissements "can be final") ---
    public void setId(int id) { 
        this.id = id; 
    }
    
    public void setDate(LocalDate date) { 
        this.date = date; 
    }
    
    public void setNomMedecin(String nomMedecin) { 
        this.nomMedecin = nomMedecin; 
    }
    
    public void setInstructionsDosage(String instructionsDosage) { 
        this.instructionsDosage = instructionsDosage; 
    }
}