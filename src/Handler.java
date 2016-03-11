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
                   // System.out.println(input);
                    if (input == null) {
                        return;
                    }
                    String[] tokens = input.split("#");
                    String errCode;
                    switch(tokens[0]){
                    case "LOGIN":
                    	errCode = Main.db.loginAttempt(tokens[1], tokens[2]);
                    	//writers.
                    	//sends results of loginAttempt... should be "loginSuccess" for correct credentials situation
                    	if( errCode.equals("loginSuccess"))
                    	{
                    		out.println(errCode + "#" + Main.db.getAccountID(tokens[1]) + "#" + Main.db.getBasicCharsInfo(Main.db.getAccountID(tokens[1])));
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
                    case "UPDATEACCOUNT":
                    	System.out.println("calling db.updateAccount() method");
                    	errCode = Main.db.updateAccount(tokens[1],tokens[2],tokens[3],tokens[4],tokens[5],tokens[6],tokens[7] );
                    	out.println(errCode);
                    	break;
                    case "CREATECHAR":
                    	System.out.println("calling db.createChar() method");
                    	errCode = Main.db.createChar(tokens[1],Integer.parseInt(tokens[2]),tokens[3],tokens[4],tokens[5],tokens[6],tokens[7], tokens[8], tokens[9], tokens[10] );
                    	//out.println(errCode );
                    	if( errCode.equals("charCreatedInventorySuccess"))
                    	{
                    		out.println(errCode + "#" + tokens[2] + "#" + Main.db.getBasicCharsInfo(Integer.parseInt(tokens[2])));
                    	}
                    	else
                    	{
                    		 out.println(errCode);	
                    	}
                    	break;
                    case "DELETECHAR":
                    	System.out.println("calling db.deleteCharacter() method");
                    	errCode = Main.db.deleteCharacter(tokens[1]);//this needs to be implemented and does not return anything
                    	//out.println(errCode);
                    	out.println(errCode + "#" + tokens[2] + "#" + Main.db.getBasicCharsInfo(Integer.parseInt(tokens[2])));
                    	break;
                    case "UPDATECHARPOS":
                    	System.out.println("calling db.updateCharInfo() method");
                    	errCode = Main.db.updateCharPosition(tokens[1],tokens[2], tokens[3],tokens[4]);
                    	//out.println(errCode + "#" + Main.db.broadcastGameChanges(tokens[1], tokens[2],tokens[3], tokens[4]));//sends charname, x, and y position data
                    	break;
                    case "GETLOCALINFO":
                    	System.out.println("calling db.getSpecificLocationInfo() method");
                    	errCode = Main.db.getSpecificLocationInfo(tokens[1]);
                    	out.println(errCode );
                    	break;
                    case "GETSECURITYINFO":
                    	System.out.println("calling db.getAccountInfo() method");
                    	errCode = Main.db.getAccountInfo(tokens[1]);
                    	out.println(errCode);
                    	break;
                    case "LOCKACCOUNT":
                    	System.out.println("calling db.lockAccount() method");
                    	Main.db.lockAccount(tokens[1]);//this needs to be implemented and does not return anything
                    	//out.println("securityInfo:" + errCode);
                    	break;
                    case "PLAYGAME":
                    	System.out.println("calling db.getCharInfo() method");
                    	errCode = Main.db.getCharInfo(tokens[1]);
                    	out.println(errCode);
                    	break;
                    case "GETLOCATIONS":
                    	System.out.println("Calling db.getLocations() method");
                    	errCode = Main.db.getLocations();
                    	out.println(errCode);
                 
                    	break;
                    case "MESSAGE":	
                    	//This was moved from the default case due to issues with multiple people logging in...
                    	System.out.println("message case");
                    	//Also need to send this message to the chat box on the server menu...
                        //Main.launch.messageArea.append(line.substring(8) + "\n");
                    	for (PrintWriter writer : writers) {
                            writer.println(input + "#" + name );
                    	   }
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
                    	
                            
                      
                    	break;
                    }
//                    errCode = Main.db.broadcastGameChanges();
//                    for (PrintWriter writer : writers) {
//                        writer.println(errCode);
//                	   }
                    
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
