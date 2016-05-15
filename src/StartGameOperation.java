import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class StartGameOperation extends Message{

	private static final long serialVersionUID = 1L;
	
	public StartGameOperation(int target) {
		super(target);
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
	
	public void log()
	{
		
	}
}
