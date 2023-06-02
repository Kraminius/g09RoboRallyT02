package dk.dtu.compute.se.pisd.roborally.model;

import org.springframework.stereotype.Service;

@Service
public class BoardService implements IBoardService {
    public static Board board;

    public BoardService(){
        Board board = new Board();
        this.board = board;
    }

    @Override
    public Board getBoard()
    {

        return board;
    }

    @Override
    public Player getPlayer(int playerNumber){
        return board.getPlayer(playerNumber);
    }

    //@Override
    public static boolean setBoard(Board board1){
        board = board1;
        return true;
    }


}
