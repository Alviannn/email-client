package dev.gamavi.emailclient;

import dev.gamavi.emailclient.menu.LoginMenu;
import dev.gamavi.emailclient.menu.MainMenu;
import dev.gamavi.emailclient.menu.RegisterMenu;

public class Main {

	public Main() {
		MainMenu mainMenu = new MainMenu();
		LoginMenu loginMenu = new LoginMenu();
		RegisterMenu registerMenu = new RegisterMenu();

		mainMenu.setSwitchMenus(loginMenu, registerMenu);

		mainMenu.show();
	}

	public static void main(String[] args) {
		new Main();
	}

}
