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
	         stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
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
	//String result = "";
	
	
	//we need to add code to prevent SQL injection attacks
	String sql = "SELECT * from AccountTable where Username = \'" + username + "\';";
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
	      return "usernameNotFound";
	      //Call a method that suggests the player to make a new account.
	 }
	
	//check for multiple entries for same username
	if ( numResults > 1 ) {
		//notify admin
		// include username and explain the possible situation of data concurrency issues....
		System.out.println("More than one record exist with that username.");
		return "moreThanOneAccountFound";
	}
	
	if ( numResults == 1 ) {
		System.out.println("rs.getInt(rs.findColumn(numFailedAttempts) )  = " + rs.getInt(rs.findColumn("numFailedAttempts") ));
		if( rs.getInt(rs.findColumn("numFailedAttempts") ) >= 5  ){
			rs.updateInt("locked", 1 );
			
		}
			
			
		if( rs.getInt(rs.findColumn("locked") ) ==1  ){
			//set errorCode to value for locked;
			errorCode ="accountLocked";
	         rs.close();
			return errorCode;
			//handler should prompt to reset password
		}
		if( rs.getInt(rs.findColumn("banned") ) ==1  ){
			//set errorCode to value for locked;
			errorCode ="accountBanned";
	         rs.close();
			return errorCode;
			//handler should prompt to reset password
		}
		
		int passwordColumnIndex = rs.findColumn("Password");;
		if ( !password.equals( rs.getString(passwordColumnIndex)) ){
			errorCode = "incorrectPassword";
			System.out.println(errorCode);
			
			int updatedNumFailedAttempts = (rs.getInt(rs.findColumn("numFailedAttempts")) + 1);
			rs.updateInt("numFailedAttempts",  updatedNumFailedAttempts);
			rs.updateRow();
			System.out.println("number of failed attempts: " + rs.getInt(rs.findColumn("numFailedAttempts") ) );
			if( rs.getInt(rs.findColumn("numFailedAttempts")) >= 5){
			 //lock account and send email
				rs.updateInt("locked", 1 );
				errorCode = "accountLocked"; 
				System.out.println(errorCode);
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
	
	public int getAccountID(String username)
	{
		int accountID = -1; //This will be returned.
		
		ResultSet rs;
		String sql = "SELECT * from AccountTable where Username = \'" + username + "\';";
		try{
			rs = stmt.executeQuery(sql);
			rs.next();
			accountID = rs.getInt("accountID");
			
		} 
	    catch (SQLException e) {
	       e.printStackTrace();
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	    finally
	    {  
		
	       System.out.println("getAccountID completed and it is :" + accountID);
	    }
		
		if( accountID == -1)
		{
			System.out.println("Unable to retrieve accountID...");
		}
		return accountID;
	}
	
	//This is the method that will be called to send info to the character select screen.
	//This method gets basic char info such as name, class, and level for each char for the account and 
	//builds a string seperating each value for a char with a space.  Each char is seperated by a colon (ie ":").
	public String getBasicCharsInfo(int accountID)
	{
		int numChars = 0;
		
		//start of pasted code...
		String sql;
		ResultSet rs;
		String ofAllStrings = "";
		
		try 
		{	
			//Check to make sure that the account name does not already exist.
			sql = "SELECT * from CharacterInfoTable where accountID = \'" + accountID + "\';";
			rs = stmt.executeQuery(sql);
	        while( rs.next() )
			{
				if(numChars>0)
				{
					ofAllStrings += ":";
				}
				ofAllStrings += (rs.getString("characterName") + " " + rs.getString("class") + " " + rs.getString("level"));
				numChars++;
			}
			System.out.println(ofAllStrings);
			rs.close();
			return ofAllStrings;
			
		} 
	    catch (SQLException e) {
	       e.printStackTrace();
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	    finally
	    {  
	       System.out.println("getNumChars completed");
	    }
			
//			rs.beforeFirst();
//			rs.next();
//			System.out.println("Before insert statement, numResults = " + numChars);
//			if( numChars >= 5 ){
//				errorCode="Maximum number of characters already reached";
//				System.out.println(errorCode);
//				rs.close();
//				return errorCode;
//			}
		
		//end of pasted code...
		return ofAllStrings;
	}
	
	public String getCharInfo(String accountID)
	{
		String charInfo = "defaultValue";  //This will be returned;
		String charName;
		String charLevel;
		String charClass;
		int numChars;
		
		String sql;
		ResultSet rs;
		
		try 
		{	
			//Check to make sure that the account name does not already exist.
			sql = "SELECT * from CharacterInfoTable where accountID = \'" + accountID + "\';";
			rs = stmt.executeQuery(sql);
	
			
			
			
		} 
	    catch (SQLException e) {
	       e.printStackTrace();
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	    finally
	    {  
	       System.out.println("create character attempt completed");
	    }
		
		
		
		return charInfo;
	}
	
	public String createChar(String charName, String accountID , String charClass, String gender, String str, String dex, String con, String charStatInt, String wil, String luck)
	{
		String errorCode = "dunnoYet";
		int mana;
		if( charClass.equals("Mage"))
		{
			mana = 20;
		}
		else mana = 0;
		String sql = "INSERT INTO CharacterInfoTable (characterName, accountID, class, level, gender, health, mana, experience, xCoord, yCoord, gold, equippedItems, strength, dexterity, constitution,  intelligence, willpower, luck) "
				+ "VALUES ( \'" + charName + "\', \'" + accountID + "\', \'" + charClass + "\', '1', \'" + gender + "\', \'" + con + "\', \'" + mana + "\', '0', '0', '0', '0', '0:0:0:0:0:0', \'" +str + "\', \'" + dex + "\', \'" + con + "\', \'" + charStatInt + "\', \'" + wil+ "\', \'" + luck + "\') ;";
		System.out.println(sql);
		try
		{
			int insertResult = stmt.executeUpdate(sql);
			if( insertResult == 1)
			{
				errorCode = "Unique Char created.";
				System.out.println(errorCode);
				//rs.close();
				return errorCode;
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
	       System.out.println("create character attempt completed");
	    }
		
		return errorCode;
	}
	
	public String createAccount(String username, String password, String email, String securityQuestion1, String securityAnswer1, String securityQuestion2, String securityAnswer2)
	{
		String errorCode = "";

		//we need to add code to prevent SQL injection attacks
		String sql;
		ResultSet rs;
		
		try {
			
			//Check to make sure that the account name does not already exist.
			sql = "SELECT * from AccountTable where Username = \'" + username + "\';";
			rs = stmt.executeQuery(sql);

			int numResults = 0;
			while( rs.next() )
			{
				numResults++;
			}
			rs.beforeFirst();
			rs.next();
			System.out.println("Before insert statement, numResults = " + numResults);
			if( numResults != 0){
				errorCode="Account already exists with Username :" + username;
				System.out.println(errorCode);
				rs.close();
				return errorCode;
			}
			
			sql = "INSERT INTO AccountTable (username, password, email, securityQuestion1, securityAnswer1, securityQuestion2, securityAnswer2 )  VALUES ( \'" + username + "\', \'" + password + "\', \'" + email + "\', \'" + securityQuestion1 + "\', \'" + securityAnswer1 + "\', \'" + securityQuestion2 + "\', \'" + securityAnswer2 + "\') ;";
			int insertResult = stmt.executeUpdate(sql);
			if( insertResult == 1)
			{
				errorCode = "accountCreated";
				System.out.println(errorCode);
				rs.close();
				return errorCode;
			}
			else if( insertResult > 1)
			{
				errorCode = "More than one account with username :" + username + " exists.  This is WRONG.";
				System.out.println(errorCode);
				rs.close();
				return errorCode;
			}
			else if( insertResult == 0)
			{
				errorCode = "Failed to create account.";
				System.out.println(errorCode);
				rs.close();
				return errorCode;
			}
			
			
			//The following code checks if a username exists.
			//
			
//			System.out.println("insert statement result should equal 1 but is really = " + insertResult);
//			sql = "SELECT * from AccountTable where Username = \'" + username + "\';";
//			rs = stmt.executeQuery(sql);
//		     
//			numResults = 0;
//			while( rs.next() )
//			{
//				numResults++;
//			}
//			rs.beforeFirst();
//			rs.next();
//			System.out.println("numResults = " + numResults);

		} 
	    catch (SQLException e) {
	       e.printStackTrace();
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	    finally
	    {  
	       System.out.println("create account attempt completed");
	    }
		System.out.println(errorCode + "and reached final return statement of the account creation method...  weird...");
		return errorCode;
	}
}


