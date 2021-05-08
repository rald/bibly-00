import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;



class Main {

	static String version = "bibly 0.1";
	static String currentBibleVersion = "kjv.csv";

  static Scanner reader = null;
  static Scanner input = new Scanner(System.in);

	static String cmd = "";



	static void cls() {
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
		println(".find\t->\tshow books per chapter");
		println(".search\t->\tshow search");
		println(".end\t->\texit");
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
		print("Search: ");    String textToSearch=input.nextLine();

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

		boolean finish=false;

		ArrayList<String> bibleBooks;

		cls();

		print(version+"\n\n");

		printHelp();

		while(!finish) {
			
			print("> ");
			
			cmd=input.nextLine();
			
			if(cmd.equals(".end")) {
				finish=true;
			} else if(cmd.equals(".help")) {
				printHelp();
			} else if(cmd.equals(".books")) {
				printBibleBooks();
			} else if(cmd.equals(".find")) {
				printBibleBooksPerChapter();
			} else if(cmd.equals(".search")) {
				printSearch();
			}

		}

  }

}
