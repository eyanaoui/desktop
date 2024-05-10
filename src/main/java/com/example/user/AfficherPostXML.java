package com.example.user;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Post;
import models.User;
import service.ServicePost;
import utils.ExportPDF;
import utils.SessionManager;
import utils.exportExcelle;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static utils.SessionManager.clearSession;

public class AfficherPostXML implements Initializable {
    @FXML
    private Button commentaireButton;

    @FXML
    private Button sortButton;

    @FXML
    private Label all;

    @FXML
    private Button logoutButton;


    @FXML
    private Button btmodifier;

    @FXML
    private Button btsupprimer;

    @FXML
    private Label cate;

    @FXML
    private TableColumn<Post, Integer> idCol;

    @FXML
    private TableColumn<Post, String> categories;

    @FXML
    private Label des;

    @FXML
    private TableColumn<Post, String> description;

    @FXML
    private TableColumn<Post, String> image;

    @FXML
    private TableColumn<Post, String> id;

    @FXML
    private AnchorPane mainForm;

    @FXML
    private TableView<Post> table;

    @FXML
    private TableColumn<Post, String> titre;

    @FXML
    private Button supprimerButton;
    @FXML
    private Button btAjouter;
    @FXML
    private Pagination pagination;

    @FXML
    private TextField searchField;

    @FXML
    private Label loginLabel;


    private boolean isAscendingOrder = true;

    private final ServicePost servicePost = new ServicePost();
    private static final int ROWS_PER_PAGE = 3; // Number of rows per page
    private List<Post> allPosts; // List of all posts
    private ObservableList<Post> currentPagePosts; // Posts for the current page

    User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.currentUser = SessionManager.getSession().getUser();
        loginLabel.setText(this.currentUser.getNom_user());
        titre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        categories.setCellValueFactory(new PropertyValueFactory< >("categorie"));
        image.setCellValueFactory(new PropertyValueFactory<>("image_name"));
       // id.setCellValueFactory(new PropertyValueFactory<>("id"));

        pagination.setPageFactory(this::createPage);

        try {
            // Populate allPosts when initializing the controller
            allPosts = servicePost.selectAll();
            populateTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, allPosts.size());
        currentPagePosts = FXCollections.observableArrayList(allPosts.subList(fromIndex, toIndex));

        // Populate your table with currentPagePosts here
        // For example, if you have a TableView called table:
        table.setItems(currentPagePosts);

        return new AnchorPane(); // Return a placeholder node for now
    }

    @FXML
    private void searchPosts(ActionEvent event) {
        // Get the search criteria entered by the user from the TextField
        String searchTerm = searchField.getText().trim().toLowerCase();

        if (!searchTerm.isEmpty()) {
            // Filter the list of posts based on the search criteria
            List<Post> filteredPosts = table.getItems().stream()
                    .filter(post -> post.getTitre().toLowerCase().contains(searchTerm) ||
                            post.getDescription().toLowerCase().contains(searchTerm) ||
                            post.getCategorie().toLowerCase().contains(searchTerm))
                    .collect(Collectors.toList() );

            // Clear the TableView and add the filtered posts
            table.getItems().clear();
            table.getItems().addAll(filteredPosts);
        } else {
            // If the search term is empty, repopulate the TableView with all posts
            try {
                populateTableView(); // Call method to repopulate TableView
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to populate table view: " + e.getMessage());
            }
        }
    }

    private void populateTableView() throws SQLException {
        // Retrieve data from the database
        allPosts = servicePost.selectAll();

        // Clear existing items in the TableView
        table.getItems().clear();
        int pageCount = (int) Math.ceil((double) allPosts.size() / ROWS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0);
        createPage(0);

        // Set cell value factories for each column
        //idCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        titre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitre()));
        description.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        categories.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorie()));
        image.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getImage_name()));

        // Add the retrieved data to the TableView
        table.getItems().addAll(allPosts);

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click detected
                Post selectedPost = table.getSelectionModel().getSelectedItem();
                if (selectedPost != null) {
                    // Navigate to UpdateUser.fxml
                    navigateToUpdatePostXML(selectedPost);
                }
            }
        });
    }





    private void navigateToUpdatePostXML(Post post) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("updatePost.fxml"));
            Parent root = loader.load();

            updatePostXML controller = loader.getController();
            controller.initData(post);
            controller.setAfficherPostXML(this);

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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void deleteOne(ActionEvent event) {
        // Get the selected user from the table view
        Post selectedUser = table.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                // Call the deleteOne method with the selected user
                servicePost.deleteOne(selectedUser);
                // Show a confirmation message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Delete Success");
                alert.setHeaderText(null);
                alert.setContentText("Post deleted successfully.");
                alert.showAndWait();
                // Refresh the table view after deletion
                populateTableView();
            } catch (SQLException ex) {
                // Show an error message if deletion fails
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Delete Error");
                alert.setHeaderText(null);
                alert.setContentText("Error deleting user: " + ex.getMessage());
                alert.showAndWait();
            }
        } else {
            // Show an alert if no user is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a Post to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    private void showCategoryStatistics(ActionEvent event) {
        // Get the list of all posts from the table
        List<Post> allPosts = table.getItems();

        // Count occurrences of each category
        Map<String, Long> categoryCounts = allPosts.stream()
                .collect(Collectors.groupingBy(Post::getCategorie, Collectors.counting()));

        // Create a PieChart Data list
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Long> entry : categoryCounts.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        // Create a PieChart
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Category Statistics");

        // Create a new alert dialog with PieChart as the content
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Category Statistics");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(pieChart);
        alert.showAndWait();
    }

    public void refreshList() {
        try {
            populateTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void navigateToAddPost() {
        try {
            // Load the AddVoiture.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ajouterPost.fxml"));
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
        navigateToAddPost();
    }

    @FXML
    void CommentaireButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) commentaireButton.getScene().getWindow();
        stage.close();
        // Navigate to the login window
        navigateToCommentaire();
    }

    private void navigateToCommentaire() {
        try {
            // Load the ListVoiture.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("afficherCommentaire.fxml"));
            javafx.scene.Parent root = loader.load();

            // Access the controller of the ListVoiture.fxml file
            AfficherCommentaire controller = loader.getController();

            // Show the scene containing the ListVoiture.fxml file
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void sortAlphabetically(ActionEvent event) {
        if (isAscendingOrder) {
            // Sort the data alphabetically in ascending order by the titre column
            Collections.sort(allPosts, Comparator.comparing(Post::getTitre));
            // Change the button color to indicate ascending order
            sortButton.setStyle("-fx-background-color: #4682B4;");
        } else {
            // Sort the data alphabetically in descending order by the titre column
            Collections.sort(allPosts, Comparator.comparing(Post::getTitre, String.CASE_INSENSITIVE_ORDER).reversed());
            // Change the button color to indicate descending order
            sortButton.setStyle("-fx-background-color: #FFA07A;");
        }

        // Toggle the sorting order for the next click
        isAscendingOrder = !isAscendingOrder;

        // Refresh the table view with the sorted data
        createPage(pagination.getCurrentPageIndex());
    }
    @FXML
    void exportToExcel(ActionEvent event) {
        ObservableList<Post> posts = table.getItems();
        String filePath = "C:\\Users\\iyadh\\Downloads\\Django2+notification\\Django2\\src\\main\\resources\\PostsData.xlsx";
        try {
            exportExcelle.exportPostsToExcel(posts, filePath); // Corrected method call
            System.out.println("Data exported to " + filePath);

            // Open the exported Excel file
            File file = new File(filePath);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void exportToPDF(ActionEvent event) {
        String filePath = "C:\\Users\\iyadh\\Downloads\\Django2+notification\\Django2\\src\\main\\resources\\PostsData.pdf";

        try {
            ExportPDF exportPDF = new ExportPDF();
            exportPDF.generatePDF(filePath, allPosts); // Pass allPosts to the generatePDF method
            System.out.println("Data exported to " + filePath);

            // Open the exported PDF file
            File file = new File(filePath);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void refreshList(ActionEvent event) {
        try {
            populateTableView(); // Call method to repopulate TableView
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to populate table view: " + e.getMessage());
        }
    }


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



}
