package dev.gamavi.emailclient.menu;

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
		emailString = Utils.SCANNER.next();
		System.out.print("Password: ");
		passwordString = Utils.SCANNER.next();
		if(emailValidation(emailString) && passwordValidation(passwordString)) {
			DashboardMenu dashboardMenu = new DashboardMenu();
			dashboardMenu.show();
		}
	}
	
	boolean emailValidation(String email) {
		//ToDo: cek data dari database
		return true;
	}
	
	boolean passwordValidation(String password) {
		//ToDo: cek data dari database
		return true;
	}

}
