package tn.services;

import tn.entities.MotifReclamation;
import tn.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceMotif {
    private final Connection connection;

    public ServiceMotif() {
        connection = MyDatabase.getInstance().getConnection();
    }

    public void ajouter(MotifReclamation motif) {
        if (motif == null || motif.getNom() == null || motif.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du motif ne peut pas être vide.");
        }

        String query = "INSERT INTO motif_reclamation (nom) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, motif.getNom());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    motif.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du motif : " + e.getMessage(), e);
        }
    }

    public void update(MotifReclamation motif) {
        if (motif == null || motif.getNom() == null || motif.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du motif ne peut pas être vide.");
        }

        String query = "UPDATE motif_reclamation SET nom = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, motif.getNom());
            statement.setInt(2, motif.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du motif : " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        String query = "DELETE FROM motif_reclamation WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du motif : " + e.getMessage(), e);
        }
    }

    public List<MotifReclamation> afficher() {
        List<MotifReclamation> motifs = new ArrayList<>();
        String query = "SELECT * FROM motif_reclamation";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                MotifReclamation motif = new MotifReclamation();
                motif.setId(resultSet.getInt("id"));
                motif.setNom(resultSet.getString("nom"));
                motifs.add(motif);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des motifs : " + e.getMessage(), e);
        }
        return motifs;
    }

    public MotifReclamation getMotifById(int id) {
        String query = "SELECT * FROM motif_reclamation WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    MotifReclamation motif = new MotifReclamation();
                    motif.setId(resultSet.getInt("id"));
                    motif.setNom(resultSet.getString("nom"));
                    return motif;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du motif par ID : " + e.getMessage(), e);
        }
        return null;
    }
}