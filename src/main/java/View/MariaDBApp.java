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
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
//import java.awt.Font;
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
    private MariaResearchLogin login;
    @Override
    public void start(Stage stage) throws Exception {
        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        Screen targetScreen = Screen.getScreens().stream()
                .filter(screen  -> screen.getBounds().contains(mousePoint.x, mousePoint.y))
                .findFirst()
                .orElse(Screen.getPrimary());
        // Get bounds of that screen
        Rectangle2D bounds = targetScreen.getVisualBounds();
        //MAIN LAYOUT OF WINDOW
        Pane root = new Pane();
        root.setBackground(Background.fill(Color.BLUE));
        root.setPrefSize(bounds.getWidth()/2, bounds.getHeight()/2);

        //URL SECTION
        //Label
        AnchorPane section1 = new AnchorPane();
        section1.prefWidthProperty().bind(root.widthProperty());
        section1.prefHeightProperty().bind(root.heightProperty().multiply(.2));
        section1.setBackground(Background.fill(Color.GREEN));
        Label urlLabel = new Label("ENTER DATABASE URL:");
        urlLabel.setFont(new Font("Courier New", 12));
        urlLabel.setBackground(Background.fill(Color.RED));
        AnchorPane.setLeftAnchor(urlLabel,20.0);
        AnchorPane.setTopAnchor(urlLabel,20.0);
        section1.getChildren().add(urlLabel);

        //TextArea
        TextField urlField = new TextField();
        urlField.setFont(new Font("Courier New", 12));
        urlField.prefWidthProperty().bind(section1.widthProperty().multiply(0.5));  // 80% width
        AnchorPane.setRightAnchor(urlField,20.0);
        AnchorPane.setTopAnchor(urlField,20.0);
        section1.getChildren().add(urlField);
        //
        TextField userField = new TextField();
        userField.setFont(new Font("Courier New", 12));
        userField.prefWidthProperty().bind(section1.widthProperty().multiply(0.15));  // 80% width
        AnchorPane.setRightAnchor(userField,220.0);
        AnchorPane.setTopAnchor(userField,60.0);
        section1.getChildren().add(userField);
        //Server Access
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setFont(new Font("Courier New", 12));
        passwordField.prefWidthProperty().bind(section1.widthProperty().multiply(0.15));
        AnchorPane.setRightAnchor(passwordField, 20.0);
        AnchorPane.setTopAnchor(passwordField, 60.0);
        section1.getChildren().add(passwordField);
        Button submit = new Button("Select Server");
        AnchorPane.setLeftAnchor(submit, 20.0);
        AnchorPane.setTopAnchor(submit, 60.0);
        submit.setOnAction(event -> {
            System.out.println("📣 Submit button clicked");
            String dbUrl = urlField.getText();
            String user = userField.getText();
            new Thread(() -> {
                try {
                    MariaResearchLogin session = new MariaResearchLogin(dbUrl, 1, user, passwordField.getText().toCharArray());
                    login = session;
                    System.out.println("Login successful");
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }).start();
        });
        section1.getChildren().add(submit);
        root.getChildren().add(section1);
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
    //public static void createLoginDisplay
}
