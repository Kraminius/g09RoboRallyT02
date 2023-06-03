package dk.dtu.compute.se.pisd.roborally.Server;

import dk.dtu.compute.se.pisd.roborally.model.CommandCardField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ServerController {

        @Autowired
        private PlayerProgramService playerProgramService;


    /**
     *  @author Nicklas Christensen     s224314.dtu.dk
     *  A method that allows the server to recieve programCardFields from players.
     *  This way it can then after all have given them share everyones card with everyone.
     * @param commandCardField
     * @param id
     * @return
     */
        @PostMapping("/playerProgram/{id}")
        public ResponseEntity<String > addProgram(@RequestBody CommandCardField commandCardField, @PathVariable int id)  {
            boolean added = playerProgramService.addProgram(commandCardField, id);
            if(added)
                return ResponseEntity.ok().body("added");
            else
                return ResponseEntity.internalServerError().body("not added");

        }


}
