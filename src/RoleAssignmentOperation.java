import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class RoleAssignmentOperation extends Message{
	
	private Socket socket;
	
	public RoleAssignmentOperation(int target) {
		super(target);
	}

	private static final long serialVersionUID = 1L;
	
	private int role;	
	
	public void log() {
		
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
