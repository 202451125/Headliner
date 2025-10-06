package headliner;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
        
        // Main floating container with shadow - made smaller as requested
        HBox mainContainerBox = new HBox();
        mainContainerBox.setStyle("-fx-background-color: transparent; -fx-effect: dropshadow(gaussian, rgba(255,255,255,0.15), 25, 0, 0, 0);");
        mainContainerBox.setMaxSize(1000, 580);
        
        // Inner content container with rounded corners
        HBox content = new HBox();
        content.setStyle("-fx-background-radius: 15;");
        content.setMaxSize(1000, 580);
        
        // Left side - Animation with darker blue background
        VBox leftSide = new VBox();
        leftSide.setAlignment(Pos.CENTER);
        leftSide.setPrefWidth(450);
        
        // Create darker blue background for left side
        Rectangle leftBackground = new Rectangle(450, 580);
        leftBackground.setFill(Color.web("#2A3B4D")); // Darker shade for animation background
        
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
        
        // Right side - Login form with #567CBD background
        VBox rightSide = new VBox(20);
        rightSide.setAlignment(Pos.CENTER);
        rightSide.setPadding(new Insets(40, 45, 40, 45));
        rightSide.setPrefWidth(550);
        
        // Create background for right side
        Rectangle rightBackground = new Rectangle(550, 580);
        rightBackground.setFill(Color.web("#567CBD"));
        
        StackPane rightContainer = new StackPane(rightBackground, rightSide);
        rightContainer.setAlignment(Pos.CENTER);
        rightContainer.setStyle("-fx-background-radius: 0 15 15 0;");
        
        // Title
        Label title = new Label("Welcome to Headliner");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        title.setFont(Font.font("System", FontWeight.EXTRA_BOLD, 28));
        
        Label subtitle = new Label("Your news companion");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #E0E6F5; -fx-font-style: italic;");
        
        // Form
        VBox form = new VBox(15);
        form.setAlignment(Pos.CENTER_LEFT);
        form.setMaxWidth(350);
        
        // Username field
        Label userLabel = new Label("Username:");
        userLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-font-size: 13px;");
        TextField userField = new TextField();
        userField.setPromptText("Enter your username");
        userField.setStyle("-fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; " +
                           "-fx-border-color: #7A9BD4; -fx-background-color: #3E5F9E; " +
                           "-fx-font-size: 13px; -fx-text-fill: #FFFFFF; -fx-prompt-text-fill: #A8BFE8;");
        
<<<<<<< HEAD
        // Email field (NEW)
        Label emailLabel = new Label("Email (optional for notifications):");
        emailLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-font-size: 13px;");
        TextField emailField = new TextField();
        emailField.setPromptText("your@email.com");
        emailField.setStyle("-fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; " +
                           "-fx-border-color: #7A9BD4; -fx-background-color: #3E5F9E; " +
                           "-fx-font-size: 13px; -fx-text-fill: #FFFFFF; -fx-prompt-text-fill: #A8BFE8;");
        
        // Password field with eye icon (NEW)
        Label passLabel = new Label("Password:");
        passLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-font-size: 13px;");
        
        HBox passwordContainer = new HBox();
        passwordContainer.setSpacing(5);
        passwordContainer.setAlignment(Pos.CENTER_LEFT);
        
=======
        // Password field - CORRECTED LINES
        Label passLabel = new Label("Password:");
        passLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-font-size: 13px;");
>>>>>>> fedec1065c6b9237717e0e8de2583da1bbcc5612
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter your password");
        passField.setStyle("-fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; " +
                          "-fx-border-color: #7A9BD4; -fx-background-color: #3E5F9E; " +
                          "-fx-font-size: 13px; -fx-text-fill: #FFFFFF; -fx-prompt-text-fill: #A8BFE8;");
<<<<<<< HEAD
        passField.setPrefWidth(280);
        
        TextField visiblePassField = new TextField();
        visiblePassField.setPromptText("Enter your password");
        visiblePassField.setStyle("-fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; " +
                                 "-fx-border-color: #7A9BD4; -fx-background-color: #3E5F9E; " +
                                 "-fx-font-size: 13px; -fx-text-fill: #FFFFFF; -fx-prompt-text-fill: #A8BFE8;");
        visiblePassField.setPrefWidth(280);
        visiblePassField.setVisible(false);
        
        // Eye toggle button (NEW)
        Button eyeButton = new Button("👁");
        eyeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #CBD9B6; -fx-font-size: 16px; -fx-padding: 8 12;");
        eyeButton.setOnAction(e -> {
            if (passField.isVisible()) {
                passField.setVisible(false);
                visiblePassField.setVisible(true);
                visiblePassField.setText(passField.getText());
                eyeButton.setText("👁‍🗨");
            } else {
                visiblePassField.setVisible(false);
                passField.setVisible(true);
                passField.setText(visiblePassField.getText());
                eyeButton.setText("👁");
            }
        });
        
        passwordContainer.getChildren().addAll(passField, visiblePassField, eyeButton);

=======
        
>>>>>>> fedec1065c6b9237717e0e8de2583da1bbcc5612
        // Buttons
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);
        
        Button loginBtn = new Button("Login");
        loginBtn.setStyle("-fx-background-color: #CBD9B6; " +
                          "-fx-text-fill: #2F4156; -fx-font-weight: bold; -fx-font-size: 14px; " +
                          "-fx-padding: 12 35; -fx-background-radius: 8; " +
                          "-fx-effect: dropshadow(gaussian, rgba(203,217,182,0.4), 8, 0, 0, 2); " +
                          "-fx-cursor: hand;");
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle("-fx-background-color: #D9E5C7; " +
                                                          "-fx-text-fill: #2F4156; -fx-font-weight: bold; -fx-font-size: 14px; " +
                                                          "-fx-padding: 12 35; -fx-background-radius: 8; " +
                                                          "-fx-effect: dropshadow(gaussian, rgba(203,217,182,0.5), 10, 0, 0, 3); " +
                                                          "-fx-cursor: hand;"));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle("-fx-background-color: #CBD9B6; " +
                                                         "-fx-text-fill: #2F4156; -fx-font-weight: bold; -fx-font-size: 14px; " +
                                                         "-fx-padding: 12 35; -fx-background-radius: 8; " +
                                                         "-fx-effect: dropshadow(gaussian, rgba(203,217,182,0.4), 8, 0, 0, 2); " +
                                                         "-fx-cursor: hand;"));
        loginBtn.setOnAction(e -> handleLogin(userField.getText(), passField.getText()));
        
        Button registerBtn = new Button("Register");
        registerBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #CBD9B6; " +
                             "-fx-border-color: #CBD9B6; -fx-border-width: 2; " +
                             "-fx-padding: 10 32; -fx-background-radius: 8; -fx-border-radius: 8; " +
                             "-fx-font-weight: bold; -fx-font-size: 14px; -fx-cursor: hand;");
        registerBtn.setOnMouseEntered(e -> registerBtn.setStyle("-fx-background-color: rgba(203,217,182,0.15); " +
                                                                "-fx-text-fill: #D9E5C7; " +
                                                                "-fx-border-color: #D9E5C7; -fx-border-width: 2; " +
                                                                "-fx-padding: 10 32; -fx-background-radius: 8; -fx-border-radius: 8; " +
                                                                "-fx-font-weight: bold; -fx-font-size: 14px; -fx-cursor: hand;"));
        registerBtn.setOnMouseExited(e -> registerBtn.setStyle("-fx-background-color: transparent; " +
                                                               "-fx-text-fill: #CBD9B6; " +
                                                               "-fx-border-color: #CBD9B6; -fx-border-width: 2; " +
                                                               "-fx-padding: 10 32; -fx-background-radius: 8; -fx-border-radius: 8; " +
                                                               "-fx-font-weight: bold; -fx-font-size: 14px; -fx-cursor: hand;"));
<<<<<<< HEAD
        registerBtn.setOnAction(e -> handleRegister(userField.getText(), emailField.getText(), passField.getText()));
=======
        registerBtn.setOnAction(e -> handleRegister(userField.getText(), passField.getText()));
>>>>>>> fedec1065c6b9237717e0e8de2583da1bbcc5612
        
        buttons.getChildren().addAll(loginBtn, registerBtn);
        
        statusLabel.setStyle("-fx-text-fill: #FFD166; -fx-font-size: 13px; -fx-font-weight: bold;");
        
<<<<<<< HEAD
        // Build form (UPDATED with email field)
        form.getChildren().addAll(
            userLabel, userField,
            emailLabel, emailField,
            passLabel, passwordContainer
=======
        // Build form
        form.getChildren().addAll(
            userLabel, userField,
            passLabel, passField
>>>>>>> fedec1065c6b9237717e0e8de2583da1bbcc5612
        );
        
        rightSide.getChildren().addAll(title, subtitle, form, buttons, statusLabel);
        
        // Combine sides
        content.getChildren().addAll(leftContainer, rightContainer);
        
        // Add to main container
        mainContainerBox.getChildren().add(content);
        
        // Center everything
        StackPane.setAlignment(mainContainerBox, Pos.CENTER);
        mainContainer.getChildren().add(mainContainerBox);
        
        // Add fade animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), mainContainerBox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
        
        return new Scene(mainContainer, 1100, 650);
    }

    private void handleLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both username and password");
            statusLabel.setStyle("-fx-text-fill: #FFD166; -fx-font-size: 13px; -fx-font-weight: bold;");
            
            // Shake animation for error
            TranslateTransition shake = new TranslateTransition(Duration.millis(300), statusLabel);
            shake.setFromX(0);
            shake.setByX(10);
            shake.setCycleCount(4);
            shake.setAutoReverse(true);
            shake.play();
            
            return;
        }

        boolean success = ApiHelper.login(username, password);
        if (success) {
            statusLabel.setText("Login successful! Redirecting...");
            statusLabel.setStyle("-fx-text-fill: #CBD9B6; -fx-font-size: 13px; -fx-font-weight: bold;");
            
            // Pulse animation for success
            ScaleTransition pulse = new ScaleTransition(Duration.millis(500), statusLabel);
            pulse.setToX(1.1);
            pulse.setToY(1.1);
            pulse.setAutoReverse(true);
            pulse.setCycleCount(4);
            pulse.play();
            
            // Use PauseTransition instead of Timer
            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(event -> {
                HomeView home = new HomeView(stage);
                stage.setScene(home.createScene());
                stage.setTitle("Headliner - News Dashboard");
            });
            pause.play();
        } else {
            statusLabel.setText("Login failed. Please check credentials.");
            statusLabel.setStyle("-fx-text-fill: #FFD166; -fx-font-size: 13px; -fx-font-weight: bold;");
            
            // Shake animation for error
            TranslateTransition shake = new TranslateTransition(Duration.millis(300), statusLabel);
            shake.setFromX(0);
            shake.setByX(10);
            shake.setCycleCount(4);
            shake.setAutoReverse(true);
            shake.play();
        }
    }

<<<<<<< HEAD
    // UPDATED handleRegister method with email
    private void handleRegister(String username, String email, String password) {
=======
    private void handleRegister(String username, String password) {
>>>>>>> fedec1065c6b9237717e0e8de2583da1bbcc5612
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both username and password");
            statusLabel.setStyle("-fx-text-fill: #FFD166; -fx-font-size: 13px; -fx-font-weight: bold;");
            
            // Shake animation for error
            TranslateTransition shake = new TranslateTransition(Duration.millis(300), statusLabel);
            shake.setFromX(0);
            shake.setByX(10);
            shake.setCycleCount(4);
            shake.setAutoReverse(true);
            shake.play();
            
            return;
        }

<<<<<<< HEAD
        boolean success = false;
        try {
            DatabaseHelper db = new DatabaseHelper();
            success = db.registerUserWithEmail(username, email, password);
        } catch (Exception e) {
            // Fallback to original registration without email
            success = ApiHelper.register(username, password);
        }

=======
        boolean success = ApiHelper.register(username, password);
>>>>>>> fedec1065c6b9237717e0e8de2583da1bbcc5612
        if (success) {
            statusLabel.setText("Registration successful! You can now login.");
            statusLabel.setStyle("-fx-text-fill: #CBD9B6; -fx-font-size: 13px; -fx-font-weight: bold;");
            
            // Pulse animation for success
            ScaleTransition pulse = new ScaleTransition(Duration.millis(500), statusLabel);
            pulse.setToX(1.1);
            pulse.setToY(1.1);
            pulse.setAutoReverse(true);
            pulse.setCycleCount(4);
            pulse.play();
        } else {
            statusLabel.setText("Registration failed. Username may exist.");
            statusLabel.setStyle("-fx-text-fill: #FFD166; -fx-font-size: 13px; -fx-font-weight: bold;");
            
            // Shake animation for error
            TranslateTransition shake = new TranslateTransition(Duration.millis(300), statusLabel);
            shake.setFromX(0);
            shake.setByX(10);
            shake.setCycleCount(4);
            shake.setAutoReverse(true);
            shake.play();
        }
    }
}