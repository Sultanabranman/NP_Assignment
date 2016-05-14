import java.io.IOException;


public class RequestCardOperation extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RequestCardOperation(int target) {
		super(target);
	}
	
	public void execute() {
		Card card = null;
		
		try {
			//Dealer draws card
			card = Dealer.draw_card();
			
			//Dealer sends card to player
			SendCardOperation message = new SendCardOperation(Definitions.PLAYER
					, card);		
		
			getOutputStream().writeObject(message);
			
		} catch (IOException e) {			
			System.err.println(e);
		}
		
	}

}
