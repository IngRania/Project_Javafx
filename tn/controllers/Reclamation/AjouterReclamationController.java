package tn.controllers.Reclamation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import tn.entities.Reclamation;
import tn.services.ServiceReclamation;
import tn.services.TraductionService;
import tn.services.ContentModerationService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class AjouterReclamationController {

    @FXML
    private TextField utilisateurTF;

    @FXML
    private ComboBox<String> motifComboBox;

    @FXML
    private TextArea contenuTA;

    @FXML
    private ComboBox<String> statutComboBox;

    @FXML
    private ComboBox<String> langueComboBox;

    @FXML
    private ComboBox<String> langueSourceComboBox;

    private final ServiceReclamation serviceReclamation = new ServiceReclamation();
    private final TraductionService traductionService = new TraductionService();
    private final ContentModerationService contentModerationService = new ContentModerationService();
    private Map<String, Integer> motifMap; // nom -> id

    @FXML
    private void initialize() {
        try {
            chargerMotifs();
            statutComboBox.getItems().addAll("en attente", "traitée", "rejetée");
            // Initialize both language combo boxes
            langueSourceComboBox.getItems().addAll("Français", "Anglais", "Arabe");
            langueComboBox.getItems().addAll("Français", "Anglais", "Arabe");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'initialisation : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void AjouterReclamation() {
        try {
            if (utilisateurTF.getText().isEmpty() || motifComboBox.getValue() == null ||
                    contenuTA.getText().isEmpty() || statutComboBox.getValue() == null) {
                showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
                return;
            }

            // Check for inappropriate content
            try {
                if (contentModerationService.containsInappropriateContent(contenuTA.getText())) {
                    showAlert("Contenu inapproprié", 
                            "Le contenu de votre réclamation contient du langage inapproprié. " +
                            "Veuillez reformuler votre message de manière respectueuse.", 
                            Alert.AlertType.WARNING);
                    return;
                }
            } catch (IOException e) {
                System.out.println("Erreur lors de la vérification du contenu: " + e.getMessage());
                // Continue with the submission even if content moderation fails
            }

            int idUtilisateur;
            try {
                idUtilisateur = Integer.parseInt(utilisateurTF.getText());
            } catch (NumberFormatException e) {
                showAlert("Erreur", "L'ID utilisateur doit être un nombre valide.", Alert.AlertType.ERROR);
                return;
            }

            String motifNom = motifComboBox.getValue();
            Integer idMotif = motifMap.get(motifNom);
            if (idMotif == null) {
                showAlert("Erreur", "Motif inconnu.", Alert.AlertType.ERROR);
                return;
            }

            String contenu = contenuTA.getText();
            String statut = statutComboBox.getValue();

            Reclamation reclamation = new Reclamation();
            reclamation.setIdUser(idUtilisateur);
            reclamation.setIdMotif(idMotif);
            reclamation.setContenu(contenu);
            reclamation.setStatut(statut);

            serviceReclamation.ajouter(reclamation);
            showAlert("Succès", "Réclamation ajoutée avec succès.", Alert.AlertType.INFORMATION);

            // Réinitialisation des champs
            utilisateurTF.clear();
            motifComboBox.setValue(null);
            contenuTA.clear();
            statutComboBox.setValue(null);
            langueComboBox.setValue(null);
            langueSourceComboBox.setValue(null);

        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout de la réclamation : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleTraduire() {
        try {
            String texte = contenuTA.getText();
            String langueSource = langueSourceComboBox.getValue();
            String langueCible = langueComboBox.getValue();

            System.out.println("Début de la traduction dans le contrôleur:");
            System.out.println("Texte à traduire: " + texte);
            System.out.println("Langue source sélectionnée: " + langueSource);
            System.out.println("Langue cible sélectionnée: " + langueCible);

            if (texte.isEmpty() || langueSource == null || langueCible == null) {
                showAlert("Erreur", "Veuillez remplir le contenu et sélectionner les langues source et cible.", Alert.AlertType.WARNING);
                return;
            }

            // Map language names to language codes
            String sourceLangCode = getLangCode(langueSource);
            String targetLangCode = getLangCode(langueCible);

            // Ensure source and target languages are different
            if (sourceLangCode.equals(targetLangCode)) {
                showAlert("Erreur", "Veuillez sélectionner des langues différentes pour la source et la cible.", Alert.AlertType.ERROR);
                return;
            }

            System.out.println("Code de langue source: " + sourceLangCode);
            System.out.println("Code de langue cible: " + targetLangCode);

            try {
                String texteTraduit = traductionService.traduireTexte(texte, sourceLangCode, targetLangCode);
                System.out.println("Traduction réussie: " + texteTraduit);
                contenuTA.setText(texteTraduit);
                showAlert("Succès", "Texte traduit avec succès.", Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                System.out.println("Erreur lors de la traduction: " + e.getMessage());
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de la traduction : " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            System.out.println("Erreur inattendue: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Une erreur inattendue s'est produite : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private String getLangCode(String langue) {
        switch (langue.toLowerCase()) {
            case "anglais":
                return "en";
            case "arabe":
                return "ar";
            case "français":
                return "fr";
            default:
                return "fr";
        }
    }

    private void chargerMotifs() {
        try {
            motifMap = serviceReclamation.getMotifsAvecIds(); // map nom -> id
            motifComboBox.getItems().addAll(motifMap.keySet());
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des motifs : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleAnnuler() {
        utilisateurTF.clear();
        motifComboBox.setValue(null);
        contenuTA.clear();
        statutComboBox.setValue(null);
        langueComboBox.setValue(null);
        langueSourceComboBox.setValue(null);
        showAlert("Information", "Action annulée.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleRetour() {
        try {
            // Charger la vue GestionReclamation.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/GestionReclamation.fxml"));
            Parent root = loader.load();

            // Ouvrir dans une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Gestion des Réclamations");
            stage.setScene(new Scene(root));
            stage.show();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) utilisateurTF.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du retour : " + e.getMessage(), Alert.AlertType.ERROR);
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