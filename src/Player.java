import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

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
	
	//Variable to store input and output streams to the server
	private ObjectOutputStream toServer; 
	private ObjectInputStream fromServer; 
	
	//Variable indicating if the game has started, set to true by message 
	//received from the server
	protected static boolean game_started = false;
	
	//Variable storing this client's client numbers, used to identify client on
	//server's end
	private int client_num;
	
	//Array to hold the cards currently held by the dealer
	private Card[] hand = new Card[5];
	
	//Variable containing current number of cards in hand
	private int cards_in_hand = 0;
	
	//Open input stream reader
	private InputStreamReader is = new InputStreamReader(System.in);
	
	//Open buffered reader 
	private BufferedReader buf_in = new BufferedReader(is);	
	
	//Variable to store socket connection to the server
	private Socket socket;
	
	//Constructor for the player class, takes input and output streams set up 
	//when client was created as parameters. Also requires client identification
	//number 
	public Player(ObjectOutputStream toServer, ObjectInputStream fromServer, 
			int client_num, Socket socket)
	{
		//Store passed in variables to allow class methods access to them
		this.toServer = toServer;
		this.fromServer = fromServer;
		this.client_num = client_num;
		this.socket = socket;
		
		//Identify that the player role has been assigned
		System.out.println("Player role Assigned");
		
		try {
			//Set timeout to ten seconds when waiting for input from the server
			socket.setSoTimeout(10000);
		} 
		catch (SocketException e) {
			e.printStackTrace();
		}
		
		//Start the main player management method
		while(true)
		{
			manage_player();
		}			
	}
	
	//Main loop for the player class, manages all functions required of the 
	//class. This function isn't left until client quits
	private void manage_player()
	{	
		//Initialise_player
		initialise_player();	
		
		//Prompt player for input if they wish to join the game, player can
		//also exit within this method
		get_player_ready_status();		
		
		//Sends the player's ready status to the server for processing
		send_ready_status();	
		
		//Wait for the game to start
		wait_for_start();					
		
		//When game has started, request initial hand (2 cards)
		request_starting_hand();
		
		//Play through hand until player is either bust, has chosen to 
		//stand, or has 5 cards in hand
		play_hand();								
		
		//Get game results from the server and notify the player
		get_game_results();		
		
		//Reset the game started flag
		game_started = false;
	}
	
	//Method to reset all variables back to default state when starting a 
	//new game
	private void initialise_player()
	{
		//Initialise hand
		Card.initialise_hand(hand);
		
		//Reset number of cards in hand
		cards_in_hand = 0;
		
		return;
	}
	
	//Method to prompt player to either join or leave the game, return only if 
	//the player selects ready
	private void get_player_ready_status()
	{
		//Variable to store player input
		String input = null;
		
		//Flag indicating if the input acquired from the user was valid
		boolean input_valid = false;
		
		//Loop until a valid input is received
		while(input_valid == false)
		{
			//Display menu options
			display_ready_options();
			
			//Get user input to indicate ready status
			input = get_user_input();
			
			//If input equal to 1 or 2, leave input as is
			if((input.equals("1")) || (input.equals("2"))) 
			{
				;
			}
			//Else set input to 0
			else
			{
				input = "0";
			}			
			
			//Convert input into an integer
			int input_num = Integer.parseInt(input);
			
			//Perform function based on user input
			switch (input_num)
			{
				//User selects ready
				case 1: 
				{
					//Indicate that the input was valid
					input_valid = true;
					break;
				}
				//User selects to exit the game
				case 2:
				{
					try {
						//Indicate that the input was valid
						input_valid = true;					
					
						toServer.close();
						fromServer.close();
						socket.close();
						buf_in.close();
						is.close();
					} 
					catch (IOException e) {						
						e.printStackTrace();
					}					
					
					//Close the client
					System.exit(1);
					
					break;
				}
				default:
				{
					System.out.println("Invalid input entered");
					break;
				}
			}
		}		
		
		return;
	}
	
	//Method to display options available to player when getting ready status
	private void display_ready_options()
	{
		//Display options for player to sign up or leave
		System.out.println("Please select one of the following options: ");
		System.out.println("1. Sign up for game");
		System.out.println("2. Exit");
		
		return;
	}
	
	//This method sends a message to the server indicating that the player 
	//wishes to sign up
	private void send_ready_status()
	{
		try
		{
			//Create new ready message to send to server
			PlayerReadyOperation ready = new PlayerReadyOperation
					(Definitions.SERVER, client_num);
			
			//Send ready message to server
			toServer.writeObject(ready);
		}
		catch(SocketException e)
		{
			System.out.println("Server shutdown");
			System.out.println("Closing");
			
			System.exit(1);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		
		return;			
	}
	
	//Method that delays player method until the game started command is 
	//received from the server
	private void wait_for_start()
	{
		System.out.println("Waiting for game to start");
		
		//Waiting for game to start
		while(game_started == false)
		{
			//Message object to store any received messages from the server
			Message start_game = null;
			
			try 
			{
				//Read in any messages received from the server
				start_game = (Message) fromServer.readObject();
				//Execute the method contained within the message
				start_game.execute(toServer, fromServer);
			} 
			catch(SocketException e)
			{
				System.out.println("Server shutdown");
				System.out.println("Closing");
				
				System.exit(1);
			}
			catch (ClassNotFoundException e) 
			{				
				e.printStackTrace();
			} 
			catch (IOException e) 
			{				
				e.printStackTrace();
			}			
		}	
		
		//Print to the console that the game has started
		System.out.println("Game started");
		
		//Return when a start game command is received
		return;
	}
	
	//Method to request 2 cards from dealer to obtain starting hand, adds 2 
	//cards to hand
	private void request_starting_hand()
	{
		request_card();
		request_card();
		
		return;
	}
	
	private void request_card()
	{
		boolean sent = false;
		
		try {				
			//Create new request for card
			RequestCardOperation request = new RequestCardOperation
					(Definitions.DEALER, client_num);			
			while(sent != true)
			{			
				try{
					//Send request to server
					toServer.writeObject(request);
					
					//Receive message from the dealer containing the card
					SendCardOperation receiveCard = (SendCardOperation) 
							fromServer.readObject();
					
					//Add received card to hand
					hand[cards_in_hand] = receiveCard.getCard();
					
					sent = true;
				}
				catch(SocketException e)
				{
					//Creates input and output streams from client to server
					toServer = new ObjectOutputStream(socket.getOutputStream());			
					fromServer = new ObjectInputStream(socket.getInputStream());
				}				
			}
			
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
		
		//Flag to indicate if the player has chosen to stand
		boolean stand = false;
				
		//Variable containing user's input
		String input = null;		
					
		//When cards are received, player has option of drawing another
		//card or standing 
		while((stand == false) && (player_is_bust == false) && 
				(cards_in_hand != 5))
		{	
			//Prints the player's current hand information to the console
			display_current_state();			
			
			//Print the user's options to the console
			display_hand_options();			
			
			input = null;
			
			//Get player input
			input = get_user_input();
			
			//If input equal to 1 or 2, leave input as is
			if((input.equals("1")) || (input.equals("2"))) 
			{
				;
			}
			//Else set input to 0
			else
			{
				input = "0";
			}
			
			//Convert player input to an integer
			int input_num = Integer.parseInt(input);
			
			switch(input_num)
			{
				//If draw a card is selected, send a request to the dealer 
				//to draw a card
				case 1:
				{
					request_card();
					
					//Check to see if player is bust
					if(Card.hand_value(hand) > 21)
					{
						//Set flag to indicate player is bust
						player_is_bust = true;
					}
										
					break;
				}
				//If stand is selected, set flag to indicate so
				case 2:
				{
					stand = true;
					break;
				}
				default:
				{
					System.out.println("Invalid option selcted");
					System.out.println();
					break;
				}
			}
		}
										
		try
		{	
			//Prints the player's current hand information to the console
			display_current_state();			
			
			//If stand selected, player is bust, or the player has 5 cards, 
			//pass hand total to the server				
			PlayerStatusMessage result = new PlayerStatusMessage
					(Definitions.SERVER, client_num, Card.hand_value(hand));
			
			toServer.writeObject(result);	
			
			return;
		}
		catch(SocketException e)
		{
			System.out.println("Server shutdown");
			System.out.println("Closing");
			
			System.exit(1);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}		
	}

	//Displays the player's options when a game is in progress 
	private void display_hand_options()
	{
		System.out.println("Please select one of the following "
				+ "options");
		System.out.println("1. Hit");
		System.out.println("2. Stand");
		
		return;
	}
	
	//Prints the player's current hand information to the console
	private void display_current_state()
	{
		//Print the player's current hand to the console
		Card.display_hand(hand);
		
		//Print current hand's total to the console
		System.out.printf("Current hand total: %d\n", 
				Card.hand_value(hand));
		
		//Print blank line for formatting
		System.out.println();
		
		return;
	}
	
	//This methods receives the game's results from the server and notifies 
	//the player
	private void get_game_results()
	{
		try
		{		
			//Wait on game results
			Message status = (Message) fromServer.readObject();
			
			//Execute message received from server
			status.execute(toServer, fromServer);
		}
		catch(SocketException e)
		{
			System.out.println("Server shutdown");
			System.out.println("Closing");
			
			System.exit(1);
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
	
	//Method to obtain user input using buffered input reader
	public String get_user_input()
	{
		String input = null;		
		
		try {		
			input = buf_in.readLine();
			
			//Convert all input characters to uppercase
			input = input.toUpperCase();
			
			//Set input to 0 if input was blank for when parse int is called or
			//else an exception will be thrown
			if(input == null || input.equals(""))
			{
				input = "0";
			}
		} 
		catch (IOException e) {
			System.err.println(e);
		}		
		
		return input;
	}

}
