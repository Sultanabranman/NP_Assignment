
public class PlayerReadyOperation extends Message{
	
	private static final long serialVersionUID = 1L;	
	
	public PlayerReadyOperation(int target) {
		super(target);		
	}	
	
	public void log() {
		
	}
	
	public void execute() {
		Server.players_ready++;
		
		if(Server.players_ready == (Server.num_clients - 1))
		{
			StartGameOperation dealer_start = new StartGameOperation
					(Definitions.DEALER);
			StartGameOperation start = new StartGameOperation
					(Definitions.PLAYER);
			
			//Get the client stored in the dealer slot and send the start 
			//message targeted at the dealer
			Server.clients.get(Definitions.dealer_slot).write(dealer_start);
			
			//For each connected client, send the start game message
			for(int i = 1; i < Server.clients.size(); i++)
			{
				Server.clients.get(i).write(start);
			}
		}
	}

}
