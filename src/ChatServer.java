//import java.io.PrintWriter;
//import java.net.ServerSocket;
//import java.util.HashSet;
//
//public class ChatServer {
//
//	//Port that the clients use to connect to
//    static final int PORT = 9001;
//
//    //Set of all names of clients connected
//    static HashSet<String> names = new HashSet<String>();
//
//    //Set of all the writers to each of the clients
//    static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
//    
//    public ChatServer(){
//    	try {
//			runChatServer();
//		} 
//    	catch (Exception e){
//			e.printStackTrace();
//		}
//    }
//    
//    public void runChatServer() throws Exception{
//    	ServerSocket listener = new ServerSocket(ChatServer.PORT);
//        try{
//            while(true){
//                new Handler(listener.accept()).start();
//            }
//        } 
//        finally {
//            listener.close();
//        }
//    }
//}