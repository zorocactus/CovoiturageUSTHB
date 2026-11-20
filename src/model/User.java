package model;
import java.util.ArrayList;
import java.util.List; 
import Enum.*;
import java.io.Serializable;
import java.time.LocalDateTime;



public class User implements Serializable {
	  private static final long serialVersionUID = 1L;
    private String nom;
    private String prenom;
    private String matricule;
    private double reputation;
    private String statut; // "Passager" ou "Chauffeur"
    private Itineraire itinerairePassager;
    private List<Itineraire> itineraireChauffeur = new ArrayList<>();
    private Preferences preferences;
    private Disponibilite disponibilite;
    private TypeCourse typeCourse; // "aller", "retour", "aller-retour"
    private List<Rating> ratings = new ArrayList<>(); 
    private LocalDateTime derniereConnexion;// gestion des évaluations
    private boolean banni;
    
    
    
    
    
    
    
    
    

    //constructeur explicitepackage Enum;

public enum PreferenceSexe {
BOY,
GIRL,
  BOTH,

}
    public User(String nom, String prenom, String matricule) {
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
        this.reputation = 0.0; // Initialisation
        this.statut = "Passager"; //par défaut
        
    }
   
    public void addRating(Rating rating) {
        ratings.add(rating);
    }
       
   //Calcule et retourne la réputation moyenne de l'utilisateur
    public double getAverageRating() {
        if (ratings.isEmpty()) return 0.0;
        double sum = 0;
        for (Rating r : ratings) {
            sum += r.getNote();
            reputation =  Math.round((sum / ratings.size()) * 100.0) / 100.0; // 2 decimales
        }
        return reputation; 
    }
    public String getNom() {
        return nom;
    }
    public String getPrenom() {
    	return prenom;
    }
    
    public void setNom(String nouveauNom) {
        if (nouveauNom == null || nouveauNom.isBlank()) {
            this.nom = "Inconnu"; //par défaut si vide
        } else {
            this.nom = nouveauNom.trim(); // trim enlève les espaces inutiles " aziz   " -> "aziz"
        }
    }
    public void setPrenom(String prenom) {
    	 if (prenom == null || prenom.isBlank()) {
             this.prenom = "Inconnu"; //par défaut si vide
         } else {
             this.prenom = prenom.trim(); // trim enlève les espaces inutiles " aziz   " -> "aziz"
         }
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getStatut() {
        return statut;
    }


    // Méthodes métier
    public void devenirChauffeur() {
        this.statut = "Chauffeur";
    }

    public void devenirPassager() {
        this.statut = "Passager";
    }

    public TypeCourse getTypeCourse() {
        return typeCourse;
    }

    public void setTypeCourse(TypeCourse typeCourse) {
        this.typeCourse = typeCourse;
    }
    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;}

    // Disponibilite
    public Disponibilite getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(Disponibilite disponibilite) {
        this.disponibilite = disponibilite;
    }

    // Itineraire
    public List<Itineraire> getItineraire() {
        if (isChauffeur()) {
            return itineraireChauffeur;
        } else {
            return List.of(itinerairePassager); // on le met dans une liste pour unifier le type
        }
    }

   

    public void setItinerairePassager(Itineraire itinerairePassager) {
        this.itinerairePassager = itinerairePassager;
    }

    
    public void setItineraireChauffeur(List<Itineraire> itineraireChauffeur) {
        this.itineraireChauffeur = itineraireChauffeur;
    }
    public boolean isChauffeur() {
        return this.statut.equals("Chauffeur");
    }
    public LocalDateTime getDerniereConnexion() {
        return derniereConnexion;
    }

    public void setDerniereConnexion(LocalDateTime derniereConnexion) {
        this.derniereConnexion = derniereConnexion;
    }
    public void setBanni(boolean banni) {
        this.banni = banni;
    }

    public boolean getBanni() {
    	return banni;
    }
    
 
    private boolean isAdmin = false;

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }
   
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return matricule != null && matricule.equals(user.matricule);
    }

    @Override
    public int hashCode() {
        return matricule != null ? matricule.hashCode() : 0;
    }
}