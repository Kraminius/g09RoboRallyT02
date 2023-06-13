package dk.dtu.compute.se.pisd.roborally.chat;

/**
 * @author Freja Egelund Grønnemose, s224286@dtu.dk
 * Interface for listening to message received events from the server
 */
public interface ViewListener {
    /**
     * @author Freja Egelund Grønnemose, s224286@dtu.dk
     * method invoked when a new message is received from the server.
     * @param message the received message
     */
    void onMessageReceived(String message);
}
