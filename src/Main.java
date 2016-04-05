
public class Main {
	
	static DatabaseConnector db; 
	static Launcher launch;
	
	public Main(){
		
	}
	
	public static void main(String[] args) throws Exception {
        ChatServer cs = new ChatServer();
        cs.start();
        db = new DatabaseConnector();
        
        launch = new Launcher();
    
        //testing code below this line...
    //db.createAccount("jagauthi2","newPass", "realemail@email.not", "securityQuestion1", "securityAnswer1", "securityQuestion2", "securityAnswer2");
        //db.getAccountID("otherAccountNum79");
     //db.createChar("Anone", "42", "Warrior", "Male", "38", "10", "20", "1", "1", "10");
     //db.createChar("Arinde", "42", "Rogue", "Male", "10", "30", "20", "1", "1", "18");
     //db.createChar("Gorski", "1", "Mage", "Male", "5", "15", "5", "35", "5", "15");
//        String equippedItems = "00:00:00-";
//        String inventorySlots[] = new String[20];
//        for(int i = 0; i < 20; i++)
//        {
//        	inventorySlots[i] = "00:00";
//        }
//        db.unlockAccount("42");
        //db.updateCharInfo("Gilsani", 1, "Mage", "2", "Male", "20", "20", "20", "200", "20", "20", "20", "20", "20", "20", "20", "20");
        //db.updateCharInventory("Gilsani", equippedItems, inventorySlots);
        //db.deleteCharacter("KittyPryde");
        //db.broadcastGameChanges("Gilsani", "5", "5");
        //db.getLocations();
        //db.addCharToClanTable("STC", "Anone");
       // db.makeClanTable("ASDF3", "asdf");
        
      //  db.testPreparedStatementAccount("42;' drop table ASDF3ClanTable;");
      //  db.deleteCharacter("42\'; drop table ASDF1ClanTable; --'");
    }
}
