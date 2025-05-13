package tn.controllers.Motifreclamation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.entities.MotifReclamation;
import tn.services.ServiceMotif;

import java.io.IOException;

public class GestionMotifReclamationController {

    @FXML
    private TableView<MotifReclamation> tableView;

    @FXML
    private TableColumn<MotifReclamation, Integer> colId;

    @FXML
    private TableColumn<MotifReclamation, String> colNom;

    @FXML
    private TextField searchField;

    private ObservableList<MotifReclamation> motifs = FXCollections.observableArrayList();
    private ServiceMotif motifService = new ServiceMotif();

    @FXML
    public void initialize() {
        // Configuration des colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));

        // Chargement initial des données
        refreshTable();

        // Écouteur de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterList(newValue);
        });
    }

    private void refreshTable() {
        try {
            motifs.clear();
            motifs.addAll(motifService.afficher());

            // Tri initial
            motifs.sort((m1, m2) -> m1.getNom().compareToIgnoreCase(m2.getNom()));

            tableView.setItems(motifs);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du chargement des données : " + e.getMessage());
        }
    }

    private void filterList(String keyword) {
        ObservableList<MotifReclamation> filteredList = FXCollections.observableArrayList();

        for (MotifReclamation motif : motifs) {
            if (motif.getNom().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(motif);
            }
        }

        // Tri alphabétique par nom
        filteredList.sort((m1, m2) -> m1.getNom().compareToIgnoreCase(m2.getNom()));

        tableView.setItems(filteredList);
    }


    @FXML
    private void handleAjouter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Motifreclamation/AjouterMotifReclamation.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter un motif de réclamation");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            refreshTable();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du chargement de la vue : " + e.getMessage());
        }
    }

    @FXML
    private void handleModifier() {
        MotifReclamation selectedMotif = tableView.getSelectionModel().getSelectedItem();
        if (selectedMotif == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner un motif à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Motifreclamation/ModifierMotifReclamation.fxml"));
            Parent root = loader.load();

            ModifierMotifReclamationController controller = loader.getController();
            controller.setMotifReclamation(selectedMotif);

            Stage stage = new Stage();
            stage.setTitle("Modifier un motif de réclamation");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            refreshTable();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du chargement de la vue : " + e.getMessage());
        }
    }

    @FXML
    private void handleSupprimer() {
        MotifReclamation selectedMotif = tableView.getSelectionModel().getSelectedItem();
        if (selectedMotif == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner un motif à supprimer.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Motifreclamation/SupprimerMotifReclamation.fxml"));
            Parent root = loader.load();

            SupprimerMotifReclamationController controller = loader.getController();
            controller.setMotifReclamation(selectedMotif);

            Stage stage = new Stage();
            stage.setTitle("Supprimer un motif de réclamation");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            if (controller.isSuppressionConfirmee()) {
                motifService.delete(selectedMotif.getId());
                refreshTable();
            }
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du chargement de la vue : " + e.getMessage());
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la suppression : " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
