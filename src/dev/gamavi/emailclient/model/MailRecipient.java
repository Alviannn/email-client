package dev.gamavi.emailclient.model;

public class MailRecipient extends AbstractModel implements Cloneable {

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

	@Override
	public MailRecipient clone() {
		MailRecipient cloned = null;

		try {
			cloned = (MailRecipient) super.clone();

			// `id`, `created_at`, and `updated_at` fields are not to be copied
			// because they are strongly connected to the database.
			cloned.setId(null);
			cloned.setCreatedAt(null);
			cloned.setUpdatedAt(null);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return cloned;
	}

}
