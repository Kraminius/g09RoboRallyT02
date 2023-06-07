package dk.dtu.compute.se.pisd.roborally.chat;

import dk.dtu.compute.se.pisd.roborally.view.Option;

public class ClientInfo {
    private String username;

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        if(username == null) getUserNameFromUser();
        return this.username;
    }
    private void getUserNameFromUser(){
        Option option = new Option("Write your Username");
        username = option.getPromptedAnswer("Username");
    }

}
