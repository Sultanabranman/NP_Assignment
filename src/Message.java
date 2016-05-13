import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	
	private static final long serialVersionUID = 1L;
	
	private int target;
	private ObjectInputStream inputStream;	
	private ObjectOutputStream outputStream; 
	
	public Message(int target)
	{
		this.target = target;
	}		
	
	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}
	
	public ObjectInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(ObjectInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(ObjectOutputStream outputStream) {
		this.outputStream = outputStream;
	}
	

	// Blank execute method to be overridden by subclasses
	public void execute()
	{
		
	}
}
