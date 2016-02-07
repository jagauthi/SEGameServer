import java.net.ServerSocket;


public class Main {
	
	public Main()
	{
		
	}
	
	public void initChatServer()
	{
		
	}

	/*
	 * 	Temporarily just having an infinite loop in the main
	 *	method running the server until the program terminates
	*/
	public static void main(String[] args) throws Exception {
	java.net.InetAddress addr = InetAddress.getLocalHost();
        System.out.println(addr.getHostName());
        System.out.println("The chat server is running.");
        java.net.InetAddress addr = java.net.InetAddress.getLocalHost();
        System.out.println(addr.getHostName());
        
        ServerSocket listener = new ServerSocket(ChatServer.PORT);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } 
        finally {
            listener.close();
        }
    }
	
}
