package tn.controllers.Reclamation;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

public class ReclamationInfoController {

    @FXML
    private TextField utilisateur;

    @FXML
    private TextField type;

    @FXML
    private TextArea message;

    public void setUtilisateur(String utilisateur) {
        this.utilisateur.setText(utilisateur);
    }

    public void setType(String type) {
        this.type.setText(type);
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }
}
