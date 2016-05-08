NP Assignment Card Game

******************************************************************************
*                               Card Game Rules                              *
******************************************************************************

There are 13 ranks of cards ranging in value from 1 - 11 with jack, queen, 
king being worth 10 points. The ace can be worth 1 or 11, whichever is more 
favourable. All other cards are worth their face value. 

The aim of the game is to get cards whose total is as close to 21 as possible.
If the cards total exceeds 21, then the hand is bust and the player loses. 
The dealer is able to go bust as well.

Each player, including the dealer, is dealt 2 cards at the beginning of the
game. 

Each player can then request additional cards at any time until they go bust
or choose to stand with the cards they currently hold. Once all players have 
finished, the dealer draws cards for their hand until they go bust or the 
cards they hold total over 17.

Each players hand is then compared to the dealer's hand. If the player hasn't
gone bust, and their cards have a greater total than the dealer's, they are 
considered to have won the round.

If the player's and dealer's hand are equal, then they draw.

******************************************************************************
*                                 Assumptions                                *
******************************************************************************

Suit is not relevant to the value of the card therefore the suit of each card 
will not be considered. E.g. if an 2 is received by the player it is worth 2 
regardless therefore the player will only see that they hold a 2.  

There is enough decks in play for the amount of cards dealt to not be tracked.
This means that it is possible for all players to have a hand full of aces.
This is unlikely as each deck is assumed to be shuffled correctly. 

Ace value is automatically determined by the program. That is, if the player
has a 5 and an ace in their hand, the ace would be worth 11. If the player
draws a 10, this would cause the player to go bust, therefore the ace is 
recalculated to be worth 1.

If the dealer's hand is over 17, the dealer will stand. This is to enable the
dealer to be autonomous.

If a client attempts to join when a game is in progress, they will be
rejected.

******************************************************************************
*                                   Server                                   *
******************************************************************************

Server is separated from the dealer e.g. the server is not the dealer. It is
an intermediary between the player and dealer.

Server must first be created before the game can be played. 

The server will be created and open a server socket. 

It will maintain a list of all connected clients (players and dealer).

There will be two logs maintained:
    - A log to store all communication between clients and server. This log 
      stores information such as when a card is dealt and what value that card 
      is.
    - A log to store all information related to the game. This log will store 
      information such as what value a player is dealt, or if a player goes 
      bust.
Each log entry is stamped with the date, time, the remote socket address, and 
the action performed.

******************************************************************************
*                                   Dealer                                   *
******************************************************************************

The first client to connect to the server will be the assigned the role of
dealer. 

The dealer has an automated role. 

It automatically deals cards when requested.

When all players are finished, the dealer automatically draws cards until it's
cards total over 17.

******************************************************************************
*                                   Player                                   *
******************************************************************************

Any client connecting after the dealer is assigned the role of player.

The player is automatically dealt 2 cards at the start of the game.

The player can then request more cards until they go bust or they choose to 
stand.

******************************************************************************
*                            Card Dealing Process                            *
******************************************************************************

Dealer randomly generates card value.

Dealer sends card information to server.

Server logs that dealer has dealt a card to the communication log file.

Server logs card value to the game log.

Server sends the card information to the player who requested the card.

******************************************************************************
*                          Card Evaluation Process                           *
******************************************************************************
If both player and dealer are bust, dealer sends draw to the server.

Else if the player is bust, the dealer sends that the dealer has won to the
server. 

Else if the dealer is bust, the dealer sends that the player won to the 
server.

Else if the player has a greater total than the dealer, the dealer sends that 
the player won to the server.

Else if the dealer has a greater total than the player, the dealer sends that 
the dealer won to the server.

Else if the dealer and player have the same total, the dealer sends draw to
the server.

******************************************************************************
*                                Single Player                               *
******************************************************************************

Process for single player game:
    - Server is created
        - Create log files
        - Create client list
        - Open server socket
        - Await client connection
        - When first client connects, assign them to the first slot in the 
          client list. This slot is reserved for the dealer.
        - Start new thread to handle the dealer.
        - Open input and output streams to communicate with the dealer.
        - Communicate to client that their role is the dealer
        - Log that the dealer has connected in communication log.
        - The next connection is considered the player.
        - When a player connects, store their information in the client list.
        - Start a new thread to handle the client.
        - Open input and output streams to communicate with the player
        - Communicate to client that their role is player.
        - Communicate to the dealer that a player has connected.
        - Log that a player has connected to both communication and game logs.
        - Manage any requests made by player and dealer, communicating to 
          the intended target. Log any communication in communication log 
          file and any game information in game log file. 
    - Dealer
        - Open socket connection to server.
        - Open input and output streams to the server.
        - Await role assignment from the server.
        - Wait for a player connection to the server.
        - When player connects to the server, start the game.
        - Deal two cards to the player
        - If the player requests another card, deal another card.
        - If the player is bust the dealer starts a new game when all game 
          information has been logged to the server.
        - If the player stands, the dealer plays their hand:
           - The dealer generates a random card for their hand.
           - Dealer send the card information to the server.
           - Dealer continues process until their hand totals greater than 17.
        - Dealer evaluates cards.
    - Player
        - Open socket connection to server.
        - Open input and output streams to the server.
        - Await role assignment from the server.
        - Wait for cards to be dealt.
        - When cards are received, if not bust, choose to draw another card
          or stand.
        - Continue drawing cards until bust or choice to stand is selected.
        - Wait on game results.

******************************************************************************
*                                Multi-Player                                *
******************************************************************************
Process for single player game:
    - Server is created
        - Create log files
        - Create client list
        - Open server socket
        - Await client connection
        - When first client connects, assign them to the first slot in the 
          client list. This slot is reserved for the dealer.
        - Start new thread to handle the dealer.
        - Open input and output streams to communicate with the dealer.
        - Communicate to client that their role is the dealer
        - Log that the dealer has connected in communication log.
        - The sunsequent connections are considered players.
        - When a player connects, store their information in the client list.
        - Start a new thread to handle the client.
        - Open input and output streams to communicate with the player.
        - Communicate to client that their role is player.
        - Communicate to the dealer that a player has connected.
        - Log that a player has connected to both communication and game logs.
        - Manage any requests made by player and dealer, communicating to 
          the intended target. Log any communication in communication log 
          file and any game information in game log file. 
        - When all players have indicated they are ready to start the game, 
          send request to dealer to start the game.
        - If a player leaves the game: 
              - Communicate to the dealer that the player has left.
              - Log player leaving to the game and communication logs.
              - Remove client from the connected client list.
    - Dealer
        - Open socket connection to server.
        - Open input and output streams to the server.
        - Await role assignment from the server.
        - Wait for a players connection to the server.
        - When server communicates to start the game, deal two cards to all 
          players.
        - If a player requests another card, deal another card.
        - When all players have finished, the dealer plays their hand:
           - The dealer generates a random card for their hand.
           - Dealer send the card information to the server.
           - Dealer continues process until their hand totals greater than 17.
        - Dealer evaluates cards.
    - Player
        - Open socket connection to server.
        - Open input and output streams to the server.
        - Await role assignment from the server.
        - Indicate that readiness to play.
        - Wait for cards to be dealt.
        - When cards are received, if not bust, choose to draw another card
          or stand.
        - Continue drawing cards until bust or choice to stand is selected.
        - Wait on game results.

******************************************************************************
*                              Required Classes                              *
******************************************************************************

- Server
- Client
- Card
- Game
- Log
- Player
- Dealer