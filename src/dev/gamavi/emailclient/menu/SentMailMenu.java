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

public class SentMailMenu extends AbstractMenu {
	
	private final Shared shared = Shared.getInstance();
	private final MailRepository mailRepo = shared.getMailRepo();

	@Override
	public void show() {
		User currentUser = Shared.getInstance().getCurrentUser();
		List<Mail> sentMails = mailRepo.findAllBySenderEmail(currentUser.getEmail()); 
		
		Utils.clearScreen();
		
		System.out.println(
			"Viewing Sent Email\n" +
			"-----------------------\n");
		
		if (sentMails.isEmpty()) {
			System.out.println("You haven't sent any mails yet :(");
			Utils.waitForEnter();
			return;
		}

		this.printMailsTable(sentMails);
		
		int choice;
		do {
			choice = Utils.scanAbsoluteInt("Choose a mail by its number to read ['0' to cancel]: ");
			if (choice == 0) {
				return;
			}
		} while (choice < 1 || choice > sentMails.size());
	}
	
	private void printMailsTable(List<Mail> sentMails) {
		MailRecipientRepository recipientRepo = shared.getMailRecipientRepo();

		String tableHeader = String.format(
			"| %-3s | %-40s | %-20s | %-19s |",
			"No.", "Subject", "Recipient", "Date");

		String rowFormat = "| %3s | %-40s | %-20s | %-19s |\n";
		
		String line = "+-----+------------------------------------------+----------------------+---------------------+";

		long sentMailCount = sentMails.size();
		System.out.printf("Total sent mails: %d\n", sentMailCount);

		System.out.println(line);
		System.out.println(tableHeader);
		System.out.println(line);

		int count = 0;
		for (Mail mail : sentMails) {
			List<MailRecipient> mailRecipients = recipientRepo.findAllByMailId(mail.getId());
			
			for (MailRecipient mailRecipient : mailRecipients) {
				User recipient = mailRecipient.getRecipient();
				
				DateFormat formatter = new SimpleDateFormat("dd MMM yyyy - HH:mm");
				
				System.out.printf(
					rowFormat,
					++count,
					mail.getTitle(),
					recipient.getDisplayName(),
					formatter.format(mail.getCreatedAt())
				);
			}
		}

		System.out.println(line);
	}

}
