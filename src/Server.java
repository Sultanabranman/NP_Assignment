/**
 * The Server class is the first role that must be assigned before Clients can 
 * be created. Server is created when server is passed in on the command line. 
 * Only one server can be created per game.
 * 
 * The Server acts as an intermediary between the Player and the Dealer roles.
 * 
 * When created, the Server generates communication and game logs, opens a 
 * server socket and awaits connections from clients.
 * 
 * When a client connects, the server opens input and output streams to the 
 * client, and assigns them a role.
 * 
 * If the client is the first client to connect, it is added to the first slot 
 * in the client list and assigned the role of dealer.
 * 
 * Subsequent connections are assigned the role of player and added to the 
 * client list.
 * 
 * After the role is assigned, the Server starts a new thread to manage the 
 * client.
 * 
 * Whenever communication is passed through the Server, it logs the event in the
 * appropriate log file.
 * 
 * @author Chris
 *
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

public class Server {

	private final int DEALER = 0;
	private final int PLAYER = 1;
	
	//Create client list
	private final ArrayList<HandleAClient> clients = new ArrayList<>();
	protected static int player_scores[] = new int[5];
	protected static int num_clients = 0;
	protected static int players_ready = 0;
	protected static int players_finished = 0;
	protected static ObjectOutputStream toDealer;
	protected static ObjectInputStream fromDealer;
	
	public Server()
	{
		System.out.println("Server started");
		
		
		//Create log files		
		
		//Open server socket
		try
		{
			ServerSocket serverSocket = new ServerSocket(8000);
				
			//Main server service loop
			while(true)
			{
				System.out.println("Awaiting client connection");
				//Await client connection
				Socket socket = serverSocket.accept();
				
				System.out.println("New client connected");
				//Log new client connection
				
				//Get current number of clients connected
				num_clients = clients.size();
				
				//Create new thread for connected client
				HandleAClient task = new HandleAClient(socket, num_clients);
				
				//Store new client thread information in client list
				clients.add(task);
				
				//Run new thread
				new Thread(task).start();				
			}
		}
		catch(IOException e)
		{
			System.err.println(e);
		}			
	}
	class HandleAClient implements Runnable
	{
		private Socket socket;
		private int client_num;
		
		public HandleAClient(Socket socket, int client_num)
		{
			this.socket = socket;
			this.client_num = client_num;
		}
		
		public synchronized void run()
		{
			
			try
			{
				//Open input and output streams to and from client
				ObjectInputStream inputFromClient = new ObjectInputStream(
						socket.getInputStream());
				
				ObjectOutputStream outputToClient = new ObjectOutputStream(
						socket.getOutputStream());
				
				//Clean any data out of output stream
				outputToClient.flush();				
				
				System.out.println("Assgining client role");
				
				RequestRoleOperation request = (RequestRoleOperation) 
						inputFromClient.readObject();
				
				//Pass in client number for request to use
				request.setTarget(client_num);
				request.setOutputStream(outputToClient);
				request.setInputStream(inputFromClient);
				
				request.execute();								
				
				//Manage requests between dealer and player
				while(true)
				{
					//Await request from client
					Message message = (Message) inputFromClient.readObject();
					
					//Pass in input and output streams to the client to allow 
					//message to send required data
					message.setInputStream(inputFromClient);
					message.setOutputStream(outputToClient);
					
					//Pass in input and output streams to the dealer to allow 
					//message to send required data
					message.setDealerOutputStream(toDealer);
					message.setDealerInputStream(fromDealer);
					
					//Log communication stored in message
					message.log();
					
					//If the target is the dealer, set the stored input and 
					//output streams to the dealer
					if(message.getTarget() == Definitions.DEALER)
					{						
						toDealer.writeObject(message);
					}	
					//If the message's target is the server, execute the command
					//within the server
					else if(message.getTarget() == Definitions.SERVER)
					{
						message.execute();
					}
					//If the target of the message is the player					
					else
					{						
						outputToClient.writeObject(message);
					}					
				}
			}
			catch(IOException e)
			{
				System.err.println(e);
			} 
			catch (ClassNotFoundException e) {
				System.err.println(e);
			}
		}
	}
}
