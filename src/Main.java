
public class Main {
	
	static DatabaseConnector db; 
	
	public Main(){
		
	}
	
	public static void main(String[] args) throws Exception {
        ChatServer cs = new ChatServer();
        cs.start();
        db = new DatabaseConnector();
    
        //testing code below this line...
        //db.createAccount("otherAccountNum79","whatever", "fake@email.not", "securityQuestion1", "securityAnswer1", "securityQuestion2", "securityAnswer2");
        //db.getAccountID("otherAccountNum79");
       // db.createChar("WarriorPrincess", "77", "Warrior", "Female", "30", "15", "15", "5", "5", "10");
        
    }
}
