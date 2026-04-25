package comsahtycaremodels;

public enum RoleUtilisateur {
    ADMINISTRATEUR("Admin", "Accès complet", true),
    MEDECIN("Médecin", "Gestion", false),
    PATIENT("Patient", "Consultation", false);

    private final String libelle;
    private final String description;
    private final boolean estAdmin;

    RoleUtilisateur(String libelle, String description, boolean estAdmin) {
        this.libelle = libelle; this.description = description; this.estAdmin = estAdmin;
    }
    public String getLibelle() { return libelle; }
    public String getDescription() { return description; }
    public boolean isAdmin() { return estAdmin; }
}