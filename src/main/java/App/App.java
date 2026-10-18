package App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {

    private static Stage palco;

    @Override
    public void start(Stage stage) throws IOException {
        palco = stage;
        trocarTela("TutorView.fxml", "Clínica Veterinária - Tutores");
        stage.show();
    }

    public static void trocarTela(String fxml, String titulo) throws IOException {
        Parent root = FXMLLoader.load(App.class.getResource("/view/" + fxml));
        palco.setScene(new Scene(root));
        palco.setTitle(titulo);
    }

    public static void main(String[] args) {
        launch(args);
    }
}