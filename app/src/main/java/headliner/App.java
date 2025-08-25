package headliner;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        LoginView login = new LoginView(stage);
        stage.setScene(login.createScene());
        stage.setTitle("Login - Headliner");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}