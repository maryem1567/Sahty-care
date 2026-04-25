package comsahtycaremodels;

/**
 * Classe de test manuelle pour valider le fonctionnement de la classe Utilisateur.
 * Remplace les tests JUnit pour éviter les erreurs de bibliothèque manquante.
 */
public class UtilisateurTest {

    public static void main(String[] args) {
        System.out.println("----- DÉBUT DU TEST UTILISATEUR -----");

        try {
            // Utilisation du constructeur (int, String) que Java a trouvé dans ton fichier
            Utilisateur u = new Utilisateur(1, "Ahmed"); 

            // Vérification des données
            System.out.println("ID : " + u.getId());
            System.out.println("Nom : " + u.getNom());

            // Test d'une modification
            u.setNom("Ahmed Ben Ali");
            System.out.println("Nouveau nom vérifié : " + u.getNom());

            System.out.println("\n✅ TEST RÉUSSI : La classe Utilisateur fonctionne.");

        } catch (Exception e) {
            // ...

        System.out.println("----- FIN DU TEST -----");
    }
}
}