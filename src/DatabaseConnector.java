import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseConnector {
	
	Connection con;
    Statement stmt;
	
	public DatabaseConnector()
	{
		//System.out.println(connectToDatabase());
		
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
	      
	      try
	      {
	         con = DriverManager.getConnection(URL, USER, PASS);
	         stmt = con.createStatement();
	      } 
	      catch (SQLException e) {
	         e.printStackTrace();
	      }
	      catch (Exception e) {
	          e.printStackTrace();
	      }
	      finally
	      {  
	         System.out.println("database connected");
	      }
	}
	
	protected void finalize(){
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
	

	//returns a String as errorCode that lets the calling method know the results of the attempt
	//based on a convention that is yet to be determined
	public String loginAttempt(String username, String password){
	//check if username exists
	//    if not, return errorCode for invalid login attempt, suggest to 
	//        create an account or try to login again with correct name
	//    check if there is more than one account with same username
	//        if so, alert admins
	//check if account locked or banned
	//     if so return errorCode for locked/banned account
	//if username is good and account is not locked, check if password correct
	//if so, return errorCode for successful login
	//     if not, increment numFailedAttempts
	//          if numFailedAttempts >= 3 { lock AccountAndSendEmail }
	
	String errorCode = "";
	String result = "";
	
	
	//we need to add code to prevent SQL injection attacks
	String sql = "SELECT * from AccountsTable where Username = \'" + username + "\';";
	ResultSet rs;
	try {
		rs = stmt.executeQuery(sql);
	
	          
	int numResults = 0;
	while( rs.next() )
	{
		numResults++;
	}
	rs.beforeFirst();
	rs.next();
	System.out.println("numResults = " + numResults);
    
	
	//If there is no results for the entered username
	if ( numResults == 0 ) {
	      System.out.println("No account found with username: " + username);
	         rs.close();
	      return "Nope!";
	      //Call a method that suggests the player to make a new account.
	 }
	
	//check for multiple entries for same username
	if ( numResults > 1 ) {
		//notify admin
		// include username and explain the possible situation of data concurrency issues....
	}
	
	if ( numResults == 1 ) {
		System.out.println("rs.getInt(rs.findColumn(numFailedAttempts) )  = " + rs.getInt(rs.findColumn("numFailedAttempts") ));
		if( rs.getInt(rs.findColumn("numFailedAttempts") ) >= 3  ){
			rs.updateInt("locked", 1 );
			
		}
			
			
		if( rs.getInt(rs.findColumn("locked") ) ==1  ){
			//set errorCode to value for locked;
			errorCode ="account is locked";
	         rs.close();
			return errorCode;
			//handler should prompt to reset password
		}
		if( rs.getInt(rs.findColumn("banned") ) ==1  ){
			//set errorCode to value for locked;
			errorCode ="account is banned";
	         rs.close();
			return errorCode;
			//handler should prompt to reset password
		}
		
		int passwordColumnIndex = rs.findColumn("Password");;
		if ( !password.equals( rs.getString(passwordColumnIndex)) ){
			errorCode = "Bad Password, berate user";
			
			rs.updateInt("numFailedAttempts", (rs.findColumn("numFailedAttempts") + 1) );
			if( rs.findColumn("numFailedAttempts") >= 3){
			 //lock account and send email
				rs.updateInt("locked", 1 );
				errorCode = "too many failed attempts, account is now locked... shame the user"; 
				         rs.close();
						return errorCode;
			}
	         rs.close();
			return errorCode;
		}
		else{
			errorCode = "loginSuccess";
			System.out.println(errorCode);
	         rs.close();
			return errorCode;
		}
		
	}
	 } 
    catch (SQLException e) {
       e.printStackTrace();
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    finally
    {  
	
       System.out.println("login attempt completed");
    }
	return errorCode;
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
