package headliner;

import java.util.concurrent.CompletableFuture;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ArticleCard extends VBox {
    private final Article article;
    private final DatabaseHelper dbHelper;
    private Button saveBtn; // Make this a field to access it later

    // THIS IS THE CORRECT CONSTRUCTOR THAT ACCEPTS A DATABASEHELPER
    public ArticleCard(Article article, DatabaseHelper dbHelper) {
        this.article = article;
        this.dbHelper = dbHelper;
        getStyleClass().add("article-card");
        createCard();
        setupHoverEffects();
    }
    
    private void createCard() {
        setSpacing(12);
        setPadding(new Insets(16));
        setMaxWidth(350);
        setMinWidth(350);
        
        StackPane imageContainer = new StackPane();
        imageContainer.setPrefSize(320, 180);
        imageContainer.getStyleClass().add("image-container");
        
        ImageView imageView = new ImageView();
        imageView.setFitWidth(320);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(true);
        
        if (article.getUrlToImage() != null && !article.getUrlToImage().isEmpty()) {
            try {
                Image image = new Image(article.getUrlToImage(), true);
                imageView.setImage(image);
                imageContainer.getChildren().add(imageView);
            } catch (Exception e) {
                setupPlaceholderImage(imageContainer);
            }
        } else {
            setupPlaceholderImage(imageContainer);
        }
        
        HBox metaBox = new HBox(10);
        metaBox.setAlignment(Pos.CENTER_LEFT);
        Label categoryLabel = new Label(article.getCategory().toUpperCase());
        categoryLabel.getStyleClass().add("category-label");
        Label dateLabel = new Label(formatDate(article.getPublishedAt()));
        dateLabel.getStyleClass().add("date-label");
        metaBox.getChildren().addAll(categoryLabel, dateLabel);
        
        Label headline = new Label(article.getTitle());
        headline.getStyleClass().add("headline");
        headline.setWrapText(true);
        headline.setMaxWidth(320);
        
        String description = article.getDescription();
        if (description != null && description.length() > 120) {
            description = description.substring(0, 120) + "...";
        }
        Label descLabel = new Label(description);
        descLabel.getStyleClass().add("description");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(320);
        
        HBox actionBox = new HBox(12);
        actionBox.setAlignment(Pos.CENTER_LEFT);
        
        Button likeBtn = createActionButton("❤", "Like");
        saveBtn = createActionButton("🔖", "Save"); // Assign to the field
        Button readBtn = createTextButton("Read More →");
        
        readBtn.setOnAction(e -> openArticleInBrowser());
        
        // --- ADDED BOOKMARK LOGIC ---
        saveBtn.setOnAction(e -> handleBookmarkAction());
        updateBookmarkButtonState();
        
        actionBox.getChildren().addAll(likeBtn, saveBtn, readBtn);
        
        getChildren().addAll(imageContainer, metaBox, headline, descLabel, actionBox);
    }

    private void updateBookmarkButtonState() {
        if (!UserSessionManager.isUserLoggedIn() || article.getUrl() == null) {
            saveBtn.setText("🔖");
            saveBtn.setDisable(true); // Disable if not logged in
            return;
        }

        saveBtn.setDisable(false);
        CompletableFuture.supplyAsync(() -> dbHelper.isBookmarked(UserSessionManager.getCurrentUserId(), article.getUrl()))
            .thenAccept(isBookmarked -> Platform.runLater(() -> {
                if (isBookmarked) {
                    saveBtn.setText("✅"); // Change emoji to show it's saved
                } else {
                    saveBtn.setText("🔖");
                }
            }));
    }

    private void handleBookmarkAction() {
        if (!UserSessionManager.isUserLoggedIn()) {
             // Optionally show an alert
            return;
        }
        if (article.getUrl() == null || article.getUrl().isEmpty()) return;

        CompletableFuture.supplyAsync(() -> dbHelper.isBookmarked(UserSessionManager.getCurrentUserId(), article.getUrl()))
            .thenAccept(isBookmarked -> {
                if (isBookmarked) {
                    dbHelper.removeBookmark(UserSessionManager.getCurrentUserId(), article.getUrl());
                } else {
                    dbHelper.addBookmark(UserSessionManager.getCurrentUserId(), article);
                }
                Platform.runLater(this::updateBookmarkButtonState);
            });
    }

    // --- Original Methods from your friend ---
    
    private void setupPlaceholderImage(StackPane container) {
        container.setStyle("-fx-background-color: #edf2f7; -fx-background-radius: 8;");
        Label placeholder = new Label("📷");
        placeholder.setStyle("-fx-font-size: 32px; -fx-text-fill: #a0aec0;");
        container.getChildren().add(placeholder);
    }
    
    private Button createActionButton(String emoji, String tooltip) {
        Button btn = new Button(emoji);
        btn.setStyle("-fx-background-color: transparent; -fx-font-size: 14px; -fx-padding: 8; -fx-text-fill: #718096; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #f7fafc; -fx-font-size: 14px; -fx-padding: 8; -fx-background-radius: 6; -fx-text-fill: #4a5568; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-font-size: 14px; -fx-padding: 8; -fx-text-fill: #718096; -fx-cursor: hand;"));
        return btn;
    }
    
    private Button createTextButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #3182ce; -fx-font-weight: bold; -fx-padding: 0; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #63b3ed; -fx-font-weight: bold; -fx-padding: 0; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #3182ce; -fx-font-weight: bold; -fx-padding: 0; -fx-cursor: hand;"));
        return btn;
    }
    
    private void setupHoverEffects() {
        setOnMouseEntered(e -> getStyleClass().add("card-hover"));
        setOnMouseExited(e -> getStyleClass().remove("card-hover"));
    }
    
    private String formatDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) return "Recent";
        try { return dateString.substring(0, 10); } catch (Exception e) { return "Recent"; }
    }
    
    private void openArticleInBrowser() {
        try {
            if (article != null && article.getUrl() != null && !article.getUrl().isBlank()) {
                java.awt.Desktop.getDesktop().browse(new java.net.URI(article.getUrl()));
            }
        } catch (Exception ex) {
            System.out.println("Error opening browser: " + ex.getMessage());
        }
    }
    
    public Article getArticle() {
        return article;
    }
}