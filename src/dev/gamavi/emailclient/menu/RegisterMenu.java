package dev.gamavi.emailclient.menu;

import dev.gamavi.emailclient.shared.Utils;

public class RegisterMenu extends AbstractMenu {
	
	@Override
	public void show() {
		Utils.clearScreen();
		
		String name, emailUsername, password;
		Boolean isEmailUsernameOk = false, isPasswordOk = false;
		
		System.out.print(
			"Register Menu\n"
			+ "-----------------------\n"
		);
		
		System.out.print("Your name: ");
		name = Utils.scan.nextLine();
		
		do {
			System.out.print("Username [your email will be username@nanadaime.net]: ");
			emailUsername = Utils.scan.nextLine().toLowerCase();
			
			isEmailUsernameOk = emailUsernameValidation(emailUsername);
		} while (!isEmailUsernameOk);
		
		do {
			System.out.print("Password: ");
			password = Utils.scan.nextLine();
			
			isPasswordOk = passwordValidation(password);
		} while (!isPasswordOk);
		
		// TODO: Insert user
		System.out.printf(
			"\nName: %s\n"
			+ "Email: %s@nanadaime.net\n"
			+ "Password: %s",
			name,
			emailUsername,
			password
		);
	}
	
	private Boolean emailUsernameValidation(String emailUsername) {
		
		/**
		 * Email Criteria:
		 * - Unique
		 * - Only letters;
		 * - Numbers;
		 * - Periods.
		 * - all lowercase (auto by system)
		 */
		
		// TODO: Check uniqueness [Waiting for UserRepository]
		for (char cUsername : emailUsername.toCharArray()) {
			if (!Character.isLetter(cUsername) && !Character.isDigit(cUsername) && cUsername != '.') {
				System.out.println("Username should only contains letters, numbers, and periods!");
				return false;
			}
		}
		
		return true;
	}
	
	// TODO: Password validation
	private Boolean passwordValidation(String password) {
		
		/**
		 * Password Criteria:
		 * - At least 8 chars
		 * - Must and only contains Upper;
		 * - Lower;
		 * - Number;
		 * - Special char;
		 */
		Boolean hasUpper = false, hasLower = false, hasNumbers = false, hasSpecialChars = false;
		
		if (password.length() < 8) {
			System.out.println("Password must have at least 8 characters!");
			return false;
		} else {
			for (char cPassword : password.toCharArray()) {
				if (Character.isUpperCase(cPassword) && !hasUpper) {
					hasUpper = true;
				}
				
				if (Character.isLowerCase(cPassword) && !hasLower) {
					hasLower = true;
				}
				
				if (Character.isDigit(cPassword) && !hasNumbers) {
					hasNumbers = true;
				}
				
				if (!(Character.isLetter(cPassword) || Character.isDigit(cPassword)) && !hasSpecialChars) {
					hasSpecialChars = true;
				}
			}
			
			if (!hasUpper) {
				System.out.println("Password must contains uppercase!");
				return false;
			}
			
			if (!hasLower) {
				System.out.println("Password must contains lowercase!");
				return false;
			}
			
			if (!hasNumbers) {
				System.out.println("Password must contains numbers!");
				return false;
			}
			
			if (!hasSpecialChars) {
				System.out.println("Password must contains special characters!");
				return false;
			}
		}
		
		return true;
	}
}
