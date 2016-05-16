import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class ResultsToPlayerOperation extends Message{

	private static final long serialVersionUID = 1L;
	
	private String results[];
	private int dealer_total;
	
	public ResultsToPlayerOperation(int target, String[] results, 
			int dealer_total) {
		super(target);	
		this.results = results;
		this.dealer_total = dealer_total;
	}
	
	public void execute(ObjectOutputStream out, ObjectInputStream in){
		//Send each player their result
		for(int i = 1; i < Server.num_clients; i++)
		{
			//Create new operation to display result on player's end
			DisplayResultsOperation result = new DisplayResultsOperation
					(i, results[i - 1], Server.player_scores[i - 1], 
							dealer_total);
			
			//Send the result to the player
			Server.clients.get(i).write(result);
		}
	}
	
	public void log(){
		
	}

}
