package dev.gamavi.emailclient.menu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import dev.gamavi.emailclient.model.Mail;
import dev.gamavi.emailclient.model.MailRecipient;
import dev.gamavi.emailclient.model.User;
import dev.gamavi.emailclient.repository.MailRecipientRepository;
import dev.gamavi.emailclient.repository.MailRepository;
import dev.gamavi.emailclient.shared.Shared;
import dev.gamavi.emailclient.shared.Utils;

public class ViewInboxMenu extends AbstractMenu {

	private final MailRepository mailRepo = Shared.getInstance().getMailRepo();
	private final MailRecipientRepository mailRecipientRepo = Shared.getInstance().getMailRecipientRepo();

	@Override
	public void show() {
		User currentUser = Shared.getInstance().getCurrentUser();
		List<MailRecipient> mailList = mailRecipientRepo.findAllByRecipientEmail(currentUser.getEmail());

		Utils.clearScreen();

		System.out.print(
			"Viewing Email Inbox\n" +
			"-----------------------\n");

		if (mailList.isEmpty()) {
			System.out.println("You haven't received any mails yet :(");
			Utils.waitForEnter();

			return;
		}

		String tableHeader = String.format(
			"| %-3s | %-40s | %-20s | %-11s |",
			"No.", "Subject", "Sender", "Date");

		String rowFormat = "| %3d | %-40s | %-20s | %-11s |";

		String line = "+-----+------------------------------------------+----------------------+-------------+";
		System.out.println(line);
		System.out.println(tableHeader);
		System.out.println(line);

		int count = 0;
		for (MailRecipient mailRecipient : mailList) {
			Mail mail = mailRecipient.getMail();
			User sender = mail.getSender();

			DateFormat formatter = new SimpleDateFormat("dd MM yyyy");

			System.out.printf(
				rowFormat,
				++count,
				mail.getTitle(), sender.getDisplayName(), formatter.format(mail.getCreatedAt()));
		}

		System.out.println(line);
	}

}
