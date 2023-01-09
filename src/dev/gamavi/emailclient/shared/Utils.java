package dev.gamavi.emailclient.shared;

import java.util.Scanner;

public class Utils {

	public static Scanner scan = new Scanner(System.in);
	
	public static void clearScreen() {
		for (int i = 0; i < 100; i++) {
			System.out.println();
		}
	}

}
