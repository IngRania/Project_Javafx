package tn.entities;

public class Reponse {
    private int id;
    private int idReclamation;
    private String reponse;

    public Reponse() {}

    public Reponse(int idReclamation, String reponse) {
        this.idReclamation = idReclamation;
        this.reponse = reponse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdReclamation() {
        return idReclamation;
    }

    public void setIdReclamation(int idReclamation) {
        this.idReclamation = idReclamation;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }
}