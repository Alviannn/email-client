package dev.gamavi.emailclient.menu;

import dev.gamavi.emailclient.model.User;
import dev.gamavi.emailclient.repository.UserRepository;
import dev.gamavi.emailclient.shared.Shared;
import dev.gamavi.emailclient.shared.Utils;

public class LoginMenu extends AbstractMenu {

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

		UserRepository userRepo = Shared.getInstance().getUserRepo();
		User foundUser = userRepo.findOne(emailString);

		// TODO: Show clear error message
		if (foundUser == null) {
			System.out.println("is null");
			// todo: email isn't registered
			return;
		}
		if (!foundUser.getPassword().equals(passwordString)) {
			System.out.println("not match");
			// todo: password doesn't match
			return;
		}

		Shared.getInstance().setCurrentUser(foundUser);

		AbstractMenu dashboardMenu = this.getSwitchMenus()[0];
		dashboardMenu.show();
	}

}
