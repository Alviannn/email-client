package dev.gamavi.emailclient.model;

public class UserBuilder implements IBuilder<User> {

	private User user;

	public UserBuilder() {
		this.user = new User();
	}

	public String getEmail() {
		return user.getEmail();
	}

	public String getDisplayName() {
		return user.getDisplayName();
	}

	public String getPassword() {
		return user.getPassword();
	}

	public UserBuilder setEmail(String email) {
		user.setEmail(email);
		return this;
	}

	public UserBuilder setDisplayName(String displayName) {
		user.setDisplayName(displayName);
		return this;
	}

	public UserBuilder setPassword(String password) {
		user.setPassword(password);
		return this;
	}

	@Override
	public User build() {
		return user;
	}

}
