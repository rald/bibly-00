import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.regex.Matcher; 
import java.util.regex.Pattern; 



class Parser {

	static String cbname=null;
	static String ccnumber=null;
	static String svnumber=null;
	static String evnumber=null;

	static void parse(String line) {
		String pattern="(\\d\\s*)?(\\D+)\\s*(\\d+)(?:[:-](\\d+))?(?:\\s*-\\s*(\\d+))?";
		Pattern r = Pattern.compile(pattern);
    Matcher m = r.matcher(line);
    if (m.find( )) {
			String lnum=m.group(1);
			String book=(lnum==null)?m.group(2):lnum+" "+m.group(2);
			String cnum=m.group(3);
			String svnum=m.group(4);
			String evnum=m.group(5);
			Main.println(book+" "+cnum+":"+svnum+(evnum==null?"":"-"+evnum));
    } else {
 			System.out.println("NO MATCH");
    }

	}

}



public class Main {

	enum State {
		DEFAULT,
		LETTER,
		BOOK,
		CHAPTER,
		START_VERSE,
		END_VERSE,
		PRINT
	}

	static String version = "bibly 0.1";

	static String currentBibleVersion = "kjv.csv";

  static Scanner reader = null;
  static Scanner input = new Scanner(System.in);

	static String cmd = "";

	static String prompt = "bibly> ";



	static void clear() {
    System.out.println("\33[2J\33[0;0H");
	}



	static void print(String text) {
    System.out.print(text);
	}



	static void println(String text) {
    System.out.println(text);
	}



	static void printHelp() {
		println("");
		println("command\t->\taction");
		println(".help\t->\tshow help");
		println(".books\t->\tshow books");
		println(".read\t->\tshow books per chapter");
		println(".search\t->\tshow search");
		println(".seek\t->\tshow seek");
		println(".clear\t->\tclear screen");
		println(".exit\t->\texit");
		println("");
	}



	static void openBibleFile(String bibleVersion) {
	  File bibleBookFile = new File(bibleVersion);
    try {
      reader = new Scanner(bibleBookFile);
    } catch(FileNotFoundException e) {
      System.out.println("File not found.");
			System.exit(0);
    }
	}



	static void closeBibleFile() {
		reader.close();
	} 



  static ArrayList<String> getBibleBooks(String bibleVersion) {
		ArrayList<String> bibleBooks=new ArrayList<>();
		String line="";
		String[] arr1;
		String[] arr2;
		openBibleFile(bibleVersion);
		while(reader.hasNextLine()) {
			line=reader.nextLine();
			arr1=line.split("\\|");
			arr2=arr1[1].split("\\:");
			if(bibleBooks.indexOf(arr1[0])==-1) bibleBooks.add(arr1[0]);
		}
		closeBibleFile();
		return bibleBooks;
	}



	static void printBibleBooks() {
		ArrayList<String> bibleBooks=getBibleBooks(currentBibleVersion);
		for(int i=0;i<bibleBooks.size();i++) {
			if(i!=0) print(", ");
			print(bibleBooks.get(i));	
		}
		println("");
	}



	static void printBibleBooksPerChapter() {
		print("Book: ");    String book=input.nextLine();
		print("Chapter: "); String chapter=input.nextLine();
		println("");		

		String line="";
		String[] arr1;
		String[] arr2;
		openBibleFile(currentBibleVersion);
		while(reader.hasNextLine()) {
			line=reader.nextLine();
			arr1=line.split("\\|");
			arr2=arr1[1].split("\\:");
			if(	arr1[0].equalsIgnoreCase(book) &&
					arr2[0].equalsIgnoreCase(chapter)) {
				print(arr1[0]+" "+arr2[0]+":"+arr2[1]+" -> "+arr1[2]+"\n\n");
			}
		}
		closeBibleFile();
	}



	static void printSearch() {
		print("Search: ");    
		String textToSearch=input.nextLine().trim();

		if(textToSearch.isEmpty()) return;

		String line="";
		String[] arr1;
		String[] arr2;
		int n=0;
		openBibleFile(currentBibleVersion);
		while(reader.hasNextLine()) {
			line=reader.nextLine().replace("|"," ");
			if(line.toLowerCase().contains(textToSearch.toLowerCase())) {
			  n++;
				print(line+"\n\n");
			}			
		}
		
		if(n==0) { 
		  println("Found none.");
		} else if(n==1) {
		  println("Found "+n+" occurence.");
		} else {
		  println("Found "+n+" occurences.");
		}
		
		closeBibleFile();
	}



  public static void main(String[] args) {

		Bible bible = new Bible();

		boolean finish=false;

		ArrayList<String> bibleBooks;

		clear();

		print(version+"\n\n");

		printHelp();

		while(!finish) {
			
			print(prompt);
			
			cmd=input.nextLine();
			
			if(cmd.equals(".exit")) {
				finish=true;
			} else if(cmd.equals(".clear")) {
				clear();
			} else if(cmd.equals(".help")) {
				printHelp();
			} else if(cmd.equals(".books")) {
				printBibleBooks();
			} else if(cmd.equals(".read")) {
				printBibleBooksPerChapter();
			} else if(cmd.equals(".search")) {
				printSearch();
			} else {				
				println("Invalid command.");
			}

		}

  }

}
