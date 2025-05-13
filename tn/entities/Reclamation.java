package tn.entities;

import java.time.LocalDateTime;

public class Reclamation {
    private int id;
    private int idMotif;
    private int idUser;
    private String contenu;
    private String statut;
    private LocalDateTime createdAt;

    public Reclamation() {}

    public Reclamation(int idMotif, int idUser, String contenu, String statut, LocalDateTime createdAt) {
        this.idMotif = idMotif;
        this.idUser = idUser;
        this.contenu = contenu;
        this.statut = statut;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMotif() {
        return idMotif;
    }

    public void setIdMotif(int idMotif) {
        this.idMotif = idMotif;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setMotifNom(String motifNom) {
        // Ajoutez un champ motifNom si nécessaire
    }
}