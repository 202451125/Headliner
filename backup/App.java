package headliner;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        try {
            // Start directly with HomeView
            HomeView home = new HomeView(stage);
            stage.setScene(home.createScene());
            stage.setTitle("Headliner - Your News App");
            stage.show();
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}