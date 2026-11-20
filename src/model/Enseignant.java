package model;
import java.io.Serializable;
import Enum.TypeCourse;

public class Enseignant extends User implements Serializable{
  private static final long serialVersionUID = 1L;
    private String anneeRecrutement;
    private String faculte;

    public Enseignant(String nom, String prenom, String matricule, double reputation, String statut,
                      Itineraire itineraire, Preferences preferences,  TypeCourse typeCourse,
                      String anneeRecrutement, String faculte) {
        super(nom, prenom, matricule);
        this.anneeRecrutement = anneeRecrutement;
        this.faculte = faculte;
    }
    public String getFaculte() {
    return faculte;
    }

    @Override
    public String toString() {
        return "Enseignant{" +
                "nom='" + getNom() + '\'' +
                ", prenom='" + getPrenom() + '\'' +
                ", matricule='" + getMatricule() + '\'' +
                ", reputation=" + getAverageRating() +
                ", statut='" + getStatut() + '\'' +
                ", itineraire=" + getItineraire() +
                ", preferences=" + getPreferences() +
                ", disponibilite=" + getDisponibilite() +
                ", typeCourse='" + getTypeCourse() + '\'' +
                ", anneeRecrutement='" + anneeRecrutement + '\'' +
                ", faculte='" + faculte + '\'' +
                '}';
    }
}