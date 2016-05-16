
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class SendCardOperation extends Message{

	private static final long serialVersionUID = 1L;

	private Card card;
	
	public SendCardOperation(int target, int sender, Card card) {
		super(target, sender);
		this.card = card;
	}	
	
	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public void log(Socket target, Socket sender){
		//String to write to log communication log
		String card_sent = "Card sent";
		
		//String to write to game log
		String card_value = card.getName() + " drawn";
		
		//Log required information for this message
		Server.comm_log.writeToLog(card_sent, sender.getInetAddress());
		Server.game_log.writeToLog(card_value, target.getInetAddress());
		
		return;
	}
	
	public void execute(ObjectOutputStream out, ObjectInputStream in){
		
	}

}
