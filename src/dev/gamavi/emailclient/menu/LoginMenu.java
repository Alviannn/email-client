package dev.gamavi.emailclient.menu;

import dev.gamavi.emailclient.model.User;
import dev.gamavi.emailclient.repository.UserRepository;
import dev.gamavi.emailclient.shared.Shared;
import dev.gamavi.emailclient.shared.Utils;

public class LoginMenu extends AbstractMenu {

	private final Shared shared = Shared.getInstance();
	private final UserRepository userRepo = shared.getUserRepo();

	@Override
	public void show() {
		Utils.clearScreen();

		String emailString, passwordString;

		System.out.println(
			"Login Menu\n" +
			"-----------------------\n");

		System.out.print("Email: ");
		emailString = Utils.SCANNER.nextLine();

		System.out.print("Password: ");
		passwordString = Utils.SCANNER.nextLine();

		User foundUser = userRepo.findOne(emailString);
		if (foundUser == null) {
			System.out.println("User isn't registered.");
			Utils.waitForEnter();
			return;
		}
		if (!foundUser.getPassword().equals(passwordString)) {
			System.out.println("Password doesn't match.");
			Utils.waitForEnter();
			return;
		}

		shared.setCurrentUser(foundUser);

		AbstractMenu dashboardMenu = this.getNextMenus()[0];
		dashboardMenu.show();
	}

}
