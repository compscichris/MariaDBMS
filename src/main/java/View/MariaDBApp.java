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
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Scanner;


import Controller.MariaResearchLogin;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.control.TextArea;
//import java.awt.Font;
import javafx.stage.Stage;
import javafx.stage.Screen;

import javafx.scene.control.Button;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.geometry.Rectangle2D;


import javafx.scene.control.PasswordField; //
import javafx.geometry.Insets;

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
    private Scene loginScene;
    private Scene mainScene;
    private int sceneSwitch=0;
    @Override
    public void start(Stage stage) throws Exception {
        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        Screen targetScreen = Screen.getScreens().stream()
                .filter(screen  -> screen.getBounds().contains(mousePoint.x, mousePoint.y))
                .findFirst()
                .orElse(Screen.getPrimary());
        // Get bounds of that screen
        Rectangle2D bounds = targetScreen.getVisualBounds();

        //INTERACTION AREA
        loginScene = new Scene(createLoginDisplay(stage), 400, 200);
        mainScene = new Scene(createMainDisplay(stage,bounds.getWidth()/1.5, bounds.getHeight()/1.5), bounds.getWidth()/1.5, bounds.getHeight()/1.5);
        /*
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        // Create a scene and set it on the stage

        //Scene scene = new Scene(loginLabel, width, height);  // 400x200 is the window size*/
        //Scene scene = new Scene(loginLabel, width, height);
        stage.setScene(loginScene);

        // Set the window title
        stage.setTitle("MariaDB management system (Login)");
        // Show the stage (window)
        //stage.setResizable(false);
        stage.show();
    }
    public Pane createMainDisplay(Stage stage, double width, double height) {
        //MAIN APPLICATION
        AnchorPane mainDisplay = new AnchorPane();
        //
        GridPane toolbar = new GridPane();
        toolbar.setHgap(10);
        toolbar.setVgap(10);
        toolbar.setPadding(new Insets(5,5, 5,5));
        toolbar.setBackground(Background.fill(Color.LIGHTBLUE));;
        //toolbar.setPrefHeight(40);
        toolbar.setPrefWidth(width);
        //

        //SCREEN LAYOUT
        GridPane screenUI = new GridPane();
        //console
        TextArea console = new TextArea();
        console.setPrefWidth(width*0.5);
        console.setPrefHeight(height*0.8);
        console.setFont(new Font("Courier New", 12));
        console.setWrapText(true);
        GridPane.setConstraints(console, 0, 0);
        screenUI.getChildren().add(console);
        //output display
        TextArea output = new TextArea();
        output.setEditable(false);
        output.setPrefWidth(width*0.5);
        output.setPrefHeight(height*0.8);
        output.setWrapText(true);
        GridPane.setConstraints(output, 1, 0);
        screenUI.getChildren().add(output);
        //error debug
        TextArea log = new TextArea();
        log.setEditable(false);
        log.setPrefWidth(width);
        output.setPrefHeight(height*0.1);
        output.setWrapText(true);
        GridPane.setConstraints(log, 0, 2);
        //ADJUSTMENT
        AnchorPane.setTopAnchor(screenUI, 35.0);
        toolbar.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            double mainHeight = newVal.getHeight();
            double mainWidth = newVal.getWidth();
            System.out.println("toolbarHeight: " + mainHeight);
            System.out.println("toolbarWidth: " + mainWidth);
            //AnchorPane.setLeftAnchor(loginLabel, (paneWidth - labelWidth) / 2.0);
        });
        TextField fileField = new TextField();
        fileField.setFont(new Font("Courier New", 12));
        fileField.setEditable(false);
        fileField.setText("No file selected.");
        fileField.setPrefWidth(width*0.5-10.0);
        GridPane.setConstraints(fileField, 0, 0);
        toolbar.getChildren().add(fileField);
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
                fileField.setText(selectedFile.getAbsolutePath());
                // You can now read/process the file
                String res = login.sqlParser(selectedFile);
                console.setText(res);
            }
            else {
                System.out.println("File selection cancelled.");
                fileField.setText("No file selected.");
            }
        });
        GridPane.setConstraints(fileExplorer, 1, 0);
        toolbar.getChildren().add(fileExplorer);
        //INJECT Button
        Button injectButton = new Button("Inject SQL File");
        injectButton.setOnAction(event -> {
            if(console.getText().equals(""))
            {
                log.setText("No content in console.");
            }
            else{
                login.injectSQL(console.getText());
            }
        });
        GridPane.setConstraints(injectButton, 2, 0);
        toolbar.getChildren().add(injectButton);
        Button quitButton = new Button("Logout");
        quitButton.setOnAction(event -> {
            console.clear();
            fileField.setText("");
            output.clear();
            login.close();
            login = null;
            stage.setScene(loginScene);
        });
        GridPane.setConstraints(quitButton, 3, 0);
        toolbar.getChildren().add(quitButton);
        mainDisplay.getChildren().add(toolbar);
        mainDisplay.getChildren().add(screenUI);
        mainDisplay.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            double mainHeight = newVal.getHeight();
            double mainWidth = newVal.getWidth();
            System.out.println("mainHeight: " + mainHeight);
            System.out.println("mainWidth: " + mainWidth);
            //AnchorPane.setLeftAnchor(loginLabel, (paneWidth - labelWidth) / 2.0);
        });

        return mainDisplay;
    }
    public Pane createLoginDisplay(Stage stage)
    {
        AnchorPane section1 = new AnchorPane();
        //LABEL CREATION
        Label loginLabel = new Label("MariaDB login");
        loginLabel.setFont(new Font("Courier New", 16));
        loginLabel.setBackground(Background.fill(Color.RED));
        AnchorPane.setLeftAnchor(loginLabel,(400-125)/2.0);
        AnchorPane.setTopAnchor(loginLabel,5.0);
        section1.getChildren().add(loginLabel);
        //URL
        Label urlLabel = new Label("URL");
        urlLabel.setFont(new Font("Courier New", 16));
        AnchorPane.setLeftAnchor(urlLabel,20.0);
        AnchorPane.setTopAnchor(urlLabel,30.0);
        section1.getChildren().add(urlLabel);
        //USERNAME
        Label userLabel = new Label("Username");
        userLabel.setFont(new Font("Courier New", 14));
        AnchorPane.setLeftAnchor(userLabel,30.0);
        AnchorPane.setTopAnchor(userLabel,72.0);
        section1.getChildren().add(userLabel);
        //PASSWORD
        Label passLabel = new Label("Password");
        passLabel.setFont(new Font("Courier New", 14));
        AnchorPane.setLeftAnchor(passLabel,30.0);
        AnchorPane.setTopAnchor(passLabel,102.0);
        section1.getChildren().add(passLabel);
        //FIELD CREATION
        //URL
        TextField urlField = new TextField();
        urlField.setFont(new Font("Courier New", 12));
        urlField.prefWidthProperty().bind(section1.widthProperty().multiply(0.8));  // 80% width
        AnchorPane.setRightAnchor(urlField,20.0);
        AnchorPane.setTopAnchor(urlField,30.0);
        section1.getChildren().add(urlField);
        //USERNAME
        TextField userField = new TextField();
        userField.setFont(new Font("Courier New", 12));
        userField.prefWidthProperty().bind(section1.widthProperty().multiply(0.4));  // 80% width
        AnchorPane.setRightAnchor(userField,125.0);
        AnchorPane.setTopAnchor(userField,70.0);
        section1.getChildren().add(userField);
        //PASSWORD
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setFont(new Font("Courier New", 12));
        passwordField.prefWidthProperty().bind(section1.widthProperty().multiply(0.4));
        AnchorPane.setRightAnchor(passwordField, 125.0);
        AnchorPane.setTopAnchor(passwordField, 100.0);
        section1.getChildren().add(passwordField);

        Button submit = new Button("Login");
        AnchorPane.setRightAnchor(submit, 20.0);
        AnchorPane.setTopAnchor(submit, 70.0);
        submit.setFont(new Font("Courier New", 16));
        submit.setPrefHeight(55);
        submit.setPrefWidth(80);
        submit.setOnAction(event -> {
            System.out.println("📣 Submit button clicked");
            String dbUrl = urlField.getText();
            String user = userField.getText();
            Platform.runLater(() -> {
                try {
                    MariaResearchLogin session = new MariaResearchLogin(dbUrl, 1, user, passwordField.getText().toCharArray());
                    login = session;
                    System.out.println("Login successful");
                    userField.clear();
                    passwordField.clear();
                    stage.setScene(mainScene);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            });
        });
        section1.getChildren().add(submit);
        return section1;
    }
}
