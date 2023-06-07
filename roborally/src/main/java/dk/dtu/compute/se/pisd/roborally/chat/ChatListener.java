package dk.dtu.compute.se.pisd.roborally.chat;

import java.io.IOException;

public interface ChatListener {
    void onMessageSent(String message);

    void disconnectClient() throws IOException;
}
