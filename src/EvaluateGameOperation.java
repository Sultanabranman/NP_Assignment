import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class EvaluateGameOperation extends Message{

	private static final long serialVersionUID = 1L;
	
	private int[] player_scores = new int[5];
	
	
	public EvaluateGameOperation(int target, int[] player_scores) {
		super(target);
		this.player_scores = player_scores;	
	}
	
	public void execute(ObjectOutputStream out, ObjectInputStream in)
	{
		Dealer.players_playing = false;
		Dealer.play_hand();
		
		String[] results = Dealer.evaluate_cards(Dealer.hand, player_scores);
		
		ResultsToPlayerOperation message = new ResultsToPlayerOperation
				(Definitions.SERVER, results, Card.hand_value(Dealer.hand));
		
		try {
			out.writeObject(message);
			
			Dealer.game_finished = true;
		} 
		catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public void log()
	{
		
	}

	
	
	
	

}
