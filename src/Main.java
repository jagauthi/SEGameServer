
public class Main {
	
	static DatabaseConnector db; 
	
	public Main(){
		
	}
	
	public static void main(String[] args) throws Exception {
        ChatServer cs = new ChatServer();
        cs.start();
        db = new DatabaseConnector();
    
        //testing code below this line...
       //db.updateAccount("otherAccountNum79","newPass", "realemail@email.not", "securityQuestion1", "securityAnswer1", "securityQuestion2", "securityAnswer2");
        //db.getAccountID("otherAccountNum79");
     //db.createChar("Anone", "42", "Warrior", "Male", "38", "10", "20", "1", "1", "10");
     //db.createChar("Arinde", "42", "Rogue", "Male", "10", "30", "20", "1", "1", "18");
     //db.createChar("Gorski", "1", "Mage", "Male", "5", "15", "5", "35", "5", "15");
    }
}
