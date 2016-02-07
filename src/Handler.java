import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Handler extends Thread {
	
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        
//          Services this thread's client by repeatedly requesting a
//          screen name until a unique one has been submitted, then
//          acknowledges the name and registers the output stream for
//          the client in a global set, then repeatedly gets inputs and
//          broadcasts them.
         
        public void run() {
            try {

                // Create character streams for the socket.
                in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Request a name from this client.  Keep requesting until
                // a name is submitted that is not already used.  Note that
                // checking for the existence of a name and adding the name
                // must be done while locking the set of names.
                while (true) {
                    out.println("SUBMITNAME");
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (ChatServer.names) {
                        if (!ChatServer.names.contains(name)) {
                        	ChatServer.names.add(name);
                            break;
                        }
                    }
                }

                // Now that a successful name has been chosen, add the
                // socket's print writer to the set of all writers so
                // this client can receive broadcast messages.
                out.println("NAMEACCEPTED");
                ChatServer.writers.add(out);

                // Accept messages from this client and broadcast them.
                // Ignore other clients that cannot be broadcasted to.
                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    for (PrintWriter writer : ChatServer.writers) {
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
                	ChatServer.names.remove(name);
                }
                if (out != null) {
                	ChatServer.writers.remove(out);
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
