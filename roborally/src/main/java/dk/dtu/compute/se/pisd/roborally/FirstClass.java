package dk.dtu.compute.se.pisd.roborally;

import java.util.Scanner;

public class FirstClass {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Your player number: ");
        String num = scanner.nextLine();

        while(true){
            System.out.println("Tab 1 to connect");

            String myAnswer = scanner.nextLine();

            int  playerNum = Integer.parseInt(num);

            Boolean bool = null;

            if(myAnswer.equals("1")){

                GameClient.weConnect(playerNum-1, "jens");
                bool = GameClient.areAllConnected();

            }
            else if(myAnswer.equals("8")){
                break;
            } else if (myAnswer.equals("7")) {
                System.out.println("Number of players: ");
                String nue = scanner.nextLine();
                int numb = Integer.parseInt(nue);
                GameClient.instaGameData(numb);
            }else if(myAnswer.equals("3")){
                int playerNumber = Integer.parseInt(MyClient.playerNumber());
                System.out.println("Your player number is: " + playerNumber);
            } else if (myAnswer.equals("5")) {
                GameClient.instaGameData(5);
            } else if (myAnswer.equals("11")) {
                GameClient.addMapName("Dizzy MotherFucker Map");
            }else if(myAnswer.equals("12")){
                System.out.println("Map Name: " + GameClient.getMapName());
            }else if(myAnswer.equals("13")){

            }

            System.out.println("All conncted: " + bool);
        }



    }


}
