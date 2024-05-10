package com.example.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Post;
import service.ServicePost;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class FrontControllerC implements Initializable {
    @FXML
    private VBox chosenFruitCard;

    @FXML
    private GridPane grid;

    @FXML
    private Label categorieLable;

    @FXML
    private Label desriptionLable;

    @FXML
    private ImageView img;

    @FXML
    private Label timeLable;

    @FXML
    private Label titreLabel;

    @FXML
    private Button profilebtn;

    private Post selectedPost;
    private List<Post> postList = new ArrayList<>();
    private Image image;
    private MyListenerC myListener;

    private List<Post> getData() throws SQLException {
        ServicePost servicePost = new ServicePost();
        List<Post> PostList = servicePost.selectAll();

        return PostList;
    }

    private void setChosenFruit(Post post) {


        // Check if the resource URL is not null before creating the image
        URL imageUrl = getClass().getResource("/image/logo-removebg-preview.png");
        if (imageUrl != null) {
            image = new Image(imageUrl.toString());
            img.setImage(image);
        } else {
            // Handle the case where the image resource is not found
            System.out.println("Image resource not found!");
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            postList = getData(); // Populate the existing PostList
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        myListener = new MyListenerC() {
            @Override
            public void onClickListener(Post post) {
                setChosenFruit(post);
                selectedPost = post;
                System.out.println(selectedPost);
            }
        };

        if (postList.size() > 0) {
            setChosenFruit(postList.get(0));
            System.out.println(postList.size());
        }

        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < postList.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("post.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                itemPost itemController = fxmlLoader.getController();
                itemController.setData(postList.get(i), myListener);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row); //(child,column,row)
                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void navigateToAddPost() {
        try {
            // Load the AddPost.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AjouterPost.fxml"));
            Parent root = loader.load();

            // Show the scene containing the AddPost.fxml file
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Set a listener to execute code after the AddPost stage is closed
            stage.setOnHiding(event -> {
                // Call the refreshList method to update the grid with the latest data
                refreshList();
            });
            stage.showAndWait();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ajouterButtonOnAction(ActionEvent event) {
        navigateToAddPost();
    }

    public void refreshList() {
        try {
            // Clear the existing list of Post objects
            postList.clear();

            // Fetch the latest data from the database
            postList = getData();

            // Clear the existing items from the grid
            grid.getChildren().clear();

            // Reset row and column indices
            int column = 0;
            int row = 1;

            // Iterate through the updated PostList
            for (int i = 0; i < postList.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("post.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                itemPost itemController = fxmlLoader.getController();
                itemController.setData(postList.get(i), myListener);

                // Check if the column exceeds the limit
                if (column == 3) {
                    column = 0;
                    row++;
                }

                // Add the anchorPane to the grid at the specified column and row
                grid.add(anchorPane, column++, row);

                // Set margins for the anchorPane
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void updateButtonClicked(ActionEvent event) {
        try {
            if (selectedPost == null) {
                // Alert the user to select a vehicle first
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Vehicle Selected");
                alert.setHeaderText(null);
                alert.setContentText("Please select a vehicle to update.");
                alert.showAndWait();
                return; // Exit the method if no vehicle is selected
            }

            // Load the UpdatePost.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("updatePost.fxml"));
            Parent root = loader.load();

            // Get the controller instance
            updatePostXML updatePostController = loader.getController();

            // Pass the selected vehicle to the controller
            updatePostController.initData(selectedPost);

            // Create a new stage for the UpdatePost scene
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Update Post");
            stage.setScene(new Scene(root));

            // Show the UpdatePost stage
            stage.showAndWait();

            // Optionally, refresh the list after updating
            refreshList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteButtonClicked(ActionEvent event) {
        // Check if a Post is selected
        if (selectedPost != null) {
            try {
                // Call the delete method from your ServicePost class to delete the selected Post
                ServicePost servicePost = new ServicePost();
                servicePost.deleteOne(selectedPost);

                // Optionally, refresh the list after deletion
                refreshList();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle any potential exceptions here
            }
        } else {
            // Alert the user to select a vehicle first
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Vehicle Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a vehicle to delete.");
            alert.showAndWait();
        }
    }



    private void navigateToShowUser() {
        try {
            // Load the ListUser.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowUser.fxml"));
            Parent root = loader.load();

            // Get the controller of ListUser.fxml
            ShowUser controller = loader.getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void profilebtnOnAction(ActionEvent event){
        navigateToShowUser();
    }

}