package util;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Class used for internationalization and localization 
 * 
 * @author Mathilde Patrissi, Samuele Marrani
 */

public class MessagesBundle {
    private static Locale currentLocale;
    private static  ResourceBundle messages;
    private static MessagesBundle instance = null;     

    // Private constructor (Singleton pattern)
    private MessagesBundle() {}    
    
    public static MessagesBundle getInstance() {
        // Create the object only if there's not an istance:
        if (instance == null) {
            instance = new MessagesBundle();
        }
        return instance;
    }
        
    /**
	 * Set the language: file localization occurs via the language country pair
	 * 
	 * @param language String
	 * @param country  String
	 */
    public static void SetLanguage(String language, String country) {
    currentLocale = new Locale(language, country);
    messages = ResourceBundle.getBundle("language.MessagesBundle", currentLocale);
    }
    /**
	 * Get the key's label
	 * 
	 * @param value String
	 * @return the key
	 */
    public static String GetResourceKey(String value) {
    	String key="";
		Enumeration  bundleKeys= messages.getKeys();
	    while (bundleKeys.hasMoreElements()) {
	         key = (String)bundleKeys.nextElement();
	         if(messages.getString(key).equals(value))
	        	 break;
	    }
    	return key;
	 
    }
    /**
	 * Get the label's key
	 * 
	 * @param key String
	 * @return the label
	 */
    public static String GetResourceValue(String key) {
    	return messages.getString(key);
	 
    }

}
