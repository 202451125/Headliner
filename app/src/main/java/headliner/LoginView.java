package headliner;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginView {
    private final Stage stage;

    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        // Title
        Label title = new Label("🌸 Welcome to Student Portal");
        title.setFont(new Font("Arial Rounded MT Bold", 26));
        title.setTextFill(Color.DARKSLATEBLUE);

        // Username
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setMaxWidth(250);

        // Password
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setMaxWidth(250);

        // Buttons
        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register");

        loginBtn.setStyle("-fx-background-color: #6C63FF; -fx-text-fill: white; -fx-font-weight: bold;");
        registerBtn.setStyle("-fx-background-color: #FF6584; -fx-text-fill: white; -fx-font-weight: bold;");

        // Message Label
        Label message = new Label();
        message.setTextFill(Color.RED);

        // Actions
        loginBtn.setOnAction(ev -> {
            String u = usernameField.getText();
            String p = passwordField.getText();

            if (ApiHelper.login(u, p)) {
                message.setTextFill(Color.GREEN);
                message.setText("Login successful 🎉");

                // Small animation before going to home page
                FadeTransition fade = new FadeTransition(Duration.seconds(1), message);
                fade.setFromValue(0);
                fade.setToValue(1);
                fade.setOnFinished(e -> {
                    HomeView home = new HomeView(stage);
                    stage.setScene(home.createScene());
                    stage.setTitle("Headliner - News Dashboard");
                });
                fade.play();

            } else {
                message.setTextFill(Color.RED);
                message.setText("❌ Invalid username or password");
            }
        });

        registerBtn.setOnAction(ev -> {
            String u = usernameField.getText();
            String p = passwordField.getText();

            if (ApiHelper.register(u, p)) {
                message.setTextFill(Color.GREEN);
                message.setText("Registered successfully! ✅ Please login.");
            } else {
                message.setTextFill(Color.RED);
                message.setText("❌ Registration failed. Try different username.");
            }
        });

        // Layout
        VBox box = new VBox(15, title, usernameField, passwordField, loginBtn, registerBtn, message);
        box.setPadding(new Insets(30));
        box.setAlignment(Pos.CENTER);

        // Background
        StackPane root = new StackPane(box);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #fbc2eb, #a6c1ee);");

        return new Scene(root, 500, 400);
    }
}