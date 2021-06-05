import org.jibble.pircbot.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.time.*;



class BiblyBot extends PircBot {

    static final String    SERVER  = "irc.dal.net";
    static final int       PORT    = 6667;
    static final String    PASS    = "paanoanggagawinko";
    static final String    CHANNEL = "#bibly";
    static final String    NICK    = "siesto";
    static final String    MASTER  = "siesta";


    static Timer timer = null;

    static int allottedTime=10;
    static int remainingTime=allottedTime;

    BiblyBot() {
        setName(NICK);
    }

    public void onMessage(
        String channel,
        String sender,
        String login,
        String hostname,
        String message) {

        if(sender.equals(MASTER)) {
            if(message.equals(".quit")) {
                quitServer("bye!");
                System.exit(0);
            }
        }

        if (message.equals(".time")) {
            String time = new java.util.Date().toString();
            sendMessage(channel, sender + ": The time is now " + time);
        }

        if (message.equals(".countdown")) {

            if(timer==null) {

                ActionListener taskPerformer = new ActionListener() {

                    public void actionPerformed(ActionEvent e) {

                        remainingTime--;

                        if(remainingTime<=0) {
                            remainingTime=0;
                        }

                        sendMessage(channel, sender + ": countdown " + remainingTime);

                        if(remainingTime==0) {
                            timer.stop();
                        }
                    }
                };

                timer = new Timer(1000,taskPerformer);

                sendMessage(channel, sender + ": Timer is starts now.");
                remainingTime=allottedTime;
                timer.setInitialDelay(0);
                timer.start();

            } else if(!timer.isRunning()) {
                sendMessage(channel, sender + ": Timer is starts now.");
                remainingTime=allottedTime;
                timer.setInitialDelay(0);
                timer.start();
            } else {
                sendMessage(channel, sender + ": Timer is running.");
            }
        }
    }
}




public class Main {

    public static void main(String[] args) throws Exception {

        BiblyBot bot = new BiblyBot();

        bot.setVerbose(true);

        bot.connect(BiblyBot.SERVER, BiblyBot.PORT, BiblyBot.PASS);

        bot.joinChannel(BiblyBot.CHANNEL);

    }

}
