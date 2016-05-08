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
public class Server {

	public Server()
	{
		//Create log files
		
		//Create client list
		
		//Open server socket
		
		//Await client connection
		
		//Create new thread for connected client
		
		//Store new client thread information in client list
		
		//Run new thread
		
		//Open input and output streams to and from client
		
		//If client is the first client to connect, assign them role of dealer
		
		//If dealer is already assigned, assign client role of player
		
		//Log new client connection to game and communication logs
		
		//When client connects, inform dealer that client has connected
		
		//Manage requests between dealer and player
	}
}
