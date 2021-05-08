import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class BibleSearch {

	public static void main(String[] args) {
		File bible = new File("kjv.csv");
		Scanner search = new Scanner(System.in);
		Scanner reader = null;
		String bookToSearch="";
		int chapterToSearch=0;
		String vpl;
		String book="",vers="";
		int cnum=0,vnum=0;
		String[] a1=null;
		String[] a2=null;
		int n=0;

		try {
			reader = new Scanner(bible);
		} catch(FileNotFoundException e) {
			System.out.println("File not found.");
		}

		System.out.print("Book: ");
		bookToSearch=search.nextLine();

		System.out.print("Chapter: ");
		chapterToSearch=search.nextInt();
		search.nextLine();

		n=0;

		while(reader.hasNextLine()) {

			vpl=reader.nextLine();

			a1=vpl.split("\\|",3);
			a2=a1[1].split("\\:",2);

			book=a1[0];
			cnum=Integer.parseInt(a2[0]);
			vnum=Integer.parseInt(a2[1]);
			vers=a1[2];

			if(		book.equalsIgnoreCase(bookToSearch) &&
					cnum==chapterToSearch) {
				n++;
				System.out.println(book+" "+cnum+":"+vnum+" -> "+vers);
			}

		}

		if(n==0)
			System.out.println("Not found.");
		else
			System.out.println("Found "+n+" occurences.");

		search.close();
		reader.close();
	}
}
