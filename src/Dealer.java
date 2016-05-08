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

	public Dealer()
	{
		//Wait for player connection to the game
		
		//When player connects to the game, start the game
		
		//Deal two cards to player
		
		//Deal two cards to dealer
		
		//If the player requests a card, deal card to player
		
		//If the player goes bust, the dealer starts a new game after all 
		//information is logged to server
		
		//If the player stands, dealer plays their hand
		
		//While Loop
			//If the dealer's hand totals over 17, the dealer stands
			
			//If the dealer's hand totals under 17, the dealer draws a card
		
		//Dealer evaluates cards
		
		//Dealer communicates results to the server.
	}
}
