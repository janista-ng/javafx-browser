package com.homework7;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class Browser creates a GUI of a browser simulation which only hosts 2 "websites":
 * "javadiscussion.com" and "1331motd.com", upon launching the Browser, users will see a top URL bar
 * where they can enter an URL. If the URL is invalid (not onne of the two it hosts), an error message
 * will pop up.
 * @author Janista Ng
 * @version 1.0
 */
public class Browser extends Application {

    private BorderPane root;
    private TextField urlBar;
    private Button btnGo;

    private ArrayList<Post> posts = new ArrayList<>();

    private static final String POST_FILE = "post_history.txt";

    private static final String DELIM = "\u001C";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Janista's Browser");

        urlBar = new TextField();
        urlBar.setPromptText("Enter url...");
        btnGo = new Button("Go");

        root = new BorderPane();
        BorderPane topPane = new BorderPane();
        topPane.setPadding(new Insets(5));
        topPane.setCenter(urlBar);
        topPane.setRight(btnGo);
        root.setTop(topPane);

        loadPosts();

        showHomePage();

        btnGo.setOnAction(e -> handleUrl(urlBar.getText().trim())); //lamda expression
        urlBar.setOnAction(new EventHandler<ActionEvent>() { //anon inner class
            @Override
            public void handle(ActionEvent event) {
                handleUrl(urlBar.getText().trim());
            }
        });


        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Handle URL input and navigate to the appropriate page if URL is valid.
     */
    private void handleUrl(String url) {
        if (url.toLowerCase().equals("javadiscussion.com")) {
            savePosts();
            displayJavaDiscussion();
        } else if (url.toLowerCase().equals("1331motd.com")) {
            savePosts();
            displayMOTD();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid URL");
            alert.setContentText("The URL you entered does not exist. Please try again.");
            alert.showAndWait();
        }
    }

    /**
     * Home page of the Browser shown upon launch.
     */
    private void showHomePage() {
        BorderPane homePane = new BorderPane();
        Text welcome = new Text("Welcome! Type a URL above to visit a page.");
        welcome.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        homePane.setCenter(welcome);
        root.setCenter(homePane);
    }

    /**
     * Displays the Java Discussion page with: Purple header containing "Java Discussion" and
     * "New Thread" button, scrollable list of posts in descending order, "Be the first to make a post!"
     * if no posts exist.
     */
    private void displayJavaDiscussion() {
        BorderPane discussionPane = new BorderPane();

        HBox header = new HBox();
        header.setPadding(new Insets(10));
        header.setSpacing(10);
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #800080;");

        Text titleText = new Text("Java Discussion");
        titleText.setFill(Color.WHITE);
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Button newThreadButton = new Button("New Thread");
        newThreadButton.setOnAction(new NewThreadHandler());

        header.getChildren().addAll(titleText, newThreadButton);
        discussionPane.setTop(header);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox postsBox = new VBox(15);
        postsBox.setPadding(new Insets(20));

        if (posts.isEmpty()) {
            Text emptyText = new Text("Be the first to make a post!");
            emptyText.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));
            postsBox.setAlignment(Pos.CENTER);
            postsBox.getChildren().add(emptyText);
        } else {
            postsBox.setAlignment(Pos.TOP_CENTER);
            for (int i = posts.size() - 1; i >= 0; i--) {
                Post p = posts.get(i);
                postsBox.getChildren().add(createPostNode(p));
            }
        }

        scrollPane.setContent(postsBox);
        discussionPane.setCenter(scrollPane);

        root.setCenter(discussionPane);
    }

    /**
     * Displays the 1331 Message-of-the-Day page.
     * Replace with shapes or other content as needed.
     */
    private void displayMOTD() {
        StackPane motdPane = new StackPane();
        motdPane.setPrefSize(900, 600);

        Group motdGroup = new Group();

        Rectangle houseBody = new Rectangle(0, 50, 200, 150);
        houseBody.setFill(Color.LIGHTGRAY);
        houseBody.setStroke(Color.BLACK);

        Polygon roof = new Polygon();
        roof.getPoints().addAll(
                0.0, 50.0,
                100.0, 0.0,
                200.0, 50.0
        );
        roof.setFill(Color.RED);
        roof.setStroke(Color.BLACK);

        Rectangle door = new Rectangle(90, 140, 20, 60);
        door.setFill(Color.SADDLEBROWN);
        door.setStroke(Color.BLACK);
        Circle window1 = new Circle(50, 120, 15);
        window1.setFill(Color.LIGHTBLUE);
        window1.setStroke(Color.BLACK);

        Circle window2 = new Circle(150, 120, 15);
        window2.setFill(Color.LIGHTBLUE);
        window2.setStroke(Color.BLACK);

        Group houseGroup = new Group();
        houseGroup.getChildren().addAll(houseBody, roof, door, window1, window2);

        Text motdText = new Text(0, 230, "CS 1331: Code Your Future (House)!");
        motdText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        motdText.setFill(Color.DARKBLUE);

        motdGroup.getChildren().addAll(houseGroup, motdText);
        motdPane.getChildren().add(motdGroup);
        root.setCenter(motdPane);
    }


    /**
     * Opens a new window to create a new post.
     * Post number is assigned based on the current size of posts + 1.
     * If the author is blank, defaults to "Anonymous".
     */
    private void openNewPostWindow() {
        Stage newPostStage = new Stage();
        newPostStage.initModality(Modality.APPLICATION_MODAL);
        newPostStage.setTitle("Create a New Thread");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label authorLabel = new Label("Author:");
        TextField authorField = new TextField();

        Label bodyLabel = new Label("Post Body:");
        TextArea bodyArea = new TextArea();
        bodyArea.setPrefRowCount(6);
        bodyArea.setWrapText(true);


        Button postButton = new Button("Post");
        postButton.setOnAction(e -> {
            String author = authorField.getText().trim();
            String body = bodyArea.getText().trim();
            if (body.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Empty Post");
                alert.setContentText("Post body cannot be empty!");
                alert.showAndWait();
                return;
            }
            if (author.isEmpty()) {
                author = "Anonymous";
            }
            int postNumber = posts.size() + 1;
            posts.add(new Post(author, postNumber, body));
            newPostStage.close();

            displayJavaDiscussion();
        });

        layout.getChildren().addAll(authorLabel, authorField, bodyLabel, bodyArea, postButton);

        Scene scene = new Scene(layout, 400, 300);
        newPostStage.setScene(scene);
        newPostStage.showAndWait();
    }

    /**
     * Creates a Node that represents a single post.
     */
    private VBox createPostNode(Post post) {
        VBox box = new VBox(5);

        String headerStr = post.author + "  #" + post.number;
        Text headerText = new Text(headerStr);
        headerText.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Text bodyText = new Text(post.body);
        bodyText.setFont(Font.font("Arial", 13));
        bodyText.setWrappingWidth(800);

        box.setStyle("-fx-background-color: #f8f8f8; -fx-border-color: #ddd; -fx-padding: 10;");
        box.getChildren().addAll(headerText, bodyText);
        return box;
    }
    /**
     * Load posts from a file.
     */
    private void loadPosts() {
        File file = new File(POST_FILE);
        if (!file.exists()) {
            return;
        }
        try (Scanner scanner = new Scanner(file)) {
            scanner.useDelimiter(DELIM);
            while (scanner.hasNext()) {
                String author = scanner.next().trim();
                if (!scanner.hasNext()) {
                    break;
                }
                String numStr = scanner.next().trim();
                int number = Integer.parseInt(numStr);
                if (!scanner.hasNext()) {
                    break;
                }
                String body = scanner.next().trim();
                posts.add(new Post(author, number, body));
            }
        } catch (FileNotFoundException e) {
        }
    }

    /**
     * Save posts to a file on exit or when leaving the page.
     */
    private void savePosts() {
        try (PrintWriter writer = new PrintWriter(new File(POST_FILE))) {
            for (Post p : posts) {
                writer.print(p.author + DELIM + p.number + DELIM + p.body + DELIM);
            }
        } catch (IOException e) {
        }
    }

    @Override
    public void stop() {
        savePosts();
    }

    /**
     * Main method that calls launch to launch the GUI.
     * @param args Command-line arguments (not used in this method).
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * An inner class to hold post data.
     */
    private static class Post {
        private String author;
        private int number;
        private String body;

        Post(String author, int number, String body) {
            this.author = author;
            this.number = number;
            this.body = body;
        }
    }
    /**
     * Named inner class used as an event handler for the New Thread button.
     */
    private class NewThreadHandler implements javafx.event.EventHandler<ActionEvent> { // named inner class
        @Override
        public void handle(ActionEvent event) {
            openNewPostWindow();
        }
    }
}
