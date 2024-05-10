package com.example.user;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Commentaire;
import models.Post;
import models.User;
import service.ServiceCommentaire;
import service.ServicePost;
import utils.SessionManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class AfficherCommentaire {
    @FXML
    private Button PostButton;


    @FXML
    private Label loginLabel;
    @FXML
    private TableColumn<Commentaire, String> auteur;

    @FXML
    private TableColumn<Commentaire, String> contenu;
    @FXML
    private Button Userbtn;
    @FXML
    private Button logoutButton;

    @FXML
    private AnchorPane mainForm;

    @FXML
    private TableView<Commentaire> table;


    User currentUser;

    public void initialize() {
        // Set cell value factories for the columns
        this.currentUser = SessionManager.getSession().getUser();
        loginLabel.setText(this.currentUser.getNom_user());
        auteur.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuteur()));
        contenu.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContenu()));

        try {
            populateTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateTableView() throws SQLException {
        // Retrieve data from the database
        ServiceCommentaire serviceCommentaire = new ServiceCommentaire();
        List<Commentaire> commentaires = serviceCommentaire.selectAll();

        // Clear existing items in the TableView
        table.getItems().clear();

        // Add the retrieved data to the TableView
        table.getItems().addAll(commentaires);

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click detected
                Commentaire selectedCommentaire = table.getSelectionModel().getSelectedItem();
                if (selectedCommentaire != null) {
                    // Navigate to UpdateUser.fxml
                    navigateToUpdatePostXML(selectedCommentaire);
                }
            }
        });
    }



    @FXML
    void deleteOne(ActionEvent event) {
        // Get the selected commentaire from the table view
        Commentaire selectedCommentaire = table.getSelectionModel().getSelectedItem();
        if (selectedCommentaire != null) {
            try {
                // Call the deleteOne method with the selected commentaire
                ServiceCommentaire serviceCommentaire = new ServiceCommentaire();
                serviceCommentaire.deleteOne(selectedCommentaire);
                // Show a confirmation message
                showAlert("Delete Success", "Commentaire deleted successfully.");
                // Refresh the table view after deletion
                populateTableView();
            } catch (SQLException ex) {
                // Show an error message if deletion fails
                showAlert("Delete Error", "Error deleting commentaire: " + ex.getMessage());
            }
        } else {
            // Show an alert if no commentaire is selected
            showAlert("No Selection", "Please select a Commentaire to delete.");
        }
    }

    /*@FXML
    void redirectToAjouterCommentaire(ActionEvent event) {
        try {
            // Load ajouterCommnentaire.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ajouterCommnentaire.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the event
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // Handle the IOException
            e.printStackTrace();
            showAlert("Error", "Failed to redirect to Ajouter: " + e.getMessage());
        }
    }*/

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void navigateToUpdatePostXML(Commentaire selectedCommentaire) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("updateCommentaire.fxml"));
            Parent root = loader.load();

            updateCommentaire controller = loader.getController();
            controller.initData(selectedCommentaire);
            controller.setAfficherCommentaire(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(event -> {
                try {
                    populateTableView();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open update window: " + e.getMessage());
        }
    }

    public void refreshList() {
        try {
            populateTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void navigateToAddCommentaire() {
        try {
            // Load the AddVoiture.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ajouterCommnentaire.fxml"));
            javafx.scene.Parent root = loader.load();

            // Show the scene containing the AddVoiture.fxml file
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Refresh the table view after adding a new voiture
            populateTableView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ajouterButtonOnAction(ActionEvent event) {
        navigateToAddCommentaire();
    }


    @FXML
    void PostButtonOnAction(ActionEvent event){

        Stage stage = (Stage) PostButton.getScene().getWindow();
        stage.close();
        // Navigate to the login window
        navigateToPost();    }
    private void navigateToPost() {
        try {
            // Load the ListVoiture.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("afficherPost.fxml"));
            javafx.scene.Parent root = loader.load();

            AfficherPostXML controller = loader.getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void navigateToListUser() {
        try {
            // Load the ListUser.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ListUser.fxml"));
            Parent root = loader.load();

            // Get the controller of ListUser.fxml
            ListUserController controller = loader.getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void UserbtnOnAction(ActionEvent event){
        navigateToListUser();
    }



}

