package Controller;
import java.sql.*;
public class QueryResults {
    ResultSet results;
    Statement stmt;
    public QueryResults(Statement stmt, ResultSet results) {
        this.results = results;
        this.stmt = stmt;
    }
    public ResultSet getResults() {
        return results;
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
