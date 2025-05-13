package tn.controllers.Reponse;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import tn.services.ServiceReponse;

public class SupprimerReponseController {

    @FXML
    private ComboBox<Integer> comboReponse;
    @FXML
    private Button btnSupprimer;

    private ServiceReponse serviceReponse = new ServiceReponse();

    @FXML
    private void initialize() {
        // Remplir le comboBox avec les réponses existantes
        comboReponse.setItems(serviceReponse.getAllReponsesId());
    }

    @FXML
    private void handleSupprimerReponse() {
        Integer idReponse = comboReponse.getValue();

        if (idReponse != null && idReponse > 0) {
            try {
                serviceReponse.deleteReponse(idReponse);

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Réponse supprimée");
                alert.setHeaderText(null);
                alert.setContentText("La réponse a été supprimée avec succès.");
                alert.showAndWait();

                // Mettre à jour le ComboBox après suppression
                comboReponse.setItems(serviceReponse.getAllReponsesId());

            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur de suppression");
                alert.setHeaderText(null);
                alert.setContentText("Erreur lors de la suppression : " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une réponse à supprimer.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAnnuler() {
        // Code pour annuler la suppression (par exemple, fermer la fenêtre)
    }
}
