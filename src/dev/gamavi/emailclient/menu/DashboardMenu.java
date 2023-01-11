package dev.gamavi.emailclient.menu;

import dev.gamavi.emailclient.shared.Utils;

public class DashboardMenu extends AbstractMenu{

	@Override
	public void show() {
		Utils.clearScreen();
		int choose;
		do {
			System.out.print(
					"Welcome USER to Email Client\n" + 
					"====================================\n" +
					"1. View emails\n" +
					"2. Compose emails\n" +
					"3. Exit\n");
			choose = Utils.scanAbsoluteInt(">> ");
			if(choose==1) viewEmail();
			else if(choose==2) composeEmail();
			else if(choose==3) {
				System.out.println("Adios amigos!");
				System.exit(0);
			}else {
				System.out.println("Invalid input!");
			}
		}while(choose<1 || choose>3);
	}
	
	void viewEmail() {
		
	}
	
	void composeEmail() {
		
	}

}
