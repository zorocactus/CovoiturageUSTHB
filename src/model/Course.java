package model;

import java.util.ArrayList;
import java.util.List;
import Enum.EtatCourse;
import Enum.TypeCourse;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * Représente une course de covoiturage.
 */
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    private static long prochainId = 1; // Compteur statique pour ID unique
    private long id;
    private User conducteur;
    private List<User> passagers;
    private Itineraire trajet;
    private LocalDateTime dateHeureDepart;
    private LocalDateTime dateHeureArrivee;
    private TypeCourse type;
    private EtatCourse etat;
    private List<Rating> evaluations;

    /*
      Met à jour le compteur d'ID après le chargement des données.
      courses Liste des courses chargées
     */
    public static void initialiserProchainId(List<Course> courses) {
        if (courses != null && !courses.isEmpty()) {
            long maxId = courses.stream()
                    .mapToLong(Course::getId)
                    .max()
                    .orElse(0);
            prochainId = maxId + 1;
        }
    }

    /*
      Constructeur d'une course.
      conducteur Utilisateur chauffeur
      trajet Itinéraire de la course
      horaire Date et heure de départ
      type Type de course (ALLER, RETOUR, ALLER_RETOUR)
     */
    public Course(User conducteur, Itineraire trajet, LocalDateTime horaire, TypeCourse type) {
        this.id = prochainId++;
        this.conducteur = conducteur;
        this.trajet = trajet;
        this.dateHeureDepart = horaire;
        this.type = type;
        this.passagers = new ArrayList<>();
        this.evaluations = new ArrayList<>();
        this.etat = EtatCourse.A_VENIR;
    }

    /*
      Vérifie la compatibilité des préférences avec un passager.
    passager Utilisateur à vérifier
      true si compatible, false sinon
     */
    public boolean estCompatibleAvecPreferences(User passager) {
        if (passager == null || conducteur == null) {
            return false;
        }

        Preferences prefsChauffeur = conducteur.getPreferences();
        Preferences prefsPassager = passager.getPreferences();

        if (prefsChauffeur == null || prefsPassager == null) {
            return false;
        }

        if (prefsChauffeur.getPreferenceSexe() != PreferenceSexe.INDIFFERENT &&
                prefsPassager.getPreferenceSexe() != prefsChauffeur.getPreferenceSexe()) {
            return false;
        }

        return prefsChauffeur.accepteMusique() == prefsPassager.accepteMusique() &&
                prefsChauffeur.accepteBagages() == prefsPassager.accepteBagages();
    }

    /*
      Vérifie la compatibilité de l'itinéraire avec celui du passager.
      itinerairePassager Itinéraire du passager
      true si compatible, false sinon
     */
    public boolean estCompatibleAvecItineraire(Itineraire itinerairePassager) {
        if (itinerairePassager == null || trajet == null) {
            return false;
        }

        String trajetCourse = trajet.getEndroit();
        String trajetPassager = itinerairePassager.getEndroit();

        if (trajetCourse == null || trajetPassager == null) {
            return false;
        }

        // Convertir en minuscules pour la comparaison
        trajetCourse = trajetCourse.toLowerCase();
        trajetPassager = trajetPassager.toLowerCase();

        // Vérification améliorée : recherche de sous-chaînes ou points communs
        String[] pointsCourse = trajetCourse.split(" -> ");
        String[] pointsPassager = trajetPassager.split(" -> ");

        // Vérifier que les itinéraires ont le format "point1 -> point2"
        if (pointsCourse.length != 2 || pointsPassager.length != 2) {
            return false;
        }

        // Comparer les points de départ ou d'arrivée
        return pointsCourse[0].contains(pointsPassager[0]) || pointsCourse[1].contains(pointsPassager[1]);
    }

    /*
     Ajoute un passager à la course.
       passager Utilisateur à ajouter
     return true si ajouté, false sinon
     */
    public boolean ajouterPassager(User passager) {
        if (passager == null || passager.getBanni() || etat != EtatCourse.A_VENIR || passagers.size() >= 4) {
            return false;
        }

        if (!estCompatibleAvecPreferences(passager)) {
            return false;
        }

        List<Itineraire> itinerairesPassager = passager.getItineraire();
        if (itinerairesPassager == null || itinerairesPassager.isEmpty() || itinerairesPassager.get(0) == null) {
            return false;
        }

        if (!estCompatibleAvecItineraire(itinerairesPassager.get(0))) {
            return false;
        }

        passagers.add(passager);
        return true;
    }

    /*
      Démarre la course.
     */
    public void demarrerCourse() {
        if (this.etat == EtatCourse.A_VENIR) {
            this.etat = EtatCourse.EN_COURS;
            this.dateHeureDepart = LocalDateTime.now();
        }
    }

    /*
      Termine la course.
     */
    public void terminerCourse() {
        if (this.etat == EtatCourse.EN_COURS) {
            this.etat = EtatCourse.TERMINEE;
            this.dateHeureArrivee = LocalDateTime.now();
        }
    }

    /*
      Ajoute une évaluation à la course.
      evaluation Évaluation à ajouter
     */
    public void ajouterEvaluation(Rating evaluation) {
        if (etat == EtatCourse.TERMINEE && evaluation != null) {
            evaluations.add(evaluation);
            conducteur.addRating(evaluation);
        }
    }

    /**
     * Annule la course.
     */
    public void annulerCourse() {
        if (this.etat == EtatCourse.A_VENIR) {
            this.etat = EtatCourse.ANNULEE;
        }
    }

    // Getters
    public EtatCourse getEtat() { return etat; }
    public User getConducteur() { return conducteur; }
    public LocalDateTime getDateTimeDepart() { return dateHeureDepart; }
    public Itineraire getTrajet() { return trajet; }
    public long getId() { return id; }
    public TypeCourse getType() { return type; }
    public LocalDateTime getDateHeureArrivee() { return dateHeureArrivee; }
    public List<User> getPassagers() { return new ArrayList<>(passagers); }
    public List<Rating> getEvaluations() { return new ArrayList<>(evaluations); }

    @Override
    public String toString() {
        return "Course{" +
                "conducteur=" + conducteur.getNom() +
                ", état=" + etat +
                ", trajet=" + trajet.getEndroit() +
                ", passagers=" + passagers.size() +
                ", Type=" + type +
                '}';
    }
    
    
    
   
    
    
    
    
}