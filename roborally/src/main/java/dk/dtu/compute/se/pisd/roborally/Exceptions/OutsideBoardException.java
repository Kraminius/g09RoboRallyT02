package dk.dtu.compute.se.pisd.roborally.Exceptions;

public class OutsideBoardException extends Exception{

    /**@author Freja Egelund Grønnemose, 224286@dtu.dk
     * A sepcial exception used to handle the situation where a player is moved outside of the board.
     */
    public OutsideBoardException() {
        super("Outside board");
    }

    public OutsideBoardException(String message) {
        super(message);
    }
}
