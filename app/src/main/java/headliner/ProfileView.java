package headliner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ProfileView {
    private final Stage stage;
    private ImageView profilePicView;

    public ProfileView(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f7fafc;");

        // Header
        VBox header = createHeader();
        
        // Content
        ScrollPane content = createContent();
        
        root.setTop(header);
        root.setCenter(content);
        
        Scene scene = new Scene(root, 1000, 700);
        
        // Load CSS safely
        try {
            String cssPath = getClass().getResource("/style.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
            System.out.println("✅ CSS loaded successfully for ProfileView");
        } catch (Exception e) {
            System.out.println("⚠ CSS file not found for ProfileView, using inline styles");
        }
        
        return scene;
    }

   private VBox createHeader() {
    VBox header = new VBox(20);
    header.setStyle("-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%); -fx-padding: 40 0;");
    header.setAlignment(Pos.CENTER);
    
    // Back button at top left
    HBox topBar = new HBox();
    topBar.setPadding(new Insets(0, 0, 10, 30));
    topBar.setAlignment(Pos.CENTER_LEFT);
    
    Button backButton = new Button("← Back to Home");
    backButton.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 15; -fx-padding: 8 15;");
    backButton.setOnAction(e -> {
        HomeView home = new HomeView(stage);
        stage.setScene(home.createScene());
    });
    
    topBar.getChildren().add(backButton);
    
    Label title = new Label("Your Profile");
    title.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");
    
    Label subtitle = new Label("Manage your account and preferences");
    subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: rgba(255,255,255,0.8);");
    
    header.getChildren().addAll(topBar, title, subtitle);
    return header;
}

    private ScrollPane createContent() {
        VBox content = new VBox(30);
        content.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 2, 4); -fx-padding: 40;");
        content.setAlignment(Pos.TOP_CENTER);
        
        // Profile Picture Section
        VBox avatarSection = createAvatarSection();
        
        // User Info Section
        VBox infoSection = createInfoSection();
        
        // Actions Section
        VBox actionsSection = createActionsSection();
        
        content.getChildren().addAll(avatarSection, infoSection, actionsSection);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        return scrollPane;
    }

    private VBox createAvatarSection() {
        VBox avatarSection = new VBox(20);
        avatarSection.setAlignment(Pos.CENTER);
        
        Label sectionTitle = new Label("Profile Picture");
        sectionTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2d3748;");
        
        profilePicView = new ImageView();
        profilePicView.setFitWidth(120);
        profilePicView.setFitHeight(120);
        
        // Create circular clip for profile picture
        Circle clip = new Circle(60, 60, 60);
        profilePicView.setClip(clip);
        
        updateProfilePicture();
        
        Button changeAvatarBtn = new Button("Change Avatar");
        changeAvatarBtn.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;");
        changeAvatarBtn.setOnAction(e -> openAvatarChooser());
        
        avatarSection.getChildren().addAll(sectionTitle, profilePicView, changeAvatarBtn);
        return avatarSection;
    }

    private VBox createInfoSection() {
        VBox infoSection = new VBox(15);
        infoSection.setAlignment(Pos.CENTER_LEFT);
        
        Label sectionTitle = new Label("Account Information");
        sectionTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2d3748;");
        
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(20);
        infoGrid.setVgap(15);
        infoGrid.setPadding(new Insets(20, 0, 0, 0));
        
        // Username
        Label userLabel = new Label("Username:");
        userLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #4a5568;");
        Label username = new Label(UserSessionManager.getCurrentUser() != null ? UserSessionManager.getCurrentUser() : "Guest");
        username.setStyle("-fx-text-fill: #2d3748;");
        
        // Email
        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #4a5568;");
        String userEmail = UserSessionManager.getCurrentUserEmail();
        Label email = new Label(userEmail != null ? userEmail : "Not provided");
        email.setStyle("-fx-text-fill: #2d3748;");
        
        // Member since
        Label memberLabel = new Label("Member since:");
        memberLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #4a5568;");
        Label memberSince = new Label("Today");
        memberSince.setStyle("-fx-text-fill: #2d3748;");
        
        infoGrid.add(userLabel, 0, 0);
        infoGrid.add(username, 1, 0);
        infoGrid.add(emailLabel, 0, 1);
        infoGrid.add(email, 1, 1);
        infoGrid.add(memberLabel, 0, 2);
        infoGrid.add(memberSince, 1, 2);
        
        infoSection.getChildren().addAll(sectionTitle, infoGrid);
        return infoSection;
    }

    private VBox createActionsSection() {
        VBox actionsSection = new VBox(15);
        actionsSection.setAlignment(Pos.CENTER_LEFT);
        
        Label sectionTitle = new Label("Account Actions");
        sectionTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2d3748;");
        
        VBox buttonsBox = new VBox(10);
        buttonsBox.setPadding(new Insets(20, 0, 0, 0));
        
        Button changeUsernameBtn = new Button("Change Username");
        Button changeEmailBtn = new Button("Update Email");
        Button changePasswordBtn = new Button("Change Password");
        Button deleteAccountBtn = new Button("Delete Account");
        
        // Style buttons
        String buttonStyle = "-fx-background-color: #667eea; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20; -fx-pref-width: 200;";
        changeUsernameBtn.setStyle(buttonStyle);
        changeEmailBtn.setStyle(buttonStyle);
        changePasswordBtn.setStyle(buttonStyle);
        deleteAccountBtn.setStyle(buttonStyle + " -fx-text-fill: #e53e3e;");
        
        changeUsernameBtn.setOnAction(e -> showAlert("Username", "Change username feature coming soon!"));
        changeEmailBtn.setOnAction(e -> showAlert("Email", "Update email feature coming soon!"));
        changePasswordBtn.setOnAction(e -> showAlert("Password", "Change password feature coming soon!"));
        deleteAccountBtn.setOnAction(e -> showAlert("Delete", "Account deletion feature coming soon!"));
        
        buttonsBox.getChildren().addAll(changeUsernameBtn, changeEmailBtn, changePasswordBtn, deleteAccountBtn);
        actionsSection.getChildren().addAll(sectionTitle, buttonsBox);
        
        return actionsSection;
    }

    private void updateProfilePicture() {
        String picName = UserSessionManager.getProfilePic();
        try {
            String imagePath = "/avatars/" + picName + ".png";
            java.net.URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                Image image = new Image(imageUrl.toExternalForm());
                profilePicView.setImage(image);
                System.out.println("✅ Loaded avatar: " + picName);
            } else {
                // Create a colored placeholder
                profilePicView.setImage(null);
                profilePicView.setStyle("-fx-background-color: #667eea; -fx-background-radius: 60;");
                System.out.println("⚠ Avatar not found: " + picName);
            }
        } catch (Exception e) {
            System.out.println("❌ Error loading avatar: " + e.getMessage());
            profilePicView.setImage(null);
            profilePicView.setStyle("-fx-background-color: #667eea; -fx-background-radius: 60;");
        }
    }

    private void openAvatarChooser() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Choose Your Avatar");
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);
        
        Label title = new Label("Select an Avatar");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        GridPane avatarGrid = new GridPane();
        avatarGrid.setHgap(15);
        avatarGrid.setVgap(15);
        avatarGrid.setPadding(new Insets(20));
        avatarGrid.setStyle("-fx-background-color: #f7fafc; -fx-background-radius: 8;");
        
        String[] avatars = {"default", "lion", "elephant", "panda", "fox", "owl", "eagle", "tiger"};
        String[] avatarNames = {"😊 Default", "🦁 Lion", "🐘 Elephant", "🐼 Panda", "🦊 Fox", "🦉 Owl", "🦅 Eagle", "🐯 Tiger"};
        
        int col = 0;
        int row = 0;
        for (int i = 0; i < avatars.length; i++) {
            final String avatar = avatars[i];
            final String avatarName = avatarNames[i];
            
            VBox avatarContainer = new VBox(10);
            avatarContainer.setAlignment(Pos.CENTER);
            avatarContainer.setPadding(new Insets(15));
            avatarContainer.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 1, 1); -fx-cursor: hand;");
            
            try {
                String imagePath = "/avatars/" + avatar + ".png";
                java.net.URL imageUrl = getClass().getResource(imagePath);
                if (imageUrl != null) {
                    ImageView avatarView = new ImageView(new Image(imageUrl.toExternalForm()));
                    avatarView.setFitWidth(80);
                    avatarView.setFitHeight(80);
                    
                    // Create circular clip
                    Circle avatarClip = new Circle(40, 40, 40);
                    avatarView.setClip(avatarClip);
                    
                    Label nameLabel = new Label(avatarName);
                    nameLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #4a5568;");
                    
                    avatarContainer.getChildren().addAll(avatarView, nameLabel);
                    
                    // Highlight current avatar
                    if (avatar.equals(UserSessionManager.getProfilePic())) {
                        avatarContainer.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(102, 126, 234, 0.5), 10, 0, 0, 3); -fx-border-color: #667eea; -fx-border-width: 2; -fx-border-radius: 8; -fx-cursor: hand;");
                    }
                    
                    avatarContainer.setOnMouseClicked(e -> {
                        UserSessionManager.setProfilePic(avatar);
                        updateProfilePicture();
                        dialog.close();
                        showAlert("Avatar", "Profile picture updated successfully!");
                    });
                    
                    avatarGrid.add(avatarContainer, col, row);
                    col++;
                    if (col > 3) {
                        col = 0;
                        row++;
                    }
                }
            } catch (Exception e) {
                System.out.println("⚠ Could not load avatar: " + avatar);
            }
        }
        
        content.getChildren().addAll(title, avatarGrid);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    
}