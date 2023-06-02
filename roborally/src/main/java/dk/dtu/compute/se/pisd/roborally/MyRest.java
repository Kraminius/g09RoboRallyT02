package dk.dtu.compute.se.pisd.roborally;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MyRest {





    @GetMapping(value = "/connected")
    public ResponseEntity<String> connector(){
        return ResponseEntity.ok().body("we connected");
    }

    @PostMapping("/products")
    public ResponseEntity<Boolean> isConnected() {
        return ResponseEntity.ok().body(true);
    }


}
