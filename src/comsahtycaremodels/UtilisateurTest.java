package com.sahtycare.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Utilisateur refactorisée
 * 
 * Pour exécuter ces tests, assurez-vous que vous avez :
 * - JUnit 5
 * - Spring Boot Validation
 * 
 * Dépendances Maven requises :
 * <dependency>
 *     <groupId>org.springframework.boot</groupId>
 *     <artifactId>spring-boot-starter-test</artifactId>
 * </dependency>
 * <dependency>
 *     <groupId>org.springframework.boot</groupId>
 *     <artifactId>spring-boot-starter-validation</artifactId>
 * </dependency>
 */
@DisplayName("Tests de la classe Utilisateur")
class UtilisateurTest {

    private Utilisateur utilisateur;
    private Validator validator;

    @BeforeEach
    void setUp() {
        // Initialiser un utilisateur de test
        utilisateur = new Utilisateur(
            "Dupont",
            "Jean",
            "jean.dupont@example.com",
            "jdupont",
            "SecurePassword123",
            RoleUtilisateur.MEDECIN
        );

        // Initialiser le validateur
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // ============ TESTS DE CRÉATION ============

    @Test
    @DisplayName("Doit créer un utilisateur avec les bonnes propriétés")
    void testCreationUtilisateur() {
        assertEquals("Dupont", utilisateur.getNom());
        assertEquals("Jean", utilisateur.getPrenom());
        assertEquals("jean.dupont@example.com", utilisateur.getEmail());
        assertEquals("jdupont", utilisateur.getUsername());
        assertEquals(RoleUtilisateur.MEDECIN, utilisateur.getRole());
        assertTrue(utilisateur.isActif());
    }

    @Test
    @DisplayName("Doit calculer le nom complet correctement")
    void testNomComplet() {
        assertEquals("Jean Dupont", utilisateur.getNomComplet());
    }

    @Test
    @DisplayName("Doit calculer les initiales correctement")
    void testInitiales() {
        assertEquals("JD", utilisateur.getInitiales());
    }

    @Test
    @DisplayName("Doit générer le displayName correct")
    void testDisplayName() {
        assertEquals("Jean Dupont [Médecin]", utilisateur.getDisplayName());
    }

    // ============ TESTS DE SÉCURITÉ / AUTHENTIFICATION ============

    @Test
    @DisplayName("Doit vérifier le mot de passe correct")
    void testVerifierMotdepasseCorrect() {
        assertTrue(utilisateur.verifierMotdepasse("SecurePassword123"));
    }

    @Test
    @DisplayName("Doit rejeter le mauvais mot de passe")
    void testVerifierMotdepasseIncorrect() {
        assertFalse(utilisateur.verifierMotdepasse("MauvaisMotDePasse"));
    }

    @Test
    @DisplayName("Doit rejeter un mot de passe null")
    void testVerifierMotdepasseNull() {
        assertFalse(utilisateur.verifierMotdepasse(null));
    }

    @Test
    @DisplayName("Doit rejeter un mot de passe vide")
    void testVerifierMotdepasseVide() {
        assertFalse(utilisateur.verifierMotdepasse(""));
    }

    @Test
    @DisplayName("Doit permettre la connexion avec bon mot de passe")
    void testConnexionReussie() {
        assertTrue(utilisateur.seConnecter("SecurePassword123"));
        assertEquals(0, utilisateur.getNombreTentativesConnexion());
        assertNotNull(utilisateur.getDateDerniereConnexion());
    }

    @Test
    @DisplayName("Doit rejeter la connexion avec mauvais mot de passe")
    void testConnexionEchouee() {
        assertFalse(utilisateur.seConnecter("MauvaisMotDePasse"));
        assertEquals(1, utilisateur.getNombreTentativesConnexion());
    }

    @Test
    @DisplayName("Doit rejeter la connexion si compte désactivé")
    void testConnexionCompteDesactive() {
        utilisateur.desactiver();
        assertFalse(utilisateur.seConnecter("SecurePassword123"));
    }

    @Test
    @DisplayName("Doit incrémenter les tentatives en cas d'échec")
    void testCompteurTentatives() {
        assertEquals(0, utilisateur.getNombreTentativesConnexion());

        utilisateur.seConnecter("MauvaisMotDePasse");
        assertEquals(1, utilisateur.getNombreTentativesConnexion());

        utilisateur.seConnecter("MauvaisMotDePasse");
        assertEquals(2, utilisateur.getNombreTentativesConnexion());

        utilisateur.seConnecter("MauvaisMotDePasse");
        assertEquals(3, utilisateur.getNombreTentativesConnexion());
    }

    @Test
    @DisplayName("Doit réinitialiser les tentatives après connexion réussie")
    void testReinitialiserTentativesAprexConnexion() {
        utilisateur.seConnecter("MauvaisMotDePasse");
        utilisateur.seConnecter("MauvaisMotDePasse");
        assertEquals(2, utilisateur.getNombreTentativesConnexion());

        assertTrue(utilisateur.seConnecter("SecurePassword123"));
        assertEquals(0, utilisateur.getNombreTentativesConnexion());
    }

    @Test
    @DisplayName("Doit détecter le blocage après trop de tentatives")
    void testBlockageParTentatives() {
        assertFalse(utilisateur.estBloqueParTentatives(5));

        for (int i = 0; i < 5; i++) {
            utilisateur.seConnecter("MauvaisMotDePasse");
        }

        assertTrue(utilisateur.estBloqueParTentatives(5));
        assertFalse(utilisateur.estBloqueParTentatives(6));
    }

    @Test
    @DisplayName("Doit changer le mot de passe correctement")
    void testChangerMotdepasseReussi() {
        assertTrue(utilisateur.changerMotdepasse("SecurePassword123", "NouveauMotDePasse456"));
        assertTrue(utilisateur.verifierMotdepasse("NouveauMotDePasse456"));
        assertFalse(utilisateur.verifierMotdepasse("SecurePassword123"));
    }

    @Test
    @DisplayName("Doit rejeter le changement avec ancien mot de passe incorrect")
    void testChangerMotdepasseAncienIncorrect() {
        assertFalse(utilisateur.changerMotdepasse("MauvaisAncien", "NouveauMotDePasse456"));
        assertTrue(utilisateur.verifierMotdepasse("SecurePassword123"));
    }

    @Test
    @DisplayName("Doit rejeter le nouveau mot de passe trop court")
    void testChangerMotdepasseNouveauTropCourt() {
        assertFalse(utilisateur.changerMotdepasse("SecurePassword123", "Court1"));
    }

    @Test
    @DisplayName("Doit réinitialiser les tentatives lors du changement de mot de passe")
    void testChangerMotdepasseReinitTentatives() {
        utilisateur.seConnecter("MauvaisMotDePasse");
        utilisateur.seConnecter("MauvaisMotDePasse");
        assertEquals(2, utilisateur.getNombreTentativesConnexion());

        utilisateur.changerMotdepasse("SecurePassword123", "NouveauMotDePasse456");
        assertEquals(0, utilisateur.getNombreTentativesConnexion());
    }

    // ============ TESTS DE PERMISSIONS ============

    @Test
    @DisplayName("Doit vérifier la permission admin")
    void testEstAdmin() {
        assertFalse(utilisateur.estAdmin()); // MEDECIN n'est pas admin

        utilisateur.setRole(RoleUtilisateur.ADMINISTRATEUR);
        assertTrue(utilisateur.estAdmin());
    }

    @Test
    @DisplayName("Doit vérifier la permission médecin")
    void testEstMedecin() {
        assertTrue(utilisateur.estMedecin());

        utilisateur.setRole(RoleUtilisateur.PATIENT);
        assertFalse(utilisateur.estMedecin());
    }

    @Test
    @DisplayName("Doit vérifier la permission patient")
    void testEstPatient() {
        assertFalse(utilisateur.estPatient());

        utilisateur.setRole(RoleUtilisateur.PATIENT);
        assertTrue(utilisateur.estPatient());
    }

    @Test
    @DisplayName("Doit rejeter les permissions si compte désactivé")
    void testPermissionsCompteDesactive() {
        assertTrue(utilisateur.estMedecin());

        utilisateur.desactiver();
        assertFalse(utilisateur.estMedecin());
    }

    @Test
    @DisplayName("Doit vérifier la permission spécifique")
    void testALaPermission() {
        assertTrue(utilisateur.aLaPermission(RoleUtilisateur.MEDECIN));
        assertFalse(utilisateur.aLaPermission(RoleUtilisateur.ADMINISTRATEUR));
    }

    // ============ TESTS D'ACTIVATION ============

    @Test
    @DisplayName("Doit créer un utilisateur actif par défaut")
    void testUtilisateurActifParDefaut() {
        assertTrue(utilisateur.isActif());
    }

    @Test
    @DisplayName("Doit pouvoir désactiver un utilisateur")
    void testDesactiver() {
        assertTrue(utilisateur.isActif());
        utilisateur.desactiver();
        assertFalse(utilisateur.isActif());
    }

    @Test
    @DisplayName("Doit pouvoir réactiver un utilisateur")
    void testReactiver() {
        utilisateur.desactiver();
        utilisateur.setActif(true);
        assertTrue(utilisateur.isActif());
    }

    // ============ TESTS EQUALS / HASHCODE ============

    @Test
    @DisplayName("Doit considérer deux utilisateurs avec le même ID comme égaux")
    void testEqualsMemID() {
        Utilisateur user1 = new Utilisateur("Dupont", "Jean", "jean@example.com", "jdupont", "Pass123", RoleUtilisateur.MEDECIN);
        user1.setId(1L);

        Utilisateur user2 = new Utilisateur("Martin", "Pierre", "pierre@example.com", "pmartin", "Pass456", RoleUtilisateur.PATIENT);
        user2.setId(1L);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    @DisplayName("Doit considérer deux utilisateurs avec le même email comme égaux (sans ID)")
    void testEqualsMemEmail() {
        Utilisateur user1 = new Utilisateur("Dupont", "Jean", "jean@example.com", "jdupont", "Pass123", RoleUtilisateur.MEDECIN);
        Utilisateur user2 = new Utilisateur("Martin", "Pierre", "jean@example.com", "pmartin", "Pass456", RoleUtilisateur.PATIENT);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    @DisplayName("Doit rejeter la comparaison si l'objet est null")
    void testEqualsNull() {
        assertNotEquals(utilisateur, null);
    }

    @Test
    @DisplayName("Doit rejeter la comparaison si l'objet est d'un type différent")
    void testEqualsDifferentType() {
        assertNotEquals(utilisateur, "not an utilisateur");
    }

    @Test
    @DisplayName("Doit considérer le même objet comme égal à lui-même")
    void testEqualsSelf() {
        assertEquals(utilisateur, utilisateur);
    }

    // ============ TESTS TOSTRING ============

    @Test
    @DisplayName("Doit générer un toString valide")
    void testToString() {
        String str = utilisateur.toString();
        assertTrue(str.contains("Dupont"));
        assertTrue(str.contains("Jean"));
        assertTrue(str.contains("jean.dupont@example.com"));
        assertTrue(str.contains("Médecin"));
        assertFalse(str.contains("motdepasse")); // Ne doit pas montrer le mot de passe
    }

    @Test
    @DisplayName("Doit générer un toDetailedString valide")
    void testToDetailedString() {
        utilisateur.setTelephone("+33612345678");
        utilisateur.setAdresse("123 Rue de la Paix");
        utilisateur.setCreatedBy("admin");

        String str = utilisateur.toDetailedString();
        assertTrue(str.contains("Dupont"));
        assertTrue(str.contains("+33612345678"));
        assertTrue(str.contains("123 Rue de la Paix"));
        assertTrue(str.contains("admin"));
        assertFalse(str.contains("motdepasse"));
    }

    // ============ TESTS DE VALIDATION ============

    @Test
    @DisplayName("Doit valider un utilisateur correct")
    void testValidationOK() {
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateur);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Doit rejeter le nom vide")
    void testValidationNomVide() {
        utilisateur.setNom("");
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateur);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Doit rejeter le email invalide")
    void testValidationEmailInvalide() {
        utilisateur.setEmail("email_invalide");
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateur);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Doit rejeter l'username invalide (caractères spéciaux)")
    void testValidationUsernameInvalide() {
        utilisateur.setUsername("invalid@username!");
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateur);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Doit rejeter le téléphone invalide")
    void testValidationTelephoneInvalide() {
        utilisateur.setTelephone("123"); // Trop court
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateur);
        assertFalse(violations.isEmpty());
    }

    // ============ TESTS DE RÔLES ============

    @Test
    @DisplayName("Doit avoir tous les rôles")
    void testTousLesRoles() {
        assertEquals(5, RoleUtilisateur.values().length);
    }

    @Test
    @DisplayName("Doit identifier correctement l'admin")
    void testRoleAdmin() {
        assertTrue(RoleUtilisateur.ADMINISTRATEUR.isAdmin());
        assertFalse(RoleUtilisateur.MEDECIN.isAdmin());
    }

    @Test
    @DisplayName("Doit retourner les permissions disponibles")
    void testGetPermissions() {
        String[] permissions = RoleUtilisateur.getPermissions();
        assertEquals(5, permissions.length);
        assertTrue(permissions[0].contains("Admin"));
    }
}
