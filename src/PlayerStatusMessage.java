import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class PlayerStatusMessage extends Message{

	private static final long serialVersionUID = 1L;
	
	private int client_num = 0;
	private int player_score = 0;

	public PlayerStatusMessage(int target, int player_score, int client_num) {
		super(target);
		this.player_score = player_score;
		this.client_num = client_num;
		
	}		
	
	public int getClient_num() {
		return client_num;
	}

	public void setClient_num(int client_num) {
		this.client_num = client_num;
	}

	public int getPlayer_score() {
		return player_score;
	}

	public void setPlayer_score(int player_score) {
		this.player_score = player_score;
	}

	public void log() {
		
	}
	
	public void execute(ObjectOutputStream out, ObjectInputStream in) {
		try {
			//Increment the number of players finished
			Server.players_finished++;
			
			//Add player score to the array stored in the server
			Server.player_scores[client_num - 1] = this.player_score;
			
			//If the number of players finished is equal to the number of 
			//clients playing minus the dealer, send the results to the dealer
			if(Server.players_finished == Server.num_clients - 1)
			{
				EvaluateGameOperation totals = new EvaluateGameOperation
						(Definitions.SERVER, Server.player_scores);
				
				//Send message through to dealer to evaluate the game based on 
				//scores sent with message
				out.writeObject(totals);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
