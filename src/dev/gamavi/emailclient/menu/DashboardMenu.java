package dev.gamavi.emailclient.menu;

import dev.gamavi.emailclient.model.User;
import dev.gamavi.emailclient.shared.Shared;
import dev.gamavi.emailclient.shared.Utils;

public class DashboardMenu extends AbstractMenu{

	@Override
	public void show() {
		Utils.clearScreen();

		Shared shared = Shared.getInstance();
		User currentUser = shared.getCurrentUser();

		int choose;
		do {
			System.out.print(
					"Welcome " + currentUser.getDisplayName() + " to Email Client\n" +
					"====================================\n" +
					"1. View inbox\n" +
					"2. View sent emails\n" +
					"3. Compose emails\n" +
					"4. Logout\n");
			choose = Utils.scanAbsoluteInt(">> ");
			if(choose==1) viewInbox();
			else if (choose == 2) viewSentEmails();
			else if(choose==3) composeEmail();
			else if(choose==4) {
				shared.setCurrentUser(null);
			}else {
				System.out.println("Invalid input!");
			}
		}while(choose<1 || choose>4);
	}

	void viewInbox() {
		AbstractMenu inboxMenu = this.getSwitchMenus()[0];
		inboxMenu.show();
	}
	
	void viewSentEmails() {
		AbstractMenu sentEmailsMenu = this.getSwitchMenus()[1];
		sentEmailsMenu.show();
	}

	void composeEmail() {
		AbstractMenu composeMenu = this.getSwitchMenus()[2];
		composeMenu.show();
	}

}
