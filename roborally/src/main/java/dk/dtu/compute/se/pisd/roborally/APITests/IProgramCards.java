package dk.dtu.compute.se.pisd.roborally.APITests;

import dk.dtu.compute.se.pisd.roborally.model.*;

import java.util.List;

public interface IProgramCards {
    List<Command> getComCard(int playerNumb);

    Command getComCardSpecific(int playerNumb, int number);
}
