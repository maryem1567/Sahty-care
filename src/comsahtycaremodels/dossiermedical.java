package com.sahtycare.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DossierMedical {
    private static final Logger logger = LoggerFactory.getLogger(DossierMedical.class);

    @NotNull
    private String id = UUID.randomUUID().toString();

    private String antecedents;

    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

    @NotNull
    private List<String> allergies = new ArrayList<>();

    @NotNull
    private String groupeSanguin;

    public void mettreAJour(String nouveauxAntecedents) {
        this.antecedents = nouveauxAntecedents;
        this.updatedAt = LocalDateTime.now();
        logger.info("Dossier médical mis à jour pour l'ID {} : {}", id, nouveauxAntecedents);
    }
}