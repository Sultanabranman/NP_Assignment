import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class RejectClientOperation extends Message{

	private static final long serialVersionUID = 1L;

	public RejectClientOperation(int target, int sender) {
		super(target, sender);
	}
	
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
