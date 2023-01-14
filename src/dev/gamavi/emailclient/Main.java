package dev.gamavi.emailclient;

import java.sql.SQLException;

import dev.gamavi.emailclient.menu.ComposeMailMenu;
import dev.gamavi.emailclient.menu.DashboardMenu;
import dev.gamavi.emailclient.menu.LoginMenu;
import dev.gamavi.emailclient.menu.MainMenu;
import dev.gamavi.emailclient.menu.RegisterMenu;
import dev.gamavi.emailclient.menu.ReplyMenu;
import dev.gamavi.emailclient.menu.InboxMenu;
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
		ComposeMailMenu composeMenu = new ComposeMailMenu();
		ReplyMenu replyMenu = new ReplyMenu();

		mainMenu.setSwitchMenus(loginMenu, registerMenu);
		loginMenu.setSwitchMenus(dashboardMenu);
		dashboardMenu.setSwitchMenus(inboxMenu, composeMenu);

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
