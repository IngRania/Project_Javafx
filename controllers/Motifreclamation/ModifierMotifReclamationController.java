package tn.controllers.Motifreclamation;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import tn.entities.MotifReclamation;
import tn.services.ServiceMotif;

public class ModifierMotifReclamationController {

    @FXML
    private TextField txtIdMotif;

    @FXML
    private TextField txtNomMotif;

    private final ServiceMotif serviceMotif = new ServiceMotif();

    @FXML
    private void handleModifier() {
        // Vérifier si les champs sont remplis
        if (txtIdMotif.getText().isEmpty() || txtNomMotif.getText().isEmpty()) {
            showAlert(AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        try {
            // Récupérer les valeurs des champs
            int idMotif = Integer.parseInt(txtIdMotif.getText());
            String nomMotif = txtNomMotif.getText().trim();

            // Vérifier si le nom n'est pas vide après suppression des espaces
            if (nomMotif.isEmpty()) {
                showAlert(AlertType.ERROR, "Erreur", "Le nom du motif ne peut pas être vide.");
                return;
            }

            // Créer l'objet MotifReclamation à modifier
            MotifReclamation motif = new MotifReclamation(idMotif, nomMotif);
            serviceMotif.update(motif); // Appel de la méthode modifier

            // Afficher un message de confirmation
            showAlert(AlertType.INFORMATION, "Succès", "Le motif a été modifié avec succès.");

        } catch (NumberFormatException e) {
            // Gérer l'erreur si l'ID n'est pas un nombre valide
            showAlert(AlertType.ERROR, "Erreur", "L'ID doit être un nombre valide.");
        } catch (Exception e) {
            // Gérer toute autre exception
            showAlert(AlertType.ERROR, "Erreur", "Une erreur est survenue : " + e.getMessage());
        }
    }

    @FXML
    private void handleAnnuler() {
        // Fermer la fenêtre ou effectuer une autre action d'annulation
        System.out.println("Modification annulée.");
    }

    public void setMotifReclamation(MotifReclamation selectedMotif) {
        if (selectedMotif != null) {
            txtIdMotif.setText(String.valueOf(selectedMotif.getId()));
            txtNomMotif.setText(selectedMotif.getNom());
        }
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}