import java.util.Scanner; 
import java.util.regex.Matcher; 
import java.util.regex.Pattern; 

public class Parser {

	static String[] tests = {
		"Genesis 1:1",
		"John 3:16",
		"1 John 1:2",
		"Song of Solomon 2:3",
		"2 John 2:1-3",
	};

	static String cbname=null;
	static String ccnumber=null;
	static String svnumber=null;
	static String evnumber=null;

	static void print(String text) {
		System.out.print(text);
	}

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
    } else {
 			System.out.println("NO MATCH");
    }

	}

	public static void main(String[] args) {
		
		for(String test:tests)
			parse(test);

	}

}
