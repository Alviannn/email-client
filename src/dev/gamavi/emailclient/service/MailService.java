package dev.gamavi.emailclient.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import dev.gamavi.emailclient.error.ServiceException;
import dev.gamavi.emailclient.model.*;
import dev.gamavi.emailclient.repository.MailRecipientRepository;
import dev.gamavi.emailclient.repository.MailRepository;
import dev.gamavi.emailclient.shared.Shared;
import dev.gamavi.emailclient.shared.Utils;

public class MailService extends AbstractService {

	private final MailRecipientRepository recipientRepo;
	private final MailRepository mailRepo;

	//TODO: Might wanna tidy up this one:
	private final UserService userService = shared.getUserService();

	public MailService(Shared shared) {
		super(shared);

		this.recipientRepo = shared.getMailRecipientRepo();
		this.mailRepo = shared.getMailRepo();
	}

	public void composeAndSend(Mail mail, List<MailRecipient> recipientList) {
		mailRepo.insert(mail);

		for (MailRecipient recipient : recipientList) {
			recipient.setMail(mail);
			recipientRepo.insert(recipient);
		}
	}

	public List<MailRecipient> findReceivedMails(String recipientEmail) {
		return recipientRepo.findAllByRecipientEmail(recipientEmail);
	}

	public List<Mail> findSentMails(String senderEmail) {
		return mailRepo.findAllBySenderEmail(senderEmail);
	}

	public List<MailRecipient> findMailRecipients(Long mailId) {
		return recipientRepo.findAllByMailId(mailId);
	}

	public void printRecipientMailsTable(List<MailRecipient> mailRecipients) {
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

	public void printSentMailsTable(List<Mail> sentMails) {
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
			List<MailRecipient> mailRecipients = this.findMailRecipients(mail.getId());

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

	public void openMail(Mail mail) {
		List<MailRecipient> currentRecipients = this.findMailRecipients(mail.getId());

		StringBuilder ccBuilder = new StringBuilder();
		StringBuilder recipientBuilder = new StringBuilder();

		for (MailRecipient recipient : currentRecipients) {
			switch (recipient.getType()) {
				case NORMAL:
					recipientBuilder.append(recipient.getRecipient().getDisplayName()).append("; ");
					break;
				case CARBON_COPY:
					ccBuilder.append(recipient.getRecipient().getDisplayName()).append("; ");
					break;
				default:
					break;
			}
		}

		System.out.println(
			"Sender   : " + mail.getSender().getDisplayName() + "\n" +
			"Recipient: " + recipientBuilder.toString() + "\n" +
			"CC       : " + ccBuilder.toString() + "\n" +
			"Subject  : " + mail.getTitle() + "\n" +
			"Message  :\n" +
			mail.getMessage() + "\n");
	}

	public void openMailAndMarkRead(MailRecipient recipient) {
		Mail mail = recipient.getMail();
		this.openMail(mail);

		if (!recipient.isHasRead()) {
			recipient.setHasRead(true);
			recipientRepo.update(recipient);
		}
	}

	public void scanRecipients(Scanner scanner, List<MailRecipient> recipientList) throws Exception {
		do {
			System.out.print("Recipient(s) address ['0' to cancel, separate by semicolon ';']: ");
			String line = scanner.nextLine();

			if (line.isEmpty()) {
				System.out.println("The mail recipient cannot be empty.");
				continue;
			}
			if (line.equals("0")) {
				throw new Exception();
			}

			try {
				List<User> userList = userService.parseMailAddresses(line);

				for (User user : userList) {
					MailRecipient recipient = new MailRecipientBuilder()
							.setRecipient(user)
							.setHasRead(false)
							.setType(ReceiveType.NORMAL)
							.build();

					recipientList.add(recipient);
				}
			} catch (ServiceException e) {
				System.out.println(e.getMessage());
				continue;
			}

			break;
		} while (true);
	}
}
