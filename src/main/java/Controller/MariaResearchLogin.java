/**
 * Author: compscichris
 * Date 5/7/2025
 */
package Controller;
import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;
/**
 * MariaResearchLogin is a class that creates a connection to the MariaDB url provided
 */
public class MariaResearchLogin
{
	private Connection conDB = null;
	private String user;
	private String mariaURL;
	//CONSTRUCTOR
	MariaResearchLogin()
	{

	}
	MariaResearchLogin(String url, int mode, char[] pass)
	{
		this.mariaURL = url;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			Console console = System.console();
			//console mode
			if(mode ==0)
			{
				this.user = console.readLine("Enter username: ");
				this.conDB = DriverManager.getConnection(mariaURL,user,new String(console.readPassword("Enter password: ")));
			}
			else if(mode == 1){
				this.conDB = DriverManager.getConnection(mariaURL,user,new String(pass));
				Arrays.fill(pass, ' ');
			}
		}
		catch (ClassNotFoundException e){
			System.out.println(e.getMessage());
			System.exit(-1);
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}
	/*
	 * selectOption is a method that handles the user interface from the console side
	 */
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
				try {
					System.out.println("Executing statement...");
					Statement stmt = conDB.createStatement();
					boolean works = stmt.execute(aggregate);
					if(!works)
					{
						System.out.println("No syntax errors detected.");
					}
					stmt.close();
				}
				catch (SQLException e)
				{
					System.err.println("*** SQLException:  "
							+ "TABLE CREATION UNSUCCESSFUL.");
					System.err.println("\tMessage:   " + e.getMessage());
					System.err.println("\tSQLState:  " + e.getSQLState());
					System.err.println("\tErrorCode: " + e.getErrorCode());
					System.exit(-1);
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
			}
			else
			{
				System.out.println("ENTER A VALID OPTION.");
			}
		}
		try{
			this.conDB.close();
		}
		catch (SQLException e){
			System.err.println(e.getMessage());
			System.exit(-1);
		}
	}

}
