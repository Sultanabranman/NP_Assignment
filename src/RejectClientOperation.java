/**
 * Message to reject a client if the server has the maximum number of clients 
 * connected
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class RejectClientOperation extends Message{

	private static final long serialVersionUID = 1L;

	//Constructor for the message
	public RejectClientOperation(int target, int sender) {
		super(target, sender);
	}
	
	//Method to be executed when message is received
	public void execute(ObjectOutputStream out, ObjectInputStream in){
		System.out.println("Game is full closing");
		
		//Close all Client connections
		try {
			Client.getFromServer().close();		
			Client.getToServer().close();
			Client.getSocket().close();
			
			//Close the client
			System.exit(1);
		
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
