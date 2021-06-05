import org.jibble.pircbot.*;
import java.time.*;



class BiblyBot extends PircBot {
    BiblyBot() {
        this.setName("siesto");
    }

    public void onMessage(
        String channel, String sender,
        String login, String hostname,
        String message) {

        if (message.equalsIgnoreCase("")) {
        }
    }
}



public class Main {
    public static void main(String[] args) throws Exception {

        // Start the bot up.
        BiblyBot bot = new BiblyBot();

        // Enable debugging output.
        bot.setVerbose(true);

        // Connect to an IRC server.
        bot.connect("irc.freenode.net",6667,"paanoanggagawinko");

        // Join the #irchacks channel.
        bot.joinChannel("#bibly");

    }
}
