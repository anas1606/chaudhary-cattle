package com.chaudhary.chaudharycattle.utils;

import com.chaudhary.chaudharycattle.ChaudharycattleApplication;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import java.io.Serializable;

@Component
public class CommanUtils implements Serializable {

    public static BorderPane landing_bp;
    public static String hashPassword(String password_plaintext) {
        String salt = BCrypt.gensalt(12);
        return(BCrypt.hashpw(password_plaintext, salt));
    }

    public static boolean checkPassword(String password_plaintext, String stored_hash) {
        boolean password_verified = false;

        if(null == stored_hash || !stored_hash.startsWith("$2a$"))
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");

        password_verified = BCrypt.checkpw(password_plaintext, stored_hash);

        return(password_verified);
    }

    public static void numberFormate(TextField tf){
        // force the field to be numeric only
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    tf.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
    public static void switchToScene (String fxmlPath, Button btn, int width, int height){
        new ChaudharycattleApplication().switchToScene(fxmlPath, btn, width, height);
    }
    public static void loadPage (String fxmlPath){
        new ChaudharycattleApplication().loadPage(landing_bp, fxmlPath);
    }

    public static void setLandingBorderPane (BorderPane bp){
        landing_bp = bp;
    }
    @FXML
    public static void informationAlert(String heading, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setStyle("-fx-font-size: 18");
        alert.setTitle("Information");
        alert.setHeaderText(heading);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public static String confirmationAlert(String heading, String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setStyle("-fx-font-size: 20");
        alert.setTitle("Confirmation");
        alert.setHeaderText(heading);
        alert.setContentText(message);
        return alert.showAndWait().get().getText();
    }
    @FXML
    public static void warningAlert(String heading, String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.getDialogPane().setStyle("-fx-font-size: 18");
        alert.setTitle("Warning");
        alert.setHeaderText(heading);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
