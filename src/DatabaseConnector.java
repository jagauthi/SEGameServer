import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DatabaseConnector {
	
    Connection con;
    Statement stmt;
    
    HashMap<String, PlayerHolder> charsOnline = new HashMap<String, PlayerHolder>();
    long lastTime;
	
	public DatabaseConnector()
	{
		//System.out.println(connectToDatabase());
		lastTime = System.currentTimeMillis();
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
        if (!charsOnline.isEmpty()) {
        	charsOnline.clear();
        }
	}
	

	//Starting point for encryption of passwords : I am going to add a salt field to the userAccountTable
	//and use that salt value along with the entered password as the hash preimage for a hash function.
	//I will then store the resulting hash value in the password field.  This seems like a secure 
	//way of storing passwords that also mitigates dictionary attacks and eliminates the need to 
	//sanitize the password field.
	
//	import java.security.MessageDigest;
//
//	MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
//	messageDigest.update(stringToEncrypt.getBytes());
//	String encryptedString = new String(messageDigest.digest());
	
	
	
	
	
	//later admin tool
	
	
	//forgotPassword(String username )
	
	
	

	//This is currently not going to be implemented.  We are under the impression that we will
	//always save accounts in case a player wishes to come back to the game.  We could
	//set a field for disabling the account instead of destroying the data.
	//deleteAccount()
	
	//We currently have two methods : one for character info and another for character inventory.
	//The need for this method is not yet clear.
	//updateChar()
	
	//Maybe a method just for updating the position of the character would be handy...
	//updateCharPosition()
	
	
	//makeRandomChar()
	
	//createClan()
	
	//addFriendToClan()
	
	//deFriend()
	
	//leaveClan()
	
	//joinClan()
	
	//destroyClan()
	
	
	//For now, we are going to try to handle groups client side, and only relay
	//messages through the server to other clients without storing
	//any group data in the database.
//	//createGroup()
//	
//	//inviteToGroup()
//	
//	//leaveGroup()
//	
//	//joinGroup()
	
	//generateEncounter()
	
	//generateLoot()

	//this method will create random items and add them to the itemTableOfPowah
	//generateRandomItem()
	

	//returns a String as errorCode that lets the calling method know the results of the attempt
	//based on a convention that is yet to be determined
	
	public String logout(String charName){
		
		String errCode = "loggedOut";

		charsOnline.remove(charName);
		
		return errCode;
	}
	
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
    
	
	//If there is no result for the entered username...
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
			rs.updateRow();
			
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
				rs.updateRow();
				errorCode = "accountLocked"; 
				System.out.println(errorCode);
				rs.close();
				
				return errorCode;
			}
	        rs.close();
			return errorCode;
		}
		else{
			// FIX THIS
			String macAddress = "loginAttemptPlaceholder";
			
			Calendar cal = Calendar.getInstance();
	        SimpleDateFormat sdf = new SimpleDateFormat("dd.MMMMM.yyyy GGG hh:mm aaa z");
	        System.out.println( sdf.format(cal.getTime()) );
			String lastLogInTime = sdf.format(cal.getTime());
			//macAddress, lastLogInTime, loggedIn
			rs.updateString("lastLogInTime", lastLogInTime);
			rs.updateString("macAddress", macAddress);
			rs.updateInt("loggedIn",  1);
			rs.updateRow();
			
			
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
			if(rs.next())
				accountID = rs.getInt("accountID");
			else
				accountID = -1;
			
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
	
	//admin tool
	//This method expects all data to be validated and performs NO error checking.
	public void lockAccount(String accountID)
	{
		ResultSet rs;
		String sql = "SELECT * from AccountTable where accountID = \'" + accountID + "\';";
		try{
			rs = stmt.executeQuery(sql);
			rs.next();
			rs.updateInt( "locked", 1 );
			rs.updateRow();
		} 
	    catch (SQLException e) {
	       e.printStackTrace();
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	    finally
	    {  
	       System.out.println("lockAccount completed" );
	    }
	}
	
	
	
	public void testPreparedStatementAccount(String accountID)
	{
		ResultSet rs;
		String test = "";
		
		//String sql = "SELECT accountID, locked from AccountTable where accountID = ?";
		String sql = "SELECT * from AccountTable where accountID = ?";
		System.out.println(sql);
		try{
			
			PreparedStatement preparedStatement = con.prepareStatement(sql, ResultSet.CONCUR_UPDATABLE);
			preparedStatement.setString(1, accountID);
			rs = preparedStatement.executeQuery();
			
			
			//rs = stmt.executeQuery(sql);
			rs.next();
			test += rs.getString(rs.findColumn("email")) + "#";
			System.out.println(test);
			
			//rs.updateInt( "locked", 0 );
			//rs.updateRow();
		} 
	    catch (SQLException e) {
	       e.printStackTrace();
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	    finally
	    {  
	       System.out.println("unlockAccount completed" );
	    }
	}

	
	//admin tool
	//This method expects all data to be validated and performs NO error checking.
		public void unlockAccount(String accountID)
		{
			ResultSet rs;
			String sql = "SELECT * from AccountTable where accountID = \'" + accountID + "\';";
			try{
				rs = stmt.executeQuery(sql);
				rs.next();
				rs.updateInt( "locked", 0 );
				rs.updateInt("numFailedAttempts", 0 );
				rs.updateRow();
			} 
		    catch (SQLException e) {
		       e.printStackTrace();
		    }
		    catch (Exception e) {
		        e.printStackTrace();
		    }
		    finally
		    {  
		       System.out.println("unlockAccount completed" );
		    }
		}
	
		//admin tool
	//This method expects all data to be validated and performs NO error checking.
	public void banAccount(String accountID)
	{
		ResultSet rs;
		String sql = "SELECT * from AccountTable where accountID = \'" + accountID + "\';";
		try{
			rs = stmt.executeQuery(sql);
			rs.next();
			rs.updateInt( "banned", 1 );
			rs.updateRow();
		} 
	    catch (SQLException e) {
	       e.printStackTrace();
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	    finally
	    {  
	       System.out.println("banAccount completed");
	    }
	}
	
	//admin tool
	//This method expects all data to be validated and performs NO error checking.
		public void unbanAccount(String accountID)
		{
			ResultSet rs;
			String sql = "SELECT * from AccountTable where accountID = \'" + accountID + "\';";
			try{
				rs = stmt.executeQuery(sql);
				rs.next();
				rs.updateInt( "banned", 0 );
				rs.updateRow();
			} 
		    catch (SQLException e) {
		       e.printStackTrace();
		    }
		    catch (Exception e) {
		        e.printStackTrace();
		    }
		    finally
		    {  
		       System.out.println("unbanAccount completed");
		    }
		}

		
		public String getAccountStatus(String accountID)
		{
			 
			String accountStatus = "" ; //This will be returned.
			String locked;
			
			
			ResultSet rs;
			String sql = "SELECT * from AccountTable where accountID = \'" + accountID + "\';";
			try{
				rs = stmt.executeQuery(sql);
				rs.next();
				locked = rs.getString(rs.findColumn("locked"));
				//String username, String password, String email, String securityQuestion1, String securityAnswer1, String securityQuestion2, String securityAnswer2
				accountStatus += locked + "#";
				
				accountStatus += rs.getString(rs.findColumn("banned"));
				
			} 
		    catch (SQLException e) {
		       e.printStackTrace();
		    }
		    catch (Exception e) {
		        e.printStackTrace();
		    }
		    finally
		    {  
		       System.out.println("getAccountStatus completed and it is :" + accountStatus);
		    }
			
			if( accountID.equals("") )
			{
				System.out.println("Unable to retrieve accountStatus...");
			}
			return accountStatus;
		}
		

	public String getAccountInfo(String accountID)
	{
		String username = ""; 
		String accountInfo = "securityInfo#" ; //This will be returned.
		
		ResultSet rs;
		String sql = "SELECT * from AccountTable where accountID = \'" + accountID + "\';";
		try{
			rs = stmt.executeQuery(sql);
			if( rs.next()){
				username = rs.getString(rs.findColumn("username"));
				//String username, String password, String email, String securityQuestion1, String securityAnswer1, String securityQuestion2, String securityAnswer2
				accountInfo += username + "#";
				
				accountInfo += rs.getString(rs.findColumn("password")) + "#";
				accountInfo += rs.getString(rs.findColumn("email")) + "#";
				accountInfo += rs.getString(rs.findColumn("securityQuestion1")) + "#";
				accountInfo += rs.getString(rs.findColumn("securityAnswer1")) + "#";
				accountInfo += rs.getString(rs.findColumn("securityQuestion2")) + "#";
				accountInfo += rs.getString(rs.findColumn("securityAnswer2"))+ "#";
				accountInfo += rs.getString(rs.findColumn("macAddress"))+ "#";
				accountInfo += rs.getString(rs.findColumn("lastLogInTime"));
			}
			else accountInfo += "Account Name Not Found";
		} 
	    catch (SQLException e) {
	       e.printStackTrace();
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	    finally
	    {  
		
	       System.out.println("getAccountInfo completed and it is :" + accountInfo);
	    }
		
		if( accountID.equals("") )
		{
			System.out.println("Unable to retrieve accountInfo...");
		}
		return accountInfo;
	}
	
	//This is the method that will be called to send info to the character select screen.
	//This method gets basic char info such as name, class, and level for each char for the account and 
	//builds a string separating each value for a char with a space.  Each char is separated by a colon (i.e. "#").
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
					ofAllStrings += "#";
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
	
	public String charSearch(String charName)
	{
		String charInfo = "characterInfo#" + charName;  //This will be returned;

		 String xCoord;
		 String yCoord;
		 int direction = 2;
		 String location;
		 String equippedItems;
		 String sex;
		 String level;
		 String status = "active";
		 String initiative;
		
		
		String charClass;
		String sql;
		ResultSet rs;
		
		try 
		{	
			
			sql = "SELECT * from CharacterInfoTable where characterName = \'" + charName + "\';";
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				
				// String charClass, String level, String gender, String str, String dex, String con, String charStatInt, String wil, String luck,  String experience, String xCoord, String yCoord, String gold, String abilities, String cooldown
				
				charClass = rs.getString(rs.findColumn("class"));
				
				charInfo += "#" + charClass + "#";
				//sql = "INSERT INTO CharacterInfoTable (characterName, accountID, class, level, gender, health, mana, experience, xCoord, yCoord, gold, equippedItems,"
				//			+ " strength, dexterity, constitution,  intelligence, willpower, luck, abilities, cooldown) "
				charInfo += rs.getString(rs.findColumn("loggedIn")) + "#";
				level = rs.getString(rs.findColumn("level"));
				charInfo += level + "#";
				sex = rs.getString(rs.findColumn("gender"));
				charInfo += sex + "#";
				charInfo += rs.getString(rs.findColumn("health")) + "#";
				charInfo += rs.getString(rs.findColumn("mana")) + "#";
				charInfo += rs.getString(rs.findColumn("experience")) + "#";
				charInfo += rs.getString(rs.findColumn("pointsToSpend")) + "#";
				
				xCoord = rs.getString(rs.findColumn("xCoord"));
				charInfo += xCoord + "#";
				
				yCoord = rs.getString(rs.findColumn("yCoord"));
				charInfo += yCoord + "#";
				
				location = rs.getString(rs.findColumn("location"));
				charInfo += location + "#";
				charInfo += rs.getString(rs.findColumn("clanName")) + "#";
				charInfo += rs.getString(rs.findColumn("strength")) + "#";
				initiative = rs.getString(rs.findColumn("dexterity"));
				charInfo += initiative + "#";
				charInfo += rs.getString(rs.findColumn("constitution")) + "#";
				charInfo += rs.getString(rs.findColumn("intelligence")) + "#";
				charInfo += rs.getString(rs.findColumn("willpower")) + "#";
				charInfo += rs.getString(rs.findColumn("luck")) + "#";
				charInfo += rs.getString(rs.findColumn("abilities")) + "#";
				charInfo += rs.getString(rs.findColumn("cooldown"));
			
				rs.close();
				
			}
			else
				charInfo = "charNotFound";
			
			
			
		} 
	    catch (SQLException e) {
	       e.printStackTrace();
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	    finally
	    {  
	      // System.out.println("get character info attempt completed");
	    }
		return charInfo;
	}
	
	
	//This needs to be changed to use the HashMap.
	public String getCharInfo(String charName)
	{
		String charInfo = "characterInfo#" + charName;  //This will be returned;

		 String xCoord;
		 String yCoord;
		 int direction = 2;
		 String location;
		 String equippedItems;
		 String sex;
		 String level;
		 String status = "active";
		 String initiative;
		
		
		String charClass;
		String sql;
		ResultSet rs;
		
		try 
		{	
			
			sql = "SELECT * from CharacterInfoTable where characterName = \'" + charName + "\';";
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				
				// String charClass, String level, String gender, String str, String dex, String con, String charStatInt, String wil, String luck,  String experience, String xCoord, String yCoord, String gold, String abilities, String cooldown
				
				charClass = rs.getString(rs.findColumn("class"));
				
				charInfo += "#" + charClass + "#";
				//sql = "INSERT INTO CharacterInfoTable (characterName, accountID, class, level, gender, health, mana, experience, xCoord, yCoord, gold, equippedItems,"
				//			+ " strength, dexterity, constitution,  intelligence, willpower, luck, abilities, cooldown) "
				charInfo += rs.getString(rs.findColumn("loggedIn")) + "#";
				level = rs.getString(rs.findColumn("level"));
				charInfo += level + "#";
				sex = rs.getString(rs.findColumn("gender"));
				charInfo += sex + "#";
				charInfo += rs.getString(rs.findColumn("health")) + "#";
				charInfo += rs.getString(rs.findColumn("mana")) + "#";
				charInfo += rs.getString(rs.findColumn("experience")) + "#";
				charInfo += rs.getString(rs.findColumn("pointsToSpend")) + "#";
				
				xCoord = rs.getString(rs.findColumn("xCoord"));
				charInfo += xCoord + "#";
				
				yCoord = rs.getString(rs.findColumn("yCoord"));
				charInfo += yCoord + "#";
				
				location = rs.getString(rs.findColumn("location"));
				charInfo += location + "#";
				charInfo += rs.getString(rs.findColumn("clanName")) + "#";
				charInfo += rs.getString(rs.findColumn("strength")) + "#";
				initiative = rs.getString(rs.findColumn("dexterity"));
				charInfo += initiative + "#";
				charInfo += rs.getString(rs.findColumn("constitution")) + "#";
				charInfo += rs.getString(rs.findColumn("intelligence")) + "#";
				charInfo += rs.getString(rs.findColumn("willpower")) + "#";
				charInfo += rs.getString(rs.findColumn("luck")) + "#";
				charInfo += rs.getString(rs.findColumn("abilities")) + "#";
				charInfo += rs.getString(rs.findColumn("cooldown"));
			
				rs.close();
				//right here!!@
				sql = "SELECT * from CharacterInventoryTable where characterName = \'" + charName + "\';";
				rs = stmt.executeQuery(sql);
				
				rs.next();
				// String charClass, String level, String gender, String str, String dex, String con, String charStatInt, String wil, String luck,  String experience, String xCoord, String yCoord, String gold, String abilities, String cooldown
				
				equippedItems = rs.getString(rs.findColumn("equippedItems"));
				rs.close();
				
				PlayerHolder temp = new PlayerHolder(charName, Integer.parseInt(xCoord), Integer.parseInt(yCoord), direction, location, equippedItems, sex, charClass, Integer.parseInt(level), status, Integer.parseInt(initiative) ); 
				//PlayerHolder(String name, int x, int y, int d, String loc, String equippedItems, String sex, String charClass, int level, String status, int initiative );
				charsOnline.put(charName, temp);
			}
			else
				charInfo = "charNotFound";
			
			
			
		} 
	    catch (SQLException e) {
	       e.printStackTrace();
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	    finally
	    {  
	      // System.out.println("get character info attempt completed");
	    }
		return charInfo;
	}
	
	
	//Test this!!
	public String getCharInventory(String charName)
	{
		String charInv = "";
		
		String gold;
		String equippedItems;
		String sql;
		ResultSet rs;
		
		try 
		{	
			//Check to make sure that the account name does not already exist.
			sql = "SELECT * from CharacterInventoryTable where charName = \'" + charName + "\';";
			rs = stmt.executeQuery(sql);
			rs.next();
			
			equippedItems = rs.getString(rs.findColumn("equippedItems"));
			charInv = equippedItems + "#";
			
			gold = rs.getString(rs.findColumn("gold"));
			charInv += gold + "#";
			for(int i = 0; i < 20; i++)
			{
				String fieldName = "inventorySlot" + (i+1);
				charInv += "#" + rs.getString(rs.findColumn(fieldName));
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
		
		return charInv;
	}
	//untested.  Expects perfect data.
	public String deleteCharacter(String charName)
	{
		String errorCode = "";
		try{
			//DELETE FROM table_name
			//			WHERE some_column=some_value;
			String sql = "Delete From CharacterInfoTable WHERE characterName = \'" + charName + "\';";
			System.out.println(sql);
			//System.exit(0);
			int insertResult = stmt.executeUpdate(sql);
			
			if( insertResult == 1)
			{
				errorCode = "charInfoDeleted";
				System.out.println(errorCode);	
			}
			else if( insertResult > 1)
			{
				errorCode = "More than one charInfo with charName :" + charName + " deleted.  This is WRONG.";
				System.out.println(errorCode);
			}
			else if( insertResult == 0)
			{
				errorCode = "Failed to delete charInfo.";
				System.out.println(errorCode);
			}
			
			sql = "Delete from CharacterInventoryTable WHERE characterName = \'" + charName + "\';";
			System.out.println(sql);
			//System.exit(0);
			insertResult = stmt.executeUpdate(sql);
			String errorCode2 = "";
			if( insertResult == 1)
			{
				errorCode2 = "charInvDeleted";
				System.out.println(errorCode2);	
			}
			else if( insertResult > 1)
			{
				errorCode2 = "More than one char inv with charName :" + charName + " deleted.  This is WRONG.";
				System.out.println(errorCode2);
			}
			else if( insertResult == 0)
			{
				errorCode2 = "Failed to delete charInv.";
				System.out.println(errorCode2);
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
	       System.out.println("char delete  attempt completed");
	    }	
		return errorCode;
	}
	
	public String createChar(String charName, int accountID , String charClass, String gender, String str, String dex, String con, String charStatInt, String wil, String luck)
	{
		String errorCode = "dunnoYet";
		ResultSet rs;
		String initialAbilities = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
								+ "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
								+ "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
		String initialCooldown  = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
								+ "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
								+ "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
		int mana =0;
		int health = 10 + (10*Integer.parseInt(con));
		if( charClass.equals("Mage"))
		{
			mana = Integer.parseInt(charStatInt) * 3 ;
		}
		else if( charClass.equals("Rogue"))
		{
			mana = Integer.parseInt(charStatInt) * 2 ;
		}
		else 
		{
			mana = Integer.parseInt(charStatInt);
		}
		
		String sql = "INSERT INTO CharacterInfoTable (characterName, accountID, class, level, gender, health, mana, experience, xCoord, yCoord, "
				+ " strength, dexterity, constitution,  intelligence, willpower, luck, abilities, cooldown) "
				+ "VALUES ( \'" + charName + "\', \'" + accountID +  "\', \'" + charClass + "\', '1', \'" + gender + "\', \'" + health + "\', \'" + mana + "\', '0', '31', '18', \'" 
				+ str + "\', \'" + dex + "\', \'" + con + "\', \'" + charStatInt + "\', \'" + wil+ "\', \'" + luck + "\', \'" + initialAbilities + "\', \'" + initialCooldown +  "\');";
		System.out.println(sql);
		try
		{
			String sqlCheck = "SELECT * from CharacterInfoTable where characterName = \'" + charName + "\';";
			rs = stmt.executeQuery(sqlCheck);
			int numResults = 0;
			while( rs.next() )
			{
				numResults++;
			}
			rs.beforeFirst();
			rs.next();
			System.out.println("char's with name before insert...  numResults = " + numResults);
		    
			
			//If there is no results for the entered charName
			if ( numResults == 0 ) {
			      System.out.println("No character found with name: " + charName);
			         rs.close();
			      
			      //Call a method that suggests the player to make a new account.
			 }
			//check for existing char name
			else if ( numResults == 1 ) {
				//notify admin
				// include username and explain the possible situation of data concurrency issues....
				System.out.println("Charname already used...");
				rs.close();
				return "charNameAlreadyInUse";
			}
			else
				return "twoRowsWithSameCharNameFoundWTF";
			
			int insertResult = stmt.executeUpdate(sql);
			if( insertResult == 1)
			{
				sql = "INSERT INTO CharacterInventoryTable (characterName) VALUES ( \'" + charName + "\' );";
				insertResult = stmt.executeUpdate(sql);
				//If we get more than one row returned from the insert statement to the inventory table...
				if(insertResult != 1)
					errorCode = "charCreatedInventoryError";
				else //otherwise, everything went perfectly and we return the appropriate error code.  Rejoicing is implied.
					errorCode = "charCreatedInventorySuccess";
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
	
	public String updateAccount(String username, String password, String email, String securityQuestion1, String securityAnswer1, String securityQuestion2, String securityAnswer2)
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
			System.out.println("Before update statement, numResults = " + numResults);
			if( numResults == 0){
				errorCode="Account does NOT exist with Username :" + username;
				System.out.println(errorCode);
				rs.close();
				return errorCode;
			}
			rs.updateInt( "locked", 0 );
			rs.updateInt("numFailedAttempts", 0 );
			rs.updateRow();
		
//			
//			UPDATE table_name
//			SET column1=value1,column2=value2,...
//			WHERE some_column=some_value;
//			
			
			sql = "UPDATE AccountTable set password = \'" + password + "\', email = \'" + email + "\', numFailedAttempts = \'" + "0" +  "\', securityQuestion1 = \'" + securityQuestion1 + "\', securityAnswer1 = \'" + securityAnswer1 + "\', securityQuestion2 = \'" + securityQuestion2 + "\', securityAnswer2 = \'" + securityAnswer2 + "\' WHERE username = \'" + username + "\';";
			System.out.println(sql);
			//System.exit(0);
			int insertResult = stmt.executeUpdate(sql);
			//System.exit(0);
			if( insertResult == 1)
			{
				errorCode = "accountUpdated";//Best case scenario
				System.out.println(errorCode);
				rs.close();
				return errorCode;
			}
			else if( insertResult > 1)
			{
				errorCode = "More than one account with username :" + username + " exists.  Both were updated, hahaha.  This is WRONG.";
				System.out.println(errorCode);
				rs.close();
				return errorCode;
			}
			else if( insertResult == 0)
			{
				errorCode = "Failed to update account.";
				System.out.println(errorCode);
				rs.close();
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
	       System.out.println("update account attempt completed");
	    }
		System.out.println(errorCode + "and reached final return statement of the account creation method...  weird...");
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
				errorCode="accountAlreadyExists";
				System.out.println(errorCode);
				rs.close();
				return errorCode;
			}
			
			// FIX THIS
			String macAddress = "placeholder";
			
			Calendar cal = Calendar.getInstance();
	        SimpleDateFormat sdf = new SimpleDateFormat("dd.MMMMM.yyyy GGG hh:mm aaa z");
	        System.out.println( sdf.format(cal.getTime()) );
			String lastLogInTime = sdf.format(cal.getTime());
			
			Boolean loggedIn = true;
			
			
			sql = "INSERT INTO AccountTable (username, password, email, securityQuestion1, securityAnswer1, securityQuestion2, securityAnswer2, macAddress, lastLogInTime, loggedIn)  VALUES ( \'" + username + "\', \'" + password + "\', \'" + email + "\', \'" + securityQuestion1 + "\', \'" + securityAnswer1 + "\', \'" + securityQuestion2 + "\', \'" + securityAnswer2 + "\', \'" + macAddress + "\', \'" + lastLogInTime + "\', \'" + loggedIn + "\') ;";
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
		} 
	    catch (SQLException e) 
		{
	       e.printStackTrace();
	    }
	    catch (Exception e) 
		{
	        e.printStackTrace();
	    }
	    finally
	    {  
	       System.out.println("create account attempt completed");
	    }
		System.out.println(errorCode + "and reached final return statement of the account creation method...  weird...");
		return errorCode;
	}
	
	//Send all clients updated positions of other players in their local area, as well as notifications of events.
		//Events can include zoning, special encounters, and other stuff that we dream up later. :)
		//broadcastGameChanges()
	
	//This is the version that uses the database and is not currently being used.
	public String broadcastGameChangesDB(String charName, String xPos, String yPos, String location)
	{
		
		
		int numChars = 0;
		
		//start of pasted code...
		String sql;
		ResultSet rs;
		String ofAllStrings = "";
		//String location = "countryView";
		
		try 
		{	
//			sql = "SELECT * from CharacterInfoTable where characterName = \'" + charName +"\' ;";
//			rs = stmt.executeQuery(sql);
//			rs.next();
//			location = rs.getString("location");
//			rs.close();
			
			//Check to make sure that the account name does not already exist.
			sql = "SELECT * from CharacterInfoTable where xCoord > \'" + (Integer.parseInt(xPos) - 500) + "\' and location = \'" + location + "\' and xCoord < \'" + (Integer.parseInt(xPos) + 500) + "\' and "
					+ " loggedIn = '1' and yCoord > \'" + (Integer.parseInt(yPos) - 300) + "\' and yCoord < \'" + (Integer.parseInt(yPos) + 300) + "\' ;";
			rs = stmt.executeQuery(sql);
	        while( rs.next() )
			{
				if(numChars>0)
				{
					ofAllStrings += "#";
				}
				ofAllStrings += (rs.getString("characterName") + " " + rs.getString("class") + " " + rs.getString("xCoord") + " " + rs.getString("yCoord"));
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
	       System.out.println("broadcastGameChanges completed");
	    }
		
		
		
		return ofAllStrings;
	}
	
	//This needs to be changed to use the HashMap.  This version is omitting the parameter String charName
		public String broadcastGameChanges()
		{
			String errorCode = "charUpdated";
//			int x = charsOnline.get(charName).xCoord;
//			int y = charsOnline.get(charName).yCoord;
//			String location = charsOnline.get(charName).location;
//			String charClass = charsOnline.get(charName).charClass;
//			
//			int numCharsOnline = charsOnline.size();
//			
//			Iterator it = charsOnline.entrySet().iterator();
//			Map.Entry pair;
////			
//			synchronized (names) {
//                if (!names.contains(name)) {
//                	names.add(name);
//                    break;
//                }
//            }
			
			synchronized (charsOnline){
			for( PlayerHolder val : charsOnline.values() ){
				//if( val.xCoord < x + 500 && val.xCoord > x -500 && location.equals(val.location) && val.yCoord < y + 300 && val.yCoord > y - 300 )
					errorCode += "#" + val.charName + " " + val.xCoord + " " + val.yCoord + " " + val.direction + " " + val.equippedItems + " " + val.charClass;
					
			}
			}
			return errorCode;
		}
		
	//
	
	//This needs to be changed to use the HashMap.
	public String updateCharPositionDB(String charName, String xCoord, String yCoord, String location ){
		String errorCode = "attemptingCharPosUpdate";
		ResultSet rs;
		String sql = "UPDATE CharacterInfoTable set xCoord = \'" + xCoord + "\', yCoord = \'" + yCoord + "\', location = \'" + location + "\' WHERE characterName = \'" + charName + "\';";
		
		//System.out.println(sql);
		try
		{
			String sqlCheck = "SELECT * from CharacterInfoTable where characterName = \'" + charName + "\';";
			rs = stmt.executeQuery(sqlCheck);
			int numResults = 0;
			while( rs.next() )
			{
				numResults++;
			}
			rs.beforeFirst();
			rs.next();
			System.out.println("char's with name before insert...  numResults = " + numResults);
		    
			
			//If there is no results for the entered charName
			if ( numResults == 0 ) {
			      System.out.println("No character found with name: " + charName);
			      rs.close();
			      errorCode = "char name not found";
			      return errorCode;
			      
			 }
			//check for existing char name
			else if ( numResults == 1 ) {
				
				System.out.println("Charname found");
			}
			else {
				rs.close();
				return "twoRowsWithSameCharNameFoundWTF";
			}
			
			int insertResult = stmt.executeUpdate(sql);
			if( insertResult == 1)
			{
				errorCode = "charUpdated";
				rs.close();
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
	       System.out.println("update character attempt completed");
	    }
		return errorCode;
	}
	
	public String updateCharPosition(String charName, String xCoord, String yCoord, String location, String direction ){
		
		
		//errorCode += "#" + val.charName + " " + val.xCoord + " " + val.yCoord + " " + val.direction + " " + val.equippedItems + " " + val.sex;
		
		long currentTime = System.currentTimeMillis();
		System.out.println("current time = " + currentTime);
		System.out.println("last time = " + lastTime);
		long difference = currentTime - lastTime;
		System.out.println("difference = " + difference);
		if( difference > 3000){
			System.out.println("difference = " + difference);
			for( PlayerHolder val : charsOnline.values() ){	
				updateCharPositionDB(val.charName, Integer.toString(val.xCoord), Integer.toString(val.yCoord), val.location );
			}
			lastTime = System.currentTimeMillis();
		}
		
		
		String errorCode = "attemptingCharPosUpdate";
		charsOnline.get(charName).location = location;
		charsOnline.get(charName).xCoord = Integer.parseInt(xCoord);
		charsOnline.get(charName).yCoord = Integer.parseInt(yCoord);
		charsOnline.get(charName).direction = Integer.parseInt(direction);
		System.out.println("Char name : " + charName + " in location " + location);
		return errorCode;
	}
	
	
	
	public String updateCharInfo(String charName, String loggedIn, String charClass, String level, String gender, String health, String mana, String str, String dex, String con, String charStatInt, String wil, String luck, String experience, String pointsToSpend, String xCoord, String yCoord, String location, String clanName,  String abilities, String cooldown)
	{
		charsOnline.get(charName).location = location;
		charsOnline.get(charName).xCoord = Integer.parseInt(xCoord);
		charsOnline.get(charName).yCoord = Integer.parseInt(yCoord);
		
		String errorCode = "";
		ResultSet rs;
		
		String sql = "UPDATE CharacterInfoTable set loggedIn = \'" + loggedIn + "\', class = \'" + charClass + "\', level = \'" + level + "\', gender = \'" + gender +  "\', health = \'" + health + "\', mana = \'" + mana + "\', experience = \'" + experience + "\', pointsToSpend = \'" + pointsToSpend + "\', xCoord = \'" + xCoord + "\', yCoord = \'" + yCoord + "\', location = \'" + location + "\', clanName = \'" + clanName + "\', strength = \'" + str + "\', dexterity = \'" + dex + "\', constitution = \'" + con + "\', intelligence = \'" + charStatInt + "\', willpower = \'" + wil + "\', luck = \'" + luck + "\', abilities = \'" + abilities + "\', cooldown = \'" + cooldown + "\' WHERE characterName = \'" + charName + "\';";
		
		System.out.println(sql);
		try
		{
			String sqlCheck = "SELECT * from CharacterInfoTable where characterName = \'" + charName + "\';";
			rs = stmt.executeQuery(sqlCheck);
			int numResults = 0;
			while( rs.next() )
			{
				numResults++;
			}
			rs.beforeFirst();
			rs.next();
			System.out.println("char's with name before insert...  numResults = " + numResults);
		    
			
			//If there is no results for the entered charName
			if ( numResults == 0 ) {
			      System.out.println("No character found with name: " + charName);
			         rs.close();
			      errorCode = "char name not found";
			      return errorCode;
			      
			 }
			//check for existing char name
			else if ( numResults == 1 ) {
				
				System.out.println("Charname found");
			}
			else {
				rs.close();
				return "twoRowsWithSameCharNameFoundWTF";
			}
			
			int insertResult = stmt.executeUpdate(sql);
			if( insertResult == 1)
			{
				errorCode = "charUpdated";
				rs.close();
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
	       System.out.println("update character attempt completed");
	    }
		
		return errorCode;
	}
	
	//Test this!!
	public String updateCharInventory(String charName, String gold, String equippedItems, String[] inventorySlots) {
		
		charsOnline.get(charName).equippedItems = equippedItems;

		String errorCode = "";
		String sql = "UPDATE CharacterInventoryTable set equippedItems = \'" + equippedItems + "\',  gold = \'" + gold + "\' ";
		for(int i = 0; i < 20; i++)
		{
			String fieldName = "inventorySlot" + (i+1);
			sql += ", " + fieldName + " = " + inventorySlots[i] + " ";
		}
		sql += "WHERE charName = \'" + charName + "\';";
		
		System.out.println(sql);
		ResultSet rs;
		
		try
		{
			String sqlCheck = "SELECT * from CharacterInventoryTable where characterName = \'" + charName + "\';";
			rs = stmt.executeQuery(sqlCheck);
			int numResults = 0;
			while( rs.next() )
			{
				numResults++;
			}
			rs.beforeFirst();
			rs.next();
			System.out.println("char's with name before update...  numResults = " + numResults);
		    
			
			//If there is no results for the entered charName
			if ( numResults == 0 ) {
			      System.out.println("No character found with name: " + charName);
			         rs.close();
			      errorCode = "char name not found";
			      return errorCode;
			      //Call a method that suggests the player to make a new account.
			 }
			//check for existing char name
			else if ( numResults == 1 ) {
				//notify admin
				// include username and explain the possible situation of data concurrency issues....
				System.out.println("Charname found");
			}
			else {
				rs.close();
				return "twoRowsWithSameCharNameFoundWTF";
			}
			
			int insertResult = stmt.executeUpdate(sql);
			if( insertResult == 1)
			{
				errorCode = "charInv updated successfully";
				rs.close();
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
	       System.out.println("update characterInv attempt completed");
	    }
		return errorCode;
	}
	
	public String getSpecificLocationInfo(String locationName)
	{
		String errorCode = "localInfo#";
		ResultSet rs;
		String sql;
		
		int numLocations = 0;
		try 
		{	
			//Check to make sure that the account name does not already exist.
			sql = "SELECT * from LocationTable where name = \'" + locationName + "\';";
			rs = stmt.executeQuery(sql);
			
			while( rs.next() )
			{
				errorCode += (rs.getString("Name") + "#" + rs.getString("levelRequirement") + "#" + rs.getString("xCoord") + "#" + rs.getString("yCoord") + "#" + rs.getString("imagePath") + "#" + rs.getString("musicPath") + "#" + rs.getString("itemTable") + "#" + rs.getString("npcTable") + "#" + rs.getString("groupRequirements") + "#" + rs.getString("exitBoxX")+ "#" + rs.getString("exitBoxY") + "#" + rs.getString("countryViewExitX") + "#" + rs.getString("countryViewExitY") + "#" + rs.getString("entranceBoxX") + "#" + rs.getString("entranceBoxY")+ "#" + rs.getString("mapPath") + "#" + rs.getString("width")+ "#" + rs.getString("height"));
				numLocations++;
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
	    	System.out.println(errorCode);
	       System.out.println("Loaded locations successfully.");
	    }
		
		return errorCode;
	}
	
	public String getLocations()
	{
		String errorCode = "loadLocations#";
		ResultSet rs;
		String sql;
		
		int numLocations = 0;
		try 
		{	
			//Check to make sure that the account name does not already exist.
			sql = "SELECT * from LocationTable;";
			rs = stmt.executeQuery(sql);
			
			while( rs.next() )
			{
				if(numLocations>0)
				{
					errorCode += "#";
				}
				errorCode += (rs.getString("Name") + " " + rs.getString("xCoord") + " " + rs.getString("yCoord"));
				numLocations++;
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
	    	System.out.println(errorCode);
	       System.out.println("Loaded locations successfully.");
	    }
		
		return errorCode;
	}
	
	public String getEnemies()
	{
		String errorCode = "encounterInfo#";
		ResultSet rs;
		String sql;
		
		int numEnemies = 0;
		try 
		{	
			sql = "SELECT * from EnemyTable;";
			rs = stmt.executeQuery(sql);
			
			while( rs.next() )
			{
				if(numEnemies>0)
				{
					errorCode += "#";
				}
				errorCode += (rs.getString("Name") + " " + rs.getString("health") + " " + rs.getString("damage"));
				numEnemies++;
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
	    	System.out.println(errorCode);
	    	System.out.println("Loaded encounter successfully.");
	    }
		
		return errorCode;
	}
	
//	//PersonID int,
//	LastName varchar(255),
//	FirstName varchar(255),
//	Address varchar(255),
//	City varchar(255)
	public String addTable(String tableName, String fieldData)
	{
		String errCode = "";
		String sql = "CREATE TABLE " + tableName + "(" + fieldData + ");";
		
		try {
			int insertResult = stmt.executeUpdate(sql);
			errCode = "createTableSuccess";

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
	    {  
	       System.out.println("addTable completed");
	    }

		return errCode;
	}
	
	public String addCharToClanTable(String clanName, String charName)
	{
		String tableName = clanName + "ClanTable";	
		String errorCode ="";
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MMMMM.yyyy GGG hh:mm aaa z");
        System.out.println( sdf.format(cal.getTime()) );
		String joinDate = sdf.format(cal.getTime());
		String sql = "INSERT INTO " + tableName+ " (charName, memberLevel, joinDate, exiled) VALUES (\'" + charName + "\', \'" + "Member" + "\', \'" + joinDate + "\', '0');";
		System.out.println(sql);
		int insertResult = 0;
		try {
			insertResult = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if( insertResult == 1)
			errorCode = "addCharToClanSuccess";
		else
			errorCode = "addCharToClanFailure";
	
	System.out.println(errorCode);
	return errorCode;
	}
	
	public String makeClanTable(String clanName, String charName )
	{
		String fieldData = "charName varchar(255), memberLevel varchar(255), joinDate varchar(255), exiled int(1)";
		String tableName = clanName + "ClanTable";
		String errorCode = addTable(tableName, fieldData);
		if( errorCode.equals("createTableSuccess") )
		{
			Calendar cal = Calendar.getInstance();
	        SimpleDateFormat sdf = new SimpleDateFormat("dd.MMMMM.yyyy GGG hh:mm aaa z");
	        System.out.println( sdf.format(cal.getTime()) );
			String joinDate = sdf.format(cal.getTime());
			
			String sql = "INSERT INTO " + tableName+ " (charName, memberLevel, joinDate, exiled) VALUES (\'" + charName + "\', \'" + "Leader" + "\', \'" + joinDate + "\', '0');";
			System.out.println(sql);
			int insertResult = 0;
			try {
				insertResult = stmt.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if( insertResult == 1)
				errorCode = "createClanSuccess";
			else
				errorCode = "createClanFailure";
		}
		System.out.println(errorCode);
		return errorCode;
    
	}

	
	public String addLocation(String name, String xCoord, String yCoord)
	{
		String errorCode = "addLocation#";
		ResultSet rs;
		String sql;
//		
//		String sql = "INSERT INTO CharacterInfoTable (characterName, accountID, class, level, gender, health, mana, experience, xCoord, yCoord, "
//				+ " strength, dexterity, constitution,  intelligence, willpower, luck, abilities, cooldown) "
//				+ "VALUES ( \'" + charName + "\', \'" + accountID +  "\', \'" + charClass + "\', '1', \'" + gender + "\', \'" + con + "\', \'" + mana + "\', '0', '0', '0', \'" 
//				+ str + "\', \'" + dex + "\', \'" + con + "\', \'" + charStatInt + "\', \'" + wil+ "\', \'" + luck + "\', \'" + initialAbilities + "\', \'" + initialCooldown +  "\');";
//		System.out.println(sql);
		
		int numLocations = 0;
		try 
		{	
			//name, levelRequirement, xCoord, yCoord, imagePath, musicPath, itemTable, npcTable, groupRequirements, exitBox, countryViewExit, entranceBox, mapPath
			sql = "INSERT INTO LocationTable (Name, xCoord, yCoord) VALUES (\'" + name + "\', \'" + xCoord + "\', \'" + yCoord + "\');";
			int insertResult = stmt.executeUpdate(sql);
			
		}
	    catch (SQLException e) {
	       e.printStackTrace();
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	    finally
	    {  
	       System.out.println("added location successfully.");
	    }
		return errorCode;
	}
}
