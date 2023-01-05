package dev.gamavi.emailclient.menu;

import dev.gamavi.emailclient.shared.Utils;

public class LoginMenu implements IMenu {

	@Override
	public void show() {
		Utils.clearScreen();

		System.out.println(
			"Login Menu\n" +
			"-----------------------\n");
	}

}
