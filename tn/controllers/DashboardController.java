package tn.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class DashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    private void handleGestionReclamation() {
        loadContent("/Reclamation/GestionReclamation.fxml");
    }

    @FXML
    private void handleGestionReponse() {
        loadContent("/Reponse/GestionReponse.fxml");
    }

    @FXML
    private void handleGestionMotifReclamation() {
        loadContent("/Motifreclamation/GestionMotifReclamation.fxml");
    }

    @FXML
    private void handleDeconnexion() {
        try {
            // Fermer l'application
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.close();  // Ferme la fenêtre actuelle
        } catch (Exception e) {
            System.err.println("Erreur lors de la déconnexion : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadContent(String fxmlPath) {
        try {
            Node content = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(content);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'interface : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
