import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Manages all functions related to the player. The player role is assigned by 
 * the server. 
 * 
 * Players have the role of playing the game. They are automatically dealt cards
 * at the beginning of the game. They then have the options of drawing another 
 * card or standing. 
 * 
 * If the player's cards value total over 21 they are considered to have gone 
 * bust and lost the game. 
 * 
 * The player can keep drawing cards until they either stand or go bust.
 * 
 * The results of the game are automatically supplied to the player.
 * 
 * @author Chris
 *
 */
public class Player {
	
	private ObjectOutputStream toServer; 
	private ObjectInputStream fromServer; 
	
	protected static boolean game_started = false;
	
	private int client_num;
	//Open input stream reader
	private InputStreamReader is = new InputStreamReader(System.in);
	
	//Open buffered reader 
	private BufferedReader buf_in = new BufferedReader(is);	
	
	//Array to hold the cards currently held by the dealer
	private Card[] hand = new Card[5];
	
	//Variable containing current number of cards in hand
	private int cards_in_hand = 0;
	
	public Player(ObjectOutputStream toServer, ObjectInputStream fromServer, 
			int client_num)
	{
		this.toServer = toServer;
		this.fromServer = fromServer;
		this.client_num = client_num;
		
		System.out.println("Player role Assigned");
		
		//While Loop
		while(true)
		{	
			try
			{	
				//Initialise hand
				Card.initialise_hand(hand);
				
				//Reset number of cards in hand
				cards_in_hand = 0;
				
				System.out.println("Please enter 'Ready' to indicate sign up "
						+ "for next game");
				
				//Get user input to indicate ready status
				String input = get_user_input();				
				
				//Indicate ready state
				if(input.equals("READY"))
				{
					//Create new ready message to send to server
					PlayerReadyOperation ready = new PlayerReadyOperation
							(Definitions.SERVER);
					
					//Send ready message to server
					toServer.writeObject(ready);
					
					//NOTE: PUT WAIT HERE
					while(game_started == false)
					{
						Message start_game = (Message) fromServer.readObject();
						start_game.execute(toServer, fromServer);
					}
					
					System.out.println("Game started");
					
					//Wait for cards to be dealt
					request_card();
					request_card();
					
					//Play hand
					play_hand();								
					
					//Wait on game results
				}
				else
				{
					System.out.println("Invalid input entered");
				}				
			}
			catch(IOException e)
			{
				System.err.println(e);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}			
		}
	}
	
	private void request_card()
	{
		try {		
		
			//Create new request for card
			RequestCardOperation request = new RequestCardOperation
					(Definitions.DEALER, client_num);
			
			//Send request to server
			toServer.writeObject(request);
			
			//Receive message from the dealer containing the card
			SendCardOperation receiveCard = (SendCardOperation) 
					fromServer.readObject();
			
			//Add received card to hand
			hand[cards_in_hand] = receiveCard.getCard();
			
			//Increment cards in hand
			cards_in_hand++;
		} 
		catch (IOException | ClassNotFoundException e) {			
			System.err.println(e);
		}
		
		return;
	}
	
	//Method to play hand until player either goes bust, chooses to stand, or 
	//has 5 cards
	private void play_hand()
	{
		//Flag to indicate if player's hand has gone over 21
		boolean player_is_bust = false;
		boolean stand = false;
		
		//Variable containing current hand's value.
		int hand_value = Card.hand_value(hand);
		
		//Variable containing user's input
		String input = null;
		
		while(true)
		{
			try
			{				
				//When cards are received, player has option of drawing another
				//card or standing 
				while(stand == false || player_is_bust == false || 
						cards_in_hand != 5)
				{
					Card.display_hand(hand);
					
					//Print out hand total
					System.out.printf("Current hand total: %d\n", 
							Card.hand_value(hand));
					
					System.out.println("Please select one of the following "
							+ "options");
					System.out.println("1. Hit");
					System.out.println("2. Stand");
					
					//Get player input
					input = get_user_input();
					
					//If draw a card is selected, send a request to the dealer 
					//to draw a card
					if(Integer.parseInt(input) == 1)
					{
						request_card();
					}
					//If stand is selected, set flag to indicate so
					else if(Integer.parseInt(input) == 2)
					{
						stand = true;
					}
					//If an invalid option is selected, print error message
					else
					{
						System.out.println("Invalid option selcted");
						System.out.println();
					}
				}								
						
				//If stand selected, player is bust, or the player has 5 cards, 
				//pass hand total to the server				
				PlayerStatusMessage result = new PlayerStatusMessage
						(Definitions.SERVER, hand_value, client_num);
				
				toServer.writeObject(result);								
			}
			catch(IOException e)
			{
				System.err.println(e);
				//Close input readers
				try {
					buf_in.close();
					is.close();
				} catch (IOException ex) {
					e.printStackTrace();
				}
			}			
		}		
	}
	
	//Method to obtain user input using buffered input reader
	public String get_user_input()
	{
		String input = null;		
		
		try {		
			input = buf_in.readLine();
			
			//Convert all input characters to uppercase
			input = input.toUpperCase();						
		} 
		catch (IOException e) {
			System.err.println(e);
		}		
		
		return input;
	}

}
