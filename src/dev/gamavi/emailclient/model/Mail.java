package dev.gamavi.emailclient.model;

public class Mail extends BaseModel {

	private Long id;
	private String title;
	private String message;
	private User sender;

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

}
