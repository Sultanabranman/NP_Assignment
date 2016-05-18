/**
 * Message sent from a player indicating that the player wishes to sign up for 
 * the game. When the server receives the request, it checks to see how many 
 * players are ready, and if all players are ready, it sends out messages to 
 * start the game
 */
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class PlayerReadyOperation extends Message{
	
	private static final long serialVersionUID = 1L;	
	
	//Constructor for the message
	public PlayerReadyOperation(int target, int sender) {
		super(target, sender);		
	}	
	
	//Method to be executed by server to log any required information
	public void log(Socket target, Socket sender) {
		String request = "Ready sent";
		
		//Log required information for this message
		Server.comm_log.writeToLog(request, sender.getInetAddress());
		
		return;
	}
	
	//Method to be executed when the message is received, checks how many 
	//players are ready. If all players are ready, sends messages to start the 
	//game
	public void execute(ObjectOutputStream out, ObjectInputStream in) {
		Server.players_ready++;
		
		//If all players are ready
		if(Server.players_ready == (Server.num_clients - 1))
		{
			System.out.println("All players ready");
			
			//Create start game messages
			StartGameOperation dealer_start = new StartGameOperation
					(Definitions.DEALER, Definitions.SERVER);
			StartGameOperation start = new StartGameOperation
					(Definitions.PLAYER, Definitions.SERVER);
			
			//Log dealer started information
			dealer_start.log(Server.clients.get(Definitions.dealer_slot)
					.getSocket());
			
			//Get the client stored in the dealer slot and send the start 
			//message targeted at the dealer
			Server.clients.get(Definitions.dealer_slot).write(dealer_start);
			
			//For each connected client, send the start game message
			for(int i = 1; i < Server.clients.size(); i++)
			{
				//If client position is null, continue to next client
				if(Server.clients.get(i) == null)
				{
					continue;
				}
				//Log player started information
				start.log(Server.clients.get(i).getSocket());
				
				//Send start message
				Server.clients.get(i).write(start);
			}
		}
	}

}
