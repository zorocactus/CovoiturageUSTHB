package main;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import Enum.EtatCourse;
import Enum.TypeCourse;
import Enum.Typedisponibilite;
import model.ATS;
import model.Course;
import model.Disponibilite;
import model.Enseignant;
import model.Etudiant;
import model.Itineraire;
import model.PlageHoraire;
import model.Preferences;
import model.PreferenceSexe;
import model.Rating;
import model.User;
import DateBase.SaveCharger;
import DateBase.Services;

/*
   Classe principale du système de covoiturage.
   Ici on gère : menu, entrées, et appels aux services.
 */
public class Main {
    private static User currentUser = null; // Pour suivre l’utilisateur connecté
    private static final List<String> ADMIN_MATRICULES = new ArrayList<>(); // Liste des matricules d’admins (à initialiser)

    static {
        // Initialisation des admins (à modifier selon vos besoins)
        ADMIN_MATRICULES.add("POO456"); // Yassir est admin pour l’exemple
    }

    public static void main(String[] args) {
        // Services et scanner pour lire les entrées utilisateur
        Services services = new Services();
        Scanner scan = new Scanner(System.in); 

        // Initialisation de l’ID des courses, pour éviter les conflits
        Course.initialiserProchainId(SaveCharger.loadCourse());
        
        // Réinitialiser les données pour éviter les conflits
        SaveCharger.clearAllData();

        // Initialisation des données une seule fois
        initialiserDonneesTest(services);

        // Boucle principale
        while (true) {
            afficherMenu();
            System.out.print("Ton choix (1-23) : ");
            String choix = scan.nextLine().trim();

            try {
                switch (choix) {
                    case "1":
                        services.afficherUser();
                        break;
                    case "2":
                        services.afficherCourses();
                        break;
                    case "3":
                        services.courseEnCours();
                        break;
                    case "4":
                        services.planCourses();
                        break;
                    case "5":
                        services.historiqueCourses();
                        break;
                    case "6":
                        services.nbrUserActif();
                        break;
                    case "7":
                        services.nombreCourseCat();
                        break;
                    case "8":
                        services.nombreCourseFac();
                        break;
                    case "9":
                        services.top10Chauffeur();
                        break;
                    case "10":
                        System.out.print("Note minimale (ex. 2.0) : ");
                        double seuilNote = Double.parseDouble(scan.nextLine().trim());
                        System.out.println("Bannir les utilisateurs ?");
                        System.out.println("1. Oui");
                        System.out.println("2. Non");
                        System.out.print("Numéro (1-2) : ");
                        String banChoix = scan.nextLine().trim();
                        boolean ban;
                        switch (banChoix) {
                            case "1": ban = true; break;
                            case "2": ban = false; break;
                            default: throw new IllegalArgumentException("T’as mis un mauvais numéro, 1 ou 2 stp !");
                        }
                        services.afficherEtBannirPiresUtilisateurs(seuilNote, ban);
                        break;
                    case "11":
                        System.out.println("Choisis un type :");
                        System.out.println("1. Etudiant");
                        System.out.println("2. Enseignant");
                        System.out.println("3. ATS");
                        System.out.print("Numéro (1-3) : ");
                        String typeChoisi = scan.nextLine().trim();
                        String type;
                        switch (typeChoisi) {
                            case "1": type = "Etudiant"; break;
                            case "2": type = "Enseignant"; break;
                            case "3": type = "ATS"; break;
                            default: throw new IllegalArgumentException("Mauvais numéro, choisis entre 1 et 3 !");
                        }
                        services.nombreUser(type);
                        break;
                    case "12":
                        ajouterNouvelUtilisateur(services, scan);
                        break;
                    case "13":
                        ajouterNouvelleCourse(services, scan);
                        break;
                    case "14":
                        ajouterPassagerCourse(services, scan);
                        break;
                    case "15":
                        rechercherCoursesCompatibles(services, scan);
                        break;
                    case "16":
                        noterUtilisateurCourse(services, scan);
                        break;
                    case "17":
                        supprimerUtilisateur(services, scan);
                        break;
                    case "18":
                        seConnecter(services, scan);
                        break;
                    case "19":
                        seDeconnecter();
                        break;
                    case "20":
                        if (currentUser != null && currentUser.isAdmin()) {
                            voirRapportsAdmin(services, scan);
                        } else {
                            System.out.println("Désolé, réservé aux admins. Connecte-toi d’abord !");
                        }
                        break;
                    case "21":
                        terminerUneCourse(services, scan);
                        break;
                    case "22":
                        demarrerUneCourse(services, scan);
                        break;
                    case "23":
                        System.out.println("À la prochaine !");
                        scan.close();
                        return;
                    default:
                        System.out.println("Choix pas bon, faut taper entre 1 et 23 !");
                }
            } catch (Exception e) {
                System.out.println("Oups, y’a eu une erreur : " + e.getMessage() + ". On réessaie ?");
            }
        }
    }

    /*
      Affiche le menu principal, c’est un peu comme le tableau de bord de l’appli
     */
    private static void afficherMenu() {
        System.out.println("\n=== Covoiturage USTHB ===");
        System.out.println("=== Consultation ===");
        System.out.println("1. Lister les utilisateurs");
        System.out.println("2. Lister les courses");
        System.out.println("3. Courses en cours");
        System.out.println("4. Planning des courses");
        System.out.println("5. Historique des courses");
        System.out.println("6. Utilisateurs actifs");
        System.out.println("7. Catégorie la plus active");
        System.out.println("8. Faculté la plus active");
        System.out.println("9. Top 10 chauffeurs");
        System.out.println("10. Bannir les mal notés");
        System.out.println("=== Gestion ===");
        System.out.println("11. Compter les utilisateurs par type");
        System.out.println("12. Ajouter un utilisateur");
        System.out.println("13. Ajouter une course");
        System.out.println("14. Ajouter un passager");
        System.out.println("15. Chercher une course");
        System.out.println("16. Noter quelqu’un");
        System.out.println("17. Supprimer un utilisateur");
        System.out.println("=== Admins ===");
        System.out.println("18. Se connecter");
        System.out.println("19. Se déconnecter");
        System.out.println("20. Voir les rapports (admins seulement)");
        System.out.println("21. Terminer une course");
        System.out.println("22. Démarrer une course"); // Ajout
        System.out.println("23. Quitter"); // Mise à jour du numéro
    }

    /*
      Ajoute des données de test pour ne pas partir de zéro
      J’ai mis des noms d’ici, genre Yassir et Fatima
      Le service qui gère tout
     */
    private static void initialiserDonneesTest(Services services) {
        List<PlageHoraire> horaires = new ArrayList<>();
        horaires.add(new PlageHoraire(DayOfWeek.TUESDAY, LocalTime.of(23, 15), LocalTime.of(23, 59))); // Ajusté à 23:15 PM CET
        Disponibilite dispo = new Disponibilite(Typedisponibilite.HEBDOMADAIRE, horaires);

        Preferences prefs = new Preferences(PreferenceSexe.INDIFFERENT, true, false);

        Etudiant etu = new Etudiant("Fatima", "Karha", "ETU456", 0.0, "Passager",
                new Itineraire("Bab Ezzouar -> USTHB"), prefs, TypeCourse.ALLER,
                "2023", "Informatique", "Réseaux");
        etu.setDisponibilite(dispo);
        SaveCharger.addUser(etu);

        Enseignant prof = new Enseignant("Yassir", "Hadj Sahraoui", "POO456", 0.0, "Passager",
                new Itineraire("Alger Centre -> USTHB"), prefs, TypeCourse.ALLER_RETOUR,
                "2015", "Mathématiques");
        prof.devenirChauffeur();
        prof.setAdmin(true);
        prof.setPreferences(prefs);
        prof.setDisponibilite(dispo);
        List<Itineraire> trajets = new ArrayList<>();
        trajets.add(new Itineraire("Alger Centre -> USTHB"));
        prof.setItineraireChauffeur(trajets);
        SaveCharger.addUser(prof);

        ATS ats = new ATS("Antony", "Prime", "ATS123", 2020, "Scolarité");
        ats.setPreferences(prefs);
        ats.setDisponibilite(dispo);
        ats.setTypeCourse(TypeCourse.ALLER);
        ats.setItinerairePassager(new Itineraire("Hussein Dey -> USTHB"));
        SaveCharger.addUser(ats);

        Course trajet = new Course(prof, trajets.get(0), LocalDateTime.of(2025, 5, 13, 23, 15), TypeCourse.ALLER);
        trajet.ajouterPassager(etu);
        trajet.demarrerCourse();
        trajet.terminerCourse();
        SaveCharger.addCourse(trajet);
    }

    /*
       Ajoute un nouvel utilisateur, genre si on veut ajouter quelqu’un comme Yacine.
       C’est un peu long à taper, mais ça marche bien, je trouve.
       services Où on sauvegarde les utilisateurs
       scan Scanner pour lire les entrées
     */
    private static void ajouterNouvelUtilisateur(Services services, Scanner scan) {
        try {
            System.out.println("\n=== Ajout d’un nouvel utilisateur ===");
            System.out.println("Type d’utilisateur :");
            System.out.println("1. Etudiant");
            System.out.println("2. Enseignant");
            System.out.println("3. ATS");
            System.out.print("Ton choix (1-3) : ");
            String typeNum = scan.nextLine().trim();
            String typeUtilisateur;
            switch (typeNum) {
                case "1": typeUtilisateur = "etudiant"; break;
                case "2": typeUtilisateur = "enseignant"; break;
                case "3": typeUtilisateur = "ats"; break;
                default: throw new IllegalArgumentException("Mauvais numéro, faut choisir entre 1 et 3.");
            }

            System.out.print("Prénom : ");
            String prenom = scan.nextLine().trim();
            if (prenom.isEmpty()) throw new IllegalArgumentException("Le prénom, c’est obligatoire !");

            System.out.print("Nom : ");
            String nom = scan.nextLine().trim();
            if (nom.isEmpty()) throw new IllegalArgumentException("Le nom aussi, c’est obligatoire !");

            System.out.print("Matricule : ");
            String mat = scan.nextLine().trim();
            if (mat.isEmpty()) throw new IllegalArgumentException("Faut un matricule, sinon ça marche pas.");
            // Vérifier si le matricule existe déjà
            if (SaveCharger.loadUsers().stream().anyMatch(u -> u.getMatricule().equals(mat))) {
                throw new IllegalArgumentException("Ce matricule existe déjà, choisis un autre !");
            }

            User newUser;
            if (typeUtilisateur.equals("etudiant")) {
                System.out.print("Année d’admission : ");
                String annee = scan.nextLine().trim();
                System.out.print("Faculté : ");
                String fac = scan.nextLine().trim();
                System.out.print("Spécialité : ");
                String spe = scan.nextLine().trim();
                newUser = new Etudiant(nom, prenom, mat, 0.0, "Passager",
                        new Itineraire(""), null, TypeCourse.ALLER, annee, fac, spe);
            } else if (typeUtilisateur.equals("enseignant")) {
                System.out.print("Année de recrutement : ");
                String anneeRecrut = scan.nextLine().trim();
                System.out.print("Faculté : ");
                String fac = scan.nextLine().trim();
                newUser = new Enseignant(nom, prenom, mat, 0.0, "Passager",
                        new Itineraire(""), null, TypeCourse.ALLER, anneeRecrut, fac);
            } else {
                System.out.print("Année de recrutement : ");
                int anneeRecrut = Integer.parseInt(scan.nextLine().trim());
                System.out.print("Service : ");
                String service = scan.nextLine().trim();
                newUser = new ATS(nom, prenom, mat, anneeRecrut, service);
            }

            System.out.println("Chauffeur ?");
            System.out.println("1. Oui");
            System.out.println("2. Non");
            System.out.print("Ton choix (1-2) : ");
            String estChauffeur = scan.nextLine().trim();
            if (estChauffeur.equals("1")) {
                newUser.devenirChauffeur();
            } else if (!estChauffeur.equals("2")) {
                throw new IllegalArgumentException("Mauvais numéro, choisis 1 ou 2 !");
            }

            System.out.print("Trajet (ex. Alger Centre -> USTHB) : ");
            String trajet = scan.nextLine().trim();
            if (trajet.isEmpty()) throw new IllegalArgumentException("Le trajet, c’est important, faut le mettre !");
            if (newUser.isChauffeur()) {
                List<Itineraire> trajets = new ArrayList<>();
                trajets.add(new Itineraire(trajet));
                newUser.setItineraireChauffeur(trajets);
            } else {
                newUser.setItinerairePassager(new Itineraire(trajet));
            }

            System.out.println("Préférence de genre :");
            System.out.println("1. Homme");
            System.out.println("2. Femme");
            System.out.println("3. Indifférent");
            System.out.print("Ton choix (1-3) : ");
            String prefGenre = scan.nextLine().trim();
            PreferenceSexe prefSexe;
            switch (prefGenre) {
                case "1": prefSexe = PreferenceSexe.HOMME; break;
                case "2": prefSexe = PreferenceSexe.FEMME; break;
                case "3": prefSexe = PreferenceSexe.INDIFFERENT; break;
                default: throw new IllegalArgumentException("Mauvais numéro, choisis entre 1 et 3 !");
            }

            System.out.println("Musique ?");
            System.out.println("1. Oui");
            System.out.println("2. Non");
            System.out.print("Ton choix (1-2) : ");
            String musiqueOuiNon = scan.nextLine().trim();
            boolean avecMusique;
            switch (musiqueOuiNon) {
                case "1": avecMusique = true; break;
                case "2": avecMusique = false; break;
                default: throw new IllegalArgumentException("Mauvais numéro, choisis 1 ou 2 !");
            }

            System.out.println("Bagages ?");
            System.out.println("1. Oui");
            System.out.println("2. Non");
            System.out.print("Ton choix (1-2) : ");
            String bagagesOuiNon = scan.nextLine().trim();
            boolean avecBagages;
            switch (bagagesOuiNon) {
                case "1": avecBagages = true; break;
                case "2": avecBagages = false; break;
                default: throw new IllegalArgumentException("Mauvais numéro, choisis 1 ou 2 !");
            }

            newUser.setPreferences(new Preferences(prefSexe, avecBagages, avecMusique));

            System.out.println("Type de trajet :");
            System.out.println("1. Aller");
            System.out.println("2. Retour");
            System.out.println("3. Aller-retour");
            System.out.print("Ton choix (1-3) : ");
            String typeTrajet = scan.nextLine().trim();
            TypeCourse trajetType;
            switch (typeTrajet) {
                case "1": trajetType = TypeCourse.ALLER; break;
                case "2": trajetType = TypeCourse.RETOUR; break;
                case "3": trajetType = TypeCourse.ALLER_RETOUR; break;
                default: throw new IllegalArgumentException("Mauvais numéro, choisis entre 1 et 3 !");
            }
            newUser.setTypeCourse(trajetType);

            List<PlageHoraire> horaires = new ArrayList<>();
            System.out.println("Jour dispo :");
            System.out.println("1. Lundi");
            System.out.println("2. Mardi");
            System.out.println("3. Mercredi");
            System.out.println("4. Jeudi");
            System.out.println("5. Vendredi");
            System.out.println("6. Samedi");
            System.out.println("7. Dimanche");
            System.out.print("Ton choix (1-7) : ");
            String jourChoisi = scan.nextLine().trim();
            DayOfWeek jour;
            switch (jourChoisi) {
                case "1": jour = DayOfWeek.MONDAY; break;
                case "2": jour = DayOfWeek.TUESDAY; break;
                case "3": jour = DayOfWeek.WEDNESDAY; break;
                case "4": jour = DayOfWeek.THURSDAY; break;
                case "5": jour = DayOfWeek.FRIDAY; break;
                case "6": jour = DayOfWeek.SATURDAY; break;
                case "7": jour = DayOfWeek.SUNDAY; break;
                default: throw new IllegalArgumentException("Mauvais numéro, choisis entre 1 et 7 !");
            }

            System.out.print("Heure de début (ex. 08:00) : ");
            LocalTime debut = LocalTime.parse(scan.nextLine().trim());

            System.out.print("Heure de fin (ex. 10:00) : ");
            LocalTime fin = LocalTime.parse(scan.nextLine().trim());

            horaires.add(new PlageHoraire(jour, debut, fin));
            newUser.setDisponibilite(new Disponibilite(Typedisponibilite.HEBDOMADAIRE, horaires));

            // Vérifier si l’utilisateur est un admin (par matricule prédéfini)
            if (ADMIN_MATRICULES.contains(mat)) {
                newUser.setAdmin(true);
                System.out.println("Utilisateur " + prenom + " " + nom + " ajouté comme admin !");
            } else {
                newUser.setAdmin(false);
            }

            SaveCharger.addUser(newUser);
            System.out.println("Utilisateur bien ajouté, c’est bon !");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage() + ". Recommence, stp !");
        }
    }

    /*
       Ajoute une nouvelle course, genre pour un trajet d’Yassir qui va à USTHB
       J’ai galéré avec le format de la date, mais maintenant ça marche
       services Pour sauvegarder la course
       scan Pour lire ce que l’utilisateur tape
     */
    private static void ajouterNouvelleCourse(Services services, Scanner scan) {
        try {
            System.out.println("\n=== Nouvelle course ===");
            System.out.print("Matricule du chauffeur : ");
            String mat = scan.nextLine().trim();
            if (mat.isEmpty()) throw new IllegalArgumentException("Faut un matricule, sinon ça va pas !");

            User chauffeur = SaveCharger.loadUsers().stream()
                    .filter(u -> u.getMatricule().equals(mat) && u.isChauffeur())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Chauffeur pas trouvé ou pas un chauffeur !"));

            if (chauffeur.getBanni()) {
                throw new IllegalArgumentException("Le chauffeur est banni et ne peut pas proposer de course !");
            }

            System.out.print("Trajet (ex. Alger Centre -> USTHB) : ");
            String trajet = scan.nextLine().trim();
            if (trajet.isEmpty()) throw new IllegalArgumentException("Le trajet, c’est obligatoire !");

            System.out.print("Date et heure (ex. 2025-05-13T23:15) : ");
            LocalDateTime dateHeure = LocalDateTime.parse(scan.nextLine().trim());
            if (dateHeure.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("La date et heure doivent être dans le futur !");
            }

            System.out.println("Type de trajet :");
            System.out.println("1. Aller");
            System.out.println("2. Retour");
            System.out.println("3. Aller-retour");
            System.out.print("Ton choix (1-3) : ");
            String typeTrajet = scan.nextLine().trim();
            TypeCourse type;
            switch (typeTrajet) {
                case "1": type = TypeCourse.ALLER; break;
                case "2": type = TypeCourse.RETOUR; break;
                case "3": type = TypeCourse.ALLER_RETOUR; break;
                default: throw new IllegalArgumentException("Mauvais numéro, choisis entre 1 et 3 !");
            }

            Course newCourse = new Course(chauffeur, new Itineraire(trajet), dateHeure, type);
            SaveCharger.addCourse(newCourse);
            System.out.println("Course ajoutée, nickel !");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage() + ". Essaie encore !");
        }
    }
    /*
      Ajoute un passager à une course, genre ajouter Fatima à un trajet.
      Faut faire gaffe à pas ajouter un passager incompatible, sinon ça plante
      services Où on gère les courses
      scan Pour lire les infos
     */
    private static void ajouterPassagerCourse(Services services, Scanner scan) {
        try {
            System.out.println("\n=== Ajouter un passager ===");
            System.out.print("Matricule du passager : ");
            String matPassager = scan.nextLine().trim();
            if (matPassager.isEmpty()) throw new IllegalArgumentException("Faut un matricule, sinon ça marche pas !");

            User passager = SaveCharger.loadUsers().stream()
                    .filter(u -> u.getMatricule().equals(matPassager) && !u.isChauffeur())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Passager pas trouvé ou c’est un chauffeur !"));

            if (passager.getBanni()) {
                throw new IllegalArgumentException("Le passager est banni et ne peut pas être ajouté à une course !");
            }

            List<Course> courses = SaveCharger.loadCourse();
            System.out.println("Courses dispo :");
            courses.stream()
                    .filter(c -> c.getEtat() == EtatCourse.A_VENIR)
                    .forEach(c -> System.out.println(
                            "ID: " + c.getId() + ", Chauffeur: " + c.getConducteur().getNom() +
                                    ", Trajet: " + c.getTrajet().getEndroit() +
                                    ", Départ: " + c.getDateTimeDepart()));

            System.out.print("ID de la course : ");
            long idCourse = Long.parseLong(scan.nextLine().trim());

            Course course = courses.stream()
                    .filter(c -> c.getId() == idCourse && c.getEtat() == EtatCourse.A_VENIR)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Course pas trouvée ou déjà passée !"));

            if (course.ajouterPassager(passager)) {
                SaveCharger.suppCourseParId(idCourse);
                SaveCharger.addCourse(course);
                System.out.println("Passager bien ajouté !");
            } else {
                System.out.println("Impossible d’ajouter, y’a un problème (course pleine ou incompatibilité).");
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage() + ". On recommence ?");
        }
    }

    /*
      Cherche des courses compatibles, par exemple pour Fatima qui veut aller à USTHB.
      Ça check les préférences et l’itinéraire,
      services Où on trouve les courses
      scan Pour lire les infos
     */
    private static void rechercherCoursesCompatibles(Services services, Scanner scan) {
        try {
            System.out.println("\n=== Chercher une course compatible ===");
            System.out.print("Matricule du passager : ");
            String matPassager = scan.nextLine().trim();
            if (matPassager.isEmpty()) throw new IllegalArgumentException("Faut un matricule !");

            User passager = SaveCharger.loadUsers().stream()
                    .filter(u -> u.getMatricule().equals(matPassager) && !u.isChauffeur())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Passager pas trouvé ou c’est un chauffeur !"));

            System.out.print("Trajet voulu (ex. Alger Centre -> USTHB) : ");
            String trajet = scan.nextLine().trim();
            if (trajet.isEmpty()) throw new IllegalArgumentException("Le trajet, c’est obligatoire !");

            List<Course> coursesCompatibles = SaveCharger.loadCourse().stream()
                    .filter(c -> c.getEtat() == EtatCourse.A_VENIR)
                    .filter(c -> c.estCompatibleAvecPreferences(passager))
                    .filter(c -> c.estCompatibleAvecItineraire(new Itineraire(trajet)))
                    .collect(Collectors.toList());

            if (coursesCompatibles.isEmpty()) {
                System.out.println("Désolé, rien de compatible pour l’instant.");
                return;
            }

            System.out.println("Courses compatibles :");
            coursesCompatibles.forEach(c -> System.out.println(
                    "ID: " + c.getId() + ", Chauffeur: " + c.getConducteur().getNom() +
                            ", Trajet: " + c.getTrajet().getEndroit() +
                            ", Départ: " + c.getDateTimeDepart()));
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage() + ". Essaie encore !");
        }
    }

    /*
      Pour noter un utilisateur après un trajet, genre dire si Antony conduit bien.
      J’ai ajouté un affichage des utilisateurs dans la course pour aider à choisir.
      services Pour gérer les notes
      scan Pour lire la note et le commentaire
     */
    private static void noterUtilisateurCourse(Services services, Scanner scan) {
        try {
            System.out.println("\n=== Noter quelqu’un après un trajet ===");
            System.out.print("ID de la course : ");
            long idCourse = Long.parseLong(scan.nextLine().trim());

            List<Course> courses = SaveCharger.loadCourse();
            Course course = courses.stream()
                    .filter(c -> c.getId() == idCourse && c.getEtat() == EtatCourse.TERMINEE)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Course pas trouvée ou pas terminée !"));

            System.out.println("Qui était dans la course :");
            System.out.println("- Chauffeur: " + course.getConducteur().getNom() + " (" + course.getConducteur().getMatricule() + ")");
            course.getPassagers().forEach(p -> System.out.println(
                    "- Passager: " + p.getNom() + " (" + p.getMatricule() + ")"));

            System.out.print("Matricule de la personne à noter : ");
            String mat = scan.nextLine().trim();
            if (mat.isEmpty()) throw new IllegalArgumentException("Faut un matricule !");

            User personne = SaveCharger.loadUsers().stream()
                    .filter(u -> u.getMatricule().equals(mat) &&
                            (u.equals(course.getConducteur()) || course.getPassagers().contains(u)))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Personne pas trouvée dans cette course !"));

            System.out.print("Note (1 à 5) : ");
            int note = Integer.parseInt(scan.nextLine().trim());
            if (note < 1 || note > 5) throw new IllegalArgumentException("La note, c’est entre 1 et 5 !");

            System.out.print("Un petit commentaire : ");
            String comm = scan.nextLine().trim();

            Rating eval = new Rating(note, comm);
            personne.addRating(eval);
            if (personne.equals(course.getConducteur())) {
                course.ajouterEvaluation(eval);
            }

            SaveCharger.saveUsers(SaveCharger.loadUsers());
            if (personne.equals(course.getConducteur())) {
                SaveCharger.suppCourseParId(idCourse);
                SaveCharger.addCourse(course);
            }
            System.out.println("Note bien ajoutée, merci !");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage() + ". On recommence ?");
        }
    }

    /*
      Supprime un utilisateur avec son matricule.
      J’ai mis un affichage avant pour pas supprimer par erreur.
      Les admins peuvent supprimer direct, les autres doivent confirmer.
      services Pour gérer les utilisateurs
      scan Pour lire le matricule
     */
    private static void supprimerUtilisateur(Services services, Scanner scan) {
        try {
            System.out.println("\n=== Supprimer un utilisateur ===");
            System.out.println("Voilà les utilisateurs actuels :");
            List<User> users = SaveCharger.loadUsers();
            if (users == null || users.isEmpty()) {
                System.out.println("Y’a personne à supprimer...");
                return;
            }
            users.forEach(u -> System.out.println(
                    "Matricule: " + u.getMatricule() + ", Nom: " + u.getNom() + " " + u.getPrenom()));

            System.out.print("Matricule de la personne à supprimer : ");
            String mat = scan.nextLine().trim();
            if (mat.isEmpty()) throw new IllegalArgumentException("Faut un matricule !");

            User aSupprimer = users.stream()
                    .filter(u -> u.getMatricule().equals(mat))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Personne pas trouvée avec ce matricule : " + mat));

            // Supprimer les courses où l'utilisateur est chauffeur ou passager
            List<Course> courses = SaveCharger.loadCourse();
            courses.removeIf(c -> c.getConducteur().equals(aSupprimer) || c.getPassagers().contains(aSupprimer));
            SaveCharger.saveCourse(courses);

            if (currentUser != null && currentUser.isAdmin()) {
                // Admin : suppression directe
                users.removeIf(u -> u.getMatricule().equals(mat));
                SaveCharger.saveUsers(users);
                System.out.println(aSupprimer.getNom() + " " + aSupprimer.getPrenom() + " a été supprimé !");
            } else {
                // Utilisateur lambda : demande confirmation
                System.out.println("Voulez-vous vraiment supprimer " + aSupprimer.getNom() + " " + aSupprimer.getPrenom() + " ?");
                System.out.println("1. Oui");
                System.out.println("2. Non");
                System.out.print("Ton choix (1-2) : ");
                String confirm = scan.nextLine().trim();
                if (confirm.equals("1")) {
                    users.removeIf(u -> u.getMatricule().equals(mat));
                    SaveCharger.saveUsers(users);
                    System.out.println(aSupprimer.getNom() + " " + aSupprimer.getPrenom() + " a été supprimé !");
                } else {
                    System.out.println("Suppression annulée.");
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage() + ". Recommence !");
        }
    }

    /*
      Permet à un utilisateur de se connecter avec son matricule.
      Si c’est un admin, ça débloque des options en plus.
      services Pour accéder aux utilisateurs
      scan Pour lire les entrées
     */
    private static void seConnecter(Services services, Scanner scan) {
        try {
            System.out.println("\n=== Connexion ===");
            System.out.print("Ton matricule : ");
            String mat = scan.nextLine().trim();
            if (mat.isEmpty()) throw new IllegalArgumentException("Faut un matricule !");

            System.out.print("Ton mot de passe (pour l’exemple, juste '1234') : ");
            String motDePasse = scan.nextLine().trim();

            List<User> users = SaveCharger.loadUsers();
            User user = users.stream()
                    .filter(u -> u.getMatricule().equals(mat))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Matricule pas trouvé !"));

            if (motDePasse.equals("1234")) {
                currentUser = user;
                user.setDerniereConnexion(LocalDateTime.now()); // Mise à jour de la dernière connexion
                SaveCharger.saveUsers(users); // Sauvegarde après mise à jour
                System.out.println("Connecté comme " + user.getNom() + " " + user.getPrenom() + " !");
                if (user.isAdmin()) {
                    System.out.println("Tu es admin, tu as accès à plus d’options !");
                }
            } else {
                throw new IllegalArgumentException("Mauvais mot de passe, essaie encore !");
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage() + ". Recommence !");
        }
    }

    /*
      Permet à un utilisateur de se déconnecter.
      Réinitialise l'utilisateur connecté à null.
     */
    private static void seDeconnecter() {
        currentUser = null;
        System.out.println("Déconnecté avec succès !");
    }

    /*
      Affiche des rapports pour les admins, genre stats ou détails.
      J’ai mis un exemple simple, à enrichir selon les besoins.
      services Pour accéder aux données
      scan Pour lire d’éventuelles entrées
     */
    private static void voirRapportsAdmin(Services services, Scanner scan) {
        try {
            System.out.println("\n=== Rapports Admin ===");
            System.out.println("Nombre total d’utilisateurs : " + SaveCharger.loadUsers().size());
            System.out.println("Nombre total de courses : " + SaveCharger.loadCourse().size());
            System.out.println("Top 10 chauffeurs (détaillé) :");
            services.top10Chauffeur(); // Réutilise la méthode existante
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage() + ". Recommence !");
        }
    }

    private static void terminerUneCourse(Services services, Scanner scan) {
        try {
            System.out.println("\n=== Terminer une course ===");
            List<Course> courses = SaveCharger.loadCourse();
            System.out.println("Courses en cours :");
            courses.stream()
                    .filter(c -> c.getEtat() == EtatCourse.EN_COURS)
                    .forEach(c -> System.out.println(
                            "ID: " + c.getId() + ", Chauffeur: " + c.getConducteur().getNom() +
                                    ", Trajet: " + c.getTrajet().getEndroit() +
                                    ", Départ: " + c.getDateTimeDepart()));

            System.out.print("ID de la course à terminer : ");
            long idCourse = Long.parseLong(scan.nextLine().trim());

            Course course = courses.stream()
                    .filter(c -> c.getId() == idCourse && c.getEtat() == EtatCourse.EN_COURS)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Course pas trouvée ou pas en cours !"));

            course.terminerCourse();
            SaveCharger.suppCourseParId(idCourse);
            SaveCharger.addCourse(course);
            System.out.println("Course terminée avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage() + ". On recommence ?");
        }
    }

    private static void demarrerUneCourse(Services services, Scanner scan) {
        try {
            System.out.println("\n=== Démarrer une course ===");
            List<Course> courses = SaveCharger.loadCourse();
            System.out.println("Courses à venir :");
            courses.stream()
                    .filter(c -> c.getEtat() == EtatCourse.A_VENIR)
                    .forEach(c -> System.out.println(
                            "ID: " + c.getId() + ", Chauffeur: " + c.getConducteur().getNom() +
                                    ", Trajet: " + c.getTrajet().getEndroit() +
                                    ", Départ: " + c.getDateTimeDepart()));

            System.out.print("ID de la course à démarrer : ");
            long idCourse = Long.parseLong(scan.nextLine().trim());

            Course course = courses.stream()
                    .filter(c -> c.getId() == idCourse && c.getEtat() == EtatCourse.A_VENIR)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Course pas trouvée ou pas à venir !"));

            course.demarrerCourse();
            SaveCharger.suppCourseParId(idCourse);
            SaveCharger.addCourse(course);
            System.out.println("Course démarrée avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage() + ". On recommence ?");
        }
    }
    
   
    
}