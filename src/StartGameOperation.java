
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class StartGameOperation extends Message{

	private static final long serialVersionUID = 1L;
	
	public StartGameOperation(int target, int sender) {
		super(target, sender);
	}
	
	public void execute(ObjectOutputStream out, ObjectInputStream in)
	{
		if(getTarget() == Definitions.DEALER)
		{
			Dealer.start_game = Definitions.YES;
		}
		else
		{
			Player.game_started = true;
		}		
	}
	
	public void log(Socket target)
	{
		String action = "Game started";
		
		//Log required information
		Server.game_log.writeToLog(action, target.getInetAddress());
	}
}
