package headliner;

import javafx.application.Platform;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HomeView {
    private final Stage stage;
    private final ObservableList<Article> articles = FXCollections.observableArrayList();
    private final ListView<Article> listView = new ListView<>();

    private String country = "us";
    private String category = "general";

    public HomeView(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        // Title
        Label title = new Label("📰 Headliner");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Language Selector
        ComboBox<String> langSelector = new ComboBox<>();
        langSelector.getItems().addAll("us", "in", "gb", "fr", "de");
        langSelector.setValue(country);
        langSelector.setOnAction(ev -> {
            country = langSelector.getValue();
            loadArticles();
        });

        // Dark/Light Mode Toggle
        ToggleButton themeToggle = new ToggleButton("Dark Mode");
        themeToggle.setOnAction(ev -> {
            if (themeToggle.isSelected()) {
                stage.getScene().getStylesheets().add(getClass().getResource("/dark.css").toExternalForm());
                themeToggle.setText("Light Mode");
            } else {
                stage.getScene().getStylesheets().clear();
                themeToggle.setText("Dark Mode");
            }
        });

        HBox topBar = new HBox(12, title, langSelector, themeToggle);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-alignment: center-left;");

        // Category Buttons
        HBox chipBox = new HBox(8);
        chipBox.setPadding(new Insets(8));
        String[] cats = {"general", "business", "science", "technology", "sports", "entertainment", "health"};
        for (String c : cats) {
            Button b = new Button(c.substring(0,1).toUpperCase() + c.substring(1));
            b.setOnAction(ev -> {
                category = c;
                loadArticles();
            });
            chipBox.getChildren().add(b);
        }

        // Article List
        listView.setItems(articles);
        listView.setCellFactory(lv -> new ArticleCell());

        listView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                Article sel = listView.getSelectionModel().getSelectedItem();
                if (sel != null && sel.getUrl() != null && !sel.getUrl().isBlank()) {
                    try {
                        Desktop.getDesktop().browse(new URI(sel.getUrl()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Layout
        BorderPane root = new BorderPane();
        root.setTop(new VBox(topBar, chipBox));
        root.setCenter(listView);

        Scene scene = new Scene(root, 900, 600);

        // Load first set of articles
        loadArticles();

        return scene;
    }

    private void loadArticles() {
        articles.clear();

        CompletableFuture.supplyAsync(() -> ApiHelper.fetchTopHeadlines(country, category))
                .thenAccept(result -> Platform.runLater(() -> {
                    articles.setAll(result);
                    if (result.isEmpty()) {
                        articles.add(new Article("No articles found", "Try changing category or country.", ""));
                    }
                }));
    }
}