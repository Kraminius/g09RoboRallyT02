package dk.dtu.compute.se.pisd.roborally.Client;

import dk.dtu.compute.se.pisd.roborally.Server.ServerProgramService;
import dk.dtu.compute.se.pisd.roborally.model.CommandCardField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class ClientController {

        @Autowired
        private ClientProgramService playerProgramService;


    /**
     *  @author Nicklas Christensen     s224314.dtu.dk
     *  A method that allows the server to recieve programCardFields from players.
     *  This way it can then after all have given them share everyones card with everyone.
     * @param commandCardField the list with all players commandCardFields
     * @return Wether we recieved and used the message or not
     */
        @PostMapping("/allPrograms")
        public ResponseEntity<String > addProgram(@RequestBody ArrayList<ArrayList<CommandCardField>> commandCardField)  {
            boolean added = playerProgramService.addAllProgram(commandCardField);
            if(added)
                return ResponseEntity.ok().body("acknowledged");
            else
                return ResponseEntity.internalServerError().body("not acknowledged");

        }


}
