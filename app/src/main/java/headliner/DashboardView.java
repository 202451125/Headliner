package headliner;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DashboardView {
    private final Stage stage;

    public DashboardView(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        Label lbl = new Label("🎉 Welcome to Dashboard!");
        StackPane root = new StackPane(lbl);
        root.setAlignment(Pos.CENTER);
        return new Scene(root, 800, 600);
    }
    
}