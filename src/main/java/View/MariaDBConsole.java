package View;

import Controller.MariaResearchLogin;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MariaDBConsole {
    /*
     * selectOption is a method that handles the user interface from the console side
     */
    MariaResearchLogin session;
    public MariaDBConsole(String mariaURL) {
        this.session = new MariaResearchLogin(mariaURL, 0, new char[1]);
    }
    public void selectOption()
    {
        Scanner input = new Scanner(System.in);
        String option = "";
        //ACCESS MENU
        while(option != "quit")
        {
            System.out.println("OPTIONS (Enter option '*' ");
            System.out.println("Option A: INPUT SQL QUERY");//WILL BE THE MOST COMPLEX
            System.out.println("Option B: INJECT SQL FILE");
            System.out.println("Option C: RETRIEVE SQL TABLES");
            System.out.println("EXIT: Enter 'quit'");
            option = input.nextLine();
            //OPTION A
            if(option.equalsIgnoreCase("a"))
            {
                System.out.println("Enter SQL statement: ");
                String user_in = input.nextLine();
                String aggregate = "";
                while(user_in.contains(";"))
                {
                    aggregate += user_in + "\n";
                    user_in = input.nextLine();
                }
                session.injectSQL(aggregate);
            }
            //OPTION B
            else if(option.equalsIgnoreCase("b"))
            {
                System.out.println("Enter SQL file: ");
                String user_in = input.nextLine();
            }
            //OPTION C
            else if(option.equalsIgnoreCase("c"))
            {

            }
            //QUIT
            else if(option.equalsIgnoreCase("quit"))
            {
                System.out.println("Logging out...");
                break;
            }
            else
            {
                System.out.println("ENTER A VALID OPTION.");
            }
        }
    }
}
