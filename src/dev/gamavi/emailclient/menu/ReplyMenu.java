package dev.gamavi.emailclient.menu;

import java.util.ArrayList;
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
import dev.gamavi.emailclient.service.MailService;
import dev.gamavi.emailclient.shared.Shared;
import dev.gamavi.emailclient.shared.Utils;

public class ReplyMenu extends AbstractMenu {

	private Mail repliedMail;
	private boolean isReplyAll;

	private final Shared shared = Shared.getInstance();
	private final MailService mailService = shared.getMailService();

	@Override
	public void show() {
		Utils.clearScreen();

		Scanner scan = Utils.SCANNER;

		// todo: output the "replied mail" to be the user's reference

		User currentUser = shared.getCurrentUser();
		User repliedSender = repliedMail.getSender();
		System.out.println("Replying to: " + repliedSender.getEmail());

		String content;
		try {
			content = this.scanContent(scan);
		} catch (Exception e) {
			// forced exception to indicate on cancelling the whole menu
			// or going 'back'
			return;
		}

		boolean shouldSend = Utils.scanAbsoluteConfirm("Are you sure that you want to send your reply email? [Y/N]: ");
		if (!shouldSend) {
			return;
		}

		Mail mail = new MailBuilder()
			.setSender(currentUser)
			.setReplyTo(repliedMail)
			.setMessage(content)
			.setTitle("RE: " + repliedMail.getTitle())
			.build();

		List<MailRecipient> replyRecipients = new ArrayList<>();
		List<MailRecipient> previousRecipients = mailService.findMailRecipients(repliedMail.getId());

		// for (MailRecipient recipient : previousRecipients) {
		// 	replyRecipients = 
		// }

		// MailRecipient recipient = new MailRecipientBuilder()
		// 	.setHasRead(false)
		// 	.setMail(mail)
		// 	.setRecipient(repliedSender)
		// 	.setType(ReceiveType.NORMAL)
		// 	.build();

		// recipientRepo.insert(recipient);
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

	public Mail getRepliedMail() {
		return repliedMail;
	}

	public boolean isReplyAll() {
		return isReplyAll;
	}

	public void setRepliedMail(Mail repliedMail) {
		this.repliedMail = repliedMail;
	}

	public void setIsReplyAll(boolean isReplyAll) {
		this.isReplyAll = isReplyAll;
	}

}
