package dev.gamavi.emailclient.menu;

import java.util.List;

import dev.gamavi.emailclient.model.MailRecipient;
import dev.gamavi.emailclient.model.User;
import dev.gamavi.emailclient.service.MailService;
import dev.gamavi.emailclient.shared.Shared;
import dev.gamavi.emailclient.shared.Utils;

public class InboxMenu extends AbstractMenu {

	private final Shared shared = Shared.getInstance();
	private final MailService mailService = shared.getMailService();

	@Override
	public void show() {
		User currentUser = shared.getCurrentUser();
		List<MailRecipient> mailRecipients = mailService.findReceivedMails(currentUser.getEmail());

		Utils.clearScreen();

		System.out.println(
			"Viewing Email Inbox\n" +
			"-----------------------\n");

		if (mailRecipients.isEmpty()) {
			System.out.println("You haven't received any mails yet :(");
			Utils.waitForEnter();
			return;
		}

		mailService.printRecipientMailsTable(mailRecipients);
		int choice;
		do {
			choice = Utils.scanAbsoluteInt("Choose a mail by its number to read ['0' to cancel]: ");
			if (choice == 0) {
				return;
			}
		} while (choice < 1 || choice > mailRecipients.size());

		ReadMailMenu menu = (ReadMailMenu) this.getNextMenus()[0];
		menu.setMailRecipientToRead(mailRecipients.get(choice - 1));
		menu.show();
	}

}
