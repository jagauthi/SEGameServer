import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseConnector {
	
	public DatabaseConnector()
	{
		System.out.println(connectToDatabase());
	}
	
	public String connectToDatabase()
	{
		String result = "";
		try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
	      }
	      catch(ClassNotFoundException ex) {
	            System.out.println("Error: unable to load driver class!");
	            System.exit(1);
	      }
	      catch(IllegalAccessException ex) {
	            System.out.println("Error: access problem while loading!");
	            System.exit(2);
	      }
	      catch(InstantiationException ex) {
	            System.out.println("Error: unable to instantiate driver!");
	            System.exit(3);
	      }
	      
	      String URL = "jdbc:mysql://sql5.freesqldatabase.com:3306/sql5104581";
	      String USER = "sql5104581";
	      String PASS = "8CHGkEPagu";
	      Connection con = null;
	      Statement stmt = null;
	      try
	      {
	         con = DriverManager.getConnection(URL, USER, PASS);
	         stmt = con.createStatement();String sql;
	         sql = "SELECT * from AccountsTable where Username = \'" + "jagauthi" + "\';";
	         ResultSet rs = stmt.executeQuery(sql);
	          
	         //If there is no results for the entered username
	         if (!rs.next()) {
	           System.out.println("No account found with username: " + "jagauthi");
	           return "Nope!";
	           //Call a method that suggests the player to make a new account.
	         }
	         
	         //Set the ResultSet counter back to the beginning of the ResultSet
	         //This is necessary since the previous method moves the counter forward to
	         //check if the ResultSet is empty.
	         rs.beforeFirst();
	         rs.next();
	         result = rs.getString("Username");
	         // cleaning up
	         rs.close();
	         stmt.close();
	         con.close();
	      } 
	      catch (SQLException e) {
	         e.printStackTrace();
	      }
	      catch (Exception e) {
	          e.printStackTrace();
	      }
	      finally
	      {  
	          //closing the resources in case they weren't closed earlier
	          try {
	          if (stmt != null)
	                  stmt.close();
	          }
	          catch (SQLException e2) {
	             e2.printStackTrace();
	          }
	          try {
	              if (con != null) {
	                  con.close();
	               }
	          } 
	          catch (SQLException e2) {
	                 e2.printStackTrace();
	          }
	      }
	      return result;
      }

}
