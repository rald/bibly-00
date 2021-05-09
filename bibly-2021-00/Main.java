import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;


class Verse {
	int vnum;
	String vers;
	Verse(int vnum,String vers) {
		this.vnum=vnum;
		this.vers=vers;
	}
}

class Chapter {
	int cnum;
	ArrayList<Verse> verses = new ArrayList<>();
	Chapter(int cnum) {
		this.cnum=cnum;
	}
}

class Book {
	String bname;
	int bnum;
	ArrayList<Chapter> chapters = new ArrayList<>();
	Book(String bname,int bnum) {
		this.bname=bname;
		this.bnum=bnum;
	}
}

class Bible {
	ArrayList<Book> books = new ArrayList<>();
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



	static Bible parseBible() {		
		Bible bible = new Bible();

		String line="";
		String[] arr1;
		String[] arr2;

		int bnum=0;
		int cnum=0;
		int vnum=0;
		String bname="";
		String vers="";
		boolean start=true;

		openBibleFile(currentBibleVersion);

		while(reader.hasNextLine()) {
			line=reader.nextLine();
			arr1=line.split("\\|");
			arr2=arr1[1].split("\\:");

			if(!arr1[0].equals(bname)) {
				if(start) start=false; else bnum++;
				bname = arr1[0]; 
			} 

			try {
				cnum = Integer.parseInt(arr2[0])-1; 
			} catch(NumberFormatException e) {
				println("Invalid Chapter Number");
				System.exit(0);
			}

			try {
				vnum = Integer.parseInt(arr2[1])-1; 
			} catch(NumberFormatException e) {
				println("Invalid Verse Number");
				System.exit(0);
			}

			vers = arr1[2]; 
			
//			println((bnum+1)+" "+bname+" "+(cnum+1)+":"+(vnum+1));

			bible.books.add(new Book(bname,bnum));
			bible.books.get(bnum).chapters.add(new Chapter(cnum));
			bible.books.get(bnum).chapters.get(cnum).verses.add(new Verse(vnum,vers));
						
		}
		closeBibleFile();

		return bible;
	}



  public static void main(String[] args) {

		Bible bible = new Bible();

		boolean finish=false;

		ArrayList<String> bibleBooks;

		clear();

		print(version+"\n\n");

		printHelp();

		bible=parseBible();

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
