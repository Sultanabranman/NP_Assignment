import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class RequestRoleOperation extends Message{

	private static final long serialVersionUID = 1L;
	
	private Socket socket;
	
	public RequestRoleOperation(int target) {
		super(target);
	}
	
	public void execute(ObjectOutputStream out, ObjectInputStream in){
		int client_num = getTarget();
		
		try {
			//If client is the first client to connect, assign them role of 
			//dealer
			if(client_num == Definitions.dealer_slot)
			{
				RoleAssignmentOperation role_assignment = new 
						RoleAssignmentOperation(client_num);
				role_assignment.setRole(Definitions.DEALER);
				out.writeObject(role_assignment);
				
				Server.toDealer = out;
				Server.fromDealer = in;
				
				System.out.println("Dealer role assgined");
			}
			//If dealer is already assigned, assign client role of player
			else
			{
				RoleAssignmentOperation role_assignment = new 
						RoleAssignmentOperation(client_num);
				role_assignment.setRole(Definitions.PLAYER);
				
				out.writeObject(role_assignment);
				
				
				System.out.println("Player role assigned");
			}	
		
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public void log(){
		
	}

	

}
