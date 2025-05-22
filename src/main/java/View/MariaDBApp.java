/**
 * Author: compscichris
 * Date: 5/12/2025
 * JavaFX application enabling users to access sql Files and connecting to database.
 *
 * Planned features:
 * 1. Connection to MariaDB database on server
 * 2. Ability to read files from user, and to be able to read them into the application.
 * 3. Ability to Log out of database system, and log back in, ability to access options
 * from command line, implemented in UI
 * 4. Export responses into a txt file.
 */
package View;
import java.awt.*;
import java.io.File;


import Controller.MariaResearch;
import Controller.MariaResearchLogin;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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


import javafx.scene.control.PasswordField; //


import javafx.stage.FileChooser; //For file explorer desktop
/**
 * Hello world!
 */
public class MariaDBApp extends Application {
    /**
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        MariaResearchLogin login;// = new MariaResearch(dbUrl,1, passwordField.getText());
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

        //URL SECTION
        //Label
        Label urlLabel = new Label("ENTER DATABASE URL:");
        urlLabel.setFont(new Font("Courier New", 20));
        urlLabel.setBackground(Background.fill(Color.RED));
        urlLabel.layoutXProperty().bind(root.widthProperty().multiply(.5).subtract(urlLabel.widthProperty().divide(2)));
        urlLabel.setLayoutY(0.0);
        root.getChildren().add(urlLabel);
        //TextArea
        TextArea urlArea = new TextArea();
        urlArea.setFont(new Font("Courier New", 12));
        urlArea.resize(root.getWidth(), 1);
        urlArea.setWrapText(true);
        urlArea.setPrefRowCount(3);
        urlArea.prefWidthProperty().bind(root.widthProperty().multiply(0.8));  // 80% width
        urlArea.layoutXProperty().bind(root.widthProperty().multiply(.2).divide(2));
        urlArea.setLayoutY(30.0);
        root.getChildren().add(urlArea);
        //Server Access
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setFont(new Font("Courier New", 12));
        passwordField.setLayoutX(100);
        passwordField.setLayoutY(200);
        root.getChildren().add(passwordField);
        Button submit = new Button("Select Server");
        submit.setLayoutY(30);
        submit.setOnAction(event -> {
            String dbUrl = urlArea.getText();


        });
        root.getChildren().add(submit);

        //FILE EXPLORER SECTION
        //Set information
        TextArea fileArea = new TextArea();
        fileArea.setFont(new Font("Courier New", 12));
        fileArea.resize(root.getWidth(), 1);
        fileArea.setWrapText(true);
        fileArea.setPrefRowCount(3);
        fileArea.setLayoutY(300);
        fileArea.setLayoutX(100);
        fileArea.setEditable(false);
        fileArea.setText("No file selected.");
        root.getChildren().add(fileArea);
        Button fileExplorer = new Button("Open File");
        fileExplorer.setLayoutY(300);
        fileExplorer.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a File");
            // Optional: Set initial directory or filters
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQL Files", "*.sql"));
            // Show the file chooser and wait for user to select a file
            File selectedFile = fileChooser.showOpenDialog(stage);
            // Handle the selected file
            if (selectedFile != null) {
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                fileArea.setText(selectedFile.getAbsolutePath());
                // You can now read/process the file
            }
            else {
                System.out.println("File selection cancelled.");
                fileArea.setText("No file selected.");
            }
        });
        root.getChildren().add(fileExplorer);

        //INTERACTION AREA

        Scene scene = new Scene(root, bounds.getWidth()/2, bounds.getHeight()/2);
        /*
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        // Create a scene and set it on the stage

        //Scene scene = new Scene(urlLabel, width, height);  // 400x200 is the window size*/
        //Scene scene = new Scene(urlLabel, width, height);
        stage.setScene(scene);

        // Set the window title
        stage.setTitle("MariaDB management system");
        // Show the stage (window)
        stage.show();
    }
}
