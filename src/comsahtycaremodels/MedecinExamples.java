package comsahtycaremodels;

import java.time.LocalDateTime;

public class MedecinExamples {
    public static void main(String[] args) {
        Medecin medecin = new Medecin("Dupont", "Jean", "jean@test.com", "jdupont", "pass123", "Cardiologie");
        Patient patient = new Patient(1, "Martin", "0612345678");
        
        RendezVous rdv = new RendezVous(LocalDateTime.now().plusDays(1), "Suivi", medecin, patient);
        medecin.ajouterRendezVous(rdv);
        medecin.consulterPlanning();
    }
}