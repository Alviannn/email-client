package dev.gamavi.emailclient.menu;

import java.util.List;

import dev.gamavi.emailclient.model.Mail;
import dev.gamavi.emailclient.model.MailRecipient;
import dev.gamavi.emailclient.model.User;
import dev.gamavi.emailclient.service.MailService;
import dev.gamavi.emailclient.shared.Shared;
import dev.gamavi.emailclient.shared.Utils;

public class InboxMenu extends AbstractMenu {

	// todo: separate inbox menu and read menu

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

		MailRecipient selectedRecipient = mailRecipients.get(choice - 1);
		mailService.openMailAndMarkRead(selectedRecipient);

		System.out.print(
			"Choose:\n" +
			"1. Reply\n" +
			"2. Reply to all\n" +
			"3. Forward mail\n" +
			"4. View replied mail\n" +
			"0. Back to dashboard\n");

		do {
			choice = Utils.scanAbsoluteInt(">> ");
			if (choice == 0) {
				return;
			}
		} while (choice < 1 || choice > 3);

		ReplyMenu replyMenu = (ReplyMenu) this.getNextMenus()[0];
		ForwardMailMenu forwardMailMenu = (ForwardMailMenu) this.getNextMenus()[1];
		Mail selectedMail = selectedRecipient.getMail();

		switch (choice) {
			case 1:
				replyMenu.setRepliedMail(selectedMail);
				replyMenu.show();
				break;
			case 2:
				replyMenu.setRepliedMail(selectedMail);
				replyMenu.setIsReplyAll(true);

				replyMenu.show();
				break;
			case 3:
				forwardMailMenu.setClonedMail(selectedMail.clone());
				forwardMailMenu.show();
				break;
			case 4:
				// todo: add view previous mail
				break;
		}
	}

}
