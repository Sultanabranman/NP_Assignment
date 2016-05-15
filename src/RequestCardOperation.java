import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class RequestCardOperation extends Message{

	private static final long serialVersionUID = 1L;
	private int client_num;
	
	
	public RequestCardOperation(int target, int client_num) {
		super(target);
		this.client_num = client_num;
	}
	
	public void log() {
		
	}
	
	public void execute(ObjectOutputStream out, ObjectInputStream in) {
		Card card = null;
		
		try {
			//Dealer draws card
			card = Dealer.draw_card();
			
			//Dealer sends card to player
			SendCardOperation message = new SendCardOperation(client_num
					, card);		
		
			out.writeObject(message);
			
		} catch (IOException e) {			
			//System.err.println(e);
			e.printStackTrace();
		}
		
	}

}
