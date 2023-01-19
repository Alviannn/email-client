package dev.gamavi.emailclient;

import java.sql.SQLException;

import dev.gamavi.emailclient.menu.*;
import dev.gamavi.emailclient.model.User;
import dev.gamavi.emailclient.shared.Shared;

public class Main {

	public Main() throws SQLException {
		Shared shared = Shared.getInstance();
		shared.initDatabase();

		MainMenu mainMenu = new MainMenu();
		LoginMenu loginMenu = new LoginMenu();
		RegisterMenu registerMenu = new RegisterMenu();
		DashboardMenu dashboardMenu = new DashboardMenu();
		InboxMenu inboxMenu = new InboxMenu();
		SentMailMenu sentMailMenu = new SentMailMenu();
		ComposeMailMenu composeMenu = new ComposeMailMenu();
		ReplyMenu replyMenu = new ReplyMenu();
		ForwardMailMenu forwardMailMenu = new ForwardMailMenu();

		mainMenu.setNextMenus(loginMenu, registerMenu);
		loginMenu.setNextMenus(dashboardMenu);
		dashboardMenu.setNextMenus(inboxMenu, sentMailMenu, composeMenu);
		inboxMenu.setNextMenus(replyMenu, forwardMailMenu);

		while (true) {
			User currentUser = shared.getCurrentUser();

			if (currentUser == null) {
				mainMenu.show();
			} else {
				dashboardMenu.show();
			}
		}
	}

	public static void main(String[] args) throws SQLException {
		new Main();
	}

}
