
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class RoleAssignmentOperation extends Message{
	
	private Socket socket;
	
	public RoleAssignmentOperation(int target, int sender) {
		super(target, sender);
	}

	private static final long serialVersionUID = 1L;
	
	private int role;	
	
	public void log(Socket target) {
		String role_assigned = null;
		
		//If the role assigned is the dealer role
		if(role == Definitions.DEALER)
		{
			role_assigned = "Dealer Assigned";
		}
		//If the role assigned is the player role
		else if(role == Definitions.PLAYER)
		{
			role_assigned = "Player Assigned";
		}
		
		//Log required information for this message
		Server.game_log.writeToLog(role_assigned, target.getInetAddress());
		
		return;		
	}
	
	public void execute(ObjectOutputStream out, ObjectInputStream in){
		if(role == Definitions.DEALER)
		{
			//Make this client dealer
			new Dealer(out, in, getTarget());
		}
		else if(role == Definitions.PLAYER)
		{
			//Make this client a player
			new Player(out, in, getTarget(), Client.socket);
		}
	}
	
	public void setRole(int role) {
		this.role = role;
	}	
}
