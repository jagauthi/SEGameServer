import java.io.PrintWriter;
import java.util.HashSet;

public class ChatServer {

	//Port that the clients use to connect to
    static final int PORT = 9001;

    //Set of all names of clients connected
    static HashSet<String> names = new HashSet<String>();

    //Set of all the writers to each of the clients
    static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

}