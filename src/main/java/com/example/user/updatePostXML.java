package com.example.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Post;
import service.ServicePost;
import utils.data;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class updatePostXML implements Initializable {

    @FXML
    private Label titre;

    @FXML
    private Label cate;

    @FXML
    private Label des;

    @FXML
    private Label all;

    @FXML
    private AnchorPane mainForm;

    @FXML
    private Button btAjouter;

    @FXML
    private TextField tfCategories;

    @FXML
    private TextArea tfDescription;

    @FXML
    private TextField tfTitre;


    @FXML
    private Button cancelButton;

    private Post selectedPost;

    private Image image;

    @FXML
    private ImageView imageP;

    @FXML
    private ImageView brandingImageView;





    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public void imageIm(ActionEvent actionEvent) {

        Post p = new Post();
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File","*png","*jpg"));
        File file = openFile.showOpenDialog(mainForm.getScene().getWindow());
        if(file != null)
        {
            data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 125,130,false , true);
            imageP.setImage(image);
        }
    }


    private final ServicePost sp = new ServicePost();






    public void initData(Post post) {
        selectedPost = post;
        tfTitre.setText(selectedPost.getTitre());
        tfCategories.setText(selectedPost.getCategorie());
        tfDescription.setText(selectedPost.getDescription());
    }
    @FXML
    void updateOne(ActionEvent event) {
        String selectedTitre = tfTitre.getText();
        String selectedCategories = tfCategories.getText();
        String selectedDescription = tfDescription.getText();

        // Validate required fields
        if (selectedTitre.isEmpty() || selectedCategories.isEmpty() || selectedDescription.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        // Modify the existing Post object with the updated values
        selectedPost.setTitre(selectedTitre);
        selectedPost.setCategorie(selectedCategories);
        selectedPost.setDescription(selectedDescription);
        if (image != null) {
            // Assuming setImagePath accepts a String representing the image path
            selectedPost.setImagePath(image.getUrl());
        }

        try {
            sp.updateOne(selectedPost); // Pass the modified Post object
            System.out.println("Post updated successfully.");
            showAlert("Success", "Post updated successfully.");

            // Refresh the list of posts in the main view
            if (afficherPostXML != null) {
                afficherPostXML.refreshList();
            }

            // Close the update post window
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to update post: " + e.getMessage());
            showAlert("Error", "Failed to update post: " + e.getMessage());
        }
    }





    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private AfficherPostXML afficherPostXML;

    public void setAfficherPostXML(AfficherPostXML afficherPostXML) {
        this.afficherPostXML = afficherPostXML;
    }

    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Image brandingImage = new Image(getClass().getResource("/image/logo-removebg-preview.png").toString());
            brandingImageView.setImage(brandingImage);
        } catch (Exception e) {
            e.printStackTrace();
        }}


}
