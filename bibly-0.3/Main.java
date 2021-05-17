import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;



class Vers {
    String bname="";
    int cnum;
    int svnum;
    int evnum;
    Vers() {}
}



enum TokenType {
    DEFAULT,
    NUMBER,
    STRING,
    COLON,
    DASH,
    SPACE,
    COMMA,
    EOF,
    UNKNOWN
}



class Token {
    TokenType type;
    String value;

    Token(TokenType type,String value) {
        this.type=type;
        this.value=value;
    }
}



enum LexerState {
    DEFAULT,
    NUMBER,
    STRING,
    COLON,
    DASH,
    SPACE,
    COMMA,
    EOF,
    UNKNOWN
}



class Lexer {

    LexerState state=LexerState.DEFAULT;

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

    ArrayList<Token> lex() {
        ArrayList<Token> tokens = new ArrayList<>();
        state = LexerState.DEFAULT;
        String value="";
        while(idx<num) {
            switch(state) {
            case DEFAULT:
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
            case NUMBER:
                while(idx<num && Character.isDigit(look(0))) {
                    value+=Character.toString(look(0));
                    idx++;
                }
                tokens.add(new Token(TokenType.NUMBER,value));
                value="";
                idx--;
                state=LexerState.DEFAULT;
                break;
            case STRING:
                while(idx<num && (Character.isLetter(look(0)) || look(0)==' ')) {
                    value+=Character.toString(look(0));
                    idx++;
                }
                tokens.add(new Token(TokenType.STRING,value.trim()));
                value="";
                idx--;
                state=LexerState.DEFAULT;
                break;
            default:
                break;
            }
            idx++;
        }
        tokens.add(new Token(TokenType.EOF,null));
        return tokens;
    }

}



class Interpreter {

    ArrayList<Token> tokens=new ArrayList<>();
    int pos=0;
    Token curtok=null;

    Interpreter(ArrayList<Token> tokens) {
        this.tokens=tokens;
    }

    Token next() {
        if(pos>tokens.size()-1) {
            return new Token(TokenType.EOF,null);
        }
        return tokens.get(pos++);
    }

    boolean match(TokenType type) {
        if(curtok.type==type) {
            return true;
        }
        return false;
    }

    Vers getLetter() {

        System.out.println("getLetter");

        Vers vers=new Vers();

        if(match(TokenType.NUMBER)) {
            vers.bname=curtok.value+" ";
            curtok=next();
        }

        if(match(TokenType.STRING)) {
            vers.bname+=curtok.value;
            curtok=next();
        }

        if(match(TokenType.NUMBER)) {
            try {
                vers.cnum=Integer.parseInt(curtok.value);
            } catch(Exception e) {
            }
            curtok=next();
        }

        if(match(TokenType.NUMBER)) {
            try {
                vers.svnum=Integer.parseInt(curtok.value);
            } catch(Exception e) {
            }
            curtok=next();
        }

        if(match(TokenType.NUMBER)) {
            try {
                vers.evnum=Integer.parseInt(curtok.value);
            } catch(Exception e) {
            }
            curtok=next();
        }

        return vers;
    }

    Vers getBook() {

        System.out.println("getBook");

        Vers vers=new Vers();

        if(match(TokenType.STRING)) {
            vers.bname+=curtok.value;
            curtok=next();
        }

        if(match(TokenType.NUMBER)) {
            try {
                vers.cnum=Integer.parseInt(curtok.value);
            } catch(Exception e) {
            }
            curtok=next();
        }

        if(match(TokenType.NUMBER)) {
            try {
                vers.svnum=Integer.parseInt(curtok.value);
            } catch(Exception e) {
            }
            curtok=next();
        }

        if(match(TokenType.NUMBER)) {
            try {
                vers.evnum=Integer.parseInt(curtok.value);
            } catch(Exception e) {
            }
            curtok=next();
        }

        return vers;
    }

    Vers expr() {

        System.out.println("expr");

        Vers vers=null;

        curtok=next();

        if(match(TokenType.NUMBER)) {
            vers=getLetter();
        }

        if(match(TokenType.STRING)) {
            vers=getBook();
        }

        return vers;
    }

}



class Main {

    static String version = "Bibly 0.1";

    static String bibleVersion = "kjv.csv";

    static 
    Font myFont = new Font(Font.MONOSPACED, Font.PLAIN, 18);

    static JFrame frame;
    static JLabel label;
    static JTextArea textarea;
    static JScrollPane scrollpane;
    static JTextField textfield;

    static void showHelp() {
        textarea.append("commands       -> action\n");
        textarea.append(".help          -> print this help\n");
        textarea.append(".clear         -> clear screen\n");
        textarea.append(".search [text] -> search for text\n\n");
    }

    static void showVerses(Vers vers) {
        System.out.println("show verses");
        try {
            InputStream in = Main.class.getResourceAsStream(bibleVersion);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));            
            String line="";
            while((line=reader.readLine())!=null) {
                String[] a1=line.split("\\|",3);
                String[] a2=a1[1].split("\\:",2);
                if(vers.svnum==0 && vers.evnum==0) {
                    if( vers.bname.equalsIgnoreCase(a1[0]) &&
                            vers.cnum==Integer.parseInt(a2[0])
                      ) textarea.append(line.replace("|"," ")+"\n\n");
                } else if(vers.svnum!=0 && vers.evnum!=0) {
                    if( vers.bname.equalsIgnoreCase(a1[0]) &&
                            vers.cnum==Integer.parseInt(a2[0]) &&
                            vers.svnum<=Integer.parseInt(a2[1]) &&
                            vers.evnum>=Integer.parseInt(a2[1])
                      ) textarea.append(line.replace("|"," ")+"\n\n");
                } else if(vers.svnum!=0 && vers.evnum==0) {
                    if( vers.bname.equalsIgnoreCase(a1[0]) &&
                            vers.cnum==Integer.parseInt(a2[0]) &&
                            vers.svnum==Integer.parseInt(a2[1])
                      ) textarea.append(line.replace("|"," ")+"\n\n");
                }
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void searchVerses(String needle) {
        try {
            InputStream in = Main.class.getResourceAsStream(bibleVersion);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));            
            String line="";
            int n=0;
            while((line=reader.readLine())!=null) {
                String haystack=line.replace("|"," ");
                if(haystack.toLowerCase().contains(needle.toLowerCase())) {
                    n++;
                    textarea.append(haystack+"\n\n");
                }
            }

            if(n==0) {
                textarea.append("None found.\n\n");
            } else if(n==1) {
                textarea.append("Found "+n+" occurence.\n\n");
            } else {
                textarea.append("Found "+n+" occurences.\n\n");
            }
            
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        frame=new JFrame(version);

        frame.setSize(640,480);

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

        textarea.append("type .help for commands\n\n");

        textfield.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if(e.getSource()==textfield) {

                    String text=textfield.getText().trim();

                    if(text.equals(".help")) {

                        showHelp();

                    } else if(text.equals(".clear")) {

                        textarea.setText("");

                    } else if(text.toLowerCase().startsWith(".search")) {

                        String textToSearch=text.substring(8);

                        searchVerses(textToSearch);

                    } else {

                        Lexer lexer=new Lexer(textfield.getText());

                        ArrayList<Token> tokens=lexer.lex();

                        Interpreter interpreter = new Interpreter(tokens);

                        Vers vers=interpreter.expr();

                        showVerses(vers);

                    }

                    textfield.setText("");

                }
            }

        });

        frame.setVisible(true);
    }
}

