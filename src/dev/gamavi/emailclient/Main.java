package dev.gamavi.emailclient;

import dev.gamavi.emailclient.menu.RegisterMenu;

public class Main {

	public Main() {
		RegisterMenu registerMenu = new RegisterMenu();
		
		registerMenu.show();
	}
	
	public static void main(String[] args) {
		new Main();
	}

}
