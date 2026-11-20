package DateBase;

import model.*;
import Enum.*;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
  Classe pour gérer les services de l'application de covoiturage.
 */
public class Services implements Serializable {
    private static final long serialVersionUID = 1L;

    /*
      Affiche la liste des courses.
     */
     public void afficherCourses() {
        List<Course> courses = SaveCharger.loadCourse();
        System.out.println("\nListe des courses :");
        courses.forEach(c -> System.out.println(
                "- Conducteur : " + c.getConducteur().getNom() +
                        ", Trajet : " + c.getTrajet().getEndroit() +
                        ", Départ : " + c.getDateTimeDepart()));
    }

    /*
      Affiche la liste des utilisateurs.
     */
     public void afficherUser() {
        List<User> users = SaveCharger.loadUsers();
        System.out.println("\nListe des utilisateurs :");
        users.forEach(u -> System.out.println("- " + u.getNom() + " (" + u.getClass().getSimpleName() + ")"));
    }

    /*
      Affiche les courses en cours.
     */
     public void courseEnCours() {
        List<Course> courses = SaveCharger.loadCourse();
        System.out.println("Les courses en cours : \n");
        courses.stream()
                .filter(c -> c.getEtat().equals(EtatCourse.EN_COURS))
                .forEach(course -> System.out.println(course));
    }

    /*
      Affiche le planning des courses.
     */
     public void planCourses() {
        List<Course> courses = SaveCharger.loadCourse();
        System.out.println("Le planning des courses : \n");
        System.out.println("JOURNALIER :");
        courses.stream()
                .filter(c -> c.getEtat().equals(EtatCourse.A_VENIR) &&
                        c.getConducteur().getDisponibilite().getType().equals(Typedisponibilite.JOURNALIER))
                .forEach(course -> System.out.println(course));
        System.out.println("HEBDOMADAIRE :");
        courses.stream()
                .filter(c -> c.getEtat().equals(EtatCourse.A_VENIR) &&
                        c.getConducteur().getDisponibilite().getType().equals(Typedisponibilite.HEBDOMADAIRE))
                .forEach(course -> System.out.println(course));
    }

    /*
      Affiche l'historique des courses terminées.
     */
     public void historiqueCourses() {
        List<Course> courses = SaveCharger.loadCourse();
        System.out.println("L'historique des courses terminées : \n");
        courses.stream()
                .filter(c -> c.getEtat().equals(EtatCourse.TERMINEE))
                .forEach(course -> System.out.println(course));
    }

    /*
      Compte le nombre d'utilisateurs par type.
      type Type d'utilisateur (Etudiant, Enseignant, ATS)
     */
     public void nombreUser(String type) {
        List<User> users = SaveCharger.loadUsers();
        long nbr = users.stream()
                .filter(u -> u.getClass().getSimpleName().equalsIgnoreCase(type))
                .count();
        System.out.println("Le nombre des " + type + " est : " + nbr);
    }

    /*
      Compte le nombre d'utilisateurs actifs (connectés dans les 30 derniers jours).
     */
    public void nbrUserActif() {
        List<User> users = SaveCharger.loadUsers();
        long nbr = users.stream()
                .filter(u -> u.getDerniereConnexion() != null &&
                        u.getDerniereConnexion().isAfter(LocalDateTime.now().minusDays(30)))
                .count();
        System.out.println("Le nombre des utilisateurs actifs : " + nbr);
    }

    /*
      Affiche la catégorie proposant le plus de courses.
     */
    public void nombreCourseCat() {
        List<Course> courses = SaveCharger.loadCourse();
        long etudiants = courses.stream()
                .filter(c -> c.getConducteur().getClass().getSimpleName().equalsIgnoreCase("Etudiant"))
                .count();
        long enseignants = courses.stream()
                .filter(c -> c.getConducteur().getClass().getSimpleName().equalsIgnoreCase("Enseignant"))
                .count();
        long ats = courses.stream()
                .filter(c -> c.getConducteur().getClass().getSimpleName().equalsIgnoreCase("ATS"))
                .count();
        long max = Math.max(Math.max(etudiants, enseignants), ats);
        if (max == 0) {
            System.out.println("Aucune course proposée");
        } else if (max == etudiants) {
            System.out.println("La catégorie qui propose le plus de courses sont les étudiants avec " + max + " courses proposées");
        } else if (max == enseignants) {
            System.out.println("La catégorie qui propose le plus de courses sont les enseignants avec " + max + " courses proposées");
        } else if (max == ats) {
            System.out.println("La catégorie qui propose le plus de courses sont les ATS avec " + max + " courses proposées");
        }
    }

    /*
      Affiche la faculté proposant le plus de courses.
     */
    public void nombreCourseFac() {
        List<Course> courses = SaveCharger.loadCourse();
        Map<String, Long> coursesParFaculte = courses.stream()
                .filter(c -> c.getConducteur() != null)
                .map(c -> {
                    User conducteur = c.getConducteur();
                    if (conducteur instanceof Etudiant) {
                        return ((Etudiant) conducteur).getFaculte();
                    } else if (conducteur instanceof Enseignant) {
                        return ((Enseignant) conducteur).getFaculte();
                    }
                    return "Autre";
                })
                .filter(faculte -> !faculte.equals("Autre"))
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        coursesParFaculte.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresentOrElse(
                        e -> System.out.println("Faculté la plus active : " + e.getKey() + " (" + e.getValue() + " courses)"),
                        () -> System.out.println("Aucune course trouvée")
                );
    }

    /*
      Affiche le top 10 des chauffeurs par note moyenne.
     */
    public void top10Chauffeur() {
        List<Course> courses = SaveCharger.loadCourse();
        Map<User, Double> chauffeursAvecNotes = courses.stream()
                .filter(c -> c.getConducteur() != null)
                .collect(Collectors.groupingBy(
                        Course::getConducteur,
                        Collectors.averagingDouble(c -> {
                            double rating = c.getConducteur().getAverageRating();
                            return Double.isNaN(rating) || rating == 0.0 ? 0.0 : rating; // Gérer les cas sans rating
                        })
                ));

        List<Map.Entry<User, Double>> top10 = chauffeursAvecNotes.entrySet().stream()
                .sorted(Map.Entry.<User, Double>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toList());

        System.out.println("\n=== TOP 10 DES CHAUFFEURS ===");
        if (top10.isEmpty()) {
            System.out.println("Aucun chauffeur trouvé.");
            return;
        }
        for (int i = 0; i < top10.size(); i++) {
            User chauffeur = top10.get(i).getKey();
            double note = top10.get(i).getValue();
            System.out.printf("%d. %s %s (Matricule: %s) - Note: %.1f/5\n",
                    i + 1, chauffeur.getPrenom(), chauffeur.getNom(), chauffeur.getMatricule(), note);
        }
    }
    /*
      Affiche et bannit les utilisateurs avec une note inférieure à un seuil.
      limiteInferieure Seuil de note
      bannir Si true, bannit les utilisateurs
     */
    public void afficherEtBannirPiresUtilisateurs(double limiteInferieure, boolean bannir) {
        List<User> tousUsers = SaveCharger.loadUsers();
        List<User> piresUsers = tousUsers.stream()
                .filter(u -> u.getAverageRating() < limiteInferieure)
                .sorted(Comparator.comparingDouble(User::getAverageRating))
                .limit(10)
                .collect(Collectors.toList());

        if (piresUsers.isEmpty()) {
            System.out.println("Aucun utilisateur avec une note inférieure à " + limiteInferieure);
            return;
        }

        System.out.println("\n=== UTILISATEURS À RISQUE ===");
        System.out.println("Seuil : note < " + limiteInferieure);
        System.out.println("----------------------------");

        for (int i = 0; i < piresUsers.size(); i++) {
            User user = piresUsers.get(i);
            System.out.printf("%d. %s %s (Matricule: %s) - Note: %.1f/5\n",
                    i + 1, user.getPrenom(), user.getNom(), user.getMatricule(), user.getAverageRating());
        }

        if (bannir) {
            System.out.println("\nBannissement des utilisateurs...");
            piresUsers.forEach(user -> {
                user.setBanni(true);
                System.out.println("- " + user.getMatricule() + " banni");
            });
            SaveCharger.saveUsers(tousUsers);
            System.out.println("Opération terminée. " + piresUsers.size() + " utilisateurs bannis.");
        }
    }

    /*
      Ajoute un utilisateur en utilisant SaveCharger.
      user Utilisateur à ajouter
     */
    public void ajouterUtilisateur(User user) {
        SaveCharger.addUser(user);
    }

    /*
      Bannit un utilisateur par matricule.
      matricule Matricule de l'utilisateur
     */
    public void bannirUtilisateur(String matricule) {
        List<User> users = SaveCharger.loadUsers();
        for (User user : users) {
            if (user.getMatricule().equals(matricule)) {
                user.setBanni(true);
                SaveCharger.saveUsers(users);
                break;
            }
        }
    }

    /*
      Compte le nombre d'étudiants.
     return Nombre d'étudiants
     */
    public int getNombreEtudiants() {
        return (int) SaveCharger.loadUsers().stream().filter(u -> u instanceof Etudiant).count();
    }

    /*
      Compte le nombre d'enseignants.
     return Nombre d'enseignants
     */
    public int getNombreEnseignants() {
        return (int) SaveCharger.loadUsers().stream().filter(u -> u instanceof Enseignant).count();
    }

    /*
      Récupère les meilleurs chauffeurs.
      topN Nombre de chauffeurs à retourner
      return Liste des chauffeurs
     */
    public List<User> getTopChauffeurs(int topN) {
        return SaveCharger.loadUsers().stream()
                .filter(User::isChauffeur)
                .sorted((u1, u2) -> Double.compare(u2.getAverageRating(), u1.getAverageRating()))
                .limit(topN)
                .collect(Collectors.toList());
    }

    /*
      Ajoute une course en utilisant SaveCharger.
      c Course à ajouter
     */
    public void ajouterCourse(Course c) {
        SaveCharger.addCourse(c);
    }

    /*
      Récupère les courses en cours.
     return Liste des courses en cours
     */
    public List<Course> getCoursesEnCours() {
        LocalDateTime maintenant = LocalDateTime.now();
        return SaveCharger.loadCourse().stream()
                .filter(c -> c.getDateTimeDepart().isAfter(maintenant))
                .collect(Collectors.toList());
    }

    /*
      Récupère le planning hebdomadaire des courses.
     return Map des courses par jour de la semaine
     */
    public Map<DayOfWeek, List<Course>> getPlanningHebdomadaire() {
        Map<DayOfWeek, List<Course>> planning = new HashMap<>();
        for (Course c : SaveCharger.loadCourse()) {
            DayOfWeek jour = c.getDateTimeDepart().getDayOfWeek();
            planning.putIfAbsent(jour, new ArrayList<>());
            planning.get(jour).add(c);
        }
        return planning;
    }
}