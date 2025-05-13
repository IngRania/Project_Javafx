package tn.controllers.Motifreclamation;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.entities.MotifReclamation;

public class SupprimerMotifReclamationController {

    @FXML
    private TextField txtIdMotif; // Champ pour entrer l'ID du motif à supprimer

    private boolean suppressionConfirmee = false; // Variable pour suivre la confirmation

    // Méthode appelée par le bouton "Supprimer"
    @FXML
    private void handleSupprimerMotif() {
        String idMotifStr = txtIdMotif.getText();
        if (idMotifStr.isEmpty()) {
            // Si l'ID n'est pas fourni
            showAlert(AlertType.ERROR, "Erreur", "Veuillez entrer l'ID du motif à supprimer.");
        } else {
            try {
                int idMotif = Integer.parseInt(idMotifStr);
                // Logique pour supprimer le motif (utiliser votre service ici)
                System.out.println("Suppression du motif avec l'ID: " + idMotif);

                // Exemple d'alerte de confirmation
                showAlert(AlertType.INFORMATION, "Suppression réussie", "Le motif a été supprimé avec succès.");
                suppressionConfirmee = true; // Confirmer la suppression
                fermerFenetre(); // Fermer la fenêtre après suppression
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Erreur", "L'ID doit être un nombre valide.");
            }
        }
    }

    // Méthode appelée par le bouton "Annuler"
    @FXML
    private void handleAnnuler() {
        suppressionConfirmee = false; // Annuler la suppression
        txtIdMotif.clear(); // Effacer le champ texte
        System.out.println("Action annulée");
        showAlert(AlertType.INFORMATION, "Annulation", "L'action a été annulée.");
        fermerFenetre(); // Fermer la fenêtre
    }

    public void setMotifReclamation(MotifReclamation selectedMotif) {
        if (selectedMotif != null) {
            txtIdMotif.setText(String.valueOf(selectedMotif.getId()));
        }
    }

    public boolean isSuppressionConfirmee() {
        return suppressionConfirmee;
    }

    // Méthode utilitaire pour afficher des alertes
    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Méthode pour fermer la fenêtre
    private void fermerFenetre() {
        Stage stage = (Stage) txtIdMotif.getScene().getWindow();
        stage.close();
    }
}