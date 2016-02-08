import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.HashSet;

public class ChatServer extends Thread{

	//Port that the clients use to connect to
    static final int PORT = 9001;

    //Set of all names of clients connected
    static HashSet<String> names = new HashSet<String>();

    //Set of all the writers to each of the clients
    static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
    
    ServerSocket listener;
    
    public ChatServer()
    {
    	try {
			listener = new ServerSocket(PORT);
			
	        System.out.println("The chat server is running.");
	        InetAddress addr = InetAddress.getLocalHost();
	        System.out.println(addr.getHostName());
		} 
    	catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void run(){
    	while(true){
	    	try {
				new Handler(listener.accept()).start();
			} 
	    	catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
}
