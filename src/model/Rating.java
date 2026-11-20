package model;

import java.io.Serializable;

public class Rating implements Serializable{
private static final long serialVersionUID = 1L;
    private int note; // entre 1 et 5
    private String commentaire;

    public Rating(int note, String commentaire) {
        if (note < 1 || note > 5) {
            throw new IllegalArgumentException("La note doit être entre 1 et 5");
        }
        this.note = note;
        this.commentaire = commentaire;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        if (note < 1 || note > 5) {
            throw new IllegalArgumentException("La note doit être entre 1 et 5");
        }
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}