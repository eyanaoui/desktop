package com.example.user;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Commentaire;
import models.Post;
import models.Reaction;
import service.ServiceCommentaire;
import service.ServicePost;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class itemPost {
    @FXML
    private Label idlabel;


    @FXML
    private HBox LikeContainer;

    @FXML
    private Label categorieLable;

    @FXML
    private HBox commentContainer;

    @FXML
    private Label desriptionLable;

    @FXML
    private ImageView dynamicImageView;

    @FXML
    private ImageView imgHate;

    @FXML
    private ImageView imgLike;

    @FXML
    private ImageView imgLove;

    @FXML
    private Label nbrcomment;

    @FXML
    private Label nbrreaction;

    @FXML
    private Label nbrshare;

    @FXML
    private ImageView profilepic;

    @FXML
    private HBox reactionContainer;

    @FXML
    private ImageView reactionimg;

    @FXML
    private Label reactionname;

    @FXML
    private HBox shareContainer;
    @FXML
    private Button smsButton;

    @FXML
    private Label titreLabel;

    @FXML
    private ImageView share;
    private long startTime=0;
    private Reaction currentReaction;
    //private Post post=new Post();
    private long postId;

    public static final String ACCOUNT_SID = "AC7627d42d1d8517a14699951bfb7af9d2";
    public static final String AUTH_TOKEN = "9a6776600dbc775e7c32f1912a1233d7";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    // private MyListener myListener;


    ServicePost servicePost=new ServicePost();

    @FXML
    private void click(MouseEvent mouseEvent) {
        myListener.onClickListener(post);
    }

    private Post post;
    private MyListenerC myListener;



    public void setData(Post post, MyListenerC myListener) {
        this.post = post;
        this.postId = post.getId();
        this.myListener = myListener;
        titreLabel.setText(post.getTitre());
        desriptionLable.setText(post.getDescription());
        categorieLable.setText(post.getCategorie());
        nbrreaction.setText(String.valueOf(post.getLike_count()));

        // Load the image using the full path
        String imagePath = post.getImage_name();
        try {
            File file = new File(imagePath);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                dynamicImageView.setImage(image);
            } else {
                System.err.println("Image file not found: " + imagePath);
                // You can provide a fallback image or display a placeholder here
            }
        } catch (Exception e) {
            System.err.println("Failed to load image: " + e.getMessage());
            e.printStackTrace();
            // Handle exception appropriately
        }

        // Fetch and display the number of comments for the post
        String commentCountMessage = fetchCommentCountForPost(postId);
        nbrcomment.setText(commentCountMessage);
    }

    private String fetchCommentCountForPost(long postId) {
        int commentCount = 0;
        String message = "";

        // Assuming you have a method in ServiceCommentaire to fetch comment count for a specific post ID
        try {
            ServiceCommentaire serviceCommentaire = new ServiceCommentaire();
            commentCount = serviceCommentaire.getCountByPostId(postId);
            message = "Number of comments: " + commentCount;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database exception
        }

        return message;
    }


    @FXML
    public void clickreactionContainer(MouseEvent event) {
        startTime=System.currentTimeMillis();
    }



    @FXML
    public void clickreaction(MouseEvent event) {
        if(System.currentTimeMillis()- startTime > 500){
            reactionContainer.setVisible(true);
        }else {
            if (reactionContainer.isVisible()){reactionContainer.setVisible(false);}
            if (currentReaction==Reaction.NON){
                setReaction(Reaction.Like);
            }else { setReaction(Reaction.NON);}
        }
    }
    @FXML
    public void initialize() {
        // Add animation for like icons
        addScaleAnimation(imgLove);
        addScaleAnimation(imgLike);
        addScaleAnimation(imgHate);

        // Add click event to cancel like when clicking outside the icons
        reactionContainer.setOnMouseClicked(e -> cancelLike());
    }

    private void addScaleAnimation(ImageView imageView) {
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(100), imageView);
        scaleIn.setFromX(1);
        scaleIn.setFromY(1);
        scaleIn.setToX(1.1);
        scaleIn.setToY(1.1);

        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(100), imageView);
        scaleOut.setFromX(1.1);
        scaleOut.setFromY(1.1);
        scaleOut.setToX(1);
        scaleOut.setToY(1);

        imageView.setOnMouseEntered(e -> {
            scaleOut.stop(); // Stop the 'scaleOut' animation if it's running
            scaleIn.playFromStart();
        });

        imageView.setOnMouseExited(e -> {
            scaleIn.stop(); // Stop the 'scaleIn' animation if it's running
            scaleOut.playFromStart();
        });
    }

    @FXML
    public void imgclickreaction(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        switch (imageView.getId()) {
            case "imgLove":
                setReaction(Reaction.LOVE);
                break;
            case "imgLike":
                setReaction(Reaction.Like);
                break;
            case "imgHate":
                setReaction(Reaction.HATE);
                break;
        }
        reactionContainer.setVisible(false);
    }

    private void cancelLike() {
        // Check if any of the scale transitions are currently playing
        boolean isAnimating = imgLove.getScaleX() != 1 || imgLike.getScaleX() != 1 || imgHate.getScaleX() != 1;

        // If any animation is active, cancel it and set the reaction to NONE
        if (isAnimating) {
            setReaction(Reaction.NONE);
            // Reset the scale of the like icons
            imgLove.setScaleX(1);
            imgLove.setScaleY(1);
            imgLike.setScaleX(1);
            imgLike.setScaleY(1);
            imgHate.setScaleX(1);
            imgHate.setScaleY(1);
        }
    }

    public void setReaction(Reaction reaction) {
        // Check if the reaction object is null
        if (reaction == null) {
            System.err.println("Reaction object is null.");
            return;
        }

        // Check if the reaction attributes are null
        if (reaction.getImgSrc() == null || reaction.getName() == null) {
            System.err.println("Reaction attributes are null.");
            return;
        }

        // Update the currentReaction field
        currentReaction = reaction;

        // Load the image
        try {
            Image image = new Image(getClass().getResourceAsStream(reaction.getImgSrc()));
            reactionimg.setImage(image);
            reactionname.setText(reaction.getName());
        } catch (NullPointerException e) {
            System.err.println("Image source is null for reaction: " + reaction);
            return;
        }

        // Update the like and dislike counts for the currentPost
        if (reaction == Reaction.Like || reaction == Reaction.LOVE) {
            post.setLike_count(post.getLike_count() + 1);
        } else if (reaction == Reaction.HATE) {
            post.setDislike_count(post.getDislike_count() + 1);
        }

        try {
            servicePost.updateLikeCount(postId, post); // Update the database
            System.out.println("Post updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to update post: " + e.getMessage());
        }

        // Update the number of reactions displayed
        nbrreaction.setText(String.valueOf(post.getLike_count()));
    }




    @FXML
    private void showCommentsPopup(MouseEvent event) {
        try {
            if (post != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("PopUpC.fxml"));
                Parent root = loader.load();

                CommentPopupController controller = loader.getController();
                controller.initData(post); // Pass the post object to the controller

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Comments");
                stage.setScene(new Scene(root));
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> fetchCommentsForPost(long postId) {
        List<String> comments = new ArrayList<>();

        // Assuming you have a method in ServiceCommentaire to fetch comments for a specific post ID
        try {
            ServiceCommentaire serviceCommentaire = new ServiceCommentaire();
            List<Commentaire> commentaires = serviceCommentaire.selectByPostId(postId);
            for (Commentaire commentaire : commentaires) {
                comments.add(commentaire.getContenu());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database exception
        }

        return comments;
    }
    public static void sendSMS(String recipientPhoneNumber, String messageBody) {
        String twilioPhoneNumber = "+14846522475";

        Message message = Message.creator(
                        new PhoneNumber(recipientPhoneNumber),
                        new PhoneNumber(twilioPhoneNumber),
                        messageBody)
                .create();

        System.out.println("SMS sent successfully. SID: " + message.getSid());
    }

    @FXML
    private void sendSMS(ActionEvent event) {
        String recipientPhoneNumber = "+21629760614";
        String messageBody = "Check out this post!";
        sendSMS(recipientPhoneNumber, messageBody);
    }



    @FXML
    private void shareViaWhatsApp() {
        // Implement your logic to share via WhatsApp
    }

    @FXML
    private void shareViaOther() {
        // Implement your logic to share via Other
    }
    @FXML
    private void showSharePopup(MouseEvent event) {
        // Create a new Popup
        Popup popup = new Popup();

        // Create ImageViews for sharing options
        ImageView smsIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/sms.png")));
        ImageView whatsappIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/whatsapp.png")));
        ImageView otherIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/gmail.png")));
        smsIcon.setOnMouseClicked(e -> {
            String recipientPhoneNumber = "+21629760614"; // Replace with the recipient's phone number
            String messageBody = "Check out this post!"; // Customize the message as needed
            sendSMS(recipientPhoneNumber, messageBody);
            popup.hide(); // Close the popup after sending the SMS
        });

        whatsappIcon.setOnMouseClicked(e -> {
            // Handle share via WhatsApp action
            popup.hide();
            String message = "Check out this post: " + post.getDescription() + "\n\n" +
                    "Download the app from: https://example.com";
            shareViaWhatsApp(message);
        });

        otherIcon.setOnMouseClicked(e -> {
            // Handle share via Other action
            popup.hide();
        });

        // Set the fitHeight and fitWidth for the icons
        smsIcon.setFitHeight(35);
        smsIcon.setFitWidth(35);
        whatsappIcon.setFitHeight(35);
        whatsappIcon.setFitWidth(35);
        otherIcon.setFitHeight(35);
        otherIcon.setFitWidth(35);

        // Create a HBox to hold the icons horizontally
        HBox hbox = new HBox(smsIcon, whatsappIcon, otherIcon);
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10));

        // Create a BorderPane to align the icons at the top
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(hbox);

        // Add the BorderPane to the Popup
        popup.getContent().addAll(borderPane);

        // Get the stage of the event
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Calculate the screen coordinates of the share icon
        Bounds bounds = share.localToScreen(share.getBoundsInLocal());
        double x = bounds.getMinX();
        double y = bounds.getMinY() - 50; // Adjust the y-coordinate to position the popup above the share icon

        // Set the position of the Popup relative to the share icon
        popup.show(stage, x, y);

        // Add an event filter to close the popup when clicking outside
        stage.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (!popup.isShowing()) return;
            Node source = mouseEvent.getPickResult().getIntersectedNode();
            if (source != share && !popup.getContent().contains(source)) {
                popup.hide();
            }
        });



        // Add animation effect for each icon
        ScaleTransition smsScaleTransition = createScaleTransition(smsIcon);
        ScaleTransition whatsappScaleTransition = createScaleTransition(whatsappIcon);
        ScaleTransition otherScaleTransition = createScaleTransition(otherIcon);

        smsIcon.setOnMouseEntered(e -> smsScaleTransition.play());
        smsIcon.setOnMouseExited(e -> smsScaleTransition.stop());

        whatsappIcon.setOnMouseEntered(e -> whatsappScaleTransition.play());
        whatsappIcon.setOnMouseExited(e -> whatsappScaleTransition.stop());

        otherIcon.setOnMouseEntered(e -> otherScaleTransition.play());
        otherIcon.setOnMouseExited(e -> otherScaleTransition.stop());
    }
    private ScaleTransition createScaleTransition(ImageView imageView) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), imageView);
        scaleTransition.setFromX(1);
        scaleTransition.setFromY(1);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        return scaleTransition;
    }

    private void shareViaWhatsApp(String message) {
        // Use an Intent to share via WhatsApp
        try {
            Desktop.getDesktop().browse(new URI("https://api.whatsapp.com/send?text=" + URLEncoder.encode(message, "UTF-8")));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }





}
