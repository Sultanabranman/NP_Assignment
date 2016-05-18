import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * The Client class is a generic class that is used when client is passed in 
 * from the command line.
 * 
 * The Client class can only be passed in after the server has been started.
 * 
 * On startup the client creates a socket to the server.
 * 
 * It then opens input and output streams to allow communication to and from the
 * server.
 * 
 * The Client then awaits role assignment from the server. 
 * 
 * If the Client is the first to connect to the server, it is assigned the 
 * dealer role and is then managed autonomously in the Dealer class.
 * 
 * If the dealer is already assigned, the Client is assigned the role of player.
 * The player role requires user input to operate and is managed in the Player 
 * class.
 * 
 * @author Chris
 *
 */
public class Client {
	
	private static ObjectOutputStream toServer;
	private static ObjectInputStream fromServer;
	protected static Socket socket;

	public Client() 
	{
		try
		{
			System.out.println("Client started");
			
			//Connects client to server
			//socket = new Socket("m1-c26n1.csit.rmit.edu.au" , Definitions.port);
			socket = new Socket("localhost" , 8000);
			
			//Creates input and output streams from client to server
			toServer = new ObjectOutputStream(socket.getOutputStream());			
			fromServer = new ObjectInputStream(socket.getInputStream());

			//Flush any data from output stream
			toServer.flush();			
			
			//Create a request to be assigned a role
			RequestRoleOperation request = new RequestRoleOperation
					(Definitions.SERVER, 0);
			
			//Send the request to the server
			toServer.writeObject(request);
			
			System.out.println("Awaiting role assignment");
			
			//Await role assignment from server
			Message message = (Message) fromServer.readObject();
			
			//Execute the message received from the server
			message.execute(toServer, fromServer);						
		}
		catch(SocketException e)
		{
			System.err.println("Error: Game is full closing");
		}
		catch(IOException e)
		{
			System.err.println(e);
		}
		catch(ClassNotFoundException e)
		{
			System.err.println(e);
		}		
	}

	//Get the output stream for this client
	public static ObjectOutputStream getToServer() {
		return toServer;
	}

	//Get the input stream for this client
	public static ObjectInputStream getFromServer() {
		return fromServer;
	}

	//Get the socket for this client
	public static Socket getSocket() {
		return socket;
	}
	
	
}
