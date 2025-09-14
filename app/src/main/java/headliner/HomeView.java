package headliner;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HomeView {
    private final Stage stage;
    private final ObservableList<Article> articles = FXCollections.observableArrayList();
    private FlowPane articlesContainer;
    private Label resultsLabel;

    private String country = "us";
    private String category = "general";
    private String language = "en";

    public HomeView(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        // Main container
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");
        
        // TOP BAR - Logo, Search and Theme
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(15, 20, 15, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getStyleClass().add("top-bar");
        
        // Logo and app title
        HBox logoBox = new HBox(10);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        
        try {
            ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/logo.png")));
            logo.setFitHeight(32);
            logo.setFitWidth(32);
            logo.setPreserveRatio(true);
            logoBox.getChildren().add(logo);
        } catch (Exception e) {
            System.out.println("Logo not found, using text only");
        }
        
        VBox titleBox = new VBox(2);
        Label title = new Label("Headliner");
        title.getStyleClass().add("app-title");
        Label slogan = new Label("Your World, Your News");
        slogan.getStyleClass().add("app-slogan");
        titleBox.getChildren().addAll(title, slogan);
        logoBox.getChildren().add(titleBox);
        
        // Search field
        TextField searchField = new TextField();
        searchField.setPromptText("Search headlines...");
        searchField.getStyleClass().add("search-field");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchField.setMaxWidth(400);
        
        // Language dropdown
        ComboBox<String> languageDropdown = new ComboBox<>();
        languageDropdown.getItems().addAll("English", "Spanish", "French", "German", "Hindi");
        languageDropdown.setValue("English");
        languageDropdown.getStyleClass().add("language-dropdown");
        languageDropdown.setOnAction(ev -> {
            String selected = languageDropdown.getValue();
            switch (selected) {
                case "Spanish": language = "es"; break;
                case "French": language = "fr"; break;
                case "German": language = "de"; break;
                case "Hindi": language = "hi"; break;
                default: language = "en";
            }
            loadArticles();
        });
        
        // Dark/Light Mode Toggle
        ToggleButton themeToggle = new ToggleButton("🌙");
        themeToggle.getStyleClass().add("theme-toggle");
        themeToggle.setOnAction(ev -> {
            if (themeToggle.isSelected()) {
                root.getStyleClass().add("dark-theme");
                themeToggle.setText("☀️");
            } else {
                root.getStyleClass().remove("dark-theme");
                themeToggle.setText("🌙");
            }
        });
        
        topBar.getChildren().addAll(logoBox, searchField, languageDropdown, themeToggle);
        
        // CATEGORY CHIPS
        HBox categoryBox = new HBox(12);
        categoryBox.setPadding(new Insets(0, 20, 15, 20));
        categoryBox.setAlignment(Pos.CENTER);
        
        String[] categories = {"National", "International", "Business", "Science", 
                              "Technology", "Sports", "Entertainment", "Health"};
        String[] categoryIds = {"general", "general", "business", "science", 
                               "technology", "sports", "entertainment", "health"};
        
        for (int i = 0; i < categories.length; i++) {
            Button categoryBtn = new Button(categories[i]);
            categoryBtn.getStyleClass().add("category-chip");
            categoryBtn.setUserData(categoryIds[i]);
            
            if (categoryIds[i].equals(category)) {
                categoryBtn.getStyleClass().add("active");
            }
            
            categoryBtn.setOnAction(ev -> {
                category = (String) categoryBtn.getUserData();
                
                // Update active state
                categoryBox.getChildren().forEach(node -> {
                    node.getStyleClass().remove("active");
                });
                categoryBtn.getStyleClass().add("active");
                
                loadArticles();
            });
            
            categoryBox.getChildren().add(categoryBtn);
        }
        
        // Results counter
        resultsLabel = new Label("Showing 0 articles");
        resultsLabel.getStyleClass().add("results-label");
        resultsLabel.setPadding(new Insets(0, 20, 10, 20));
        
        // ARTICLES CONTAINER
        articlesContainer = new FlowPane();
        articlesContainer.setHgap(25);
        articlesContainer.setVgap(25);
        articlesContainer.setPadding(new Insets(0, 20, 20, 20));
        articlesContainer.setAlignment(Pos.TOP_CENTER);
        
        ScrollPane scrollPane = new ScrollPane(articlesContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        
        // Assemble layout
        VBox contentBox = new VBox(10, categoryBox, resultsLabel, scrollPane);
        root.setTop(topBar);
        root.setCenter(contentBox);
        
        Scene scene = new Scene(root, 1400, 900);
        scene.getStylesheets().add(getClass().getResource("/dashboard-style.css").toExternalForm());
        
        // Load articles
        loadArticles();
        
        return scene;
    }

    private void loadArticles() {
        articlesContainer.getChildren().clear();
        
        // Show loading
        Label loading = new Label("Loading articles...");
        loading.getStyleClass().add("loading-label");
        articlesContainer.getChildren().add(loading);

        CompletableFuture.supplyAsync(() -> ApiHelper.fetchTopHeadlines(country, category))
                .thenAccept(result -> Platform.runLater(() -> {
                    articlesContainer.getChildren().clear();
                    
                    if (result == null || result.isEmpty()) {
                        Label noArticles = new Label("No articles found. Try another category.");
                        noArticles.getStyleClass().add("no-articles-label");
                        articlesContainer.getChildren().add(noArticles);
                        resultsLabel.setText("Showing 0 articles");
                        return;
                    }
                    
                    resultsLabel.setText("Showing " + result.size() + " articles");
                    
                    for (Article article : result) {
                        ArticleCard card = new ArticleCard(article);
                        card.setOnMouseClicked(e -> {
                            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                                openArticleInBrowser(article);
                            }
                        });
                        articlesContainer.getChildren().add(card);
                    }
                }))
                .exceptionally(ex -> {
                    Platform.runLater(() -> {
                        articlesContainer.getChildren().clear();
                        Label error = new Label("Error loading articles. Please check connection.");
                        error.getStyleClass().add("error-label");
                        articlesContainer.getChildren().add(error);
                        resultsLabel.setText("Showing 0 articles");
                    });
                    return null;
                });
    }

    private void openArticleInBrowser(Article article) {
        try {
            if (article != null && article.getUrl() != null && !article.getUrl().isBlank()) {
                Desktop.getDesktop().browse(new URI(article.getUrl()));
            }
        } catch (Exception ex) {
            System.out.println("Error opening browser: " + ex.getMessage());
        }
    }
}