package tn.services;

import tn.entities.Reclamation;
import tn.utils.MyDatabase;

import java.sql.*;
import java.util.*;

public class ServiceReclamation {
    private final Connection cnx;

    public ServiceReclamation() {
        cnx = MyDatabase.getInstance().getConnection();
    }

    public void ajouter(Reclamation r) throws SQLException {
        if (r.getStatut() == null || r.getStatut().trim().isEmpty()) {
            r.setStatut("en attente");
        }
        if (r.getCreatedAt() == null) {
            r.setCreatedAt(java.time.LocalDateTime.now());
        }

        String sql = "INSERT INTO reclamation (id_motif, created_at, id_user, contenu, statut) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, r.getIdMotif());
            pst.setTimestamp(2, Timestamp.valueOf(r.getCreatedAt()));
            pst.setInt(3, r.getIdUser());
            pst.setString(4, r.getContenu());
            pst.setString(5, r.getStatut());
            pst.executeUpdate();
        }
    }

    // Récupération de la liste des noms de motifs (ancienne méthode)
    public List<String> recupererMotifs() throws SQLException {
        List<String> motifs = new ArrayList<>();
        String sql = "SELECT nom FROM motif_reclamation";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                motifs.add(rs.getString("nom"));
            }
        }
        return motifs;
    }

    // ✅ Nouvelle méthode : mapping nom → ID
    public Map<String, Integer> getMotifsAvecIds() throws SQLException {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT id, nom FROM motif_reclamation";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                map.put(rs.getString("nom"), rs.getInt("id"));
            }
        }
        return map;
    }

    public List<Reclamation> afficher() throws SQLException {
        List<Reclamation> list = new ArrayList<>();
        String sql = "SELECT r.*, m.nom AS motif_nom FROM reclamation r JOIN motif_reclamation m ON r.id_motif = m.id";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Reclamation r = new Reclamation();
                r.setId(rs.getInt("id"));
                r.setIdMotif(rs.getInt("id_motif"));
                r.setMotifNom(rs.getString("motif_nom")); // ajoute un champ motifNom si besoin
                r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                r.setIdUser(rs.getInt("id_user"));
                r.setContenu(rs.getString("contenu"));
                r.setStatut(rs.getString("statut"));
                list.add(r);
            }
        }
        return list;
    }

    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM reclamation WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    public void modifier(Reclamation r) throws SQLException {
        if (r == null || r.getContenu() == null || r.getContenu().trim().isEmpty()) {
            throw new IllegalArgumentException("Le contenu de la réclamation ne peut pas être vide.");
        }

        String sql = "UPDATE reclamation SET id_motif=?, id_user=?, contenu=?, statut=? WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, r.getIdMotif());
            pst.setInt(2, r.getIdUser());
            pst.setString(3, r.getContenu());
            pst.setString(4, r.getStatut());
            pst.setInt(5, r.getId());
            pst.executeUpdate();
        }
    }
} 