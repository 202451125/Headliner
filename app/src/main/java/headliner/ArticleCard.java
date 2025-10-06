package headliner;

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
    
    public ArticleCard(Article article) {
        this.article = article;
        getStyleClass().add("article-card");
        createCard();
        setupHoverEffects();
    }
    
    private void createCard() {
        setSpacing(12);
        setPadding(new Insets(16));
        setMaxWidth(350);
        setMinWidth(350);
        
        // Image container with StackPane for placeholder
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
        
        // Category and Date
        HBox metaBox = new HBox(10);
        metaBox.setAlignment(Pos.CENTER_LEFT);
        
        Label categoryLabel = new Label(article.getCategory().toUpperCase());
        categoryLabel.getStyleClass().add("category-label");
        
        Label dateLabel = new Label(formatDate(article.getPublishedAt()));
        dateLabel.getStyleClass().add("date-label");
        
        metaBox.getChildren().addAll(categoryLabel, dateLabel);
        
        // Headline
        Label headline = new Label(article.getTitle());
        headline.getStyleClass().add("headline");
        headline.setWrapText(true);
        headline.setMaxWidth(320);
        
        // Description (truncated)
        String description = article.getDescription();
        if (description != null && description.length() > 120) {
            description = description.substring(0, 120) + "...";
        }
        Label descLabel = new Label(description);
        descLabel.getStyleClass().add("description");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(320);
        
        // Action buttons
        HBox actionBox = new HBox(12);
        actionBox.setAlignment(Pos.CENTER_LEFT);
        
        Button likeBtn = createActionButton("❤️", "Like");
        Button saveBtn = createActionButton("🔖", "Save");
        Button readBtn = createTextButton("Read More →");
        
        // Make Read More button open article
        readBtn.setOnAction(e -> openArticleInBrowser());
        
        actionBox.getChildren().addAll(likeBtn, saveBtn, readBtn);
        
        getChildren().addAll(imageContainer, metaBox, headline, descLabel, actionBox);
    }
    
    private void setupPlaceholderImage(StackPane container) {
        container.setStyle("-fx-background-color: #edf2f7; -fx-background-radius: 8;");
        Label placeholder = new Label("📷");
        placeholder.setStyle("-fx-font-size: 32px; -fx-text-fill: #a0aec0;");
        container.getChildren().add(placeholder);
    }
    
    private Button createActionButton(String emoji, String tooltip) {
        Button btn = new Button(emoji);
        btn.setStyle("-fx-background-color: transparent; -fx-font-size: 14px; -fx-padding: 8; -fx-text-fill: #718096;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #f7fafc; -fx-font-size: 14px; -fx-padding: 8; -fx-background-radius: 6; -fx-text-fill: #4a5568;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-font-size: 14px; -fx-padding: 8; -fx-text-fill: #718096;"));
        return btn;
    }
    
    private Button createTextButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #3182ce; -fx-font-weight: bold; -fx-padding: 0;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #63b3ed; -fx-font-weight: bold; -fx-padding: 0;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #3182ce; -fx-font-weight: bold; -fx-padding: 0;"));
        return btn;
    }
    
    private void setupHoverEffects() {
        setOnMouseEntered(e -> {
            getStyleClass().add("card-hover");
        });
        
        setOnMouseExited(e -> {
            getStyleClass().remove("card-hover");
        });
    }
    
    private String formatDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) return "Recent";
        try {
            return dateString.substring(0, 10);
        } catch (Exception e) {
            return "Recent";
        }
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