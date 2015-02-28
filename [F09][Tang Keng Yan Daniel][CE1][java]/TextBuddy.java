import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TextBuddy{
	final static int MEMORY_SIZE = 256; 

	public static void main(String [] args){
		for (int i=0; i < args.length; i++){
			startTextBuddy(args[i]);
		}
	}

	public static void startTextBuddy(String filename) {
		printWelcomeMessage(filename);
		File file = new File(filename);
		int lineNum = 1; //line number starts from 1
		String[] str = new String[MEMORY_SIZE];
		try{
			readAndProcess(filename, file, lineNum, str);
		}
		catch ( FileNotFoundException fnf ){
			displayErrorMessage(filename);
		}
	}

	public static void printWelcomeMessage(String filename) {
		System.out.println("Welcome to TextBuddy. "+ filename +" is ready for use");
		printCommandAndWaitForInput();
	}

	public static void printCommandAndWaitForInput() {
		System.out.print("command: ");
	}

	public static void displayErrorMessage(String filename) {
		System.out.println("OOPS! " + filename + " was not found.");
	}

	public static void readAndProcess(String filename, File file, int lineNum, String[] str) throws FileNotFoundException {
		Scanner sc = new Scanner(file);
		if (sc.hasNext()) {
			processingCommands(filename, lineNum, str, sc);
		}
		else { 
			System.out.println("No content in this file."); 
		}
		sc.close();
	}

	public static void processingCommands(String filename, int lineNum, String[] str, Scanner sc) {
		String input = sc.next();
		while(!input.equalsIgnoreCase("exit")){
			if(input.equalsIgnoreCase("add")){
				lineNum = addCommand(filename, lineNum, str, sc, input);
			}   
			else if(input.equalsIgnoreCase("delete")){
				lineNum = deleteCommand(filename, lineNum, str, sc, input);
			}
			else if(input.equalsIgnoreCase("clear")){
				lineNum = clearCommand(filename, lineNum, str, input);
			}	                         
			else if(input.equalsIgnoreCase("display")){
				displayCommand(filename, lineNum, str, input);
			} 
			else{
				printCommandHelp(input);
			}
			printCommandAndWaitForInput();    
			input = sc.next();
		}
		System.out.println(input);
	}
	//displays help page if proper commands are not used
	public static void printCommandHelp(String input) {
		System.out.println(input);
		System.out.println("Type \"add\" followed by your sentence to add sentence."); 
		System.out.println("Type \"delete\" followed by sentence line number to delete sentence.");
		System.out.println("Type \"clear\" to clear display memory.");
		System.out.println("Type \"display\" to display all sentences in memory.");
		System.out.println("Type \"exit\" to end TextBuddy.");
	}
	//Does adding of lines to memory
	public static int addCommand(String filename, int lineNum, String[] str, Scanner sc, String input) {
		String word = sc.nextLine().substring(1);
		System.out.println(input +" "+word);
		System.out.println("added to "+filename+": \""+word+"\"");
		str[lineNum] = word;
		return ++lineNum;
	}
	//Does deleting of line from memory
	public static int deleteCommand(String filename, int lineNum, String[] str,	Scanner sc, String input) {
		int delLine = sc.nextInt();
		if(str[delLine]!=null){
			System.out.println(input + " " + delLine);
			System.out.println("deleted from "+filename+": \""+str[delLine]+"\"");
			for(; delLine<MEMORY_SIZE-1; delLine++){
				str[delLine] = str[delLine+1];
			}		
		}else{
			System.out.println(input + " " + delLine);
			System.out.println("Nothing to delete");
		}
		return --lineNum;
	}
	//Clears all lines from memory
	public static int clearCommand(String filename, int lineNum, String[] str, String input) {
		str = null;
		System.out.println(input + "\nall content deleted from "+filename);
		return lineNum = 1;
	}
	//Displays all lines available in memory else display it's empty
	public static void displayCommand(String filename, int lineNum, String[] str, String input) {
		System.out.println(input);
		if(lineNum > 1){
			printLines(str);
		}
		else{
			printEmptyMessage(filename);
		}
	}

	public static void printLines(String[] str) {
		int j = 1; //text position starts from str[1]
		while(str[j] != null && !str[j].isEmpty()){
			System.out.println(j+". "+str[j]);
			j++;
		}
	}

	public static void printEmptyMessage(String filename) {
		System.out.println(filename+" is empty");
	}	
}
