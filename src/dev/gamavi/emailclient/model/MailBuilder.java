package dev.gamavi.emailclient.model;

public class MailBuilder implements IBuilder<Mail> {

	private Mail mail;
	
	public MailBuilder() {
		this.mail = new Mail();
	}
	
	public MailBuilder setTitle(String title) {
		mail.setTitle(title);
		return this;
	}
	
	public MailBuilder setMessage(String message) {
		mail.setMessage(message);
		return this;
	}
	
	public MailBuilder setSender(User sender) {
		mail.setSender(sender);
		return this;
	}
	
	public String getTitle() {
		return mail.getTitle();
	}
	
	public String getMessage() {
		return mail.getMessage();
	}
	
	public User getSender() {
		return mail.getSender();
	}

	@Override
	public Mail build() {
		return mail;
	}

}
