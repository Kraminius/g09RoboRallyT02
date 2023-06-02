package dk.dtu.compute.se.pisd.roborally.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class AntennaHandler {


    /**
     * Created class to uphold MVC pattern, instead of having both logic and view components in GameController.
     * The responsibility of this class is to determine the turn sequence the players based on their position on the map.
     * The player closest to the antenna will get the first turn.
     *
     * @author Kenneth Kaiser, s221064@dtu.dk
     * Method for determining in what order the players will play their turn.
     */
    public List<Player> antennaPriority(Board board){

        List<Player> sortedPlayers = findPlayerSequence(board.getAntenna(), board);

        //Testing to see the player sequence in console
        System.out.println("Player sequence:");
        for (Player player : sortedPlayers) {
            System.out.println("Player " + player.getName());
        }

        //Maybe change how we save the sequence playerlist
        return sortedPlayers;
    }


    /**
     * @author Kenneth Kaiser, s221064@dtu.dk
     * Finds the player sequence, based on distance and position.
     * Then it sorts a list and returns it.
     * @param antenna
     * @return
     */
    public List<Player> findPlayerSequence(Space antenna, Board board) {

        List<Player> players = new ArrayList<>();

        //Used for checking the position of each player
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            players.add(board.getPlayer(i));
            System.out.println("x: " + board.getPlayer(i).getSpace().x+ " y: " + board.getPlayer(i).getSpace().x);

        }

        //Antenna sequence
        System.out.println("Antenna: " + antenna.x + " " +antenna.y);

        List<Player> sortedPlayers = new ArrayList<>(players);

        Collections.sort(sortedPlayers, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                double distance1 = calculateDistance(antenna, p1.getSpace());
                double distance2 = calculateDistance(antenna, p2.getSpace());
                if (distance1 == distance2) {
                    return compareByClockwiseRadar(antenna, p1, p2);
                } else {
                    return Double.compare(distance1, distance2);
                }

            }
        });

        return sortedPlayers;
    }

    /**
     * @author Kenneth Kaiser, s221064@dtu.dk
     * Calculates the distance between player and antenna in absolute numbers.
     * Then it returns the distance
     * @param space
     * @param antenna
     * @return
     */
    public double calculateDistance(Space space, Space antenna){
        int dx = Math.abs(space.x - antenna.x);
        int dy = Math.abs(space.y - antenna.y);

        return dx + dy;

    }

    /**
     * @author Kenneth Kaiser, s221064@dtu.dk
     * If 2 or more players have the same distance a rader will determine who goes first. This is based on a direction
     * then based on that direction it follows a clockwise movement.
     * @param antenna
     * @param p1
     * @param p2
     * @return
     */
    public int compareByClockwiseRadar(Space antenna, Player p1, Player p2) {

        //Right now the direction is always east, Antenna object should have a direction value on it
        String initialDirection = "east";

        double angle1 = Math.atan2(p1.getSpace().y - antenna.y, p1.getSpace().x - antenna.x);
        double angle2 = Math.atan2(p2.getSpace().y - antenna.y, p2.getSpace().x - antenna.x);

        angle1 = adjustAngle(angle1, initialDirection);
        angle2 = adjustAngle(angle2, initialDirection);

        if (angle1 < 0) {
            angle1 += 2 * Math.PI;
        }
        if (angle2 < 0) {
            angle2 += 2 * Math.PI;
        }

        return Double.compare(angle1, angle2);
    }

    /**
     * @author Kenneth Kaiser, s221064@dtu.dk
     * Adjusts the angle based on direction (North, west, east and south).
     * @param angle
     * @param initialDirection
     * @return
     */
    public double adjustAngle(double angle, String initialDirection) {
        double adjustment;
        switch (initialDirection.toLowerCase()) {
            case "north":
                adjustment = Math.PI / 2;
                break;
            case "west":
                adjustment = Math.PI;
                break;
            case "south":
                adjustment = -Math.PI / 2;
                break;
            case "east":
            default:
                adjustment = 0;
                break;
        }
        return angle + adjustment;
    }


}
