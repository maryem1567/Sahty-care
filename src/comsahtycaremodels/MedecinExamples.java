package comsahtycaremodels;

import java.time.LocalDateTime;

public class MedecinExamples {
    public static void main(String[] args) {
        Medecin medecin = new Medecin("Dupont", "Jean", "jean@test.com", "jdupont", "pass123", "Cardiologie");
        Patient patient = new Patient(1, "Martin", "0612345678");
        

// APRÈS : On ajoute la variable 'patient' en deuxième position
RendezVous rdv1 = new RendezVous(1, patient, LocalDateTime.now(), "Contrôle", 50.0);
        

        medecin.ajouterRendezVous(rdv1);
        medecin.consulterPlanning();
    }
}