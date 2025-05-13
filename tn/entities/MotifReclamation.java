package tn.entities;

import java.util.Objects;
public class MotifReclamation {
    private int id;
    private String nom;

    public MotifReclamation() {}

    public MotifReclamation(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return nom; // Cela permet d’afficher juste le nom dans le ComboBox
    }
}
