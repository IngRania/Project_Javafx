package tn.controllers.Reponse;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import tn.entities.Reponse;
import tn.services.ServiceReponse;

public class ModifierReponseController {

    @FXML
    private ComboBox<Integer> comboReponse;

    @FXML
    private TextArea txtReponse;

    @FXML
    private Button btnModifier;

    private final ServiceReponse serviceReponse = new ServiceReponse();
    private Reponse reponseSelectionnee;

    @FXML
    private void initialize() {
        // Optionnel : chargé si on utilise le combo dans d'autres cas
        comboReponse.setItems(serviceReponse.getAllReponsesId());
    }

    // ✅ Méthode à appeler depuis GestionReponseController
    public void setReponse(Reponse reponse) {
        this.reponseSelectionnee = reponse;
        txtReponse.setText(reponse.getReponse());
        comboReponse.setValue(reponse.getIdReclamation()); // facultatif si vous souhaitez afficher le lien
    }

    @FXML
    private void handleModifierReponse() {
        if (reponseSelectionnee == null) {
            showError("Aucune réponse sélectionnée.");
            return;
        }

        String nouveauTexte = txtReponse.getText();
        if (nouveauTexte == null || nouveauTexte.trim().isEmpty()) {
            showError("Le champ réponse ne peut pas être vide.");
            return;
        }

        // Mise à jour de l'objet réponse
        reponseSelectionnee.setReponse(nouveauTexte);

        try {
            serviceReponse.modifier(reponseSelectionnee);
            showInfo("Réponse modifiée", "La réponse a été modifiée avec succès.");
            txtReponse.clear();
            comboReponse.getSelectionModel().clearSelection();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors de la modification de la réponse.");
        }
    }

    @FXML
    private void handleAnnuler() {
        txtReponse.clear();
        comboReponse.getSelectionModel().clearSelection();
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
