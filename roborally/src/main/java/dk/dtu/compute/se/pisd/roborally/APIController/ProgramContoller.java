package dk.dtu.compute.se.pisd.roborally.APIController;


import java.util.List;

import dk.dtu.compute.se.pisd.roborally.APITests.*;
import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProgramContoller {

    @Autowired
    private IProgramCards programCards;

    //My first attempt at this
    @GetMapping("/CommandCard/{player}")
    public ResponseEntity<List<Command>> getComCard(@PathVariable int player) {
        List<Command> p;
        p = programCards.getComCard(player);
        return ResponseEntity.ok().body(p);

    }

    @GetMapping("/CommandCard/{player}/{number}")
    public ResponseEntity<Command> getComCardSpecific(@PathVariable int numb, int player) {
        Command p = programCards.getComCardSpecific(player, numb);
        return ResponseEntity.ok().body(p);
    }


}
