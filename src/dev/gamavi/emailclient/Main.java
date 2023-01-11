package dev.gamavi.emailclient;

import java.sql.SQLException;

import dev.gamavi.emailclient.menu.DashboardMenu;
import dev.gamavi.emailclient.menu.LoginMenu;
import dev.gamavi.emailclient.menu.MainMenu;
import dev.gamavi.emailclient.menu.RegisterMenu;
import dev.gamavi.emailclient.menu.ViewInboxMenu;
import dev.gamavi.emailclient.shared.Shared;

public class Main {

	public Main() throws SQLException {
		Shared shared = Shared.getInstance();
		shared.initDatabase();

		MainMenu mainMenu = new MainMenu();
		LoginMenu loginMenu = new LoginMenu();
		RegisterMenu registerMenu = new RegisterMenu();
		DashboardMenu dashboardMenu = new DashboardMenu();
		ViewInboxMenu inboxMenu = new ViewInboxMenu();

		mainMenu.setSwitchMenus(loginMenu, registerMenu);
		loginMenu.setSwitchMenus(dashboardMenu);
		dashboardMenu.setSwitchMenus(inboxMenu);

		mainMenu.show();
	}

	public static void main(String[] args) throws SQLException {
		new Main();
	}

}
