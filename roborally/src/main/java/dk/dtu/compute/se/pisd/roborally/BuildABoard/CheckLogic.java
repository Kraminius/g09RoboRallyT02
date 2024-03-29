package dk.dtu.compute.se.pisd.roborally.BuildABoard;

import java.util.ArrayList;

public class CheckLogic {
    private BoardBuild build;
    private CheckBoardBuild collection;
    /**
     * @Author Tobias Gørlyk s224271
     * Constructor for this class. Setting the curren build and the collection of ArrayList of different elements.
     */
    public CheckLogic(BoardBuild build, CheckBoardBuild sortedBoardBuild) {
        this.build = build;
        this.collection = sortedBoardBuild;
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Checks a string to see if it can be a name for the board.
     * It both checks if it is trying to overwrite an original board which they shouldn't and also if the first character in the name cant be in the file name.
     * @param name name of the board
     * @return The error string if it is wrong, null if all is okay
     */
    public static String checkName(String name){
        if(name == null || name.equals("")){ //Check if no name has been written in input
            return "You have not written a name for your board. Please input a name.";
        }
        if(name.toCharArray()[0] == ' ') return "You cannot name your board with a spacebar character as your first character.";
        if(Character.isDigit(name.toCharArray()[0])) return "You cannot name your board with a number character as your first character.";
        if(!Character.isLetter(name.toCharArray()[0])) return "You cannot name your board with a character that is not a letter, as your first character.";
        //Check if name is an Original board
        String original = isOriginal(name);
        if(original != null) return original;
        return null;
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Checks if a string is an original board.
     * @param name the name
     * @return the error string, or null if all is okay.
     */
    public static String isOriginal(String name){
        name = name.toLowerCase();
        switch (name){
            case "chop shop challenge":
            case "dizzy highway":
            case "fractionation":
            case "testboard":
            case "testboardempty":
                return "You cannot overwrite an original board. Please save under a new name.";
        }
        return null;
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Checks the board if it has the correct numbers on the checkpoint
     * Checks the board if it has the correct numbers on the startFields
     * Checks the board if it has an antenna
     * Checks the board if it has a respawnField
     * @return String error, null if no errors.
     */
    public String checkBoard(){
        String checkpointCheck = checkCheckPoints();
        if(checkpointCheck != null) return checkpointCheck;
        String antennaCheck = checkAntenna();
        if(antennaCheck != null) return antennaCheck;
        String respawnCheck = checkRespawn();
        if(respawnCheck != null) return respawnCheck;
        String startFieldCheck = checkStartFields();
        if(startFieldCheck != null) return startFieldCheck;
        return null;
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Checks the board if it has the correct numbers on the checkpoint
     * @return String error, null if no errors.
     */
    private String checkCheckPoints(){
        int amount = collection.getCheckpoints().size();
        if(amount < 1) return "You must have at least one checkpoint";
        if(amount > 6) return "You must have a maximum of six checkpoints";
        int next = 1;
        ArrayList<BoardBuildElement> elementArrayList = collection.getCheckpoints();
        for(int i = 0; i < amount; i++){
            for(int n = 0; n < elementArrayList.size(); n++){
                if(elementArrayList.get(n).getCheckpoint() == next) {
                    elementArrayList.remove(n);
                    next++;
                }
            }
        }
        if(next != amount+1) return "You dont have the full range of checkpoints. You cannot skip checkpoints. ex: 1-2-4 is not allowed. You must use 1-2-3.";
        return null;
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Checks the board if it has an antenna
     * @return String error, null if no errors.
     */
    private String checkAntenna(){
        int size = collection.getAntenna().size();
        if(size == 0) return "You do not have an antenna on your board. You must have one to save and play on the board.";
        if(size == 1) return null;
        return "You have too many antennas on your board. You cannot have more than one.";
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Checks the board if it has a respawnField
     * @return String error, null if no errors.
     */
    private String checkRespawn(){
        int size = collection.getRespawn().size();
        if(size == 0) return "You do not have a reboot field on your board. You must have one to save and play on the board.";
        if(size == 1) return null;
        return "You have too many reboot fields on your board. You cannot have more than one.";
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Checks the board if it has the correct numbers on the startFields
     * @return String error, null if no errors.
     */
    private String checkStartFields(){
        int amount = collection.getStartFields().size();
        if(amount < 6) return "You must have all six start fields on your board, to accommodate for games with up to six players.";
        if(amount > 6) return "You must have a maximum of six start fields on your board.";
        int next = 1;
        ArrayList<BoardBuildElement> elementArrayList = collection.getStartFields();
        for(int i = 0; i < amount; i++){
            for(int n = 0; n < elementArrayList.size(); n++){
                if(elementArrayList.get(n).getStartField() == next) {
                    elementArrayList.remove(n);
                    next++;
                }
            }
        }
        if(next != amount+1) return "You dont have the full range of start fields on your board. You must have only one of each number.";
        return null;
    }
}
