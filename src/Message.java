import java.io.Serializable;

/** 
 * Message class is used for communication between Player, Dealer, and Server 
 * classes.
 * 
 * When a class needs to send messages out, it creates a message object with the
 * required information and sends it through the ObjectOutputStream
 * 
 * @author Chris
 *
 */
public class Message implements Serializable{
	private String target;
	//Constructor for Message object
	public Message(String target)
	{
		this.target = target;
	}
	
	
	
	public String getTarget() {
		return target;
	}



	public void setTarget(String target) {
		this.target = target;
	}



	// Blank execute method to be overridden by subclasses
	public void execute()
	{
		
	}
}
