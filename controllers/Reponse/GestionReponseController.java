package tn.controllers.Reponse;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.entities.Reponse;
import tn.services.ServiceReponse;

import java.io.IOException;
import java.sql.SQLException;

public class GestionReponseController {

    @FXML
    private TableView<Reponse> tableView;

    @FXML
    private TableColumn<Reponse, Integer> colId;

    @FXML
    private TableColumn<Reponse, Integer> colIdReclamation;

    @FXML
    private TableColumn<Reponse, String> colReponse;

    private final ServiceReponse serviceReponse = new ServiceReponse();
    private final ObservableList<Reponse> reponses = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIdReclamation.setCellValueFactory(new PropertyValueFactory<>("idReclamation"));
        colReponse.setCellValueFactory(new PropertyValueFactory<>("reponse"));

        refreshTable();
    }

    private void refreshTable() {
        try {
            reponses.clear();
            reponses.addAll(serviceReponse.afficher());
            tableView.setItems(reponses);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des réponses", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleAjouter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reponse/AjouterReponse.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter une réponse");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            refreshTable();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du chargement de la vue d'ajout", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleModifier() {
        Reponse selectedReponse = tableView.getSelectionModel().getSelectedItem();
        if (selectedReponse == null) {
            showAlert("Attention", "Veuillez sélectionner une réponse à modifier", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reponse/ModifierReponse.fxml"));
            Parent root = loader.load();

            ModifierReponseController controller = loader.getController();
            controller.setReponse(selectedReponse); // Transfert de l’objet à modifier

            Stage stage = new Stage();
            stage.setTitle("Modifier une réponse");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            refreshTable();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du chargement de la vue de modification", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleSupprimer() {
        Reponse selectedReponse = tableView.getSelectionModel().getSelectedItem();
        if (selectedReponse == null) {
            showAlert("Attention", "Veuillez sélectionner une réponse à supprimer", Alert.AlertType.WARNING);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réponse ?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                serviceReponse.deleteReponse(selectedReponse.getId());
                refreshTable();
                showAlert("Succès", "Réponse supprimée avec succès", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de la suppression de la réponse", Alert.AlertType.ERROR);
            }
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
