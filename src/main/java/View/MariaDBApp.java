package View;
import java.awt.*;
import java.io.File;

import javafx.application.Application;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Screen;

import javafx.scene.control.Button;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.geometry.Rectangle2D;

//For file explorer desktop
import javafx.stage.FileChooser;
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
        Label label = new Label("ENTER DATABASE URL:");
        label.setFont(new Font("Courier New", 20));
        label.setBackground(Background.fill(Color.RED));
        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        Screen targetScreen = Screen.getScreens().stream()
                .filter(screen -> screen.getBounds().contains(mousePoint.x, mousePoint.y))
                .findFirst()
                .orElse(Screen.getPrimary());
        // Get bounds of that screen
        Rectangle2D bounds = targetScreen.getVisualBounds();
        //MAIN LAYOUT OF WINDOW
        Pane root = new Pane();
        root.setBackground(Background.fill(Color.BLUE));

        //CREATE A LOGIN MECHANISM
        root.getChildren().add(label);
        label.layoutXProperty().bind(root.widthProperty().multiply(.5).subtract(label.widthProperty().divide(2)));
        label.setLayoutY(0.0);
        //
        //Text text = new Text("Enter the URL");
        //text.layoutXProperty().bind(root.widthProperty().multiply(.5).subtract(label.widthProperty().divide(2)));
        //text.layoutYProperty().bind(root.heightProperty().multiply(.1));
        //root.getChildren().add(text);
        //
        TextArea textArea = new TextArea();
        textArea.layoutXProperty().bind(root.widthProperty().multiply(.5).subtract(textArea.widthProperty().divide(2)));
        textArea.layoutYProperty().bind(root.heightProperty().multiply(.5));
        root.getChildren().add(textArea);
        Button fileExplorer = new Button("Open File");
        fileExplorer.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a File");

            // Optional: Set initial directory or filters
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            // fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            // Show the file chooser and wait for user to select a file
            File selectedFile = fileChooser.showOpenDialog(stage);

            // Handle the selected file
            if (selectedFile != null) {
                System.out.println("User selected file: " + selectedFile.getAbsolutePath());
                // You can now read/process the file
            }
            else {
                System.out.println("File selection cancelled.");
            }
        });
        root.getChildren().add(fileExplorer);
        Scene scene = new Scene(root, bounds.getWidth()/2, bounds.getHeight()/2);
        /*
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        // Create a scene and set it on the stage

        //Scene scene = new Scene(label, width, height);  // 400x200 is the window size*/
        //Scene scene = new Scene(label, width, height);
        stage.setScene(scene);

        // Set the window title
        stage.setTitle("JavaFX Hello World");
        stage.setTitle("JavaFX Application");
        // Show the stage (window)
        stage.show();
    }
}
