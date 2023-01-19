package dev.gamavi.emailclient.model;

import java.util.Optional;

public class Mail extends AbstractModel {

	private Long id;
	private String title;
	private String message;

	private User sender;
	private Optional<Mail> replyTo;

	public Mail() {
		this.replyTo = Optional.empty();
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getMessage() {
		return message;
	}

	public User getSender() {
		return sender;
	}

	public Optional<Mail> getReplyTo() {
		return replyTo;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public void setReplyTo(Mail replyTo) {
		this.replyTo = Optional.ofNullable(replyTo);
	}

}
