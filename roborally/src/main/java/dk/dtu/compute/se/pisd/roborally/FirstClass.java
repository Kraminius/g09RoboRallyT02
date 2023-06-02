package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.model.Board;

import java.util.Scanner;

public class FirstClass {

    public static void main(String[] args) throws Exception {
        System.out.println("Tab 1 to connect");
        Scanner scanner = new Scanner(System.in);
        String myAnswer = scanner.nextLine();

        Boolean bool;

        if(myAnswer.equals("1")){

            bool = MyClient.weConnect();

        }else{
            bool = false;
        }

        System.out.println(bool);

    }


}
