import java.io.IOException;


public class RequestRoleOperation extends Message{

	private static final long serialVersionUID = 1L;
	
	public RequestRoleOperation(int target) {
		super(target);
	}
	
	public void execute(){
		int client_num = getTarget();
		
		try {
			//If client is the first client to connect, assign them role of 
			//dealer
			if(client_num == Definitions.dealer_slot)
			{
				RoleAssignmentOperation role_assignment = new 
						RoleAssignmentOperation(client_num);
				role_assignment.setRole(Definitions.DEALER);
				getOutputStream().writeObject(role_assignment);
				
				Server.toDealer = getOutputStream();
				Server.fromDealer = getInputStream();
				
				System.out.println("Dealer role assgined");
			}
			//If dealer is already assigned, assign client role of player
			else
			{
				RoleAssignmentOperation role_assignment = new 
						RoleAssignmentOperation(client_num);
				role_assignment.setRole(Definitions.PLAYER);
				
				getOutputStream().writeObject(role_assignment);
				
				
				System.out.println("Player role assigned");
			}	
		
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public void log(){
		
	}

	

}
