/**
 * Message that is sent to the Dealer to request that the results of the game be
 * evaluated. Dealer compares the dealer hand to each player's hand and 
 * generates the win status based on the results. The dealer then sends the 
 * results over to the server for distribution.
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class EvaluateGameOperation extends Message{

	private static final long serialVersionUID = 1L;
	
	private int[] player_scores = new int[5];
	
	//Constructor for the message, requires the player scores to evaluate the 
	//game
	public EvaluateGameOperation(int target, int sender, int[] player_scores) {
		super(target, sender);
		this.player_scores = player_scores;	
	}
	
	//Method to be executed when the message is received, resets required dealer
	//game flags and evaluates the game
	public void execute(ObjectOutputStream out, ObjectInputStream in)
	{
		//Array to hold the cards currently held by the dealer
		Card[] hand = new Card[5];
		
		//Dealer plays through hand
		hand = Dealer.play_hand();
		
		//Evaluate the results by comparing dealer hand to player hands
		String[] results = Dealer.evaluate_cards(hand, player_scores);
		
		//Create new results to player message
		ResultsToPlayerOperation message = new ResultsToPlayerOperation
				(Definitions.SERVER, Definitions.DEALER, results, 
						Card.hand_value(hand));
		
		try {
			//Send message to the server
			Dealer.toServer.writeObject(message);
		} 
		catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	//Method to be run when message is received by the server, logs required 
	//information to appropriate logs
	public void log(Socket target)
	{		
		String eval = "Evaluating cards";
		
		//Log required information for this message
		Server.game_log.writeToLog(eval, target.getInetAddress());
		
		return;
	}
}
