import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class TextBuddy{
	//all messages for user
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1$s is ready for use";
	private static final String MESSAGE_ASK_COMMAND = "command: ";
	private static final String MESSAGE_ADD = "added to %1$s: \"%2$s\"";
	private static final String MESSAGE_DELETE = "deleted from %1$s: \"%2$s\"";
	private static final String MESSAGE_DELETE_NOTHING = "Nothing to delete";
	private static final String MESSAGE_CLEAR = "all content deleted from %1$s";
	private static final String MESSAGE_EMPTY = "%1$s is empty";
	private static final String MESSAGE_DISPLAY = "%1$s. %2$s";
	private static final String MESSAGE_USER_COMMAND = "%1$s";
	private static final String MESSAGE_USER_COMMAND_WITH_WORD = "%1$s %2$s";
	private static final String MESSAGE_SEARCH = "%1$s %2$s";
	
	//help messages
	private static final String HELP_ADD = "Type \"add\" followed by your sentence to add sentence.";
	private static final String HELP_DELETE = "Type \"delete\" followed by sentence line number to delete sentence.";
	private static final String HELP_CLEAR = "Type \"clear\" to clear display memory.";
	private static final String HELP_DISPLAY = "Type \"display\" to display all sentences in memory.";
	private static final String HELP_SORT = "Type \"sort\" to sort lines alphabetically.";
	private static final String HELP_SEARCH = "Type \"search\" followed by a word to search all lines that contain the word.";
	private static final String HELP_EXIT = "Type \"exit\" to end TextBuddy.";
	
	//error messages
	private static final String ERROR_FILE_NOT_FOUND = "OOPS! %1$s was not found.";
	private static final String ERROR_NO_CONTENT = "No content in this file.";
	private static final String ERROR_IN_PROGRAM = "!!!!!!!!!!!!!!!!!!!!!!!!!!!!ERROR IN PROGRAM!!!!!!!!!!!!!!!!!!!!!!!!!!!";
	
	private static final int MEMORY_SIZE = 256; 
	
	public static int lineCount = 1; //line number starts from 1
	public static int getLineCount(){
		return lineCount;
	}
	public static void setLineCount(int lineNum){
		lineCount = lineNum;
	}
	
	public static void main(String [] args){
		for (int i=0; i < args.length; i++){
     			startTextBuddy(args[i]);
		}
	}
	
	public static void startTextBuddy(String filename) {
		printWelcomeMessage(filename);
		File file = new File(filename);
		String[] str = new String[MEMORY_SIZE];
		try{
		    readAndProcess(filename, file, getLineCount(), str);
		}
		catch ( FileNotFoundException fnf ){
			displayErrorMessage(filename);
		}
	}

	public static void readAndProcess(String filename, File file, int lineNum, String[] str) throws FileNotFoundException {
		Scanner sc = new Scanner(file);
		if (sc.hasNext()) {
		    processingCommands(filename, getLineCount(), str, sc);
		}
		else { 
			System.out.println(ERROR_NO_CONTENT); 
		}
		sc.close();
	}
	
	public static void processingCommands(String filename, int lineNum, String[] str, Scanner sc) {
		String input = sc.next();
		while(!input.equalsIgnoreCase("exit")){
		    if(input.equalsIgnoreCase("add")){
		    	int test = TextBuddy.getLineCount();
		    	setLineCount(addCommand(filename, TextBuddy.getLineCount(), str, sc, input));
		    	assertEquals(test+1, TextBuddy.getLineCount());
		    }   
		    else if(input.equalsIgnoreCase("delete")){
		    	int test = TextBuddy.getLineCount();
		    	setLineCount(deleteCommand(filename, TextBuddy.getLineCount(), str, sc, input));
		    	assertEquals(test-1, TextBuddy.getLineCount());
		    }
		    else if(input.equalsIgnoreCase("clear")){
		    	setLineCount(clearCommand(filename, TextBuddy.getLineCount(), str, input));
		    	///check if the file was actually cleared
		    	assertEquals(1, TextBuddy.getLineCount());
		    }	                         
		    else if(input.equalsIgnoreCase("display")){
		    	displayCommand(filename, TextBuddy.getLineCount(), str, input);
		    } 
		    else if(input.equalsIgnoreCase("sort")){
		    	sortCommand(filename, TextBuddy.getLineCount(), str, input);
		    }
		    else if(input.equalsIgnoreCase("search")){
		    	searchCommand(filename, TextBuddy.getLineCount(), str, sc, input);
		    }
		    else{
		    	printCommandHelp(input);
		    }
		    printCommandAndWaitForInput();    
		    input = sc.next();
		}
		System.out.println(String.format(MESSAGE_USER_COMMAND, input));
	}
	
	//Does adding of lines to memory
	public static int addCommand(String filename, int lineNum, String[] str, Scanner sc, String input) {
		String word = sc.nextLine().substring(1);
		System.out.println(String.format(MESSAGE_USER_COMMAND_WITH_WORD, input, word));
		System.out.println(String.format(MESSAGE_ADD,filename,word));
		str[lineNum] = word;
		return ++lineNum;
	}
	//Does deleting of line from memory
	public static int deleteCommand(String filename, int lineNum, String[] str, Scanner sc, String input) {
		int delLine = sc.nextInt();
		if(str[delLine]!=null){
			System.out.println(String.format(MESSAGE_USER_COMMAND_WITH_WORD, input, delLine));
			System.out.println(String.format(MESSAGE_DELETE,filename,str[delLine]));
			for(; delLine<MEMORY_SIZE-1; delLine++){
				str[delLine] = str[delLine+1];
			}		
		}else{
			System.out.println(String.format(MESSAGE_USER_COMMAND_WITH_WORD, input, delLine));
			System.out.println(MESSAGE_DELETE_NOTHING);
		}
		return --lineNum;
	}
	//Clears all lines from memory
	public static int clearCommand(String filename, int lineNum, String[] str, String input) {
		str = null;
		System.out.println(String.format(MESSAGE_USER_COMMAND, input));
		System.out.println(String.format(MESSAGE_CLEAR,input,filename));
		return lineNum = 1;
	}
	//Displays all lines available in memory else display it's empty
	public static void displayCommand(String filename, int lineNum, String[] str, String input) {
		System.out.println(String.format(MESSAGE_USER_COMMAND, input));
		if(lineNum > 1){
			printLines(str);
		}
		else{
			printEmptyMessage(filename);
		}
	}
	//Sort lines alphabetically
	public static void sortCommand(String filename, int lineNum, String[] str, String input) {
		System.out.println(String.format(MESSAGE_USER_COMMAND, input));
		if(lineNum > 1){
			Arrays.sort(str,1,lineNum);
		}
		else{
			printEmptyMessage(filename);
		}
	}
	//Search command to search for a word in the file and return the lines containing that word.
	public static void searchCommand(String filename, int lineNum, String[] str, Scanner sc, String input) {
		String word = sc.nextLine().substring(1);
		System.out.println(String.format(MESSAGE_SEARCH, input,word));
		if(lineNum > 1){
			for(int counter = 1;counter<lineNum; counter++){
				if(str[counter].indexOf(word)!=-1){
					//found
					System.out.println(String.format(MESSAGE_DISPLAY,counter,str[counter]));
				}
			}	

		}
		else{
			printEmptyMessage(filename);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////Test-driven development (TDD)///////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	public static void assertEquals(int a, int b) {
		if(a!=b){
			printErrorInProgram();
		}
	}
	public static void assertEquals(String a, String b) {
		if(!a.equals(b)){
			printErrorInProgram();
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////PRINTING METHODS///////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////
	//displays help page if proper commands are not used
	public static void printCommandHelp(String input) {
		System.out.println(String.format(MESSAGE_USER_COMMAND, input));
		System.out.println(HELP_ADD); 
		System.out.println(HELP_DELETE);
		System.out.println(HELP_CLEAR);
		System.out.println(HELP_DISPLAY);
		System.out.println(HELP_SORT);
		System.out.println(HELP_SEARCH);
		System.out.println(HELP_EXIT);
	}
	public static void printEmptyMessage(String filename) {
		System.out.println(String.format(MESSAGE_EMPTY,filename));
	}	
	
	public static void printLines(String[] str) {
		int j = 1; //text position starts from str[1]
		while(str[j] != null && !str[j].isEmpty()){
			System.out.println(String.format(MESSAGE_DISPLAY,j,str[j]));
			j++;
		}
	}

	public static void printWelcomeMessage(String filename) {
		System.out.println(String.format(MESSAGE_WELCOME, filename));
		printCommandAndWaitForInput();
	}

	public static void printCommandAndWaitForInput() {
		System.out.print(MESSAGE_ASK_COMMAND);
	}

	public static void displayErrorMessage(String filename) {
		System.out.println(String.format(ERROR_FILE_NOT_FOUND,filename));
	}
	public static void printErrorInProgram() {
		System.out.println(ERROR_IN_PROGRAM);
	}
}
