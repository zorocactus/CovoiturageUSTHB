package model;

import java.io.Serializable;
import Enum.TypeCourse;
public class Etudiant extends User implements Serializable {
  private static final long serialVersionUID = 1L;
    private String anneeAdmission;
    private String faculte;
    private String specialite;

    public Etudiant(String nom, String prenom, String matricule, double reputation, String statut,
                    Itineraire itineraire, Preferences preferences, TypeCourse typeCourse,
                    String anneeAdmission, String faculte, String specialite) {
        super(nom, prenom, matricule);
        this.anneeAdmission = anneeAdmission;
        this.faculte = faculte;
        this.specialite = specialite;
    }
    public String getFaculte() {
    return faculte;
    }
    public String getSpecialite() {
        return this.specialite;
    }
    @Override
    public String toString() {
        return "Etudiant{" +
                "nom='" + getNom() + '\'' +
                ", prenom='" + getPrenom() + '\'' +
                ", matricule='" + getMatricule() + '\'' +
                ", reputation=" + getAverageRating() +
                ", statut='" + getStatut() + '\'' +
                ", itineraire=" + getItineraire() +
                ", preferences=" + getPreferences() +
                ", disponibilite=" + getDisponibilite() +
                ", typeCourse='" + getTypeCourse() + '\'' +
                ", anneeAdmission='" + anneeAdmission + '\'' +
                ", faculte='" + faculte + '\'' +
                  ", Specialite ='" + specialite + '\'' +
                '}';
    }
}