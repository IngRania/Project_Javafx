package tn.controllers.Reponse;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import tn.entities.Reponse;
import tn.services.ServiceReponse;
import java.io.IOException;
import javafx.collections.FXCollections;

public class AjouterReponseController {

    @FXML
    private ComboBox<Integer> comboReclamation;

    @FXML
    private TextArea txtReponse;

    @FXML
    private ComboBox<String> comboColor;

    @FXML
    private ComboBox<String> comboFontStyle;

    @FXML
    private ComboBox<String> comboFontFamily;

    @FXML
    private ComboBox<String> comboFontSize;

    @FXML
    private ComboBox<String> comboTextDecoration;

    @FXML
    private Button btnAjouter;

    private final ServiceReponse serviceReponse = new ServiceReponse();

    @FXML
    private void initialize() {
        // Remplir le ComboBox avec les IDs des réclamations existantes
        comboReclamation.setItems(serviceReponse.getAllReclamationsId());

        // Initialiser le ComboBox pour les couleurs
        comboColor.setItems(FXCollections.observableArrayList(
                "black", "gray", "white", "red", "orange", "yellow", "green", "blue", "purple", "brown",
                "crimson", "darkred", "darkorange", "gold", "lime", "cyan", "navy", "violet", "sienna",
                "darkgray", "lightgray", "firebrick", "coral", "goldenrod", "forestgreen", "teal", "indigo", "maroon", "salmon"
        ));
        comboColor.setPromptText("Sélectionner une couleur");

        // Écouteur pour changer la couleur du texte
        comboColor.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                txtReponse.setStyle(getCurrentStyle() + ";-fx-text-fill: " + newValue + ";");
            }
        });

        // Initialiser le ComboBox pour les styles de police
        comboFontStyle.setItems(FXCollections.observableArrayList(
                "Normal", "Italique", "Gras", "Gras Italique"
        ));
        comboFontStyle.setPromptText("Sélectionner un style");

        // Écouteur pour changer le style de police
        comboFontStyle.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                String fontStyle = "normal";
                String fontWeight = "normal";
                switch (newValue) {
                    case "Italique":
                        fontStyle = "italic";
                        break;
                    case "Gras":
                        fontWeight = "bold";
                        break;
                    case "Gras Italique":
                        fontStyle = "italic";
                        fontWeight = "bold";
                        break;
                }
                txtReponse.setStyle(getCurrentStyle() + ";-fx-font-style: " + fontStyle + ";-fx-font-weight: " + fontWeight + ";");
            }
        });

        // Initialiser le ComboBox pour les familles de police
        comboFontFamily.setItems(FXCollections.observableArrayList(
                "Arial", "Times New Roman", "Courier New", "Verdana", "Georgia", "Trebuchet MS", "Comic Sans MS", "Impact", "System"
        ));
        comboFontFamily.setPromptText("Sélectionner une police");

        // Écouteur pour changer la famille de police
        comboFontFamily.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                txtReponse.setStyle(getCurrentStyle() + ";-fx-font-family: '" + newValue + "';");
            }
        });

        // Initialiser le ComboBox pour les tailles de police
        comboFontSize.setItems(FXCollections.observableArrayList(
                "10px", "12px", "14px", "16px", "18px", "20px", "24px", "28px", "32px"
        ));
        comboFontSize.setPromptText("Sélectionner une taille");

        // Écouteur pour changer la taille de police
        comboFontSize.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                txtReponse.setStyle(getCurrentStyle() + ";-fx-font-size: " + newValue + ";");
            }
        });

        // Initialiser le ComboBox pour les décorations de texte
        comboTextDecoration.setItems(FXCollections.observableArrayList(
                "Aucune", "Souligné", "Barré"
        ));
        comboTextDecoration.setPromptText("Sélectionner une décoration");

        // Écouteur pour changer la décoration de texte
        comboTextDecoration.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                String decoration = "none";
                switch (newValue) {
                    case "Souligné":
                        decoration = "underline";
                        break;
                    case "Barré":
                        decoration = "line-through";
                        break;
                }
                txtReponse.setStyle(getCurrentStyle() + ";-fx-text-decoration: " + decoration + ";");
            }
        });
    }

    // Méthode utilitaire pour conserver les styles existants
    private String getCurrentStyle() {
        String currentStyle = txtReponse.getStyle();
        if (currentStyle == null) {
            return "";
        }
        return currentStyle;
    }

    @FXML
    private void handleAjouterReponse() {
        Integer idReclamation = comboReclamation.getValue();
        String reponse = txtReponse.getText();

        if (idReclamation != null && !reponse.trim().isEmpty()) {
            Reponse newReponse = new Reponse(idReclamation, reponse);
            try {
                serviceReponse.ajouter(newReponse);
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Réponse ajoutée");
                alert.setHeaderText(null);
                alert.setContentText("La réponse a été ajoutée avec succès.");
                alert.showAndWait();
                txtReponse.clear();
                comboReclamation.getSelectionModel().clearSelection();
                comboColor.getSelectionModel().clearSelection();
                comboFontStyle.getSelectionModel().clearSelection();
                comboFontFamily.getSelectionModel().clearSelection();
                comboFontSize.getSelectionModel().clearSelection();
                comboTextDecoration.getSelectionModel().clearSelection();
                txtReponse.setStyle("-fx-text-fill: black;-fx-font-style: normal;-fx-font-weight: normal;-fx-font-family: System;-fx-font-size: 14px;-fx-text-decoration: none;");
            } catch (Exception e) {
                e.printStackTrace();
                showError("Erreur lors de l'ajout de la réponse.");
            }
        } else {
            showError("Veuillez remplir tous les champs.");
        }
    }

    @FXML
    private void handleAnnuler() {
        txtReponse.clear();
        comboReclamation.getSelectionModel().clearSelection();
        comboColor.getSelectionModel().clearSelection();
        comboFontStyle.getSelectionModel().clearSelection();
        comboFontFamily.getSelectionModel().clearSelection();
        comboFontSize.getSelectionModel().clearSelection();
        comboTextDecoration.getSelectionModel().clearSelection();
        txtReponse.setStyle("-fx-text-fill: black;-fx-font-style: normal;-fx-font-weight: normal;-fx-font-family: System;-fx-font-size: 14px;-fx-text-decoration: none;");
    }

    @FXML
    private void handleRetour(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Reponse/GestionReponse.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Gestion des Réponses");
            stage.setScene(new Scene(root));
            stage.show();
            Stage current = (Stage) ((Node) event.getSource()).getScene().getWindow();
            current.close();
        } catch (IOException e) {
            showError("Impossible de revenir à la gestion : " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}