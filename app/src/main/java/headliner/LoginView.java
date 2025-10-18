package headliner;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginView {
    private final Stage stage;
    private final Label statusLabel = new Label();

    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        // Main container with BLACK background
        StackPane mainContainer = new StackPane();
        mainContainer.setStyle("-fx-background-color: #000000;");
        
        // Main floating container with shadow
        HBox mainContainerBox = new HBox();
        mainContainerBox.setStyle("-fx-background-color: transparent; -fx-effect: dropshadow(gaussian, rgba(255,255,255,0.15), 25, 0, 0, 0);");
        mainContainerBox.setMaxSize(1000, 580);
        
        // --- NEW: Back Button ---
        Button backBtn = new Button("<- Back to Home");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #cccccc; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;");
        backBtn.setOnMouseEntered(e -> backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;"));
        backBtn.setOnMouseExited(e -> backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #cccccc; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;"));
        
        backBtn.setOnAction(e -> {
            HomeView home = new HomeView(stage);
            stage.setScene(home.createScene());
            stage.setTitle("Headliner - News Dashboard");
        });
        
        // Position the back button in the top-left corner
        StackPane.setAlignment(backBtn, Pos.TOP_LEFT);
        StackPane.setMargin(backBtn, new Insets(20));
        // --- End of New Code ---

        // Inner content container with rounded corners
        HBox content = new HBox();
        content.setStyle("-fx-background-radius: 15;");
        content.setMaxSize(1000, 580);
        
        // Left side - Animation with darker blue background
        VBox leftSide = new VBox();
        leftSide.setAlignment(Pos.CENTER);
        leftSide.setPrefWidth(450);
        
        Rectangle leftBackground = new Rectangle(450, 580);
        leftBackground.setFill(Color.web("#2A3B4D"));
        
        StackPane leftContainer = new StackPane(leftBackground);
        leftContainer.setAlignment(Pos.CENTER);
        leftContainer.setStyle("-fx-background-color: #2A3B4D; -fx-background-radius: 15 0 0 15;");
        
        try {
            ImageView animation = new ImageView(new Image(getClass().getResourceAsStream("/headliner_login_animation.gif")));
            animation.setFitWidth(380);
            animation.setFitHeight(380);
            animation.setPreserveRatio(true);
            animation.setStyle("-fx-effect: dropshadow(gaussian, rgba(134,172,217,0.5), 30, 0, 0, 0);");
            leftContainer.getChildren().add(animation);
        } catch (Exception e) {
            Label placeholder = new Label("Animation Placeholder");
            placeholder.setStyle("-fx-text-fill: #CBD9B6; -fx-font-size: 18px; -fx-font-weight: bold;");
            leftContainer.getChildren().add(placeholder);
        }
        
        leftSide.getChildren().add(leftContainer);
        
        // Right side - Login form
        VBox rightSide = new VBox(20);
        rightSide.setAlignment(Pos.CENTER);
        rightSide.setPadding(new Insets(40, 45, 40, 45));
        rightSide.setPrefWidth(550);
        
        Rectangle rightBackground = new Rectangle(550, 580);
        rightBackground.setFill(Color.web("#567CBD"));
        
        StackPane rightContainer = new StackPane(rightBackground, rightSide);
        rightContainer.setAlignment(Pos.CENTER);
        rightContainer.setStyle("-fx-background-radius: 0 15 15 0;");
        
        Label title = new Label("Welcome to Headliner");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        
        Label subtitle = new Label("Your news companion");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #E0E6F5; -fx-font-style: italic;");
        
        VBox form = new VBox(15);
        form.setAlignment(Pos.CENTER_LEFT);
        form.setMaxWidth(350);
        
        Label userLabel = new Label("Username:");
        userLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-font-size: 13px;");
        TextField userField = new TextField();
        userField.setPromptText("Enter your username");
        userField.setStyle("-fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #7A9BD4; -fx-background-color: #3E5F9E; -fx-font-size: 13px; -fx-text-fill: #FFFFFF; -fx-prompt-text-fill: #A8BFE8;");
        
        Label emailLabel = new Label("Email (optional for notifications):");
        emailLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-font-size: 13px;");
        TextField emailField = new TextField();
        emailField.setPromptText("your@email.com");
        emailField.setStyle("-fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #7A9BD4; -fx-background-color: #3E5F9E; -fx-font-size: 13px; -fx-text-fill: #FFFFFF; -fx-prompt-text-fill: #A8BFE8;");
        
        Label passLabel = new Label("Password:");
        passLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-font-size: 13px;");
        
        HBox passwordContainer = new HBox();
        passwordContainer.setSpacing(5);
        passwordContainer.setAlignment(Pos.CENTER_LEFT);
        
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter your password");
        passField.setStyle("-fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #7A9BD4; -fx-background-color: #3E5F9E; -fx-font-size: 13px; -fx-text-fill: #FFFFFF; -fx-prompt-text-fill: #A8BFE8;");
        passField.setPrefWidth(280);
        
        TextField visiblePassField = new TextField();
        visiblePassField.setPromptText("Enter your password");
        visiblePassField.setStyle("-fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #7A9BD4; -fx-background-color: #3E5F9E; -fx-font-size: 13px; -fx-text-fill: #FFFFFF; -fx-prompt-text-fill: #A8BFE8;");
        visiblePassField.setPrefWidth(280);
        visiblePassField.setVisible(false);
        
        Button eyeButton = new Button("👁");
        eyeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #CBD9B6; -fx-font-size: 16px; -fx-padding: 8 12;");
        eyeButton.setOnAction(e -> {
            boolean isHidden = passField.isVisible();
            visiblePassField.setText(isHidden ? passField.getText() : visiblePassField.getText());
            passField.setText(isHidden ? passField.getText() : visiblePassField.getText());
            passField.setVisible(!isHidden);
            visiblePassField.setVisible(isHidden);
            eyeButton.setText(isHidden ? "👁‍🗨" : "👁");
        });
        
        passwordContainer.getChildren().addAll(passField, visiblePassField, eyeButton);

        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);
        
        Button loginBtn = new Button("Login");
        loginBtn.setStyle("-fx-background-color: #CBD9B6; -fx-text-fill: #2F4156; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(203,217,182,0.4), 8, 0, 0, 2); -fx-cursor: hand;");
        loginBtn.setOnAction(e -> handleLogin(userField.getText(), passField.getText()));
        
        Button registerBtn = new Button("Register");
        registerBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #CBD9B6; -fx-border-color: #CBD9B6; -fx-border-width: 2; -fx-padding: 10 32; -fx-background-radius: 8; -fx-border-radius: 8; -fx-font-weight: bold; -fx-font-size: 14px; -fx-cursor: hand;");
        registerBtn.setOnAction(e -> handleRegister(userField.getText(), emailField.getText(), passField.getText()));
        
        buttons.getChildren().addAll(loginBtn, registerBtn);
        statusLabel.setStyle("-fx-text-fill: #FFD166; -fx-font-size: 13px; -fx-font-weight: bold;");
        
        form.getChildren().addAll(userLabel, userField, emailLabel, emailField, passLabel, passwordContainer);
        rightSide.getChildren().addAll(title, subtitle, form, buttons, statusLabel);
        content.getChildren().addAll(leftSide, rightContainer);
        mainContainerBox.getChildren().add(content);
        
        StackPane.setAlignment(mainContainerBox, Pos.CENTER);
        
        // Add both the main content and the back button to the root StackPane
        mainContainer.getChildren().addAll(mainContainerBox, backBtn);
        
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), mainContainerBox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
        
        return new Scene(mainContainer, 1100, 650);
    }

    private void handleLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both username and password");
            return;
        }

        boolean success = ApiHelper.login(username, password);
        if (success) {
            UserSessionManager.loginUser(username);
            
            statusLabel.setText("Login successful! Redirecting...");
            statusLabel.setStyle("-fx-text-fill: #CBD9B6; -fx-font-size: 13px; -fx-font-weight: bold;");
            
            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(event -> {
                HomeView home = new HomeView(stage);
                stage.setScene(home.createScene());
                stage.setTitle("Headliner - News Dashboard");
            });
            pause.play();
        } else {
            statusLabel.setText("Login failed. Please check credentials.");
        }
    }

    private void handleRegister(String username, String email, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both username and password");
            return;
        }

        boolean success = ApiHelper.registerWithEmail(username, email, password);

        if (success) {
            statusLabel.setText("Registration successful! You can now login.");
            statusLabel.setStyle("-fx-text-fill: #CBD9B6; -fx-font-size: 13px; -fx-font-weight: bold;");
        } else {
            statusLabel.setText("Registration failed. Username may exist.");
        }
    }
}