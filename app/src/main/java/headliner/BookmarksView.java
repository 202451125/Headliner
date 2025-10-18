package headliner;

import java.awt.Desktop;
import java.net.URI;
import java.util.concurrent.CompletableFuture;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane; // FIXED: Added the missing import for Stage
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class BookmarksView {
    private final Stage stage;
    private VBox bookmarksContainer;
    private final DatabaseHelper dbHelper = new DatabaseHelper();

    public BookmarksView(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f8fafc;");
        root.setTop(createHeader());
        
        bookmarksContainer = new VBox(20);
        bookmarksContainer.setPadding(new Insets(30));
        
        ScrollPane scrollPane = new ScrollPane(bookmarksContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setCenter(scrollPane);

        loadBookmarks();
        return new Scene(root, 1400, 900);
    }

    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setStyle("-fx-background-color: #1B3C53; -fx-padding: 15 0 20 0;");
        header.setAlignment(Pos.CENTER);
        
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(0, 30, 10, 30));
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        Button backButton = new Button("<- Back to Home");
        backButton.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 15; -fx-padding: 8 15; -fx-cursor: hand;");
        backButton.setOnAction(e -> {
            HomeView home = new HomeView(stage);
            stage.setScene(home.createScene());
        });
        topBar.getChildren().add(backButton);
        
        Label title = new Label("My Bookmarks");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        header.getChildren().addAll(topBar, title);
        return header;
    }

    private void loadBookmarks() {
        bookmarksContainer.getChildren().clear();
        if (!UserSessionManager.isUserLoggedIn()) {
            bookmarksContainer.setAlignment(Pos.CENTER);
            bookmarksContainer.getChildren().add(createEmptyState("Login Required", "Please log in to view your saved articles."));
            return;
        }
        
        Label loadingLabel = new Label("Loading your bookmarks...");
        loadingLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #64748b;");
        bookmarksContainer.setAlignment(Pos.CENTER);
        bookmarksContainer.getChildren().add(loadingLabel);

        CompletableFuture.supplyAsync(() -> dbHelper.getBookmarks(UserSessionManager.getCurrentUserId()))
            .thenAccept(articles -> Platform.runLater(() -> {
                bookmarksContainer.getChildren().clear();
                if (articles.isEmpty()) {
                    bookmarksContainer.setAlignment(Pos.CENTER);
                    bookmarksContainer.getChildren().add(createEmptyState("No Bookmarks Yet", "When you save an article, it will appear here."));
                } else {
                    bookmarksContainer.setAlignment(Pos.TOP_CENTER);
                    for (Article article : articles) {
                        bookmarksContainer.getChildren().add(createBookmarkCard(article));
                    }
                }
            }));
    }

    private VBox createEmptyState(String title, String message) {
        VBox emptyState = new VBox(15);
        emptyState.setAlignment(Pos.CENTER);
        emptyState.setPadding(new Insets(100, 20, 100, 20));
        emptyState.setMaxWidth(600);
        emptyState.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: #e2e8f0; -fx-border-radius: 12;");
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: 700; -fx-text-fill: #334155;");
        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #64748b;");
        emptyState.getChildren().addAll(titleLabel, messageLabel);
        return emptyState;
    }

    private BorderPane createBookmarkCard(Article article) {
        BorderPane card = new BorderPane();
        card.setMaxWidth(1000);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20;");
        card.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.1)));

        ImageView thumb = new ImageView();
        thumb.setFitWidth(200);
        thumb.setFitHeight(112);
        thumb.setPreserveRatio(true);
        Rectangle clip = new Rectangle(200, 112);
        clip.setArcWidth(12);
        clip.setArcHeight(12);
        thumb.setClip(clip);

        if (article.getUrlToImage() != null && !article.getUrlToImage().isBlank()) {
            thumb.setImage(new Image(article.getUrlToImage(), true));
        } else {
            StackPane placeholder = new StackPane(new Label("No Image"));
            placeholder.setPrefSize(200, 112);
            placeholder.setStyle("-fx-background-color: #f1f5f9; -fx-background-radius: 8;");
            card.setLeft(placeholder);
        }
        if(thumb.getImage() != null) card.setLeft(thumb);
        BorderPane.setMargin(card.getLeft(), new Insets(0, 25, 0, 0));
        
        VBox contentBox = new VBox(8);
        Label title = new Label(article.getTitle());
        title.setWrapText(true);
        title.setStyle("-fx-font-weight: 800; -fx-font-size: 20px; -fx-text-fill: #1e293b;");

        Label desc = new Label(article.getDescription());
        desc.setWrapText(true);
        desc.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");
        desc.setMaxHeight(40);

        Label meta = new Label(String.format("%s • %s", article.getSource(), formatDate(article.getPublishedAt())));
        meta.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8; -fx-font-weight: 600;");

        contentBox.getChildren().addAll(title, desc, meta);
        card.setCenter(contentBox);

        Button removeBtn = new Button("Remove");
        removeBtn.setStyle("-fx-background-color: #fef2f2; -fx-text-fill: #dc2626; -fx-font-weight: 700; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 16;");
        removeBtn.setOnAction(e -> {
            dbHelper.removeBookmark(UserSessionManager.getCurrentUserId(), article.getUrl());
            loadBookmarks();
        });
        
        VBox rightContainer = new VBox(removeBtn);
        rightContainer.setAlignment(Pos.CENTER);
        card.setRight(rightContainer);
        BorderPane.setMargin(rightContainer, new Insets(0, 0, 0, 25));

        card.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getTarget() != removeBtn) {
                try {
                    Desktop.getDesktop().browse(new URI(article.getUrl()));
                } catch (Exception ex) {
                    System.err.println("Could not open article URL.");
                }
            }
        });

        return card;
    }

    private String formatDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) return "Recent";
        try {
            return dateString.substring(0, 10);
        } catch (Exception e) {
            return "Recent";
        }
    }
}