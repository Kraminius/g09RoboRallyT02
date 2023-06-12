package dk.dtu.compute.se.pisd.roborally.chat;

import dk.dtu.compute.se.pisd.roborally.view.Option;

/**
 * @author Freja Egelund Grønnemose, s224286@dtu.dk
 * Class representing client information.
 */
public class ClientInfo {
    private String username;

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Sets the username for the client.
     * @param username the username to set
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Gets the username of the client.
     * If the username is not set, prompts the user to enter a username.
     * @return the username of the client
     */
    public String getUsername(){
        if(username == null) getUserNameFromUser();
        return this.username;
    }

    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * Prompts the user to enter a username.
     */
    private void getUserNameFromUser(){
        Option option = new Option("Write your Username");
        username = option.getPromptedAnswer("Username");
    }

}
