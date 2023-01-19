package dev.gamavi.emailclient.menu;

import dev.gamavi.emailclient.model.Mail;
import dev.gamavi.emailclient.model.MailRecipient;
import dev.gamavi.emailclient.service.MailService;
import dev.gamavi.emailclient.shared.Shared;
import dev.gamavi.emailclient.shared.Utils;

public class ReadMailMenu extends AbstractMenu {

	private MailRecipient mailRecipientToRead;
	private final MailService mailService = Shared.getInstance().getMailService();

	@Override
	public void show() {
		mailService.openMailAndMarkRead(mailRecipientToRead);

		System.out.print(
			"Choose:\n" +
			"1. Reply\n" +
			"2. Reply to all\n" +
			"3. Forward mail\n" +
			"4. View replied mail\n" +
			"0. Back to dashboard\n");

		int choice;
		do {
			choice = Utils.scanAbsoluteInt(">> ");
			if (choice == 0) {
				return;
			}
		} while (choice < 1 || choice > 3);

		ReplyMenu replyMenu = (ReplyMenu) this.getNextMenus()[0];
		ForwardMailMenu forwardMailMenu = (ForwardMailMenu) this.getNextMenus()[1];
		Mail selectedMail = mailRecipientToRead.getMail();

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

	public MailRecipient getMailToRead() {
		return mailRecipientToRead;
	}

	public void setMailRecipientToRead(MailRecipient mailRecipient) {
		mailRecipientToRead = mailRecipient;
	}

}
