package model;

import java.io.Serializable;

public class ATS extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    private int anneeRecrutement;  // Changé de String à int pour cohérence
    private String serviceRattachement;
    private boolean isAdmin;

    // Constructeur simplifié
    public ATS(String nom, String prenom, String matricule, int anneeRecrutement, String serviceRattachement) {
        super(nom, prenom, matricule);
        this.anneeRecrutement = anneeRecrutement;
        this.serviceRattachement = serviceRattachement;
        this.isAdmin = true; // Par défaut, les ATS ont des droits admin
    }

    // Getters
    public int getAnneeRecrutement() {
        return anneeRecrutement;
    }

    public String getServiceRattachement() {
        return serviceRattachement;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    // Setter pour les droits admin
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    
    @Override
    public String toString() {
        return "ATS{" +
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
                ",serviceRattachement='" + serviceRattachement + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}