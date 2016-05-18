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
		
		String server_address = null;
		int port = 0;
		
		
		//If the wrong number of arguments is entered
		if(args.length != 3)
		{
			invalid_args_hook();
			return;
		}
		
		//If server is passed in
		if(args[0].equals(server))
		{
			server_address = args[1];
			port = Integer.parseInt(args[2]);
			new Server(server_address, port);
		}
		//If client is passed in
		else if(args[0].equals(client))
		{
			server_address = args[1];
			port = Integer.parseInt(args[2]);
			new Client(server_address, port);
		}
		//If right number of arguments is passed in, but argument doesn't match
		//'server' or 'client'
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
				+ "<Program Type> <server> <port>\nWhere <Program Type> is "
				+ "either client or server.");
		System.err.println("<server> is the server to connect to "
				+ "(can be anything if starting server)");
		System.err.println("<port> is the server port to connect to/start");
		
		return;
	}

}
