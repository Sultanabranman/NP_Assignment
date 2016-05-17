
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class PlayerReadyOperation extends Message{
	
	private static final long serialVersionUID = 1L;	
	
	public PlayerReadyOperation(int target, int sender) {
		super(target, sender);		
	}	
	
	public void log(Socket target, Socket sender) {
		String request = "Ready sent";
		
		//Log required information for this message
		Server.comm_log.writeToLog(request, sender.getInetAddress());
		
		return;
	}
	
	public void execute(ObjectOutputStream out, ObjectInputStream in) {
		Server.players_ready++;
		
		if(Server.players_ready == (Server.num_clients - 1))
		{
			System.out.println("All players ready");
			
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
