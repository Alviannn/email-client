package dev.gamavi.emailclient.menu;

import dev.gamavi.emailclient.model.User;
import dev.gamavi.emailclient.model.UserBuilder;
import dev.gamavi.emailclient.repository.UserRepository;
import dev.gamavi.emailclient.shared.Shared;
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
		name = Utils.SCANNER.nextLine();
		
		do {
			System.out.print("Username [your email will be username@nanadaime.net]: ");
			emailUsername = Utils.SCANNER.nextLine().toLowerCase();
			
			isEmailUsernameOk = emailUsernameValidation(emailUsername);
		} while (!isEmailUsernameOk);
		
		do {
			System.out.print("Password: ");
			password = Utils.SCANNER.nextLine();
			
			isPasswordOk = passwordValidation(password);
		} while (!isPasswordOk);

		User user = new UserBuilder()
			.setEmail(emailUsername + "@nanadaime.net")
			.setDisplayName(name)
			.setPassword(password)
			.build();

		UserRepository userRepo = Shared.getInstance().getUserRepo();
		userRepo.insert(user);
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
