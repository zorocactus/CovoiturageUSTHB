package model;

import Enum.Typedisponibilite;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Disponibilite implements Serializable {
private static final long serialVersionUID = 1L;
    private Typedisponibilite type;
    private List<PlageHoraire> plagesHoraires;
    private LocalDate dateSpecifique; // Uniquement pour JOURNALIER

    
    // Constructeur pour QUOTIDIEN/HEBDOMADAIRE
    public Disponibilite(Typedisponibilite type, List<PlageHoraire> plagesHoraires) {
        this.type = type;
        this.plagesHoraires = plagesHoraires;
    }

    // Constructeur pour JOURNALIER (avec date spécifique)
    public Disponibilite(LocalDate dateSpecifique, List<PlageHoraire> plagesHoraires) {
        this.type = Typedisponibilite.JOURNALIER;
        this.dateSpecifique = dateSpecifique;
        this.plagesHoraires = plagesHoraires;
    }
    public boolean estDisponible(LocalDate date, LocalTime heure) {
    // Vérifie si le chauffeur est disponible à une date/heure donnée
    for (PlageHoraire plage : plagesHoraires) {
        // Vérifie si l'heure est dans la plage horaire
        boolean heureValide = !heure.isBefore(plage.getHeureDebut()) && 
                            !heure.isAfter(plage.getHeureFin());
        
        if (type == Typedisponibilite.QUOTIDIEN) {
            // Quotidien: on vérifie juste l'heure
            if (heureValide) return true;
        } 
        else if (type == Typedisponibilite.HEBDOMADAIRE) {
            // Hebdomadaire: on vérifie le jour + l'heure
            if (plage.getJourSemaine() == date.getDayOfWeek() && heureValide) {
                return true;
            }
        } 
        else if (type == Typedisponibilite.JOURNALIER) {
            // Journalier: on vérifie la date exacte + l'heure
            if (date.equals(dateSpecifique) && heureValide) {
                return true;
            }
        }
    }
    return false; // Aucune plage trouvée
}

    // Getters
    public Typedisponibilite getType() {
        return type;
    }

    public List<PlageHoraire> getPlagesHoraires() {
        return plagesHoraires;
    }}