
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class EvaluateGameOperation extends Message{

	private static final long serialVersionUID = 1L;
	
	private int[] player_scores = new int[5];
	
	
	public EvaluateGameOperation(int target, int sender, int[] player_scores) {
		super(target, sender);
		this.player_scores = player_scores;	
	}
	
	public void execute(ObjectOutputStream out, ObjectInputStream in)
	{
		//Set players playin flag to false indicating that dealer needs to play
		Dealer.players_playing = false;
		
		//Dealer plays through hand
		Dealer.play_hand();
		
		//Evaluate the results by comparing dealer hand to player hands
		String[] results = Dealer.evaluate_cards(Dealer.hand, player_scores);
		
		//Create new results to player message
		ResultsToPlayerOperation message = new ResultsToPlayerOperation
				(Definitions.SERVER, Definitions.DEALER, results, 
						Card.hand_value(Dealer.hand));
		
		try {
			//Send message to the server
			out.writeObject(message);
			
			//Set flag to indicate that the game is finished
			Dealer.game_finished = true;
		} 
		catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public void log(Socket target)
	{		
		String eval = "Evaluating cards";
		
		//Log required information for this message
		Server.game_log.writeToLog(eval, target.getInetAddress());
		
		return;
	}
}
