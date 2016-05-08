/**
 * The Card_Game class is the entry point into the program.
 * 
 * It starts either a Client or a Server class based on the information passed 
 * in from the command line.
 * 
 * @author Chris
 *
 */
public class Card_Game {

	public static void main(String[] args) {
		String server = "server";
		String client = "client";
		
		if(args.length != 1)
		{
			invalid_args_hook();
			return;
		}
		
		if(args[0].equals(server))
		{
			new Server();
		}
		else if(args[0].equals(client))
		{
			new Client();
		}
		else
		{
			invalid_args_hook();
			return;
		}
	}
	
	// Method to print argument error to error stream when invalid arguments
	//passed in. 
	public static void invalid_args_hook()
	{
		System.err.println("Error: Invalid arguments.");
		System.err.println("\t Correct format is: java Card_Game "
				+ "<Program Type>\nWhere <Program Type> is either client "
				+ "or server.");
		
		return;
	}

}
