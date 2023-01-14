package dev.gamavi.emailclient.menu;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import dev.gamavi.emailclient.model.Mail;
import dev.gamavi.emailclient.model.MailBuilder;
import dev.gamavi.emailclient.model.MailRecipient;
import dev.gamavi.emailclient.model.MailRecipientBuilder;
import dev.gamavi.emailclient.model.ReceiveType;
import dev.gamavi.emailclient.model.User;
import dev.gamavi.emailclient.repository.MailRecipientRepository;
import dev.gamavi.emailclient.repository.MailRepository;
import dev.gamavi.emailclient.repository.UserRepository;
import dev.gamavi.emailclient.shared.Shared;
import dev.gamavi.emailclient.shared.Utils;

public class ComposeMailMenu extends AbstractMenu {

	private final Shared shared = Shared.getInstance();
	private final UserRepository userRepo = shared.getUserRepo();
	private final MailRepository mailRepo = shared.getMailRepo();
	private final MailRecipientRepository recipientRepo = shared.getMailRecipientRepo();

	@Override
	public void show() {
		Utils.clearScreen();
		Scanner scanner = Utils.SCANNER;

		System.out.print(
			"Compose Mail Menu\n" +
			"-----------------------\n");

		List<User> targetUsers, ccUsers, bccUsers;
		String subject, content;
		User currentUser = shared.getCurrentUser();

		try {
			targetUsers = this.scanRecipients(scanner);
			subject = this.scanSubject(scanner);
			ccUsers = this.scanCarbonCopy(scanner);
			bccUsers = this.scanBlindCarbonCopy(scanner);
			content = this.scanContent(scanner);
		} catch (Exception e) {
			// forced exception to indicate on cancelling the whole menu
			// or going 'back'
			return;
		}

		boolean shouldSend = Utils.scanAbsoluteConfirm("Are you sure that you want to send this email? [Y/N]: ");
		if (!shouldSend) {
			return;
		}

		Mail mail = new MailBuilder()
			.setTitle(subject)
			.setMessage(content)
			.setSender(currentUser)
			.build();

		mailRepo.insert(mail);

		for (User user : targetUsers) {
			MailRecipient recipient = new MailRecipientBuilder()
				.setMail(mail)
				.setHasRead(false)
				.setRecipient(user)
				.setType(ReceiveType.NORMAL)
				.build();

			recipientRepo.insert(recipient);
		}
		for (User user : ccUsers) {
			MailRecipient recipient = new MailRecipientBuilder()
				.setMail(mail)
				.setHasRead(false)
				.setRecipient(user)
				.setType(ReceiveType.CARBON_COPY)
				.build();

			recipientRepo.insert(recipient);
		}
		for (User user : bccUsers) {
			MailRecipient recipient = new MailRecipientBuilder()
				.setMail(mail)
				.setHasRead(false)
				.setRecipient(user)
				.setType(ReceiveType.BLIND_CARBON_COPY)
				.build();

			recipientRepo.insert(recipient);
		}
	}

	private List<User> scanRecipients(Scanner scanner) throws Exception {
		List<User> recipientList;

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
				recipientList = Utils.parseEmailInput(line, userRepo);
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				continue;
			}

			break;
		} while (true);

		return recipientList;
	}

	private String scanSubject(Scanner scanner) throws Exception {
		String subject;
		do {
			System.out.print("Subject ['0' to cancel]: ");
			subject = scanner.nextLine();

			if (subject.isEmpty()) {
				System.out.println("Subject cannot be empty.`");
				continue;
			}
			if (subject.equals("0")) {
				throw new Exception();
			}

			break;
		} while (true);

		return subject;
	}

	private String scanContent(Scanner scanner) throws Exception {
		StringBuilder contentBuilder = new StringBuilder();

		String contentPrompt = "Enter the content of your email ['/cancel' to cancel, '/end' to save and send email]:";
		System.out.println(contentPrompt);

		while (scanner.hasNext()) {
			String line = scanner.nextLine();

			if (line.equals("/cancel")) {
				throw new Exception();
			} else if (line.equals("/end")) {
				if (contentBuilder.length() == 0) {
					System.out.println(contentPrompt);
					continue;
				}

				break;
			} else {
				contentBuilder.append(line);
			}
		}

		return contentBuilder.toString();
	}

	private List<User> scanCarbonCopy(Scanner scanner) throws Exception {
		List<User> ccUsers = new ArrayList<>();
		boolean useCC = Utils.scanAbsoluteConfirm("Do you want to include a CC (Carbon Copy) [Y/N]: ");

		if (!useCC) {
			return ccUsers;
		}

		do {
			System.out.print("CC ['0' to cancel, separate by semicolon ';']: ");
			String line = scanner.nextLine();

			if (line.isEmpty()) {
				System.out.println("The mail recipient cannot be empty.");
				continue;
			}
			if (line.equals("0")) {
				throw new Exception();
			}

			try {
				ccUsers = Utils.parseEmailInput(line, userRepo);
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				continue;
			}

			break;
		} while (true);

		return ccUsers;
	}

	private List<User> scanBlindCarbonCopy(Scanner scanner) throws Exception {
		List<User> bccUsers = new ArrayList<>();
		boolean useBCC = Utils.scanAbsoluteConfirm("Do you want to include a BCC (Blind Carbon Copy) [Y/N]: ");

		if (!useBCC) {
			return bccUsers;
		}

		do {
			System.out.print("BCC ['0' to cancel, separate by semicolon ';']: ");
			String line = scanner.nextLine();

			if (line.isEmpty()) {
				System.out.println("The mail recipient cannot be empty.");
				continue;
			}
			if (line.equals("0")) {
				throw new Exception();
			}

			try {
				bccUsers = Utils.parseEmailInput(line, userRepo);
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				continue;
			}

			break;
		} while (true);

		return bccUsers;
	}

}
