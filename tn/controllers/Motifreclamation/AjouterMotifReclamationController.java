package tn.controllers.Motifreclamation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import tn.entities.MotifReclamation;
import tn.services.ServiceMotif;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterMotifReclamationController {

    @FXML
    private TextField txtNomMotif;

    private ServiceMotif serviceMotif;
    private boolean isSuccess = false;

    @FXML
    public void initialize() {
        serviceMotif = new ServiceMotif();
        setupTextFieldValidation();
    }

    private void setupTextFieldValidation() {
        txtNomMotif.textProperty().addListener((obs, old, niu) -> {
            if (niu != null && niu.length() > 50) {
                txtNomMotif.setText(old);
            }
        });
    }

    @FXML
    private void handleAjouterMotif() {
        if (!validateInput()) return;

        MotifReclamation motif = new MotifReclamation();
        motif.setNom(txtNomMotif.getText().trim());

        try {
            serviceMotif.ajouter(motif);
            isSuccess = true;
            showAlert("Succès", "Le motif a été ajouté avec succès.", AlertType.INFORMATION);
            fermerFenetre();
        } catch (Exception e) {
            // Si c'est bien une SQLException, on appelle ton gestionnaire spécialisé,
            // sinon on affiche un message générique.
            if (e instanceof SQLException) {
                handleDatabaseError((SQLException) e);
            } else {
                showAlert("Erreur", "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
            }
        }
    }

    private boolean validateInput() {
        String nom = txtNomMotif.getText().trim();
        if (nom.isEmpty()) {
            showAlert("Erreur de validation", "Le nom du motif ne peut pas être vide.", AlertType.ERROR);
            txtNomMotif.requestFocus();
            return false;
        }
        if (nom.length() < 3) {
            showAlert("Erreur de validation", "Le nom du motif doit contenir au moins 3 caractères.", AlertType.ERROR);
            txtNomMotif.requestFocus();
            return false;
        }
        return true;
    }

    private void handleDatabaseError(SQLException e) {
        String msg = e.getMessage().contains("Duplicate entry")
                ? "Un motif avec ce nom existe déjà."
                : "Une erreur est survenue lors de l'ajout : " + e.getMessage();
        showAlert("Erreur", msg, AlertType.ERROR);
    }

    @FXML
    private void handleAnnuler() {
        fermerFenetre();
    }

    @FXML
    private void handleRetour(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Motifreclamation/GestionMotifReclamation.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Gestion des Motifs de Réclamation");
            stage.setScene(new Scene(root));
            stage.show();

            Stage cur = (Stage)((Node)event.getSource()).getScene().getWindow();
            cur.close();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de revenir à la gestion : " + e.getMessage(), AlertType.ERROR);
        }
    }

    private void fermerFenetre() {
        Stage stage = (Stage) txtNomMotif.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String titre, String msg, AlertType type) {
        Alert a = new Alert(type);
        a.setTitle(titre);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
