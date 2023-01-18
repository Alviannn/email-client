package dev.gamavi.emailclient.menu;

import dev.gamavi.emailclient.error.ServiceException;
import dev.gamavi.emailclient.service.UserService;
import dev.gamavi.emailclient.shared.Shared;
import dev.gamavi.emailclient.shared.Utils;

public class LoginMenu extends AbstractMenu {

	private final UserService userService = Shared.getInstance().getUserService();

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

		try {
			userService.login(emailString, passwordString);
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			Utils.waitForEnter();
			return;
		}

		AbstractMenu dashboardMenu = this.getNextMenus()[0];
		dashboardMenu.show();
	}

}
