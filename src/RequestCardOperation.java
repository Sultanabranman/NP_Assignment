
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class RequestCardOperation extends Message{

	private static final long serialVersionUID = 1L;
	private int sender;
	
	
	public RequestCardOperation(int target, int sender) {
		super(target, sender);
		this.sender = sender;
	}
	
	public void log(Socket target, Socket sender) {
		String request = "Requested card"; 
		String action = "Card request received";
				
		//Log required information for this message
		Server.comm_log.writeToLog(request, sender.getInetAddress());
		Server.game_log.writeToLog(action, target.getInetAddress());
		
		return;
	}
	
	public void execute(ObjectOutputStream out, ObjectInputStream in) {
		Card card = null;
		
		try {
			//Dealer draws card
			card = Dealer.draw_card();
			
			//Dealer sends card to player
			SendCardOperation message = new SendCardOperation(sender, 
					Definitions.DEALER, card);		
		
			out.writeObject(message);
			
		} catch (IOException e) {			
			//System.err.println(e);
			e.printStackTrace();
		}
		
	}

}
