package comsahtycaremodels;

public class Utilisateur {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String username;
    private String motDePasse;
    private String telephone;
    private String adresse;
    private RoleUtilisateur role;
    private boolean actif = true;

    public Utilisateur() {}

    public Utilisateur(int id, String nom) { 
        this.id = id; 
        this.nom = nom; 
    }

    public Utilisateur(String nom, String prenom, String email, String username, String motDePasse, RoleUtilisateur role) {
        this.nom = nom; this.prenom = prenom; this.email = email; 
        this.username = username; this.motDePasse = motDePasse; this.role = role;
    }

    public String getDisplayName() { return nom + (prenom != null ? " " + prenom : ""); }
    public String getInitiales() { 
        if (nom != null && prenom != null && !nom.isEmpty() && !prenom.isEmpty())
            return (nom.substring(0, 1) + prenom.substring(0, 1)).toUpperCase();
        return "";
    }

    // Getters et Setters compactés
    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public String getNom() { return nom; } public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; } public void setPrenom(String p) { this.prenom = p; }
    public String getEmail() { return email; } public void setEmail(String e) { this.email = e; }
    public String getUsername() { return username; } public void setUsername(String u) { this.username = u; }
    public String getMotDePasse() { return motDePasse; } public void setMotDePasse(String m) { this.motDePasse = m; }
    public String getTelephone() { return telephone; } public void setTelephone(String t) { this.telephone = t; }
    public String getAdresse() { return adresse; } public void setAdresse(String a) { this.adresse = a; }
    public RoleUtilisateur getRole() { return role; } public void setRole(RoleUtilisateur r) { this.role = r; }
    public boolean isActif() { return actif; } public void setActif(boolean a) { this.actif = a; }
}