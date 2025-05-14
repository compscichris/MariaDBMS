/**
/**TODO: VERIFY COMPLETION OF FILE, TEST FOR ERRORS, FINISH DOCUMENTATION
 * Filename: Prog4.java
 * Authors: Chris Chen
 */


package Controller;
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
        if(args.length == 0) {
        	mariaURL ="jdbc:mariadb://localhost:3306/MariaResearch";
        }
        else if(args.length == 1)
        {
        	mariaURL = args[0];
        }
        else if(args.length == 2)
        {
        	mariaURL = args[0];
        	mode = Integer.parseInt(args[1]);
        }
        else {
            System.out.println("\nIncorrect Format Detected\n Expected Format:  java MariaLogin <username> <password> ");
            System.exit(-1);
        }
        MariaResearchLogin session = new MariaResearchLogin(mariaURL, mode);
    }
}