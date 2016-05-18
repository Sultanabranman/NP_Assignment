
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

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
	
	private static final long serialVersionUID = 1L;
	
	private int target;
	
	private int sender;

	public Message(int target, int sender)
	{
		this.target = target;
	}		
	
	//Getter for the message's target
	public int getTarget() {
		return target;
	}

	//Setter for the target of the message
	public void setTarget(int target) {
		this.target = target;
	}	
	
	//Getter for the sender of the message
	public int getSender() {
		return sender;
	}

	//Blank log method to be overridden by subclasses
	public void log(Socket target, Socket sender)
	{
		
	}
	

	// Blank execute method to be overridden by subclasses
	public void execute(ObjectOutputStream out, ObjectInputStream in)
	{
		
	}
}
