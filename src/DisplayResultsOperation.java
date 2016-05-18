/**
 * Message that is sent to the player. When the player receives this message, 
 * their result is printed to the console along with the dealer's score
 */
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class DisplayResultsOperation extends Message{

	private static final long serialVersionUID = 1L;

	private String result;
	private int player_total;
	private int dealer_total;
	
	//Constructor for the message, stores required result information
	public DisplayResultsOperation(int target, int sender, String result, 
			int player_total, int dealer_total) {
		super(target, sender);
		this.result = result;
		this.player_total = player_total;
		this.dealer_total = dealer_total;
	}
	
	//Method to be executed when the player received this message, prints the 
	//results to the console
	public void execute(ObjectOutputStream out, ObjectInputStream in){
		System.out.printf("You scored: %d\n", player_total);
		System.out.printf("The dealer scored: %d\n", dealer_total);
		System.out.printf("You %s\n", result);
	}
	
	//Method to be executed on the server that logs message information to the 
	//required logs
	public void log(Socket target){
		String status = result + " game";
		
		//Log required information for this message
		Server.game_log.writeToLog(status, target.getInetAddress());
		
		return;
	}
}
