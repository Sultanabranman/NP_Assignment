import java.io.IOException;
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
	private int client_num;
	
	//Array to hold the cards currently held by the dealer
	private Card[] hand = new Card[5];
	
	//Variable containing current number of cards in hand
	private int cards_in_hand = 0;
	
	public Player(ObjectOutputStream toServer, ObjectInputStream fromServer, int client_num)
	{
		this.toServer = toServer;
		this.fromServer = fromServer;
		this.client_num = client_num;
		
		//While Loop
		while(true)
		{	
			try
			{	
				//Initialise hand
				Card.initialise_hand(hand);
				
				//Reset number of cards in hand
				cards_in_hand = 0;
				
				//Get user input to indicate ready status
				String input = get_user_input();
				
				//Indicate ready state
				if(input == "READY")
				{
					PlayerReadyOperation ready = new PlayerReadyOperation
							(Definitions.SERVER, client_num);
					
					toServer.writeObject(ready);
					
					//Wait for cards to be dealt
					request_card();
					request_card();
					
					//Play hand
					play_hand();								
					
					//Wait on game results
				}
				
			}
			catch(IOException e)
			{
				System.err.println(e);
			}			
		}
	}
	
	private void request_card()
	{
		try {		
		
			//Create new request for card
			RequestCardOperation request = new RequestCardOperation
					(Definitions.DEALER);
			
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
				
				//Get player input
				input = get_user_input();
				
				//If draw a card is selected
				if(input == "CARD")
				{
					request_card();
				}				
						
				//If stand selected or player is bust
				if(input == "STAND" || player_is_bust)
				{
					PlayerStatusMessage result = new PlayerStatusMessage
							(Definitions.SERVER, hand_value, client_num);
					
					toServer.writeObject(result);
				}				
			}
			catch(IOException e)
			{
				System.err.println(e);
			}			
		}		
	}
	
	public String get_user_input()
	{
		String input = null;
		
		return input;
	}

}
