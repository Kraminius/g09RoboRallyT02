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

                MyClient.weConnect(playerNum-1);
                bool = GameClient.areAllConnected(playerNum-1);

            }
            else if(myAnswer.equals("8")){
                break;
            }

            System.out.println("All conncted: " + bool);
        }



    }


}
