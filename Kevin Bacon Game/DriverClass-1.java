/*
 * 
 * PS4
 * @Author Tooryananand Seetohul
 * Based on instructions from Professor Timothy Pierson
 * Driver class to play the Kevin Bacon Game
 */

import java.util.Scanner;

public class DriverClass extends KevinBaconGame{

	public static void main(String[] args) throws Exception {
		KevinBaconGame game = new KevinBaconGame();
		game.StartGame();
		
		Scanner in = new Scanner(System.in);
		String line = "";
		
		while (line != ("q")) {
			System.out.println("\nKevin Bacon game > \n");
			line = in.nextLine();
			game.HandleCases(line);
		}
		
		in.close();
	}
}
