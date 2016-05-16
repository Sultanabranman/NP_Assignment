
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ResultsToPlayerOperation extends Message{

	private static final long serialVersionUID = 1L;
	
	private String results[];
	private int dealer_total;
	
	public ResultsToPlayerOperation(int target, int sender, String[] results, 
			int dealer_total) {
		super(target, sender);
		this.results = results;
		this.dealer_total = dealer_total;
	}
	
	public void execute(ObjectOutputStream out, ObjectInputStream in){
		//Send each player their result
		for(int i = 1; i < Server.num_clients; i++)
		{
			//Create new operation to display result on player's end
			DisplayResultsOperation result = new DisplayResultsOperation
					(i, Definitions.SERVER, results[i - 1], 
							Server.player_scores[i - 1], dealer_total);
			
			//Log required information to the server logs
			result.log(Server.clients.get(i).getSocket());
			
			//Send the result to the player
			Server.clients.get(i).write(result);
		}
		
		//Reset game values stored in server to default
		reset_server_values();
	}
	
	public void log(Socket target, Socket sender){
		String request = "Sending results to player";
		String action = "Results sent to players";
		
		//Log required information
		Server.comm_log.writeToLog(request, sender.getInetAddress());
		Server.game_log.writeToLog(action, sender.getInetAddress());
		
		return;
	}
	
	public void reset_server_values()
	{
		//Reset player scores
		Server.player_scores = new int[5];
		Server.players_ready = 0;
		Server.players_finished = 0;
		
		return;
	}

}
