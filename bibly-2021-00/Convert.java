import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Convert {
	public static void main(String[] args) {
		File bible = new File("kjv.vpl");
		Scanner reader = null;
		String textToSearch;
		String vpl;

		try {
			reader = new Scanner(bible);
		} catch(FileNotFoundException e) {
			System.out.println("File not found.");
		}

		while(reader.hasNextLine()) {
			vpl=reader.nextLine();
			if(vpl.startsWith("Song of Solomon")) {
				String[] arr=vpl.substring(16).split(" ",2);
				System.out.println("Song of Solomon"+"|"+arr[0]+"|"+arr[1]);
			} else if(vpl.startsWith("1") || vpl.startsWith("2") || vpl.startsWith("3")) {
				String book=vpl.substring(0,vpl.indexOf(" ",2));
				String[] arr=vpl.substring(vpl.indexOf(" ",2)+1).split(" ",2);
				System.out.println(book+"|"+arr[0]+"|"+arr[1]);
			} else {
				String[] arr=vpl.split(" ",3);
				System.out.println(arr[0]+"|"+arr[1]+"|"+arr[2]);
			}
		}

		reader.close();
	}
}
