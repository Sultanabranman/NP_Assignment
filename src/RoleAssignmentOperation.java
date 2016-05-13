import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RoleAssignmentOperation extends Message{

	private ObjectInputStream fromServer;	
	private ObjectOutputStream toServer;
	
	public RoleAssignmentOperation(int target) {
		super(target);
	}

	private static final long serialVersionUID = 1L;
	
	private int role;	
	
	public void execute(){
		if(role == Definitions.DEALER)
		{
			//Make this client dealer
			new Dealer(getToServer(), getFromServer());
		}
		else if(role == Definitions.PLAYER)
		{
			//Make this client a player
			new Player(toServer, fromServer);
		}
	}
	
	public void setRole(int role) {
		this.role = role;
	}
	
	public ObjectInputStream getFromServer() {
		return fromServer;
	}

	public void setFromServer(ObjectInputStream fromServer) {
		this.fromServer = fromServer;
	}

	public ObjectOutputStream getToServer() {
		return toServer;
	}

	public void setToServer(ObjectOutputStream toServer) {
		this.toServer = toServer;
	}
}
