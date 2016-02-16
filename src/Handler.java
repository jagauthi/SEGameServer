import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;

public class Handler extends Thread {
	
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        
        //Port that the clients use to connect to
        static final int PORT = 9001;

        //Set of all names of clients connected
        static HashSet<String> names = new HashSet<String>();

        //Set of all the writers to each of the clients
        static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

        public Handler(Socket socket) {
            this.socket = socket;
        }
         
        public void run() {
            try {
                // Create character streams for the socket.
                in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                
                while (true) {
                    out.println("SUBMITNAME");
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!names.contains(name)) {
                        	names.add(name);
                            break;
                        }
                    }
                }

                out.println("NAMEACCEPTED");
                writers.add(out);

                // Accept messages from this client and broadcast them.
                // Ignore other clients that cannot be broadcasted to.
                while (true) {
                    String input = in.readLine();
                    System.out.println(input);
                    if (input == null) {
                        return;
                    }
                    String[] tokens = input.split(":");
                    String errCode;
                    switch(tokens[0]){
                    case "LOGIN":
                    	errCode = Main.db.loginAttempt(tokens[1], tokens[2]);
                    	//writers.
                    	//sends results of loginAttempt... should be "loginSuccess" for correct credentials situation
                    	if( errCode.equals("loginSuccess"))
                    	{
                    		out.println(errCode + ":" + Main.db.getAccountID(tokens[1]) + ":" + Main.db.getBasicCharsInfo(Main.db.getAccountID(tokens[1])));
                    	}
                    	else
                    	{
                    		out.println(errCode);	
                    	}
                    	
                    	System.out.println("Login result errCode =  " + errCode);
                    	break;
                    //case "MESSAGE":
                    case "CREATEACCOUNT":
                    	System.out.println("calling db.createAccount() method");
                    	errCode = Main.db.createAccount(tokens[1],tokens[2],tokens[3],tokens[4],tokens[5],tokens[6],tokens[7] );
                    	out.println(errCode);
                    	break;
//                    case "TESTLOGOUT":
//                    	names.remove(tokens[1]);
//                    	System.out.println("Removed the chat name : " + tokens[1]);
//                    	break;
                    
//                    case "FORGOTPASSWORD":
//                    	System.out.println("calling db.createAccount() method");
//                    	errCode = Main.db.forgotPassword(tokens[1],tokens[2],tokens[3],tokens[4],tokens[5],tokens[6],tokens[7] );
//                    	out.println(errCode);
//                    	break;
                    default:
                    	System.out.println("somethings wrong");
                    	break;
                    }
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE " + name + ": " + input);
                    }
                }
            } 
            catch (IOException e) {
                e.printStackTrace();
            } 
            //Closes everything
            finally {
            	if (name != null) {
                	names.remove(name);
                }
                if (out != null) {
                	writers.remove(out);
                }
                try {
                    socket.close();
                } 
                catch (IOException e) {
                	e.printStackTrace();
                }
            }
        }
    }
