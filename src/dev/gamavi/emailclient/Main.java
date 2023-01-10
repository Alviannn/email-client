package dev.gamavi.emailclient;

import dev.gamavi.emailclient.menu.MainMenu;

public class Main {

	public Main() {
		

		MainMenu mainMenu = new MainMenu();
		mainMenu.show();
	}

	public static void main(String[] args) {
		new Main();
	}

}
