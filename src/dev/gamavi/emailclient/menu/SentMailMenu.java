package dev.gamavi.emailclient.menu;

import java.util.List;

import dev.gamavi.emailclient.model.Mail;
import dev.gamavi.emailclient.model.User;
import dev.gamavi.emailclient.service.MailService;
import dev.gamavi.emailclient.shared.Shared;
import dev.gamavi.emailclient.shared.Utils;

public class SentMailMenu extends AbstractMenu {
	
	private final Shared shared = Shared.getInstance();
	private final MailService mailService = shared.getMailService();

	@Override
	public void show() {
		User currentUser = Shared.getInstance().getCurrentUser();
		List<Mail> sentMails = mailService.findSentMails(currentUser.getEmail());
		
		Utils.clearScreen();
		
		System.out.println(
			"Viewing Sent Email\n" +
			"-----------------------\n");
		
		if (sentMails.isEmpty()) {
			System.out.println("You haven't sent any mails yet :(");
			Utils.waitForEnter();
			return;
		}

		mailService.printSentMailsTable(sentMails);
		
		int choice;
		do {
			choice = Utils.scanAbsoluteInt("Choose a mail by its number to read ['0' to cancel]: ");
			if (choice == 0) {
				return;
			}
		} while (choice < 1 || choice > sentMails.size());

		Mail selectedMail = sentMails.get(choice - 1);
		mailService.openMail(selectedMail);

		Utils.waitForEnter();
	}

}
