/*
 *      By: Gerald Q. Cruz
 *
 *      for my friends
 *
 *      GOD Bless You Always
 *
 */


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

    static final int DEBUG=0;

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
                    if(DEBUG==1) System.out.println("number");
                    idx--;
                    state=LexerState.NUMBER;
                } else if(Character.isLetter(look(0))) {
                    if(DEBUG==1) System.out.println("string");
                    idx--;
                    state=LexerState.STRING;
                } else if(look(0)==':') {
                    if(DEBUG==1) System.out.println("colon");
//						tokens.addElement(new Token(Token.COLON,":"));
                } else if(look(0)=='-') {
                    if(DEBUG==1) System.out.println("dash");
//						tokens.addElement(new Token(Token.DASH,"-"));
                } else if(look(0)==',') {
                    if(DEBUG==1) System.out.println("comma");
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

    static final int DEBUG=0;

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

        if(DEBUG==1) System.out.println("getLetter");

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

        if(DEBUG==1) System.out.println("getBook");

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

        if(DEBUG==1) System.out.println("expr");

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

    static final int DEBUG=0;

    static final String newline=System.getProperty("line.separator");

    static final String cwd=System.getProperty("user.dir");

    static String version = "Bibly 0.6";

    static String bibleVersion = "kjv.csv";

    static String prefix = ".";

    static Font myFont = new Font(Font.MONOSPACED, Font.PLAIN, 20);

    static JFrame frame;
    static JLabel label;
    static JTextArea textarea;
    static JScrollPane scrollpane;
    static JTextField textfield;

    static void showHelp() {
        String helpText=
            "<html>"+
            "<table>"+
            "<tr><td>"+prefix+"help           <td>-&gt; print this help"+
            "<tr><td>"+prefix+"books          <td>-&gt; show books"+
            "<tr><td>"+prefix+"clear          <td>-&gt; clear screen"+
            "<tr><td>"+prefix+"pick           <td>-&gt; pick a random verse"+
            "<tr><td>"+prefix+"search [text]  <td>-&gt; search for text"+
            "<tr><td>"+prefix+"list           <td>-&gt; list bibly files"+
            "<tr><td>"+prefix+"save [filename]<td>-&gt; save text to bibly file"+   
            "<tr><td>"+prefix+"open [filename]<td>-&gt; load text from bibly file"+
            "<tr><td>"+prefix+"delete [filename]<td>-&gt; delete a bibly file"+
            "<tr><td>"+prefix+"rename [filename], [filename]<td>-&gt; rename a bibly file"+
            "</table></html>"+newline+newline;

        JTextPane helpTextPane=new JTextPane();
        helpTextPane.setContentType("text/html");
        helpTextPane.setFont(new Font(Font.MONOSPACED,Font.PLAIN,18));
        helpTextPane.setText(helpText);
        JOptionPane.showMessageDialog(null,helpTextPane,"Info",JOptionPane.INFORMATION_MESSAGE);
    }

    static void showVerses(Vers vers) {
        if(DEBUG==1) System.out.println("show verses");
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
                      ) textarea.append(line.replace("|"," ")+newline+newline);
                } else if(vers.svnum!=0 && vers.evnum!=0) {
                    if( vers.bname.equalsIgnoreCase(a1[0]) &&
                            vers.cnum==Integer.parseInt(a2[0]) &&
                            vers.svnum<=Integer.parseInt(a2[1]) &&
                            vers.evnum>=Integer.parseInt(a2[1])
                      ) textarea.append(line.replace("|"," ")+newline+newline);
                } else if(vers.svnum!=0 && vers.evnum==0) {
                    if( vers.bname.equalsIgnoreCase(a1[0]) &&
                            vers.cnum==Integer.parseInt(a2[0]) &&
                            vers.svnum==Integer.parseInt(a2[1])
                      ) textarea.append(line.replace("|"," ")+newline+newline);
                }
            }

            reader.close();

        } catch(Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }

    static void searchVerses(String needle) {
        if(DEBUG==1) System.out.println("search verses");
        try {
            InputStream in = Main.class.getResourceAsStream(bibleVersion);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line="";
            int n=0;
            while((line=reader.readLine())!=null) {
                String haystack=line.replace("|"," ");
                if(haystack.toLowerCase().contains(needle.toLowerCase())) {
                    n++;
                    textarea.append(haystack+newline+newline);
                }
            }

            if(n==0) {
                textarea.append("None found."+newline+newline);
            } else if(n==1) {
                textarea.append("Found "+n+" occurence."+newline+newline);
            } else {
                textarea.append("Found "+n+" occurences."+newline+newline);
            }

            reader.close();

        } catch(Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }

    static void showBooks() {
        if(DEBUG==1) System.out.println("show books");
        try {
            InputStream in = Main.class.getResourceAsStream(bibleVersion);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line="";

            ArrayList<String> books=new ArrayList<>();

            while((line=reader.readLine())!=null) {
                String[] a1=line.split("\\|",3);
                String[] a2=a1[1].split("\\:",2);
                if(books.indexOf(a1[0])==-1) books.add(a1[0]);
            }

            textarea.append("Books of the Bible"+newline+newline);
            textarea.append("Old Testament"+newline+newline);

            for(int i=0; i<books.size(); i++) {
                if(books.get(i).toString().equalsIgnoreCase("Matthew")) {
                    textarea.append(newline+newline+"New Testament"+newline+newline);
                } else if(i!=0) textarea.append(", ");
                textarea.append(books.get(i));
            }
            textarea.append(newline+newline);

            reader.close();

        } catch(Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }

    static void randomVerse() {
        if(DEBUG==1) System.out.println("random verses");
        try {
            InputStream in = Main.class.getResourceAsStream(bibleVersion);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line="";
            String chosen="";
            int lineno=0;
            while((line=reader.readLine())!=null) {
                if(Math.random()<1.0/(++lineno)) {
                    chosen=line;
                }
            }
            textarea.append(chosen.replace("|"," ")+newline+newline);
            reader.close();
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }

    static void save(String filename) {
        if(DEBUG==1) System.out.println("save");

        filename=cwd+"/bibly/"+filename+".bibly";

        if(filename.indexOf(",")!=-1) {
            JOptionPane.showMessageDialog(null,"Invalid character in filename.");            
            return;
        } 

        File file = new File(filename);

        if(file.exists() && !file.isDirectory()) {
            int dialogResult = JOptionPane.showConfirmDialog(null,"File already exists do you want to overwrite?","Warning",JOptionPane.YES_NO_OPTION);
            if(dialogResult == JOptionPane.NO_OPTION) {
                return;
            }
        }

        try {

            FileWriter writer = new FileWriter(filename);

            String[] lines=textarea.getText().split("\\n");

            for(int i=0; i<lines.length; i++) {
                String line=lines[i].trim();
                if(!line.isEmpty()) writer.write(line+newline+newline);
            }

            writer.close();

            JOptionPane.showMessageDialog(null,"File saved.");

        } catch(Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }

    static void open(String filename) {
        if(DEBUG==1) System.out.println("load");
        try {

            filename=cwd+"/bibly/"+filename+".bibly";

            BufferedReader reader = new BufferedReader(new FileReader(filename));

            String line="";

            while((line=reader.readLine())!=null) {
                textarea.append(line.trim()+newline);
            }

            reader.close();

        } catch(Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }

    static void showFileList(String ext) {
        if(DEBUG==1) System.out.println("show file list");
        File folder = new File(cwd+"/bibly");
        File[] listOfFiles = folder.listFiles();
        Arrays.sort(listOfFiles);
        String files="";
        boolean first=true;
        boolean found=false;

        for (int i=0; i<listOfFiles.length; i++) {
            String name=listOfFiles[i].getName();
            if (	listOfFiles[i].isFile() &&
                    name.toLowerCase().endsWith(ext)) {
                found=true;
                //if(first) first=false; else files+=", ";
                files+=name.substring(0,name.lastIndexOf("."))+newline;
            }
        }

        textarea.append("Bibly files:"+newline+newline);
        if(found) {
            textarea.append(files+newline);
        } else {
            textarea.append("None found."+newline);
        }
    }

    static void deleteFile(String filename) {
        if(DEBUG==1) System.out.println("delete file");
        filename=cwd+"/bibly/"+filename.trim()+".bibly";
        File file = new File(filename);
        if(file.exists() && !file.isDirectory()) {
            int dialogResult = JOptionPane.showConfirmDialog(null,"Do you want to delete file: "+filename,"Warning",JOptionPane.YES_NO_OPTION);
            if(dialogResult == JOptionPane.NO_OPTION) {
                return;
            }
        }
        if (file.delete()) {
            JOptionPane.showMessageDialog(null,"Deleted file: "+filename,"Info",JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,"Failed to delete: "+filename,"Warning",JOptionPane.WARNING_MESSAGE);
        }
    }


    static void rename(String params) {
        if(DEBUG==1) System.out.println("rename");
        try {

            String[] a=params.split("\\,",2);

            String oldname=cwd+"/bibly/"+a[0].trim()+".bibly";
            String newname=cwd+"/bibly/"+a[1].trim()+".bibly";

            if(DEBUG==1) System.out.println(oldname+", "+newname);

            File file1 = new File(oldname);

            File file2 = new File(newname);

            if (file2.exists()) {
                throw new java.io.IOException("File "+a[1]+" exists.");
            }

            boolean success = file1.renameTo(file2);

            if (success) {
                JOptionPane.showMessageDialog(null,"File renamed.","Info",JOptionPane.INFORMATION_MESSAGE);            
            } else {
                JOptionPane.showMessageDialog(null,"File not renamed.","Warning",JOptionPane.WARNING_MESSAGE);            
            }

        } catch(Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
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
//        textarea.setEditable(false);
        textarea.setLineWrap(true);
        textarea.setFont(myFont);
//		textarea.setForeground(Color.BLUE);

        scrollpane=new JScrollPane(textarea);

        frame.add(scrollpane,BorderLayout.CENTER);

        textfield=new JTextField();
        textfield.setFont(myFont);
        frame.add(textfield,BorderLayout.SOUTH);

        frame.addWindowListener( new WindowAdapter() {
            public void windowOpened( WindowEvent e ) {
                textfield.requestFocus();
            }
        });

        frame.setVisible(true);

        JOptionPane.showMessageDialog(null,"Type "+prefix+"help for commands.","Information",JOptionPane.INFORMATION_MESSAGE);

        textfield.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if(e.getSource()==textfield) {

                    String text=textfield.getText().trim();

                    if(text.isEmpty()) return;

                    if(text.equals(prefix+"help")) {

                        showHelp();

                    } else if(text.equals(prefix+"pick")) {

                        randomVerse();

                    } else if(text.startsWith(prefix+"delete")) {

                        String filename=text.substring(7).trim();

                        deleteFile(filename);

                    } else if(text.equals(prefix+"list")) {

                        showFileList("bibly");

                    } else if(text.startsWith(prefix+"save")) {

                        String filename=text.substring(5).trim();

                        save(filename);

                    } else if(text.startsWith(prefix+"open")) {

                        String filename=text.substring(5).trim();

                        open(filename);

                    } else if(text.startsWith(prefix+"rename")) {

                        String filename=text.substring(7).trim();

                        rename(filename);                                                

                    } else if(text.equals(prefix+"clear")) {

                        textarea.setText("");

                    } else if(text.equals(prefix+"books")) {

                        showBooks();

                    } else if(text.toLowerCase().startsWith(prefix+"search")) {

                        String textToSearch=text.substring(8).trim();

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

    }

}

