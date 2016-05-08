/**
 * Holds all functions related to card information.
 * Used to generate cards when the Dealer deals to players.
 * Used by Player class for reference to cards in their hand and to determine 
 * the value of the ace in their hand.
 * 
 * @author Chris
 *
 */

import java.util.Random;

public class Card {
	private String name;
	private int value;
	
	//Constructor for a card
	public Card()
	{
		//Initialise card name and value to known safe values
		this.name = null;
		this.value = 0;		
	}
	
	//Getter for name value
	public String getName() {
		return name;
	}

	//Setter for name value
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

	//Setter for card value
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

	//Draw a card. Generates a random number and assigns name and value based on
	//generated value
	public Card draw_card()
	{
		//Create blank card object
		Card generated_card = new Card();
		
		//Generate a random number to use to set card name and value
		int value = generate_random_value();
		
		//Set card name based on value generated
		generated_card.setName(value);
		
		//Set card value based on value generated
		generated_card.setValue(value);		
		
		//Return randomly generated card
		return generated_card;
	}
	
	//Method to generate a random number in range 0-12. This value is used to 
	//determine card information
	public int generate_random_value()
	{
		//Create random number generator
		Random randomGenerator = new Random();
		
		//Generate random int in range 0-13
		int randomInt = randomGenerator.nextInt(14);
		
		//Return generated int
		return randomInt;
	}
}
