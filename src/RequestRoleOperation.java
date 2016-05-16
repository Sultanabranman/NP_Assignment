
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class RequestRoleOperation extends Message{

	private static final long serialVersionUID = 1L;
	
	private Socket socket;
	
	public RequestRoleOperation(int target, int sender) {
		super(target, sender);
	}
	
	public void execute(ObjectOutputStream out, ObjectInputStream in){
		int client_num = getTarget();
		
		try {
			//If client is the first client to connect, assign them role of 
			//dealer
			if(client_num == Definitions.dealer_slot)
			{
				//Create new role assignment operation
				RoleAssignmentOperation role_assignment = new 
						RoleAssignmentOperation(client_num, Definitions.SERVER);
				
				//Set the role in message to dealer
				role_assignment.setRole(Definitions.DEALER);
				
				//Log required information to the server logs
				role_assignment.log(Server.clients.get(client_num).getSocket());
				
				//Send message to client
				out.writeObject(role_assignment);
				
				//Set dealer input and output streams in the server
				Server.toDealer = out;
				Server.fromDealer = in;
				
				System.out.println("Dealer role assgined");
			}
			//If dealer is already assigned, assign client role of player
			else
			{
				//Create new role assignment operation
				RoleAssignmentOperation role_assignment = new 
						RoleAssignmentOperation(client_num, Definitions.SERVER);
				
				//Set role in message to player
				role_assignment.setRole(Definitions.PLAYER);
				
				//Log required information to the server logs
				role_assignment.log(Server.clients.get(client_num).getSocket());
				
				//Send message to client
				out.writeObject(role_assignment);				
				
				System.out.println("Player role assigned");
			}	
		
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public void log(Socket sender){
		String request = "Role requested";
		
		//Log required information
		Server.comm_log.writeToLog(request, sender.getInetAddress());
		
		return;
	}

	

}
