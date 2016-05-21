import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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

	/**************************************************************************/
	/*                               VARIABLES                                */
	/**************************************************************************/
	protected static ObjectOutputStream toServer; 
	private ObjectInputStream fromServer; 
	
	//Variable containing current number of cards in hand
	private static int cards_in_hand = 0;	
	
	private ServerSocket serverSocket;
	
	/**************************************************************************/
	/*                               	CODE                                  */
	/**************************************************************************/
	
	//Constructor for the Dealer class
	public Dealer(ObjectOutputStream toServer, ObjectInputStream fromServer, 
			int client_num)
	{
		//Store passed in data in this object
		this.toServer = toServer;
		this.fromServer = fromServer;
		
		System.out.println("Dealer role assigned");
		
		//Create a thread to handle any communication on main server 
		//communication streams
		ManageMainThread manage = new ManageMainThread(toServer, fromServer);
		
		//Start the newly created thread
		new Thread(manage).start();
		
		//Open server socket on port defined in the definitions class
		try 
		{
			serverSocket = new ServerSocket(Definitions.port);
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		
		//Main loop for dealer
		while(true)
		{
			try
			{				
					System.out.println("Awaiting new player connection");
					
					//Wait for a player to connect to the server which will then
					//open a new connection to the dealer
					Socket socket = serverSocket.accept();		
					
					System.out.println("New client connected");
					
					//Create a new thread to handle any messages received from 
					//this socket connection
					ManageRequests task = new ManageRequests(socket);
					
					//Start the newly created thread
					new Thread(task).start();
			}
			
			catch(IOException e)
			{
				System.out.println("Server shutdown");
				System.out.println("Closing");
				
				System.exit(1);
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
	
	class ManageMainThread implements Runnable
	{
		private ObjectInputStream in;
		private ObjectOutputStream out;
		
		//Constructor for thread, requires input and output streams to the 
		//server to be passed in
		public ManageMainThread(ObjectOutputStream out, ObjectInputStream in)
		{
			this.out = out;
			this.in = in;
		}
		
		public synchronized void run()
		{
			try
			{
				while(true)
				{
					//Wait for message to be received
					Message message = (Message) in.readObject();
					
					//Execute the method contained within the message
					message.execute(out, in);
				}				
			}
			catch(IOException | ClassNotFoundException e)
			{
				System.out.println("Server shutdown");
				System.out.println("Closing");
				
				System.exit(1);
			}			
		}
	}
	
	class ManageRequests implements Runnable
	{
		//Variable to store the passed in socket
		private Socket socket;
		
		//Variable to store the input/output streams to the server
		private ObjectInputStream fromClient;
		private ObjectOutputStream toClient;
		
		//Constructor for thread, requires a socket to be passed in to allow new
		//input and output streams to be created
		public ManageRequests(Socket socket)
		{
			this.socket = socket;
		}
		
		public synchronized void run()
		{
			try
			{
				//Create object input/output streams to allow messages to be 
				//sent and received
				toClient = new ObjectOutputStream(socket.getOutputStream());
				
				//Clean any data out of output stream
				toClient.flush();
				fromClient = new ObjectInputStream(socket.getInputStream());					
				
				while(true)
				{
					//Wait for message to be received
					Message message = (Message) fromClient.readObject();
					
					//Execute the method contained within the message
					message.execute(toClient, fromClient);
				}
				
			}
			catch(IOException | ClassNotFoundException e)
			{
				System.out.println("Server shutdown");
				System.out.println("Closing");
				
				System.exit(1);
			}
			
		}
	}
	
	//Thread to manage any requests received from the server. New threads are 
	//started so that more than one request can be dealt with at a time
	class HandleRequest implements Runnable
	{
		private Message message;
		
		//Constructor for request thread, takes a received message as a 
		//parameter
		public HandleRequest(Message message)
		{
			this.message = message;
		}
		
		public synchronized void run()
		{			
			//Execute the method contained within the message
			message.execute(toServer, fromServer);			
		}
	}
	
	//Automatically plays hand until hand value totals over 17 or 5 cards are
	//held.
	protected static Card[] play_hand()
	{
		//Array to hold the cards currently held by the dealer
		Card[] hand = new Card[5];
		
		//Reset dealers hand at start of the game to be blank
		Card.initialise_hand(hand);
		
		//Reset cards in hand to be 0
		cards_in_hand = 0;
		
		//Deal two cards to dealer
		for(int i = 0; i < 2; i++)
		{
			//Draw card for hand
			hand[i] = draw_card();
			cards_in_hand++;
		}
		
		//Variable containing current hand's value.
		int hand_value = Card.hand_value(hand);
		
		//While the dealer's hand value is under 17, and the dealer isn't 
		//holding 5 cards
		while((hand_value < 17) && (cards_in_hand != hand.length))
		{			
			//If the dealer's hand totals under 17, the dealer draws a card
			hand[cards_in_hand] = draw_card();		
			
			//Increment the number of cards in the dealer's hand
			cards_in_hand++;
			
			//Get the current value of cards in hand
			hand_value = Card.hand_value(hand);			
		}
		
		//If the dealer's hand totals over 17, the dealer stands. Return		
		return hand;			
	}
	
	//Determine the winner by comparing each player's hand total to the dealer's
	//Returns strings with the player's win status
	protected static String[] evaluate_cards
		(Card dealer_hand[], int[] player_totals)
	{
		//Array to be filled with player's win status and returned to calling 
		//function
		String results[] = new String[player_totals.length];
		
		int dealer_total = Card.hand_value(dealer_hand);
		
		//Loop through all player results passed in
		for(int i = 0; i < player_totals.length; i++)
		{
			//If the player total is not completed, there are no more players 
			//to evaluate, set all subsequent results to null
			if(player_totals[i] == 0)
			{
				results[i] = null;
				continue;
				
			}
			//If the player went bust, player loses
			if(player_totals[i] > Definitions.MAX_HAND_VALUE)
			{
				results[i] = "Lost";
			}
			//If the dealer went bust, player wins
			else if(dealer_total > Definitions.MAX_HAND_VALUE)
			{
				results[i] = "Won";
			}
			//If the player's score was greater then the dealer's, player wins
			else if(player_totals[i] > dealer_total)
			{
				results[i] = "Won";
			}
			//If the player's total equaled the dealer's total
			else if(dealer_total == player_totals[i])
			{
				results[i] = "Drew";
			}
			//If the player's total was less then the dealer's
			else
			{
				results[i] = "Lost";
			}
		}
					
		return results;
	}
}
