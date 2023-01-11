package dev.gamavi.emailclient.model;

public class MailRecipientBuilder implements IBuilder<MailRecipient> {

	private MailRecipient mailRecipient;

	public MailRecipientBuilder() {
		this.mailRecipient = new MailRecipient();
	}

	public MailRecipientBuilder setMail(Mail mail) {
		mailRecipient.setMail(mail);
		return this;
	}

	public MailRecipientBuilder setRecipient(User recipient) {
		mailRecipient.setRecipient(recipient);
		return this;
	}

	public MailRecipientBuilder setType(ReceiveType type) {
		mailRecipient.setType(type);
		return this;
	}

	public MailRecipientBuilder setHasRead(boolean hasRead) {
		mailRecipient.setHasRead(hasRead);
		return this;
	}

	public Mail getMail() {
		return mailRecipient.getMail();
	}

	public User getRecipient() {
		return mailRecipient.getRecipient();
	}

	public ReceiveType getType() {
		return mailRecipient.getType();
	}

	public boolean isHasRead() {
		return mailRecipient.isHasRead();
	}

	@Override
	public MailRecipient build() {
		return mailRecipient;
	}

}
