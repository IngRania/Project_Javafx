package tn.controllers.Reclamation;

import tn.entities.Reclamation;
import tn.entities.MotifReclamation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.services.ServiceMotif;
import tn.services.ServiceReclamation;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifierReclamationController implements Initializable {

    @FXML
    private TextField idTF;
    @FXML
    private TextField utilisateurTF;
    @FXML
    private ComboBox<MotifReclamation> motifComboBox;
    @FXML
    private TextArea contenuTA;
    @FXML
    private ComboBox<String> statutComboBox;

    private final ServiceReclamation serviceReclamation = new ServiceReclamation();
    private final ServiceMotif serviceMotif = new ServiceMotif();
    private Reclamation reclamationToModify;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Charger les motifs
        motifComboBox.getItems().addAll(serviceMotif.afficher());

        // Configurer le ComboBox pour afficher le nom du motif
        motifComboBox.setCellFactory(param -> new ListCell<MotifReclamation>() {
            @Override
            protected void updateItem(MotifReclamation item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNom());
                }
            }
        });

        // Ajouter les statuts possibles
        statutComboBox.getItems().addAll("En attente", "En cours", "Résolue", "Annulée");
    }

    public void setReclamation(Reclamation reclamation) {
        this.reclamationToModify = reclamation;
        populateFields();
    }

    private void populateFields() {
        if (reclamationToModify != null) {
            idTF.setText(String.valueOf(reclamationToModify.getId()));
            utilisateurTF.setText(String.valueOf(reclamationToModify.getIdUser()));
            contenuTA.setText(reclamationToModify.getContenu());
            statutComboBox.setValue(reclamationToModify.getStatut());

            // Sélectionner le bon motif
            MotifReclamation motif = serviceMotif.getMotifById(reclamationToModify.getIdMotif());
            motifComboBox.setValue(motif);
        }
    }

    @FXML
    private void handleModifier() {
        try {
            // Validation des champs
            if (utilisateurTF.getText().trim().isEmpty() ||
                    motifComboBox.getValue() == null ||
                    contenuTA.getText().trim().isEmpty() ||
                    statutComboBox.getValue() == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs", Alert.AlertType.ERROR);
                return;
            }

            Reclamation r = new Reclamation();
            r.setId(Integer.parseInt(idTF.getText()));
            r.setIdUser(Integer.parseInt(utilisateurTF.getText()));
            r.setIdMotif(motifComboBox.getValue().getId());
            r.setContenu(contenuTA.getText().trim());
            r.setStatut(statutComboBox.getValue());

            // Appel de la méthode modifier
            serviceReclamation.modifier(r);

            showAlert("Succès", "Réclamation modifiée avec succès", Alert.AlertType.INFORMATION);
            fermerFenetre();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID utilisateur doit être un nombre", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la modification de la réclamation : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void handleAnnuler() {
        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) idTF.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}