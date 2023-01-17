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

	private final Shared shared = Shared.getInstance();
	private final MailRecipientRepository recipientRepo = shared.getMailRecipientRepo();

	@Override
	public void show() {
		User currentUser = Shared.getInstance().getCurrentUser();
		List<MailRecipient> mailRecipients = recipientRepo.findAllByRecipientEmail(currentUser.getEmail());

		Utils.clearScreen();

		System.out.println(
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

		Mail selectedMail = openEmail(choice, mailRecipients);
		System.out.print(
			"Choose:\n" +
			"1. Reply\n" +
			"2. Reply to all\n" +
			"0. Back to dashboard\n");

		do {
			choice = Utils.scanAbsoluteInt(">> ");
			if (choice == 0) {
				return;
			}
		} while (choice < 1 || choice > 2);

		ReplyMenu replyMenu = (ReplyMenu) this.getNextMenus()[0];
		switch (choice) {
			case 1:
				replyMenu.setRepliedMail(selectedMail);
				break;
			case 2:
				// todo: add reply to all (meaning to all CC'd users)
				break;
		}

		replyMenu.show();
	}

	private Mail openEmail(int index, List<MailRecipient> mailRecipients) {
		MailRecipient mailRecipient = mailRecipients.get(index - 1);
		Mail mail = mailRecipient.getMail();

		System.out.println(
			"Sender: " + mail.getSender().getDisplayName() + "\n" +
			"Subject: " + mail.getTitle() + "\n" +
			"Message: " + mail.getMessage() + "\n");

		return mail;
	}

	private void printMailsTable(List<MailRecipient> mailRecipients) {
		User currentUser = Shared.getInstance().getCurrentUser();

		String tableHeader = String.format(
			"| %-3s | %-40s | %-20s | %-19s |",
			"No.", "Subject", "Sender", "Date");

		String rowFormat = "| %3s | %-40s | %-20s | %-19s |\n";
		String coloredRowFormat = "| $y%3s$r | $y%-40s$r | $y%-20s$r | $y%-19s$r |\n"
			.replaceAll("\\$y", Utils.ANSI_YELLOW)
			.replaceAll("\\$r", Utils.ANSI_RESET);

		String line = "+-----+------------------------------------------+----------------------+---------------------+";

		long unreadCount = recipientRepo.countUnreadMails(currentUser.getEmail());
		System.out.printf("Total unread mails: %d\n", unreadCount);

		System.out.println(line);
		System.out.println(tableHeader);
		System.out.println(line);

		int count = 0;
		for (MailRecipient mailRecipient : mailRecipients) {
			Mail mail = mailRecipient.getMail();
			User sender = mail.getSender();

			DateFormat formatter = new SimpleDateFormat("dd MMM yyyy - HH:mm");
			String chosenFormat = mailRecipient.isHasRead() ? rowFormat : coloredRowFormat;

			System.out.printf(
				chosenFormat,
				++count,
				mail.getTitle(),
				sender.getDisplayName(), formatter.format(mail.getCreatedAt()));
		}

		System.out.println(line);
	}

}
