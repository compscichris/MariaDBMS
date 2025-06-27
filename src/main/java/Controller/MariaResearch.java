/**
 * Program: MariaResearchLogin
 * Author: compscichris
 */


package Controller;

//import View.MariaResearchLogin;

import View.MariaDBApp;
import View.MariaDBConsole;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * MariaResearch is the main class that initiates the running of the
 * application, and has the feature of being able to run different variants
 * of the application for different use cases
 */
public class MariaResearch
{
    public static void main (String [] args)
    {
        int mode = -1;
        char password[];
        //NO ARG
        if(args.length == 0) {
            mode = 0;
        }
        //SINGLE ARG
        else if(args.length == 1)
        {
            mode = Integer.parseInt(args[0]);
            MariaResearch mariaResearch = new MariaResearch(mode);
        }
        else {
            System.out.println("\nIncorrect Format Detected\n Expected Format: java MariaResearch <mode>");
            System.exit(-1);
        }
    }

    /**
     * Object of MariaResearch is used to streamline and allow modularity
     * of application for testing
     * @param mode: int value to select operation mode of MariaResearch.
     */
    public MariaResearch(int mode)
    {
        if(mode == 0) {
            //Not implemented feature
        }
        //console or application gui mode
        else if(mode == 1)
        {
            MariaDBConsole console = new MariaDBConsole();
        }
        else if(mode == 2)
        {
            Application.launch(MariaDBApp.class);
        }
        else {
            System.out.println("\nIncorrect Format Detected\n Expected Format: java MariaResearch <mode>");
            System.exit(-1);
        }
    }
}