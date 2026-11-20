package model;

import java.io.Serializable;

public class Itineraire implements Serializable {
private static final long serialVersionUID = 1L;
    private String endroit; // Lieu de ramassage ou de dépôt

    public Itineraire(String endroit) {
        this.endroit = endroit;
    }

    public String getEndroit() {
        return endroit;
    }

    public void setEndroit(String endroit) {
        this.endroit = endroit;
    }
}