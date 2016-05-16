import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Class to manage the generation and maintenance of log files by the Server
 * Constructs the log files and manages new entries.
 * 
 * There are two types of log files, communication and game logs.
 * 
 * @author Chris
 *
 */
public class Log {
	
	private File log;
	
	//Constructor for a new log file
	public Log(String fileName, String logName)
	{
		this.log = new File(fileName);	
		
		//Creates the file to store the log
		create_new_file(fileName, logName);
	}
	
	//Creates a new file to store the log
	public void create_new_file(String fileName, String logTitle)
	{
		//Variable to hold file ouput stream
		FileOutputStream out;
		
		try {
			//Create new file output stream to file to be created
			out = new FileOutputStream(log);
			
			//Append a newline character to log title
			logTitle += '\n';
			
			//Print log title to the newly created file
			out.write(logTitle.getBytes());
			
			//Close the file output stream
			out.close();
			
			return;
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Add new entry to log file
	public void writeToLog(String input, InetAddress inetAddress)
	{	
		//Get the current date and time in formatted string form.
		String date_and_time = getDateAndTime();
		
		//Get the socket's ip address for log entry
		String address = inetAddress.getHostAddress();
		
		//Put log entry together in one String
		String log_entry = date_and_time + " " + address + " " + input + '\n';
		
		try {
			//Create a file writer to append to the log file
			FileWriter out = new FileWriter(log, true);
			
			//Output the log entry to the log file
			out.write(log_entry);
			
			//Close the file writer
			out.close();
			
			return;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Gets the current date and time and returns it as a string for output to 
	//the log file
	public static String getDateAndTime()
	{
		//Create a date format for printing
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		//Get the current date and time
		Date date = new Date(System.currentTimeMillis());
		
		//Format date and time using created date format
		String date_and_time = dateFormat.format(date);
		
		//Return date and time for log to use
		return date_and_time;
	}

}
