package View;

import Controller.MariaLoginException;
import Controller.MariaResearchLogin;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * MariaDBConsole is the console variant of the application that can run on machines without GUI, and
 * can be used for testing and automation purposes.
 *
 */
public class MariaDBConsole {
    private MariaResearchLogin session;
    public MariaDBConsole() {
        System.out.println("\nMariaDB Console\nEnter Server IP Address: ");
        Scanner getURL = new Scanner(System.in);
        String mariaURL = getURL.next();
        try{
            this.session = new MariaResearchLogin(mariaURL);
        }
        catch(MariaLoginException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getCause());
        }
        this.selectOption();
    }

    /**
     * selectOption() is
     */
    public void selectOption()
    {
        Scanner input = new Scanner(System.in);
        String option = "";
        //ACCESS MENU
        while(option != "quit")
        {
            System.out.println("OPTIONS (Enter option '*' ");
            System.out.println("Option A: INJECT SQL QUERY");//WILL BE THE MOST COMPLEX
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
                try{
                    session.parseSQL(aggregate);
                } catch (MariaLoginException e) {
                    System.err.println(e.getMessage());
                }
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
                session.close();
                break;
            }
            else
            {
                System.out.println("ENTER A VALID OPTION.");
            }
        }
    }
}
