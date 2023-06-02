package dk.dtu.compute.se.pisd.roborally.model;

public interface IBoardService {
    //static void setBoard(Board board1) {
      //  BoardService.board = BoardService.board;
    //}

    Board getBoard();

    Player getPlayer(int playerNumber);

    static boolean setBoard(Board board1) {
        return false;
    }
}
