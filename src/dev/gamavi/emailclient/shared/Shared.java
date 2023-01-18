package dev.gamavi.emailclient.shared;

import java.sql.SQLException;

import dev.gamavi.emailclient.model.User;
import dev.gamavi.emailclient.repository.MailRecipientRepository;
import dev.gamavi.emailclient.repository.MailRepository;
import dev.gamavi.emailclient.repository.UserRepository;
import dev.gamavi.emailclient.service.MailService;
import dev.gamavi.emailclient.service.UserService;

public class Shared {

	private static Shared instance;

	public static Shared getInstance() {
		if (instance == null) {
			instance = new Shared();
		}

		return instance;
	}

	private final SQLHelper helper;

	private final UserRepository userRepo;
	private final MailRepository mailRepo;
	private final MailRecipientRepository mailRecipientRepo;

	private final UserService userService;
	private final MailService mailService;

	/**
	 * Stores the currently authenticated user.
	 */
	private User currentUser;

	private Shared() {
		this.helper = new SQLHelper("localhost", 3306, "email_client", "root", "");
		this.userRepo = new UserRepository(helper);
		this.mailRepo = new MailRepository(helper);
		this.mailRecipientRepo = new MailRecipientRepository(helper);

		this.userService = new UserService(this);
		this.mailService = new MailService(this);
	}

	public SQLHelper getHelper() {
		return helper;
	}

	public UserRepository getUserRepo() {
		return userRepo;
	}

	public MailRepository getMailRepo() {
		return mailRepo;
	}

	public MailRecipientRepository getMailRecipientRepo() {
		return mailRecipientRepo;
	}

	public UserService getUserService() {
		return userService;
	}

	public MailService getMailService() {
		return mailService;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void initDatabase() throws SQLException {
		helper.connect();

		userRepo.createTable();
		mailRepo.createTable();
		mailRecipientRepo.createTable();
	}

}
