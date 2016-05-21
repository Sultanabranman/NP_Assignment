/**
 * Definitions class holds any constant definitions that are used by all classes
 * Serves no purpose other than to store definitions
 */
import java.io.Serializable;


public class Definitions implements Serializable{
	private static final long serialVersionUID = 1L;
	public final static int SERVER = -1;
	public final static int DEALER = 0;
	public final static int PLAYER = 1;
	public final static int NO = 0;
	public final static int YES = 1;
	//Client slot on server reserved for dealer
	public final static int dealer_slot = 0;
	public final static int port = 24875;
	public final static String dealer_server = "m1-c25n1.csit.rmit.edu.au";
	
	public final static int MAX_HAND_VALUE = 21;
	public final static int MAX_CLIENTS = 6;
	public Definitions(){
		
	}
}
