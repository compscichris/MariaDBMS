package Controller;
import java.sql.*;
public class QueryResults {
    private ResultSet results;
    private Statement stmt;
    private String message;
    public QueryResults(Statement stmt, ResultSet results, String message) {
        this.results = results;
        this.stmt = stmt;
        this.message = message;
    }
    public ResultSet getResults() {
        return results;
    }
    public String getMessage() {
        return message;
    }
    public void close() {
        try{
            this.stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error closing statement");
        }
    }
}
