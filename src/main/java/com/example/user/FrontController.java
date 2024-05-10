package com.example.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import utils.SessionManager;

import static utils.SessionManager.clearSession;


public  class FrontController implements Initializable {
    @FXML
    private VBox chosenFruitCard;

    @FXML
    private GridPane grid;

    @FXML
    private ImageView img;

    @FXML
    private Label marqueLable;

    @FXML
    private Label matriculeLabel;

    @FXML
    private Label priceLable;

    @FXML
    private Label puissanceLable;

    @FXML
    private ScrollPane scroll;
    @FXML
    private Label loginLabel;
    @FXML
    private Button logoutButton;

    private Image image;
    User currentUser;









    @FXML
    void logoutButtonOnAction(ActionEvent event) {
        // Create a confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Êtes-vous sûr de vouloir vous déconnecter?");

        // Show the confirmation dialog
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // User confirmed, clear the session and navigate to the login window
                clearSession();
                // Close the current window
                Stage stage = (Stage) logoutButton.getScene().getWindow();
                stage.close();
                // Navigate to the login window
                navigateToLogin();
            }
        });
    }
    private void navigateToLogin() {
        try {
            // Load the UpdateUser.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            javafx.scene.Parent root = loader.load();

            // Access the controller and pass the selected user to it
            LoginController controller = loader.getController();


            // Show the scene containing the UpdateUser.fxml file
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}