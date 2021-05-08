import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Search {
	public static void main(String[] args) {
		File bible = new File("kjv.vpl");
		Scanner search = new Scanner(System.in);
		Scanner reader = null;
		String textToSearch;
		String vpl;

		try {
			reader = new Scanner(bible);
		} catch(FileNotFoundException e) {
			System.out.println("File not found.");
		}

		System.out.print("Search: ");
		textToSearch=search.nextLine();

		while(reader.hasNextLine()) {
			vpl=reader.nextLine();
			if(vpl.toLowerCase().contains(textToSearch.toLowerCase())) {
				System.out.println(vpl);
			}
		}

		search.close();
		reader.close();
	}
}
