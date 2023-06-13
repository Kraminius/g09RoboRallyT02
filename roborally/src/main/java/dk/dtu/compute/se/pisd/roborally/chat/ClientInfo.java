package dk.dtu.compute.se.pisd.roborally.chat;

import dk.dtu.compute.se.pisd.roborally.view.Option;

/**
 * @author Freja Egelund Grønnemose, s224286@dtu.dk
 * Class representing client information.
 */
public class ClientInfo {
    private static String username;

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Sets the username for the client.
     * @param newUsername the username to set
     */
    public static void setUsername(String newUsername){
        username = newUsername;
    }

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Gets the username of the client.
     * If the username is not set, prompts the user to enter a username.
     * @return the username of the client
     */
    public static String getUsername(){
        if(username == null) getUserNameFromUser();
        return username;
    }

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Prompts the user to enter a username.
     */
    private static void getUserNameFromUser(){
        Option option = new Option("Write your Username");
        username = option.getPromptedAnswer("Username");
    }

}
