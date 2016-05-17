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
	
	public final static int MAX_HAND_VALUE = 21;
	public final static int MAX_CLIENTS = 2;
	public Definitions(){
		
	}
}
