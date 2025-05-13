package tn.controllers.Reclamation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.entities.Reclamation;
import tn.services.ServiceReclamation;

import java.io.IOException;
import java.sql.SQLException;

public class GestionReclamationController {

    @FXML
    private TableView<Reclamation> tableView;
    @FXML
    private TableColumn<Reclamation, Integer> colId;
    @FXML
    private TableColumn<Reclamation, String> colTitre;
    @FXML
    private TableColumn<Reclamation, String> colStatut;
    @FXML
    private ComboBox<String> comboFiltreStatut;

    private final ObservableList<Reclamation> reclamations = FXCollections.observableArrayList();
    private final ServiceReclamation serviceReclamation = new ServiceReclamation();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        comboFiltreStatut.setItems(FXCollections.observableArrayList("Tous", "En attente", "Traité", "Rejeté"));
        comboFiltreStatut.getSelectionModel().selectFirst();
        comboFiltreStatut.setOnAction(e -> filtrerParStatut());

        chargerDonnees();
    }

    private void chargerDonnees() {
        try {
            reclamations.clear();
            reclamations.addAll(serviceReclamation.afficher());
            filtrerParStatut();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des réclamations", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleAjouter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/AjouterReclamation.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter une réclamation");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            chargerDonnees();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du chargement de la vue", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleModifier() {
        Reclamation selectedReclamation = tableView.getSelectionModel().getSelectedItem();
        if (selectedReclamation == null) {
            showAlert("Attention", "Veuillez sélectionner une réclamation à modifier", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/ModifierReclamation.fxml"));
            Parent root = loader.load();

            ModifierReclamationController controller = loader.getController();
            controller.setReclamation(selectedReclamation);

            Stage stage = new Stage();
            stage.setTitle("Modifier une réclamation");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            chargerDonnees();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du chargement de la vue", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleSupprimer() {
        Reclamation selectedReclamation = tableView.getSelectionModel().getSelectedItem();
        if (selectedReclamation == null) {
            showAlert("Attention", "Veuillez sélectionner une réclamation à supprimer", Alert.AlertType.WARNING);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réclamation ?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                serviceReclamation.supprimer(selectedReclamation.getId());
                chargerDonnees();
                showAlert("Succès", "Réclamation supprimée avec succès", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de la suppression", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void filtrerParStatut() {
        String statutSelectionne = comboFiltreStatut.getValue();
        if (statutSelectionne == null || statutSelectionne.equals("Tous")) {
            tableView.setItems(reclamations);
        } else {
            ObservableList<Reclamation> filtre = FXCollections.observableArrayList();
            for (Reclamation r : reclamations) {
                if (r.getStatut().equalsIgnoreCase(statutSelectionne)) {
                    filtre.add(r);
                }
            }
            tableView.setItems(filtre);
        }
    }

    @FXML
    private void handleRetour(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AfficherReclamation.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Réclamations");
            stage.show();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}