import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

/**
 * Manages all functions related to the Dealer role. The dealer role is 
 * assigned automatically.
 * 
 * The dealer is autonomous but is required for a game to take place.
 * 
 * The dealer has a dual role of dealing cards to the players and playing the 
 * game to determine the outcome.
 * 
 * At the beginning of the game the dealer automatically deals 2 cards to all 
 * players including itself.
 * 
 * When requested, the dealer deals cards to the player that requested the card.
 * 
 * When all players are finished, the dealer automatically draws cards until the
 * cards in their hand total over 17.
 * 
 * The dealer then evaluates the state of the game and determine which players 
 * win, lose of tie based on the comparison between the dealer's and player's 
 * hand.
 * 
 * @author Chris
 *
 */
public class Dealer {

	private ObjectOutputStream toServer; 
	private ObjectInputStream fromServer; 
	protected static int start_game = Definitions.NO;
	private int client_num;
	//private Socket socket;
	
	//Array to hold the cards currently held by the dealer
	private Card[] hand = new Card[5];
	
	//Variable containing current number of cards in hand
	private int cards_in_hand = 0;	
	
	public Dealer(ObjectOutputStream toServer, ObjectInputStream fromServer, 
			int client_num)
	{
		//Store passed in data in this object
		this.toServer = toServer;
		this.fromServer = fromServer;
		this.client_num = client_num;
		
		System.out.println("Dealer role assigned");
		
		//Main loop for dealer
		while(true)
		{
			try
			{				
				//Wait for players ready command from server.
				while(start_game == Definitions.NO)
				{
					//Retrieve any messages from the server
					Message message = (Message) fromServer.readObject();
					
					message.execute(toServer, fromServer);
					
					//If players ready is communicated from server, start the 
					//game 
					if(start_game == Definitions.YES)
					{
						System.out.println("Game started");
						//When players are ready, start the game
						start_game();
					}
				}					
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}		
	}
	
	//Draw a card. Generates a random number and assigns name and value based on
	//generated value
	protected static Card draw_card()
	{		
		//Generate a random number to use to set card name and value
		int value = generate_random_value();
		
		//Generate card based on generated value
		Card generated_card = new Card(value);	
		
		//Return randomly generated card
		return generated_card;
	}
	
	//Method to generate a random number in range 0-12. This value is used to 
	//determine card information
	private static int generate_random_value()
	{
		//Create random number generator
		Random randomGenerator = new Random();
		
		//Generate random int in range 0-13
		int randomInt = randomGenerator.nextInt(14);
		
		//Return generated int
		return randomInt;
	}
	
	private void start_game()
	{
		//Reset dealers hand at start of the game to be blank
		Card.initialise_hand(hand);
		
		//Deal two cards to dealer
		for(int i = 0; i < 2; i++)
		{
			//Draw card for hand
			hand[i] = draw_card();
			cards_in_hand++;
		}			
		
		//Flag to indicate that a game is currently running
		boolean game_finished = false;
		
		while(!game_finished)
		{
			serve_players();				
			
			//If the player stands, dealer plays their hand
			play_hand();				
			
			//Dealer evaluates cards
			//evaluate_cards();
			
			//Dealer communicates results to the server.
			communicate_results();
		}				
		
	}
	private void serve_players()
	{		
		try
		{
			//Flag to indicate players are still able to draw cards
			boolean players_playing = true;
			
			//While the players have not gone bust or chosen to stand
			while(players_playing)
			{	
				//Retrieve messages from server
				Message message = (Message) fromServer.readObject();
				
				HandleRequest request = new HandleRequest(message);
				
				new Thread(request).start();
			}			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return;
	}
	
	//Thread to manage any requests received from the server. New threads are 
	//started so that more than one request can be dealt with at a time
	class HandleRequest implements Runnable
	{
		private Message message;
		public HandleRequest(Message message)
		{
			this.message = message;
		}
		
		public synchronized void run()
		{					
			message.execute(toServer, fromServer);			
		}
	}
	
	//Automatically plays hand until hand value totals over 17 or 5 cards are
	//held.
	private void play_hand()
	{
		//Variable containing current hand's value.
		int hand_value = Card.hand_value(hand);
		
		//While the dealer's hand value is under 17, and the dealer isn't 
		//holding 5 cards
		while((hand_value <= 17) && (cards_in_hand != 5))
		{			
			//If the dealer's hand totals under 17, the dealer draws a card
			hand[cards_in_hand - 1] = draw_card();			
			
			//Get the current value of cards in hand
			hand_value = Card.hand_value(hand);			
		}
		
		//If the dealer's hand totals over 17, the dealer stands. Return		
		return;			
	}
	
	//Determine the winner by comparing each player's hand total to the dealer's
	protected void evaluate_cards(int dealer_total, int[] player_totals)
	{
		int MAX_HAND_VALUE = 21;
		
		for(int i = 0; i < player_totals.length; i++)
		{
			if(player_totals[i] > MAX_HAND_VALUE)
			{
				//Dealer wins
			}
			else if(dealer_total > MAX_HAND_VALUE)
			{
				//Player wins
			}
			else if(dealer_total == player_totals[i])
			{
				//Draw
			}
			else
			{
				//Dealer_wins
			}
		}
		
		return;
	}
	
	private void communicate_results()
	{
		return;
	}
}
