
public class Main {
	
	public Main(){
		
	}
	
	public static void main(String[] args) throws Exception {
        ChatServer cs = new ChatServer();
        cs.start();
        new DatabaseConnector();
    }
}
