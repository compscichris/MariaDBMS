/**
 * Author: compscichris
 * Date 5/7/2025
 */
package Controller;
import com.sun.jna.WString;

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
	private String userName;
	private String mariaURL;
	//CONSTRUCTOR
	public MariaResearchLogin(String url, int mode, String user, char[] pass)
	{
		this.mariaURL = url;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			//console mode
			if(mode == 0)
			{
				Console console = System.console();
				this.userName = console.readLine("Enter username: ");
				this.conDB = DriverManager.getConnection(mariaURL,userName,new String(console.readPassword("Enter password: ")));
			}
			else if(mode == 1){
				this.userName = user;
				this.conDB = DriverManager.getConnection(mariaURL,userName,new String(pass));
				Arrays.fill(pass, ' ');
			}
		}
		catch (ClassNotFoundException e){
			System.out.print("ClassNotFoundException: ");
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
	public int injectSQL(String aggregate)
	{
		try {
			System.out.println("Executing statement...");
			Statement stmt = conDB.createStatement();
			boolean works = stmt.execute(aggregate);
			if(!works)
			{
				System.out.println("No syntax errors detected.");
			}
			stmt.close();
			return 1;
		}
		catch (SQLException e)
		{
			System.err.println("*** SQLException:  "
					+ "TABLE CREATION UNSUCCESSFUL.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			return 0;
		}
	}
	public String sqlParser(File file){
		String res = "";
		try{
			Scanner scanner = new Scanner(file);
			while(scanner.hasNext())
			{
				res += scanner.nextLine();
			}
			System.out.println(res);
		}
		catch (FileNotFoundException e){
			System.out.println("File not found.");
		}
		return res;
	}
	public void close(){
		try{
			this.conDB.close();
		}
		catch (SQLException e){
			System.err.println(e.getMessage());
			System.exit(-1);
		}
	}
}
