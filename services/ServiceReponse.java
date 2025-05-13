package tn.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tn.entities.Reponse;
import tn.utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ServiceReponse {
    private Connection cnx;

    public ServiceReponse() {
        cnx = MyDatabase.getInstance().getConnection();
    }

    public void ajouter(Reponse r) throws SQLException {
        String sql = "INSERT INTO reponse (id_reclamation, reponse) VALUES (?, ?)";
        PreparedStatement pst = cnx.prepareStatement(sql);
        pst.setInt(1, r.getIdReclamation());
        pst.setString(2, r.getReponse());
        pst.executeUpdate();
    }

    public void modifier(Reponse r) throws SQLException {
        String sql = "UPDATE reponse SET reponse=? WHERE id=?";
        PreparedStatement pst = cnx.prepareStatement(sql);
        pst.setString(1, r.getReponse());
        pst.setInt(2, r.getId());
        pst.executeUpdate();
    }

    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM reponse WHERE id=?";
        PreparedStatement pst = cnx.prepareStatement(sql);
        pst.setInt(1, id);
        pst.executeUpdate();
    }

    public List<Reponse> afficher() throws SQLException {
        List<Reponse> list = new ArrayList<>();
        String sql = "SELECT * FROM reponse";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            Reponse r = new Reponse();
            r.setId(rs.getInt("id"));
            r.setIdReclamation(rs.getInt("id_reclamation"));
            r.setReponse(rs.getString("reponse"));
            list.add(r);
        }
        return list;
    }

    public ObservableList<Reponse> getAllReponses() {
        ObservableList<Reponse> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM reponse";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Reponse r = new Reponse();
                r.setId(rs.getInt("id"));
                r.setIdReclamation(rs.getInt("id_reclamation"));
                r.setReponse(rs.getString("reponse"));
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public ObservableList<Integer> getAllReclamationsId() {
        ObservableList<Integer> list = FXCollections.observableArrayList();
        String sql = "SELECT id FROM reclamation";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ✅ Méthode manquante ajoutée : Récupérer tous les IDs des réponses
    public ObservableList<Integer> getAllReponsesId() {
        ObservableList<Integer> list = FXCollections.observableArrayList();
        String sql = "SELECT id FROM reponse";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ✅ Méthode bonus utile : Récupérer une réponse par ID
    public Reponse getReponseById(int id) {
        String sql = "SELECT * FROM reponse WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Reponse r = new Reponse();
                r.setId(rs.getInt("id"));
                r.setIdReclamation(rs.getInt("id_reclamation"));
                r.setReponse(rs.getString("reponse"));
                return r;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void deleteReponse(int id) throws SQLException {
        supprimer(id);
    }

}
