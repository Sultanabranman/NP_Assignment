import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class RoleAssignmentOperation extends Message{
	
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
			new Player(out, in, getTarget());
		}
	}
	
	public void setRole(int role) {
		this.role = role;
	}	
}
