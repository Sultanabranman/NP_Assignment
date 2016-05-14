import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PlayerReadyOperation extends Message{
	
	private static final long serialVersionUID = 1L;

	private int ready_status;
	
	public PlayerReadyOperation(int target) {
		super(target);
		
	}	
	
	public void log() {
		
	}
	
	public void execute() {
		
	}

}
