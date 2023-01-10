package dev.gamavi.emailclient;

import dev.gamavi.emailclient.menu.MainMenu;
import dev.gamavi.emailclient.menu.RegisterMenu;

public class Main {

	public Main() {
		MainMenu mainMenu = new MainMenu();
		mainMenu.show();
	}
	
	public static void main(String[] args) {
		new Main();
	}

}
