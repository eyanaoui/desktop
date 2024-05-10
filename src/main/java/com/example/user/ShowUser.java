package com.example.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.User;
import utils.SessionManager;

import java.net.URL;
import java.util.ResourceBundle;

public class ShowUser implements Initializable {

    @FXML
    private Label idLabel;

    @FXML
    private Label nomLabel;

    @FXML
    private Label prenomLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label rolesLabel;

    @FXML
    private Label loginLabel;

    @FXML
    private Button upbtn;



    User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.currentUser = SessionManager.getSession().getUser();
        // Initialize the labels with user data
        if (currentUser != null) {
            nomLabel.setText(currentUser.getNom_user());
            prenomLabel.setText(currentUser.getPrenom_user());
            emailLabel.setText(currentUser.getEmail());

        }
    }

    public void setUser(User user) {
        this.currentUser = user;
        initialize(null, null); // Update labels when user is set
    }

    private void navigateToUpdateUser(User user) {
        try {
            // Load the UpdateUser.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateUser.fxml"));
            javafx.scene.Parent root = loader.load();

            // Access the controller and pass the selected user to it
            UpdateUser controller = loader.getController();
            controller.initData(user);


            // Show the scene containing the UpdateUser.fxml file
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void upbtnOnAction(ActionEvent event) {

        navigateToUpdateUser(currentUser);

    }
}
