/**
 * Message sent from the Server to all connected clients indicating to start the
 * game. Sets the flags in each client to start the game
 */
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class StartGameOperation extends Message{

	private static final long serialVersionUID = 1L;
	
	//Constructor for the message
	public StartGameOperation(int target, int sender) {
		super(target, sender);
	}
	
	//Method to be executed when the message is received. Sets the start game 
	//flags in the dealer or player
	public void execute(ObjectOutputStream out, ObjectInputStream in)
	{
		if(getTarget() == Definitions.DEALER)
		{
			;
		}
		else
		{
			Player.game_started = true;
		}		
	}
	
	//Method to be executed on the server to log required information
	public void log(Socket target)
	{
		String action = "Game started";
		
		//Log required information
		Server.game_log.writeToLog(action, target.getInetAddress());
	}
}
