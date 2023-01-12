package dev.gamavi.emailclient.menu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import dev.gamavi.emailclient.model.Mail;
import dev.gamavi.emailclient.model.MailRecipient;
import dev.gamavi.emailclient.model.User;
import dev.gamavi.emailclient.repository.MailRecipientRepository;
import dev.gamavi.emailclient.shared.Shared;
import dev.gamavi.emailclient.shared.Utils;

public class InboxMenu extends AbstractMenu {

	private final MailRecipientRepository mailRecipientRepo = Shared.getInstance().getMailRecipientRepo();

	@Override
	public void show() {
		User currentUser = Shared.getInstance().getCurrentUser();
		List<MailRecipient> mailRecipients = mailRecipientRepo.findAllByRecipientEmail(currentUser.getEmail());

		Utils.clearScreen();

		System.out.print(
			"Viewing Email Inbox\n" +
			"-----------------------\n");

		if (mailRecipients.isEmpty()) {
			System.out.println("You haven't received any mails yet :(");
			Utils.waitForEnter();
			return;
		}

		this.printMailsTable(mailRecipients);
		int choice;
		do {
			choice = Utils.scanAbsoluteInt("Choose a mail by its number to read ['0' to cancel]: ");
			if (choice == 0) {
				return;
			}
		} while (choice < 1 || choice > mailRecipients.size());

		// todo: open read menu and bring the selected mail
	}

	private void printMailsTable(List<MailRecipient> mailRecipients) {
		String tableHeader = String.format(
			"| %-3s | %-40s | %-20s | %-19s |",
			"No.", "Subject", "Sender", "Date");

		String rowFormat = "| %3d | %-40s | %-20s | %-19s |\n";

		String line = "+-----+------------------------------------------+----------------------+---------------------+";
		System.out.println(line);
		System.out.println(tableHeader);
		System.out.println(line);

		int count = 0;
		for (MailRecipient mailRecipient : mailRecipients) {
			Mail mail = mailRecipient.getMail();
			User sender = mail.getSender();

			DateFormat formatter = new SimpleDateFormat("dd MMM yyyy - HH:mm");

			System.out.printf(
				rowFormat,
				++count,
				mail.getTitle(), sender.getDisplayName(), formatter.format(mail.getCreatedAt()));
		}

		System.out.println(line);
	}

}
