package dev.gamavi.emailclient.model;

public class User extends AbstractModel {

	private String email;
	private String displayName;
	private String password;

	public String getEmail() {
		return email;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getPassword() {
		return password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
