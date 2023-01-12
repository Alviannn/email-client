package dev.gamavi.emailclient.shared;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import dev.gamavi.emailclient.model.User;
import dev.gamavi.emailclient.repository.UserRepository;

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
	 * Scans the console for Yes (Y) and No (N) question to the user.
	 * It is mainly used for confirming something to the user.
	 *
	 * @param prefix The input prompt that the user will see.
	 */
	public static boolean scanAbsoluteConfirm(String prefix) {
		while (true) {
			System.out.print(prefix);

			String line = SCANNER.nextLine();
			if (!line.equals("Y") && !line.equals("N")) {
				continue;
			}

			return line.equals("Y");
		}
	}

	/**
	 * Ever had those in console app where it says "Press enter to continue..."?
	 * Yeah, this function is exactly to replicate that behavior.
	 */
	public static void waitForEnter() {
		System.out.print("Press enter to continue...");
		SCANNER.nextLine();
	}

	/**
	 * In this application, we need to scan email inputs
	 * from the user for something like composing an email.
	 * <p>
	 * It's here since we may always use this specific functionality
	 * to parse such as 'email1;email2' inputs.
	 *
	 * @param input The email input (for the recipients, cc, bcc, etc.) separated by ';'.
	 * @param repo The user repository, will be used to validate the email exisitence.
	 * @throws InputMismatchException When an email doesn't exists within the database.
	 */
	public static List<User> parseEmailInput(String input, UserRepository repo) throws InputMismatchException {
		List<User> users = new ArrayList<>();

		String[] emails = input.split(";");
		for (String email : emails) {
			User user = repo.findOne(email);
			if (user == null) {
				throw new InputMismatchException("Cannot find user with email '" + email + "'.");
			}

			users.add(user);
		}

		return users;
	}

}
