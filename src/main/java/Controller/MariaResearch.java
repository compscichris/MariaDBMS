/**
 /**TODO: VERIFY COMPLETION OF FILE, TEST FOR ERRORS, FINISH DOCUMENTATION
 * Filename: Prog4.java
 * Authors: Chris Chen
 */


package Controller;

//import View.MariaResearchLogin;

import View.MariaDBApp;
import View.MariaDBConsole;
import javafx.application.Application;
import javafx.stage.Stage;

public class MariaResearch
{
    public static final String tName[] = {"PCparts", "CPUspec", "MOBOspec", "GPUspec", "flight_staff"};
    /**
     * This main method handles all the code that is needed to create the connection to the oracle dbms.
     * It creates the tables for us by reading the csv files table data.
     *
     */
    public static void main (String [] args)
    {
        String mariaURL="";
        int mode = 0;
        char password[];
        //default run configuration
        if(args.length == 0) {
            mariaURL ="jdbc:mariadb://localhost:3306/MariaResearch";
        }
        //single arg run configuration
        else if(args.length == 1)
        {
            mariaURL = args[0];
        }
        //dual arg run configuration
        else if(args.length == 2)
        {
            mariaURL = args[0];
            mode = Integer.parseInt(args[1]);
        }
        else {
            System.out.println("\nIncorrect Format Detected\n Expected Format:  java MariaLogin <MariaDB URL> <mode (optional)> ");
            System.exit(-1);
        }
        //console or application gui mode
        if(mode == 0)
        {
            MariaDBConsole console = new MariaDBConsole();
        }
        else if(mode == 1)
        {
            Application.launch(MariaDBApp.class);
        }
    }
    public MariaResearch(String mariaURL, int mode)
    {
        //Application;
        //MariaResearchLogin session = new MariaResearchLogin(mariaURL, mode);
    }
}