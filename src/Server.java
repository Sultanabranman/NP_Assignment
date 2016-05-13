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
	
	private final ArrayList<HandleAClient> clients = new ArrayList<>();
	private int num_clients = 0;
	private int players_ready = 0;
	private ObjectOutputStream toDealer;
	private ObjectInputStream fromDealer;
	
	public Server()
	{
		//Create client list
		
		
		//Create log files
		
		
		
		//Open server socket
		try
		{
			ServerSocket serverSocket = new ServerSocket(8000);
				
			//Main server service loop
			while(true)
			{
				//Await client connection
				Socket socket = serverSocket.accept();
				
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
			//Client slot on server reserved for dealer
			int dealer_slot = 0;
			try
			{
				//Open input and output streams to and from client
				ObjectInputStream inputFromClient = new ObjectInputStream(
						socket.getInputStream());
				
				ObjectOutputStream outputToClient = new ObjectOutputStream(
						socket.getOutputStream());
				
				//Clean any data out of output stream
				outputToClient.flush();				
				
				//If client is the first client to connect, assign them role of 
				//dealer
				if(client_num == dealer_slot)
				{
					RoleAssignmentOperation role_assignment = new RoleAssignmentOperation(client_num);
					role_assignment.setRole(Definitions.DEALER);
					outputToClient.writeObject(role_assignment);
					
					toDealer = outputToClient;
					fromDealer = inputFromClient;
				}
				//If dealer is already assigned, assign client role of player
				else
				{
					RoleAssignmentOperation role_assignment = new RoleAssignmentOperation(client_num);
					role_assignment.setRole(Definitions.PLAYER);
					outputToClient.writeObject(role_assignment);
				}					
				
				//Manage requests between dealer and player
				while(true)
				{
					//Await role assignment from server
					Message message = (Message) inputFromClient.readObject();
					
					if(message.getTarget() == Definitions.DEALER)
					{
						message.setDealerOutputStream(toDealer);
						message.setDealerInputStream(fromDealer);
					}
					
					message.setInputStream(inputFromClient);
					message.setOutputStream(outputToClient);
					
					message.execute();
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
