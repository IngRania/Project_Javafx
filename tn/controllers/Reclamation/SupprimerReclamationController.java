package tn.controllers.Reclamation;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import tn.services.ServiceReclamation;

import java.sql.SQLException;

public class SupprimerReclamationController {

    @FXML
    private TextField idTF;

    private final ServiceReclamation serviceReclamation = new ServiceReclamation();

    @FXML
    public void SupprimerReclamation() {
        try {
            int id = Integer.parseInt(idTF.getText());
            serviceReclamation.supprimer(id);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Réclamation supprimée !");
            alert.show();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
