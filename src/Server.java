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

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

public class Server {
	
	//Create client list
	protected static final ArrayList<HandleAClient> clients = new ArrayList<>();
	protected static int player_scores[] = new int[5];
	protected static int num_clients = 0;
	protected static int players_ready = 0;
	protected static int players_finished = 0;
	protected static ObjectOutputStream toDealer;
	protected static ObjectInputStream fromDealer;
	
	protected static ServerSocket serverSocket;
	
	protected static Log comm_log;
	protected static Log game_log;
	
	public Server()
	{
		int client_num = 0;
		
		System.out.println("Server started");	
		
		//Create log files	
		comm_log = new Log("communication_log.txt", "Communication Log");
		game_log = new Log("game_log.txt", "Game Log");
		
		//Open server socket
		try
		{
			serverSocket = new ServerSocket(8000);
			
			//Initialise array list with null values
			for(int i = 0; i < Definitions.MAX_CLIENTS; i++)
			{
				clients.add(null);
			}
				
			//Main server service loop
			while(true)
			{
				System.out.println("Awaiting client connection");
				//Await client connection
				Socket socket = serverSocket.accept();
				
				System.out.println("New client connected");
				//Log new client connection
				
				//If the client list is full, reject the new client
				if(num_clients == Definitions.MAX_CLIENTS)
				{
					//reject client
					System.out.println("Game full");
					reject_client(socket);
					continue;
				}
				
				//Get the first available position in client list
				for(int i = 0; i < clients.size(); i++)
				{
					if(clients.get(i) == null)
					{
						//Set client number to available position 
						client_num = i;
						break;
					}
				}
				
				//Create new thread for connected client
				HandleAClient task = new HandleAClient(socket, client_num);
				
				//Store new client thread information in client list
				clients.set(client_num, task);
				
				//Increment the number of clients
				num_clients++;
				
				//Run new thread
				new Thread(task).start();				
			}
		}
		catch(IOException e)
		{
			System.err.println(e);
		}			
	}
	
	public void reject_client(Socket socket)
	{
		
		//Open input and output streams to and from client
		try {	
			//Open object output stream
			ObjectOutputStream outputToClient = new ObjectOutputStream
					(socket.getOutputStream());
			
			//Create rejection message
			RejectClientOperation reject = new RejectClientOperation
					(Definitions.PLAYER, Definitions.SERVER);
			
			//Send reject message to client
			outputToClient.writeObject(reject);
			
			//Close the output stream
			outputToClient.close();
			
			//Close the socket
			socket.close();
			
			return;
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class HandleAClient implements Runnable
	{
		private Socket socket;
		private int client_num;
		private Socket targetSocket;
		private Socket senderSocket;
		
		private ObjectInputStream inputFromClient = null;
		private ObjectOutputStream outputToClient = null;
		
		public HandleAClient(Socket socket, int client_num)
		{
			this.socket = socket;
			this.client_num = client_num;
		}
		
		//Method to allow other threads to output to this client
		public void write(Message message)
		{
			try
			{
				outputToClient.writeObject(message);
			}
			catch(IOException ex)
			{
				System.err.println(ex);
			}
		}
		
		
		//Getter for the socket in this thread
		public Socket getSocket() {
			return socket;
		}		
		
		//Getter from this client's object output stream
		public ObjectOutputStream getOutputToClient() {
			return outputToClient;
		}

		//Set this client's output stream
		public void setOutputToClient(ObjectOutputStream outputToClient) {
			this.outputToClient = outputToClient;
		}		
		
		//Getter for this client's object input stream
		public ObjectInputStream getInputFromClient() {
			return inputFromClient;
		}

		//Set this clients input stream
		public void setInputFromClient(ObjectInputStream inputFromClient) {
			this.inputFromClient = inputFromClient;
		}

		public synchronized void run()
		{			
			try
			{
				//Open input and output streams to and from client
				inputFromClient = new ObjectInputStream
						(socket.getInputStream());
				
				outputToClient = new ObjectOutputStream
						(socket.getOutputStream());
				
				//Clean any data out of output stream
				outputToClient.flush();				
				
				System.out.println("Assigning client role");
				
				RequestRoleOperation request = (RequestRoleOperation) 
						inputFromClient.readObject();
				
				//Pass in client number for request to use
				request.setTarget(client_num);
				
				request.log(clients.get(request.getSender()).getSocket());
				
				request.execute(outputToClient, inputFromClient);				
				
				//Manage requests between dealer and player
				while(true)
				{
					Message message = null;
					try
					{					
						//Await request from client
						message = (Message) inputFromClient.readObject();
					}
					catch(EOFException e)
					{
						System.out.println("Client disconnected");
						//Decrease the number of clients
						num_clients--;
						//Set the index in clients to null
						clients.set(client_num, null);
						
						return;
					}
					
					//If the message's target is the server, don't attempt to 
					//get the server's socket
					if(message.getTarget() == Definitions.SERVER)
					{
						targetSocket = null;
					}
					//If the message's target isn't the server, set the target 
					//socket
					else
					{
						//Get the target's socket for logging purposes
						targetSocket = clients.get
								(message.getTarget()).getSocket();
					}					
					
					senderSocket = clients.get(message.getSender()).getSocket();
					
					//Log communication stored in message
					message.log(targetSocket, senderSocket);
					
					//If the target is the dealer, set the stored input and 
					//output streams to the dealer
					if(message.getTarget() == Definitions.DEALER)
					{						
						toDealer.writeObject(message);
						toDealer.flush();
					}	
					//If the message's target is the server, execute the command
					//within the server
					else if(message.getTarget() == Definitions.SERVER)
					{
						message.execute(outputToClient, inputFromClient);
						outputToClient.flush();
					}
					//If the target of the message is the player					
					else
					{			
						try
						{						
							clients.get(message.getTarget()).write(message);
							clients.get(message.getTarget()).
								getOutputToClient().flush();
						}
						catch(SocketException e)
						{
							ObjectInputStream input = new ObjectInputStream
									(clients.get(message.getTarget()).											
											getSocket().getInputStream());
							
							ObjectOutputStream output = new ObjectOutputStream
									(clients.get(message.getTarget()).											
											getSocket().getOutputStream());
							
							clients.get(message.getTarget()).setOutputToClient
								(output);
							clients.get(message.getTarget()).setInputFromClient
								(input);							
						}						
					}					
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
