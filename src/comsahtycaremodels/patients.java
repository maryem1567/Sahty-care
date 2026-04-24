package com.sahtycare.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class Patient extends Utilisateur {
    private static final Logger logger = LoggerFactory.getLogger(Patient.class);

    @NotNull
    @Pattern(
        regexp = "^(1|2)(?:\\d{2})(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01])\\d{8}$",
        message = "Numéro de sécurité sociale invalide"
    )
    private String numeroSecu;

    @NotNull
    private LocalDate dateNaissance;

    @NotNull
    private Sexe sexe;

    public Patient() {}

    public Patient(int id, String nom, String numeroSecu, LocalDate dateNaissance, Sexe sexe) {
        super(id, nom);
        this.numeroSecu = numeroSecu;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
    }

    public void sInscrire() {
        logger.info("Le patient {} s'est inscrit avec succès.", getNom());
    }

    public enum Sexe {
        MASCULIN,
        FEMININ,
        AUTRE
    }
}