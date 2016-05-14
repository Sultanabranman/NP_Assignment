
public class StartGameOperation extends Message{

	private static final long serialVersionUID = 1L;
	
	public StartGameOperation(int target) {
		super(target);
	}
	
	public void execute()
	{
		Dealer.start_game = Definitions.YES;
	}
	
	public void log()
	{
		
	}
}
