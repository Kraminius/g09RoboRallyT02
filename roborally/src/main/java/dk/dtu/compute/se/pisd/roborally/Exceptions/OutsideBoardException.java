package dk.dtu.compute.se.pisd.roborally.Exceptions;

public class OutsideBoardException extends Exception{
    public OutsideBoardException() {
        super("Outside board");
    }

    public OutsideBoardException(String message) {
        super(message);
    }
}
