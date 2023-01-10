package dev.gamavi.emailclient.menu;

import dev.gamavi.emailclient.shared.Utils;

public class MainMenu extends AbstractMenu{

	@Override
	public void show() {
		Utils.clearScreen();
		int choose;

		AbstractMenu loginMenu = this.getSwitchMenus()[0];
		AbstractMenu registerMenu = this.getSwitchMenus()[1];

		do {
			System.out.print(
					"Email client\n" +
					"====================\n" +
					"1. Login\n" +
					"2. Register\n" +
					"3. Exit\n" +
					">> ");
			choose = Utils.SCANNER.nextInt();
			if(choose==1) loginMenu.show();
			else if(choose==2) registerMenu.show();
			else if(choose==3) {
				System.out.println("Adios amigos!");
				System.exit(0);
			}else {
				System.out.println("Invalid input!");
			}
		}while(choose<1 || choose>3);
	}

}
