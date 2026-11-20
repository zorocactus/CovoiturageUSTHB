package model;

import java.io.Serializable;

public class Preferences implements Serializable {
private static final long serialVersionUID = 1L;
private PreferenceSexe preferenceSexe;
    private boolean accepteBagages;
    private boolean accepteMusique;

    public Preferences(PreferenceSexe preferenceSexe, boolean accepteBagages, boolean accepteMusique) {
        this.preferenceSexe = preferenceSexe;
        this.accepteBagages = accepteBagages;
        this.accepteMusique = accepteMusique;
    }

    // Getters
    public PreferenceSexe getPreferenceSexe() {
        return preferenceSexe;
    }

    public boolean accepteBagages() {
        return accepteBagages;
    }

    public boolean accepteMusique() {
        return accepteMusique;
    }

    // Setters
    public void setPreferenceSexe(PreferenceSexe preferenceSexe) {
        this.preferenceSexe = preferenceSexe;
    }

    public void setAccepteBagages(boolean accepteBagages) {
        this.accepteBagages = accepteBagages;
    }

    public void setAccepteMusique(boolean accepteMusique) {
        this.accepteMusique = accepteMusique;
    }

    @Override
    public String toString() {
        return "Préférence [Sexe: " + preferenceSexe +
               ", Bagages: " + (accepteBagages ? "Oui" : "Non") +
               ", Musique: " + (accepteMusique ? "Oui" : "Non") + "]";
    }
}
