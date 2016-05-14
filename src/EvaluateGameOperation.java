
public class EvaluateGameOperation extends Message{

	private static final long serialVersionUID = 1L;
	
	private int[] player_scores = new int[5];
	
	public EvaluateGameOperation(int target, int[] player_scores) {
		super(target);
		this.player_scores = player_scores;
		
	}
	
	public void execute()
	{
		
	}
	
	public void log()
	{
		
	}

	
	
	
	

}
