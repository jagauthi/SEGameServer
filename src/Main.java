public class Main {
	
	static DatabaseConnector db; 
	
	public Main(){
		
	}
	
	public static void main(String[] args) throws Exception {
        ChatServer cs = new ChatServer();
        cs.start();
        db = new DatabaseConnector();
    }
}
