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
