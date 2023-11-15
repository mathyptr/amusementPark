import db.*;
import util.MessagesBundle;

/**
 * Welcome to a Muse ment Park!
 *
 */
public class Main 
{
    public static void main( String[] args )
    {
     	MessagesBundle msgB= new MessagesBundle();
		msgB.SetLanguage("en", "US");
		System.out.println(MessagesBundle.GetResourceValue("welcome_messages"));
//        System.out.println( "Welcome to a Muse ment Park!" );
    }
}
