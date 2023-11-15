package util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Class used for internationalization and localization 
 * 
 * @author Mathilde Patrissi, Samuele Marrani
 */

public class MessagesBundle {
    private String language="it";
    private String country="IT";
    private Locale currentLocale;
    private static  ResourceBundle messages;
    
    public void MessagesBundle() {
        }
    /**
	 * Set the language: file localization occurs via the language country pair
	 * 
	 * @param language String
	 * @param country  String
	 */
    public void SetLanguage(String language, String country) {
    this.language=language;
    this.country=country;
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
