import java.io.Serializable;

/**
 * Holds all functions related to card information.
 * Used to generate cards when the Dealer deals to players.
 * Used by Player class for reference to cards in their hand and to determine 
 * the value of the ace in their hand.
 * 
 * @author Chris
 *
 */

public class Card implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private int value;
	
	//Constructor for a card
	public Card(int generated_int)
	{
		//Initialise card name and value to known safe values
		this.setName(generated_int);
		this.setValue(generated_int);		
	}
	
	//Getter for name value
	public String getName() {
		return name;
	}

	//Setter for name value, matches value passed in to name stored in index 
	//with that value.
	private void setName(int value) {
		
		//Array containing all possible card names
		String[] card_names = 
		{
			"Ace",
			"One",
			"Two",
			"Three",
			"Four",
			"Five",
			"Six",
			"Seven",
			"Eight",
			"Nine",
			"Ten",
			"Jack",
			"Queen",
			"King"
		};
				
		//Set card name based on value passed in
		this.name = card_names[value];
	}

	//Setter for card value, changes Jack, Queen, and King values to 10 
	private void setValue(int value) {
		
		//If card drawn is a Jack, Queen, or king, set value to 10.
		if(value > 10)
		{
			value = 10;
		}
		
		//Set value to randomly generated card's value. Ace is left as 0 as an 
		//identifier
		this.value = value;
	}

	//Getter for card value
	public int getValue() {
		return value;
	}
	
	//Initialise hand to be blank, used by both players and dealer
	public static void initialise_hand(Card[] hand)
	{
		//For the size of hand, set all values to null
		for(int i = 0; i < hand.length; i++)
		{
			hand[i] = null;
		}
		
		//When hand is initialised to blank, return.
		return;
	}
	
	//Returns value of current hand
	public static int hand_value(Card[] hand)
	{		
		int hand_value = 0;
		
		int num_aces = 0;
		
		for(int i = 0; i < hand.length; i++)
		{
			//If the current card in hand is empty
			if(hand[i] == null)
			{
				break;
			}
			
			if(hand[i].value == 0)
			{
				//increment number of aces count
				num_aces++;
				//Increase hand_value by 1
				hand_value++;
				continue;
			}
			
			//Add card value to hand total.
			hand_value += hand[i].value;
		}
		
		//Convert aces to appropriate value based on current hand total
		hand_value = convert_aces(num_aces, hand_value);		
		
		//Return the hand value;
		return hand_value;
	}
	
	//Determine value of aces in hand
	public static int convert_aces(int num_aces, int hand_value)
	{
		//Maximum value before going bust
		int MAX_HAND_VALUE = 21;
		
		//Maximum value of an ace
		int MAX_ACE_VALUE = 11;
		
		//For the number of aces in the hand
		for(int i = 0; i < num_aces; i++)
		{		
			//If converting an ace to it's maximum value increases hand total to
			//over 21, return hand without converting more aces
			if((hand_value + (MAX_ACE_VALUE - 1)) > MAX_HAND_VALUE)
			{
				return hand_value;
			}
			else
			{
				//Convert ace to equal 11
				hand_value += (MAX_ACE_VALUE - 1);
			}
		}
		return hand_value;
	}
	
	//Method to show cards currently in hand
	public static void display_hand(Card[] hand)
	{
		//Loop counter
		int i = 0;
		
		System.out.println("Cards in hand: ");
				
		while(hand[i] != null)
		{
			//Display card name
			System.out.println(hand[i].name);
			//Print line for formatting
			System.out.println();
			//Increment loop counter
			i++;
		}
		
		return;
	}
}
