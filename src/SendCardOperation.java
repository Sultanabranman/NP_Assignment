
public class SendCardOperation extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Card card;
	
	public SendCardOperation(int target, Card card) {
		super(target);
		this.card = card;
	}	
	
	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public void log(){
		
	}
	
	public void execute(){
		
	}

}
