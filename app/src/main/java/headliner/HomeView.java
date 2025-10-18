package headliner;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.Desktop;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HomeView {
    private final Stage stage;
    private FlowPane articlesFlow;
    private VBox articlesContainer;
    private ScrollPane scrollPane;
    private HBox topHeader;
    private VBox sidebar;
    private boolean sidebarVisible = false;
    
    private String country = "us";
    private String category = "general";
    private BorderPane root;
    private VBox heroSection;
    private boolean isDarkTheme = false;
    private Label typingSlogan;
    
    private double xOffset = 0;
    private double yOffset = 0;

    private final DatabaseHelper dbHelper = new DatabaseHelper();

    public HomeView(Stage stage) {
        this.stage = stage;
        if (stage.getStyle() != StageStyle.UNDECORATED) {
            stage.initStyle(StageStyle.UNDECORATED);
        }
    }

    public Scene createScene() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(135deg, #FCF9EA 0%, #F4E9D7 100%);");
        
        sidebar = createSidebar();
        topHeader = createTopHeader();
        heroSection = createHeroSection();
        VBox mainContent = createMainContent();
        
        VBox topContainer = new VBox(topHeader, heroSection);
        root.setTop(topContainer);
        root.setCenter(mainContent);
        
        StackPane overlayRoot = new StackPane();
        overlayRoot.getChildren().addAll(root, sidebar);
        StackPane.setAlignment(sidebar, Pos.CENTER_LEFT);
        sidebar.setTranslateX(-280);
        
        Scene scene = new Scene(overlayRoot, 1400, 900);
        scene.setFill(Color.TRANSPARENT);
        
        enableWindowDragging(topHeader);
        startTypingAnimation();
        loadArticles();
        
        return scene;
    }

    private void enableWindowDragging(HBox header) {
        header.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        header.setOnMouseDragged((MouseEvent event) -> {
            if (!stage.isMaximized()) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
    }

    private HBox createTopHeader() {
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #1B3C53; -fx-padding: 8 15; -fx-alignment: center-left; -fx-cursor: move;");
        
        Button sidebarBtn = new Button("Menu");
        sidebarBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 50; -fx-min-height: 35; -fx-cursor: hand;");
        sidebarBtn.setOnAction(e -> toggleSidebar());
        
        Label title = new Label("Headliner");
        title.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px; -fx-padding: 0 20;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button themeBtn = new Button(isDarkTheme ? "Light" : "Dark");
        themeBtn.setStyle("-fx-background-color: #567CBD; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 15; -fx-padding: 8 20; -fx-cursor: hand;");
        themeBtn.setOnAction(e -> toggleDarkMode());
        
        Button loginBtn = new Button("Login");
        loginBtn.setStyle("-fx-background-color: #567CBD; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 15; -fx-padding: 8 20; -fx-cursor: hand;");
        loginBtn.setOnAction(e -> openLoginView());
        
        HBox windowControls = createWindowControls();
        
        header.getChildren().addAll(sidebarBtn, title, spacer, themeBtn, loginBtn, windowControls);
        return header;
    }

    private HBox createWindowControls() {
        HBox controls = new HBox(0);
        controls.setAlignment(Pos.CENTER_RIGHT);
        String normalStyle = "-fx-background-color: transparent; -fx-text-fill: #cccccc; -fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 45; -fx-min-height: 30; -fx-border-color: transparent; -fx-cursor: hand;";
        String hoverStyle = "-fx-background-color: #405770; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 45; -fx-min-height: 30; -fx-border-color: transparent; -fx-cursor: hand;";
        
        Button minimizeBtn = new Button("_");
        Button maximizeBtn = new Button("[]");
        Button closeBtn = new Button("X");
        
        minimizeBtn.setStyle(normalStyle);
        maximizeBtn.setStyle(normalStyle);
        closeBtn.setStyle(normalStyle);
        
        minimizeBtn.setOnMouseEntered(e -> minimizeBtn.setStyle(hoverStyle));
        minimizeBtn.setOnMouseExited(e -> minimizeBtn.setStyle(normalStyle));
        maximizeBtn.setOnMouseEntered(e -> maximizeBtn.setStyle(hoverStyle));
        maximizeBtn.setOnMouseExited(e -> maximizeBtn.setStyle(normalStyle));
        closeBtn.setOnMouseEntered(e -> closeBtn.setStyle("-fx-background-color: #e81123; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 45; -fx-min-height: 30; -fx-cursor: hand;"));
        closeBtn.setOnMouseExited(e -> closeBtn.setStyle(normalStyle));
        
        minimizeBtn.setOnAction(e -> stage.setIconified(true));
        maximizeBtn.setOnAction(e -> toggleMaximize());
        closeBtn.setOnAction(e -> Platform.exit());
        
        controls.getChildren().addAll(minimizeBtn, maximizeBtn, closeBtn);
        return controls;
    }

    private void toggleMaximize() {
        stage.setMaximized(!stage.isMaximized());
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(280);
        sidebar.setMaxWidth(280);
        sidebar.setStyle("-fx-background-color: #2F4156; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 30, 0, 0, 5);");
        
        HBox sidebarHeader = new HBox();
        sidebarHeader.setStyle("-fx-background-color: #1B3C53; -fx-padding: 12 15; -fx-alignment: center-left;");
        Label sidebarTitle = new Label("Headliner Menu");
        sidebarTitle.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button closeSidebarBtn = new Button("X");
        closeSidebarBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #CBD9B6; -fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand;");
        closeSidebarBtn.setOnAction(e -> toggleSidebar());
        sidebarHeader.getChildren().addAll(sidebarTitle, spacer, closeSidebarBtn);

        VBox profileSection = new VBox(15);
        profileSection.setAlignment(Pos.CENTER);
        profileSection.setPadding(new Insets(25, 20, 25, 20));
        profileSection.setStyle("-fx-background-color: #1B3C53; -fx-border-color: #567CBD; -fx-border-width: 0 0 1 0;");
        
        Circle profilePic = new Circle(40, Color.web("#567CBD"));
        Label userName = new Label(UserSessionManager.isUserLoggedIn() ? UserSessionManager.getCurrentUser() : "Guest User");
        userName.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px;");
        Label userStatus = new Label(UserSessionManager.isUserLoggedIn() ? "Premium Member" : "Free Account");
        userStatus.setStyle("-fx-text-fill: #CBD9B6; -fx-font-size: 13px;");
        Button profileBtn = new Button("View Profile");
        profileBtn.setStyle("-fx-background-color: #567CBD; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 15; -fx-padding: 10 20; -fx-cursor: hand;");
        profileBtn.setOnAction(e -> {
            toggleSidebar();
            openProfileView();
        });
        profileSection.getChildren().addAll(profilePic, userName, userStatus, profileBtn);

        VBox navSection = new VBox(0);
        navSection.setPadding(new Insets(10, 0, 20, 0));
        String[] navItems = { "Home", "Bookmarks", "Favorites", "Settings", "Notifications", "Statistics", "Logout" };
        
        for (String item : navItems) {
            Button navBtn = new Button(item);
            navBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-alignment: center-left; -fx-padding: 15 25; -fx-cursor: hand; -fx-border-color: transparent;");
            navBtn.setMaxWidth(Double.MAX_VALUE);
            navBtn.setOnMouseEntered(e -> navBtn.setStyle("-fx-background-color: #567CBD; -fx-text-fill: white; -fx-font-weight: bold; -fx-alignment: center-left; -fx-padding: 15 25; -fx-cursor: hand;"));
            navBtn.setOnMouseExited(e -> navBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-alignment: center-left; -fx-padding: 15 25; -fx-cursor: hand;"));
            
            if (item.equals("Home")) {
                navBtn.setOnAction(e -> toggleSidebar());
            } else if (item.equals("Bookmarks")) {
                navBtn.setOnAction(e -> openBookmarksView());
            } else if (item.equals("Logout")) {
                navBtn.setOnAction(e -> {
                    toggleSidebar();
                    logoutUser();
                });
            } else {
                navBtn.setOnAction(e -> {
                    toggleSidebar();
                    showAlert(item, "Feature coming soon!");
                });
            }
            navSection.getChildren().add(navBtn);
            if (!item.equals("Logout")) {
                Separator separator = new Separator();
                separator.setStyle("-fx-background-color: #405770;");
                navSection.getChildren().add(separator);
            }
        }
        sidebar.getChildren().addAll(sidebarHeader, profileSection, navSection);
        return sidebar;
    }

    private void toggleSidebar() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(300), sidebar);
        tt.setToX(sidebarVisible ? -280 : 0);
        tt.play();
        sidebarVisible = !sidebarVisible;
    }

    private void toggleDarkMode() {
        isDarkTheme = !isDarkTheme;
        
        Button themeBtn = null;
        for (Node node : topHeader.getChildren()) {
            if (node instanceof Button && (((Button)node).getText().contains("Light") || ((Button)node).getText().contains("Dark"))) {
                themeBtn = (Button) node;
                break;
            }
        }
        if (themeBtn != null) {
            themeBtn.setText(isDarkTheme ? "Light" : "Dark");
        }
        
        if (isDarkTheme) applyDarkTheme();
        else applyLightTheme();
    }

    private void applyDarkTheme() {
        root.setStyle("-fx-background-color: #1B2A3B;");
        heroSection.setStyle("-fx-background-color: linear-gradient(90deg, #2F4156 0%, #567CBD 100%);");
        articlesContainer.setStyle("-fx-background-color: #2F4156; -fx-background-radius: 30; -fx-effect: dropshadow(gaussian, rgba(203,217,182,0.1), 30, 0, 10, 10); -fx-border-color: #567CBD; -fx-border-width: 2; -fx-border-radius: 30; -fx-padding: 30;");
        updateCategoriesBarForDarkTheme();
        loadArticles();
    }

    private void applyLightTheme() {
        root.setStyle("-fx-background-color: linear-gradient(135deg, #FCF9EA 0%, #F4E9D7 100%);");
        heroSection.setStyle("-fx-background-color: linear-gradient(90deg, #1CB5E0 0%, #000851 100%);");
        articlesContainer.setStyle("-fx-background-color: #FCF9EA; -fx-background-radius: 30; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 30, 0, 10, 10); -fx-border-color: #BADFDB; -fx-border-width: 2; -fx-border-radius: 30; -fx-padding: 30;");
        updateCategoriesBarForLightTheme();
        loadArticles();
    }

    private void updateCategoriesBarForDarkTheme() {
        HBox categoriesBar = (HBox) ((VBox) root.getCenter()).getChildren().get(0);
        for (Node node : categoriesBar.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                if (btn.getText().equalsIgnoreCase(category)) {
                    btn.setStyle("-fx-background-color: linear-gradient(to right, #567CBD, #CBD9B6); -fx-text-fill: #2F4156; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10 20; -fx-cursor: hand;");
                } else {
                    btn.setStyle("-fx-background-color: #3E5F9E; -fx-text-fill: #CBD9B6; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10 20; -fx-cursor: hand;");
                }
            }
        }
    }

    private void updateCategoriesBarForLightTheme() {
        HBox categoriesBar = (HBox) ((VBox) root.getCenter()).getChildren().get(0);
        for (Node node : categoriesBar.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                if (btn.getText().equalsIgnoreCase(category)) {
                    btn.setStyle("-fx-background-color: linear-gradient(to right, #1CB5E0, #000851); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10 20; -fx-cursor: hand;");
                } else {
                    btn.setStyle("-fx-background-color: #BADFDB; -fx-text-fill: #1B3C53; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10 20; -fx-cursor: hand;");
                }
            }
        }
    }

    private VBox createHeroSection() {
        VBox hero = new VBox(20);
        hero.setAlignment(Pos.CENTER);
        hero.setPadding(new Insets(30, 0, 30, 0));
        hero.setStyle("-fx-background-color: linear-gradient(90deg, #1CB5E0 0%, #000851 100%);");
        
        Label mainTitle = new Label("HEADLINER");
        mainTitle.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 72px; -fx-font-weight: 900; -fx-text-fill: #FFFFFF; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 15, 0, 3, 3);");
        
        typingSlogan = new Label("");
        typingSlogan.setStyle("-fx-font-size: 20px; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 8, 0, 2, 2);");
        
        HBox searchContainer = new HBox(10);
        searchContainer.setAlignment(Pos.CENTER);
        searchContainer.setPadding(new Insets(15, 0, 0, 0));
        
        TextField searchField = new TextField();
        searchField.setPromptText("Search for news, topics, or keywords...");
        searchField.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 25; -fx-border-color: #1CB5E0; -fx-border-radius: 25; -fx-border-width: 2; -fx-prompt-text-fill: #666; -fx-font-size: 16px; -fx-text-fill: #1B3C53; -fx-padding: 12 20;");
        searchField.setPrefWidth(400);
        
        Button searchBtn = new Button("Search News");
        searchBtn.setStyle("-fx-background-color: #1CB5E0; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 12 25; -fx-cursor: hand;");
        searchBtn.setOnAction(e -> performSearch(searchField.getText()));
        searchField.setOnAction(e -> performSearch(searchField.getText()));
        
        searchContainer.getChildren().addAll(searchField, searchBtn);
        hero.getChildren().addAll(mainTitle, typingSlogan, searchContainer);
        return hero;
    }

    private VBox createMainContent() {
        VBox mainContent = new VBox();
        mainContent.setPadding(new Insets(20));
        
        HBox categoriesBar = createCategoriesBar();
        
        articlesContainer = new VBox();
        articlesContainer.setStyle("-fx-background-color: #FCF9EA; -fx-background-radius: 30; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 30, 0, 10, 10); -fx-border-color: #BADFDB; -fx-border-width: 2; -fx-border-radius: 30; -fx-padding: 30;");
        
        articlesFlow = new FlowPane();
        articlesFlow.setHgap(25);
        articlesFlow.setVgap(25);
        articlesFlow.setPadding(new Insets(10));
        articlesFlow.setPrefWrapLength(1200);
        articlesFlow.setAlignment(Pos.CENTER);
        
        articlesContainer.getChildren().add(articlesFlow);
        
        scrollPane = new ScrollPane(articlesContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        setupScrollEffects();
        
        mainContent.getChildren().addAll(categoriesBar, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        return mainContent;
    }

    private void setupScrollEffects() {
        scrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isScrollingDown = newVal.doubleValue() > oldVal.doubleValue();
            if (isScrollingDown && newVal.doubleValue() > 0.05) {
                if (heroSection.isManaged()) {
                    TranslateTransition tt = new TranslateTransition(Duration.millis(300), heroSection);
                    tt.setToY(-heroSection.getHeight());
                    tt.setOnFinished(e -> {
                        heroSection.setVisible(false);
                        heroSection.setManaged(false);
                    });
                    tt.play();
                }
            } else if (!isScrollingDown && newVal.doubleValue() < 0.05) {
                if (!heroSection.isManaged()) {
                    heroSection.setVisible(true);
                    heroSection.setManaged(true);
                    TranslateTransition tt = new TranslateTransition(Duration.millis(300), heroSection);
                    tt.setToY(0);
                    tt.play();
                }
            }
        });
    }

    private HBox createCategoriesBar() {
        HBox categoriesBar = new HBox(15);
        categoriesBar.setAlignment(Pos.CENTER);
        categoriesBar.setPadding(new Insets(20, 0, 30, 0));
        String[] categories = {"General", "Business", "Tech", "Science", "Sports", "Entertainment", "Health", "Politics"};
        
        for (String cat : categories) {
            Button catBtn = new Button(cat);
            catBtn.setStyle("-fx-background-color: " + (cat.equalsIgnoreCase(category) ? "linear-gradient(to right, #1CB5E0, #000851)" : "#BADFDB") + "; -fx-text-fill: " + (cat.equalsIgnoreCase(category) ? "white" : "#1B3C53") + "; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10 20; -fx-cursor: hand;");
            
            catBtn.setOnAction(e -> {
                category = cat.toLowerCase();
                loadArticles();
                
                categoriesBar.getChildren().forEach(node -> {
                    Button btn = (Button) node;
                    btn.setStyle("-fx-background-color: #BADFDB; -fx-text-fill: #1B3C53; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10 20; -fx-cursor: hand;");
                });
                catBtn.setStyle("-fx-background-color: linear-gradient(to right, #1CB5E0, #000851); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10 20; -fx-cursor: hand;");
            });
            categoriesBar.getChildren().add(catBtn);
        }
        return categoriesBar;
    }

    private void performSearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            loadArticles();
            return;
        }
        showLoading();
        CompletableFuture.supplyAsync(() -> ApiHelper.searchArticles(query, "en"))
                .thenAccept(result -> Platform.runLater(() -> displayArticles(result)));
    }

    private void loadArticles() {
        showLoading();
        CompletableFuture.supplyAsync(() -> ApiHelper.fetchTopHeadlines(country, category))
                .thenAccept(result -> Platform.runLater(() -> displayArticles(result)));
    }

    private void displayArticles(List<Article> articles) {
        articlesFlow.getChildren().clear();
        if (articles.isEmpty()) {
            articlesFlow.getChildren().add(createNoResultsCard());
            return;
        }
        for (Article article : articles) {
            articlesFlow.getChildren().add(createArticleCard(article));
        }
    }

    private VBox createArticleCard(Article article) {
        VBox card = new VBox();
        card.setPrefWidth(370);
        card.setMaxWidth(370);
        
        if (isDarkTheme) {
            card.setStyle("-fx-background-color: #2F4156; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 5, 5); -fx-border-color: #567CBD; -fx-border-width: 1; -fx-border-radius: 20; -fx-cursor: hand;");
        } else {
            card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 5, 5); -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 20; -fx-cursor: hand;");
        }

        ImageView imageView = new ImageView();
        imageView.setFitWidth(370);
        imageView.setFitHeight(200);
        imageView.setStyle("-fx-background-radius: 20 20 0 0;");
        if (article.getUrlToImage() != null && !article.getUrlToImage().isEmpty()) {
            try {
                imageView.setImage(new Image(article.getUrlToImage(), 370, 200, false, true, true));
            } catch (Exception e) {
                imageView.setStyle("-fx-background-color: linear-gradient(135deg, #1CB5E0, #000851); -fx-background-radius: 20 20 0 0;");
            }
        } else {
            imageView.setStyle("-fx-background-color: linear-gradient(135deg, #1CB5E0, #000851); -fx-background-radius: 20 20 0 0;");
        }

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        HBox metaInfo = new HBox();
        metaInfo.setAlignment(Pos.CENTER_LEFT);
        
        Label categoryLabel = new Label(article.getCategory().toUpperCase());
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label dateLabel = new Label(formatDate(article.getPublishedAt()));
        
        Label title = new Label(article.getTitle());
        title.setWrapText(true);
        title.setMaxHeight(50);
        Label description = new Label(article.getDescription());
        description.setWrapText(true);
        description.setMaxHeight(60);
        
        if (isDarkTheme) {
            categoryLabel.setStyle("-fx-text-fill: #CBD9B6; -fx-font-weight: bold; -fx-font-size: 12px;");
            dateLabel.setStyle("-fx-text-fill: #7A9BD4; -fx-font-size: 12px;");
            title.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #CBD9B6;");
            description.setStyle("-fx-text-fill: #A8BFE8; -fx-font-size: 14px;");
        } else {
            categoryLabel.setStyle("-fx-text-fill: #1CB5E0; -fx-font-weight: bold; -fx-font-size: 12px;");
            dateLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");
            title.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #1B3C53;");
            description.setStyle("-fx-text-fill: #666; -fx-font-size: 14px;");
        }
        metaInfo.getChildren().addAll(categoryLabel, spacer, dateLabel);

        HBox actionBox = new HBox(10);
        actionBox.setAlignment(Pos.CENTER_LEFT);
        
        Button readMoreBtn = new Button("Read More");
        Button saveBtn = new Button();

        String readMoreStyle = isDarkTheme ? "-fx-background-color: #567CBD; -fx-text-fill: white;" : "-fx-background-color: #1CB5E0; -fx-text-fill: white;";
        readMoreBtn.setStyle(readMoreStyle + " -fx-font-weight: bold; -fx-background-radius: 15; -fx-padding: 8 20; -fx-cursor: hand;");
        readMoreBtn.setOnAction(e -> openArticle(article));

        updateBookmarkButtonState(saveBtn, article);
        saveBtn.setOnAction(e -> handleBookmarkAction(saveBtn, article));
        
        Region actionSpacer = new Region();
        HBox.setHgrow(actionSpacer, Priority.ALWAYS);
        actionBox.getChildren().addAll(saveBtn, actionSpacer, readMoreBtn);

        content.getChildren().addAll(metaInfo, title, description, actionBox);
        card.getChildren().addAll(imageView, content);

        return card;
    }

    private void updateBookmarkButtonState(Button btn, Article article) {
        if (!UserSessionManager.isUserLoggedIn() || article.getUrl() == null) {
            btn.setText("Save");
            btn.setDisable(true);
            return;
        }
        btn.setDisable(false);
        CompletableFuture.supplyAsync(() -> dbHelper.isBookmarked(UserSessionManager.getCurrentUserId(), article.getUrl()))
            .thenAccept(isBookmarked -> Platform.runLater(() -> {
                String baseStyle = "-fx-font-weight: bold; -fx-background-radius: 15; -fx-padding: 8 20; -fx-cursor: hand;";
                if (isBookmarked) {
                    btn.setText("Saved");
                    btn.setStyle("-fx-background-color: #10B981; -fx-text-fill: white;" + baseStyle);
                } else {
                    btn.setText("Save");
                    String saveStyle = isDarkTheme ? "-fx-background-color: #3E5F9E; -fx-text-fill: #CBD9B6;" : "-fx-background-color: #BADFDB; -fx-text-fill: #1B3C53;";
                    btn.setStyle(saveStyle + baseStyle);
                }
            }));
    }

    private void handleBookmarkAction(Button btn, Article article) {
        if (!UserSessionManager.isUserLoggedIn()) {
            showAlert("Login Required", "You must be logged in to save articles.");
            return;
        }
        CompletableFuture.supplyAsync(() -> dbHelper.isBookmarked(UserSessionManager.getCurrentUserId(), article.getUrl()))
            .thenAccept(isBookmarked -> {
                if (isBookmarked) {
                    dbHelper.removeBookmark(UserSessionManager.getCurrentUserId(), article.getUrl());
                } else {
                    dbHelper.addBookmark(UserSessionManager.getCurrentUserId(), article);
                }
                Platform.runLater(() -> updateBookmarkButtonState(btn, article));
            });
    }

    private void openBookmarksView() {
        toggleSidebar();
        BookmarksView bookmarksView = new BookmarksView(stage);
        stage.setScene(bookmarksView.createScene());
    }

    private void showLoading() {
        articlesFlow.getChildren().clear();
        for (int i = 0; i < 6; i++) {
            articlesFlow.getChildren().add(createLoadingCard());
        }
    }

    private VBox createLoadingCard() {
        VBox card = new VBox(15);
        card.setPrefWidth(370);
        card.setPrefHeight(400); // Approximate height
        card.setStyle("-fx-background-color: #f8f8f8; -fx-background-radius: 20;");
        card.setAlignment(Pos.CENTER);
        
        ProgressIndicator spinner = new ProgressIndicator();
        Label loading = new Label("Loading...");
        loading.setStyle("-fx-text-fill: #666;");
        
        card.getChildren().addAll(spinner, loading);
        return card;
    }

    private VBox createNoResultsCard() {
        VBox card = new VBox(20);
        card.setPrefWidth(600);
        card.setPrefHeight(200);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-padding: 40;");
        
        Label icon = new Label("Info");
        icon.setStyle("-fx-font-size: 48px;");
        Label message = new Label("No articles found. Try a different search or category.");
        message.setStyle("-fx-text-fill: #666; -fx-font-size: 16px;");
        
        card.getChildren().addAll(icon, message);
        return card;
    }
    
    private void startTypingAnimation() {
        String[] slogans = { "Your Gateway to Global News", "Stay Informed, Stay Ahead", "Breaking News, Real-Time Updates", "News That Matters to You" };
        Thread animationThread = new Thread(() -> {
            try {
                int sloganIndex = 0;
                while (true) {
                    String slogan = slogans[sloganIndex];
                    for (int i = 0; i <= slogan.length(); i++) {
                        final String text = slogan.substring(0, i);
                        Platform.runLater(() -> typingSlogan.setText(text));
                        Thread.sleep(100);
                    }
                    Thread.sleep(2000);
                    for (int i = slogan.length(); i >= 0; i--) {
                        final String text = slogan.substring(0, i);
                        Platform.runLater(() -> typingSlogan.setText(text));
                        Thread.sleep(50);
                    }
                    sloganIndex = (sloganIndex + 1) % slogans.length;
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        animationThread.setDaemon(true);
        animationThread.start();
    }

    private void openLoginView() {
        LoginView loginView = new LoginView(stage);
        stage.setScene(loginView.createScene());
    }

    private void openProfileView() {
        ProfileView profileView = new ProfileView(stage);
        stage.setScene(profileView.createScene());
    }

    private void logoutUser() {
        UserSessionManager.logoutUser();
        refreshUI();
    }

    private void openArticle(Article article) {
        if (article.getUrl() != null && !article.getUrl().isBlank()) {
            try {
                Desktop.getDesktop().browse(new URI(article.getUrl()));
            } catch (Exception ex) {
                showAlert("Error", "Could not open article in browser.");
            }
        }
    }
    
    private void shareArticle(Article article) {
        showAlert("Share", "This would open a sharing dialog.");
    }

    private String formatDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) return "Recent";
        try {
            return dateString.substring(0, 10);
        } catch (Exception e) {
            return "Recent";
        }
    }

    private void refreshUI() {
        HomeView newHome = new HomeView(stage);
        stage.setScene(newHome.createScene());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}