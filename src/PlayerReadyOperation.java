
public class PlayerReadyOperation extends Message{
	
	private static final long serialVersionUID = 1L;

	private int ready_status;
	private int player_id;
	
	public PlayerReadyOperation(int target, int player_id) {
		super(target);
		this.player_id = player_id;
	}	
	
	public void log() {
		
	}
	
	public void execute() {
		Server.players_ready++;
		
		if(Server.players_ready == (Server.num_clients - 1))
		{
			
		}
	}

}
