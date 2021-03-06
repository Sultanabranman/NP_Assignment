/** 
 * Message sent by player to communicate their final score for the game in 
 * progress. If all players are finished, a request is sent to the dealer to 
 * evaluate the game.
 */
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class PlayerStatusMessage extends Message{

	private static final long serialVersionUID = 1L;
	
	private int sender = 0;
	private int player_score = 0;

	//Constructor for the message, requires the player's score
	public PlayerStatusMessage(int target, int sender, int player_score) {
		super(target, sender);
		this.player_score = player_score;
		this.sender = sender;
		
	}		
	
	//Getter for the player's client number
	public int getClient_num() {
		return sender;
	}

	//Setter for the player's client number within this message
	public void setClient_num(int client_num) {
		this.sender = client_num;
	}

	//Getter for the player's score
	public int getPlayer_score() {
		return player_score;
	}

	//Setter for the player's score
	public void setPlayer_score(int player_score) {
		this.player_score = player_score;
	}

	//Method to be executed on the server to log required information
	public void log(Socket target, Socket sender) {
		String request = "Score sent";
		String action = "Player scored " + player_score;
		
		//Log required information for this message
		Server.game_log.writeToLog(request, sender.getInetAddress());
		Server.game_log.writeToLog(action, sender.getInetAddress());
		
		return;
	}
	
	//Method to be executed when the message is received. Indicates to the 
	//server that the player is finished, and if all players are finished, 
	//sends a message to the dealer to evaluate the game
	public void execute(ObjectOutputStream out, ObjectInputStream in) {
		
		//Increment the number of players finished
		Server.players_finished++;
		
		//Add player score to the array stored in the server
		Server.player_scores[sender - 1] = this.player_score;
		
		//If the number of players finished is equal to the number of 
		//clients playing minus the dealer, send the results to the dealer
		if(Server.players_finished == Server.num_clients - 1)
		{
			//Create new evaluate game message
			EvaluateGameOperation totals = new EvaluateGameOperation
					(Definitions.DEALER, Definitions.SERVER, 
							Server.player_scores);
			
			//Log the required information on the server logs
			totals.log(Server.clients.get(Definitions.DEALER).getSocket());
			
			//Send message through to dealer to evaluate the game based on 
			//scores sent with message
			Server.clients.get(Definitions.dealer_slot).write(totals);
		}		
	}
}
