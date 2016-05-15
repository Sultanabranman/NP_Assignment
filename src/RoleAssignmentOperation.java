
public class RoleAssignmentOperation extends Message{
	
	public RoleAssignmentOperation(int target) {
		super(target);
	}

	private static final long serialVersionUID = 1L;
	
	private int role;	
	
	public void log() {
		
	}
	
	public void execute(){
		if(role == Definitions.DEALER)
		{
			//Make this client dealer
			new Dealer(getOutputStream(), getInputStream(), getTarget());
		}
		else if(role == Definitions.PLAYER)
		{
			//Make this client a player
			new Player(getOutputStream(), getInputStream(), getTarget());
		}
	}
	
	public void setRole(int role) {
		this.role = role;
	}	
}
