package dev.gamavi.emailclient.model;

public class MailRecipient extends BaseModel {

	private Long id;
	private Mail mail;
	private User recipient;
	
	private ReceiveType type;
	private boolean hasRead;
	
	public Long getId() {
		return id;
	}
	
	public Mail getMail() {
		return mail;
	}
	
	public User getRecipient() {
		return recipient;
	}
	
	public ReceiveType getType() {
		return type;
	}
	
	public boolean isHasRead() {
		return hasRead;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setMail(Mail mail) {
		this.mail = mail;
	}
	
	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}
	
	public void setType(ReceiveType type) {
		this.type = type;
	}
	
	public void setHasRead(boolean hasRead) {
		this.hasRead = hasRead;
	}

}
