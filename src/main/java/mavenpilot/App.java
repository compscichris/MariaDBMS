package mavenpilot;
import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;

/**
 * Hello world!
 */
public class App extends Application {
    public static void main(String[] args) {
        launch(args);
        System.out.println("Hello World!");
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Create a label and set text
        Label label = new Label("Hello, JavaFX!");

        // Create a scene and set it on the stage
        Scene scene = new Scene(label, 400, 200);  // 400x200 is the window size
        stage.setScene(scene);

        // Set the window title
        stage.setTitle("JavaFX Hello World");

        // Show the stage (window)
        stage.show();
    }
}
