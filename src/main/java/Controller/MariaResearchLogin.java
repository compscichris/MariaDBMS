/**
 * Author: compscichris
 * Date 5/7/2025
 */
package Controller;
import com.sun.jna.WString;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Function;

/**
 * MariaResearchLogin is a class that creates a connection to the MariaDB url provided, with different
 * methods of connection
 */
public class MariaResearchLogin
{
	private Connection conDB = null;
	private String userName;
	private String mariaURL;

	//MariaDBConsole CONSTRUCTOR
	public MariaResearchLogin(String url)
	{
		this.mariaURL = url;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			//console mode
			Console console = System.console();
			this.userName = console.readLine("Enter username: ");
			this.conDB = DriverManager.getConnection(mariaURL,userName,new String(console.readPassword("Enter password: ")));
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
	//MariaDBApplication CONSTRUCTOR
	public MariaResearchLogin(String url, String user, char[] pass)
	{
		this.mariaURL = url;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			//console mode
			this.userName = user;
			this.conDB = DriverManager.getConnection(mariaURL,userName,new String(pass));
			Arrays.fill(pass, ' ');
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
	/**
	 * injectSQL() is a method that executes an SQL statement if possible, and returns the result of
	 * the SQL query.
	 * @return a ResultSet object that contains the response of a valid query.
	 */
	//THROW
	public ResultSet injectSQL(String aggregate)
	{
		try {
			System.out.println("Executing statement...");
			Statement stmt = conDB.createStatement();
			ResultSet result = stmt.executeQuery(aggregate);
			stmt.close();
			return result;
		}
		catch (SQLException e)
		{
			System.err.println("*** SQLException:  "
					+ "TABLE CREATION UNSUCCESSFUL.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			return null;
		}
	}
	/**
	 * parseResponse() is a method that parses the response from the server to a provided query.
	 * @return 2D ArrayList of String storing the results.
	 */
	//THROW
	public ArrayList<ArrayList<String>>parseResponse(ResultSet result){
		try{
			int columns = result.getMetaData().getColumnCount();
			ArrayList<ArrayList<String>> response = new ArrayList<ArrayList<String>>();
			while(result.next()){
				ArrayList<String> row = new ArrayList<String>(columns);
				for(int i = 0; i < columns; i++){
					row.add(result.getObject(i+1).toString() != null ?
							result.getObject(i+1).toString() : "null");
				}
				response.add(row);
			}
			return response;
		} catch (SQLException e) {
            throw new RuntimeException(e);
        }
	}

	/**
	 * processSQL(File file) is a method that retrieves an SQL file, and converts the query into
	 * string. As of current, no additional function except to read in the file query into a string,
	 * and has no verification of the query syntax.
	 * @param file
	 * @return string representation of the query in the SQL file.
	 */
	//THROW
	public String processSQL(File file){
		String res = "";
		if(!file.getName().endsWith(".sql")){
			return res;
		}
		try{
			Scanner scanner = new Scanner(file);
			while(scanner.hasNext())
			{
				res += scanner.nextLine() + "\n";
			}
			System.out.println(res);
		}
		catch (FileNotFoundException e){
			System.out.println("File not found.");
		}
		return res;
	}
	/**
	 * processCSV(File file) is a method that reads and parses CSV files, and then determines the best
	 * table creation to the user.
	 * @param file
	 * @return
	 */
	//THROWS
	public ArrayList<ArrayList<String>> processCSV(File file){
		if(!file.getName().endsWith(".csv"))
		{
			return null;
		}
		ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
		int tableSize = -1;

		try{
			Scanner scanner = new Scanner(file);
			String firstLine[] = scanner.nextLine().split(",");
			tableSize = firstLine.length;
			if(scanner.hasNext()){
				ArrayList<String> firstRow = new ArrayList<String>(tableSize);
				for(int i = 0; i < tableSize; i++){
					firstRow.set(i,firstLine[i]);
				}
				res.add(firstRow);
				while(scanner.hasNext())
				{
					String line[] = scanner.nextLine().split(",");
					ArrayList<String> curRow = new ArrayList<String>(tableSize);
					if(line.length != tableSize)
					{
						return null;
					}
					for(int j = 0; j < tableSize; j++){
						curRow.set(j,firstLine[j]);
					}
					res.add(curRow);
				}
			}
		}
		catch (FileNotFoundException e){
			System.out.println("File not found.");
		}
		return res;
	}
	/**
	 * close() is a method that terminates the connection.
	 */
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
