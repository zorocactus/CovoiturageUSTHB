package model;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class PlageHoraire implements Serializable {
private static final long serialVersionUID = 1L;
    private DayOfWeek jourSemaine; // Pour HEBDOMADAIRE (ex: DayOfWeek.MONDAY)
    private LocalTime heureDebut;
    private LocalTime heureFin;

    public PlageHoraire(DayOfWeek jourSemaine, LocalTime heureDebut, LocalTime heureFin) {
        this.jourSemaine = jourSemaine;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }

    // Pour QUOTIDIEN (pas besoin de jourSemaine)
    public PlageHoraire(LocalTime heureDebut, LocalTime heureFin) {
        this(null, heureDebut, heureFin);
    }

    public boolean inclutHeure(LocalTime heure) {
        return !heure.isBefore(heureDebut) && !heure.isAfter(heureFin);
    }

    // Getters
    public DayOfWeek getJourSemaine() {
        return jourSemaine;
    }
    public LocalTime getHeureDebut() {
    return heureDebut;}
    public LocalTime getHeureFin() {
    return heureFin;}
    }