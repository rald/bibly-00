import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;


class Result {
	String bname="";
	int cnum;
	int svnum;
	int evnum;
	Result() {}
}


class Token {

	static final int 		DEFAULT           = 0,
                      NUMBER            = 1,
                      STRING            = 2,
                      COLON             = 3,
                      DASH              = 4,
                      SPACE							=	5,
                      COMMA							= 6,
                      EOF								= 7,
                      UNKNOWN						= 8;

	int type;
	String value;

	Token(int type,String value) {
		this.type=type;
		this.value=value;
	}
	
}



class LexerState {
	static final int 		DEFAULT           =  0,
                      NUMBER            =  1,
                      STRING            =  2,
                      COLON             =  3,
                      DASH              =  4,
                      SPACE							=	 5,
                      COMMA							=  6,
											EOF								=  7,
                      UNKNOWN						=	 8;
}



class Lexer {

	int state=LexerState.DEFAULT;

	String txt="";
	int idx=0;
	int num=0;

	Lexer(String txt) {
		this.txt=txt;
		this.num=txt.length();
	}

	char look(int ofs) {
		if(idx+ofs>=0 && idx+ofs<num) { 
			return txt.charAt(idx+ofs);
		}
		return '\0';
	}

	Vector lex() {
		Vector tokens = new Vector();
		state = LexerState.DEFAULT;
		String value="";
		while(idx<num) {
			switch(state) {
				case LexerState.DEFAULT:
					if(Character.isDigit(look(0))) {
						System.out.println("number");
						idx--;
						state=LexerState.NUMBER;							
					} else if(Character.isLetter(look(0))) {
						System.out.println("string");
						idx--;
						state=LexerState.STRING;							
					} else if(look(0)==':') {
						System.out.println("colon");
//						tokens.addElement(new Token(Token.COLON,":"));							
					} else if(look(0)=='-') {
						System.out.println("dash");
//						tokens.addElement(new Token(Token.DASH,"-"));							
					} else if(look(0)==',') {
						System.out.println("comma");
//						tokens.addElement(new Token(Token.COMMA,","));							
					} else if(look(0)==' ') {
						while(idx<num && look(0)==' ') {
							idx++;
						}
						idx--;
					}
				break;
				case LexerState.NUMBER:
					while(idx<num && Character.isDigit(look(0))) {
						value+=Character.toString(look(0));						
						idx++;
					}
					tokens.addElement(new Token(Token.NUMBER,value));				
					value="";
					idx--;
					state=LexerState.DEFAULT;
				break;
				case LexerState.STRING: 
					while(idx<num && (Character.isLetter(look(0)) || look(0)==' ')) {
						value+=Character.toString(look(0));						
						idx++;
					}
					tokens.addElement(new Token(Token.STRING,value.trim()));
					value="";
					idx--;
					state=LexerState.DEFAULT;
				break;
				default: break;
			}
			idx++;
		}
		tokens.addElement(new Token(Token.EOF,null));
		return tokens;
	}
	
}



class Interpreter {

	Vector tokens = null;
	int pos=0;
	Token curtok=null;

	Interpreter(Vector tokens) {
		this.tokens=tokens;
	}

	Token next() {
		if(pos>tokens.size()-1) { 
				return new Token(Token.EOF,null);
		}
		return ((Token)tokens.elementAt(pos++));
	}

	boolean match(int type) {
		if(curtok.type==type) {
			return true;
		} 
		return false;
	}


	Result getLetter() {

		System.out.println("getLetter");

		Result result=new Result();

		if(match(Token.NUMBER)) {
			result.bname=curtok.value+" ";
			curtok=next();
		}
				
		if(match(Token.STRING)) {
			result.bname+=curtok.value;
			curtok=next();
		}
		
		if(match(Token.NUMBER)) {
			try {
				result.cnum=Integer.parseInt(curtok.value);
			} catch(Exception e) {
			}
			curtok=next();
		}

		if(match(Token.NUMBER)) {
			try {
				result.svnum=Integer.parseInt(curtok.value);
			} catch(Exception e) {
			}
			curtok=next();
		}

		if(match(Token.NUMBER)) {
			try {
				result.evnum=Integer.parseInt(curtok.value);
			} catch(Exception e) {
			}
			curtok=next();
		}

		return result;
	}

	Result getBook() {

		System.out.println("getBook");

		Result result=new Result();

		if(match(Token.STRING)) {
			result.bname+=curtok.value;
			curtok=next();
		}
		
		if(match(Token.NUMBER)) {
			try {
				result.cnum=Integer.parseInt(curtok.value);
			} catch(Exception e) {
			}
			curtok=next();
		}

		if(match(Token.NUMBER)) {
			try {
				result.svnum=Integer.parseInt(curtok.value);
			} catch(Exception e) {
			}
			curtok=next();
		}

		if(match(Token.NUMBER)) {
			try {
				result.evnum=Integer.parseInt(curtok.value);
			} catch(Exception e) {
			}
			curtok=next();
		}

		return result;
	}

	Result expr() {

		System.out.println("expr");

		Result result=null;

		curtok=next();

		if(match(Token.NUMBER)) {
			result=getLetter();
		}

		if(match(Token.STRING)) {
			result=getBook();
		}

		return result;
	}

}

class Main {

	static String version = "Bibly 0.1";

	static String bibleVersion = "kjv.csv";

	static Font myFont = new Font("Sans-serif", Font.BOLD, 12);

	static JFrame frame;
	static JLabel label;
	static JTextArea textarea;
	static JScrollPane scrollpane;
	static JTextField textfield;

	static void showVerses(String bibleVersion,Result result) {
		System.out.println("show verses");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(bibleVersion));
			String line="";
			while((line=reader.readLine())!=null) {
				String[] a1=line.split("\\|",3);
				String[] a2=a1[1].split("\\:",2);
				if(result.svnum==0 && result.evnum==0) {
					if( result.bname.equalsIgnoreCase(a1[0]) &&
							result.cnum==Integer.parseInt(a2[0])
					) textarea.append(line.replace("|"," ")+"\n\n");
				} else if(result.svnum!=0 && result.evnum!=0) {
					if( result.bname.equalsIgnoreCase(a1[0]) &&
							result.cnum==Integer.parseInt(a2[0]) &&
							result.svnum<=Integer.parseInt(a2[1]) &&
							result.evnum>=Integer.parseInt(a2[1])
					) textarea.append(line.replace("|"," ")+"\n\n");
				} else if(result.svnum!=0 && result.evnum==0){
					if( result.bname.equalsIgnoreCase(a1[0]) &&
							result.cnum==Integer.parseInt(a2[0]) &&
							result.svnum==Integer.parseInt(a2[1])
					) textarea.append(line.replace("|"," ")+"\n\n");
				}
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) {

		frame=new JFrame(version);

		frame.setSize(320,240);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});

		frame.setLayout(new BorderLayout());

		textarea=new JTextArea();
		textarea.setEditable(false);
		textarea.setLineWrap(true);
		textarea.setFont(myFont);
//		textarea.setForeground(Color.BLUE);

		scrollpane=new JScrollPane(textarea);

		frame.add(scrollpane,BorderLayout.CENTER);

		textfield=new JTextField();
		textfield.setFont(myFont);
		frame.add(textfield,BorderLayout.SOUTH);

		textfield.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource()==textfield) {

					Lexer lexer=new Lexer(textfield.getText());

					Vector tokens=lexer.lex();

					Interpreter interpreter = new Interpreter(tokens);

					Result result=interpreter.expr();

					showVerses(bibleVersion,result);
				}
			}
		});

		frame.setVisible(true);

	}
}
