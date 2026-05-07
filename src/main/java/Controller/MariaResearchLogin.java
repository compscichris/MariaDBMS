/**
 * Author: compscichris
 * Date 5/7/2025
 */
package Controller;
import com.sun.jna.WString;
import com.sun.source.tree.BinaryTree;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * MariaResearchLogin is a class that encapsulates the connection to the MariaDB url provided, with
 * different modes of connection allowed to accommodate the different forms of application.
 */
public class MariaResearchLogin
{
	private Connection conDB = null;
	private String userName;
	private String mariaURL;

	//MariaDBConsole CONSTRUCTOR
	public MariaResearchLogin(String url) throws MariaLoginException
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
			throw new MariaLoginException(MariaLoginException.Error.JDBC_DRIVER_NOT_FOUND);
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}
	//MariaDBApplication CONSTRUCTOR
	public MariaResearchLogin(String url, String user, char[] pass) throws MariaLoginException
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
			throw new MariaLoginException(MariaLoginException.Error.JDBC_DRIVER_NOT_FOUND);
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}
	/* CLIENT SIDE PROCESSING */


	/**
	 * processFile(File file) is a method that retrieves an SQL file, and converts the query into
	 * string. As of current, no additional function except to read in the file query into a string,
	 * and has no verification of the query syntax.
	 * @param file
	 * @return string representation of the query in the SQL file.
	 */
	//THROW
	public String processFile(File file){
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
			//System.out.println(res);
		}
		catch (FileNotFoundException e){
			System.out.println("File not found.");
		}
		return res;
	}

	/**
	 *
	 * parseSQL() handles the processing of raw SQL sent by the user
	 * @param rawSQL
	 * @return
	 * @throws MariaLoginException
	 */
	public ArrayList<QueryResults> parseSQL(String rawSQL) throws MariaLoginException {
		//SANITIZE SQL for atomic execution
		String [] inputStmt = rawSQL.split(";");
		Pattern allowedSqlPattern = Pattern.compile("(?i)^(SELECT|INSERT|UPDATE)\\s.*$");
		ArrayList<QueryResults> queryResults = new ArrayList<>();
        for (String s : inputStmt) {
            //CASES
            //1. Statement is not blank
            //2. Statement is blank
            if (!s.trim().isEmpty()) {
                //check if the regex matches (ALLOW ONLY SELECT, INSERT, UPDATE)
                if (allowedSqlPattern.matcher(s).matches()) {
                    try {
                        System.out.println("Executing statement...");
                        Statement stmt = conDB.createStatement();
                        String[] splitSQL = s.split("\\s+");
                        ResultSet result = null;
                        QueryResults queryResult = null;
                        if (splitSQL[0].equalsIgnoreCase("select")) {
                            result = stmt.executeQuery(s);
                            queryResult = new QueryResults(stmt, result, "");
                        } else if (splitSQL[0].equalsIgnoreCase("insert")) {
                            int rows = stmt.executeUpdate(s);
                            String message = "INSERTED " + rows + " rows.";
                            queryResult = new QueryResults(stmt, null, message);
                        } else if (splitSQL[0].equalsIgnoreCase("update")) {
                            int rows = stmt.executeUpdate(s);
                            String message = "DELETED " + rows + " rows.";
                            queryResult = new QueryResults(stmt, null, message);
                        }
                        queryResults.add(queryResult);
                    } catch (SQLException e) {
                        System.err.println("\tMessage:   " + e.getMessage());
                        System.err.println("\tSQLState:  " + e.getSQLState());
                        System.err.println("\tErrorCode: " + e.getErrorCode());
                        throw new MariaLoginException(MariaLoginException.Error.SQL_SYNTAX_ERROR);
                    }
                }
            }
        }
		return queryResults;
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
	/* SERVER RESPONSE */



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
