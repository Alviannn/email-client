package dev.gamavi.emailclient.shared;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Utils {

	public static Scanner SCANNER = new Scanner(System.in);

	/**
	 * Clears the console screen
	 */
	public static void clearScreen() {
		for (int i = 0; i < 100; i++) {
			System.out.println();
		}
	}

	/**
	 * Scans the console for integer input, but it MUST be integer.
	 * <p>
	 * This input scanner already clears the buffer automatically.
	 * <p>
	 * The word "absolute" from this function means it will try again and again
	 * until the scanner gets integer as its value.
	 *
	 * @param prefix The string printed before asking for input,
	 *               maybe something like ">> " can make it look pretty.
	 */
	public static int scanAbsoluteInt(String prefix) {
		while (true) {
			System.out.print(prefix);

			try {
				return SCANNER.nextInt();
			} catch (InputMismatchException ignored) {
				System.out.println("Input must be integer");
			} finally {
				// clears the buffer
				SCANNER.nextLine();
			}
		}
	}

	/**
	 * Ever had those in console app where it says "Press enter to continue..."?
	 * Yeah, this is it.
	 */
	public static void waitForEnter() {
		System.out.print("Press enter to continue...");
		SCANNER.nextLine();
	}

}
