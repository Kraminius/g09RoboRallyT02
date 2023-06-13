package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.chat.Server.ChatServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class G09RoboRally3WpApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(G09RoboRally3WpApplication.class, args);
        ChatServer.main(args);
    }

}
