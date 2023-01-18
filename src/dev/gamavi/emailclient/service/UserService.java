package dev.gamavi.emailclient.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.gamavi.emailclient.error.ServiceException;
import dev.gamavi.emailclient.model.User;
import dev.gamavi.emailclient.repository.UserRepository;
import dev.gamavi.emailclient.shared.Shared;

public class UserService extends AbstractService {

	private final UserRepository userRepo;

	public UserService(Shared shared) {
		super(shared);

		this.userRepo = shared.getUserRepo();
	}

	public void register(User user) throws ServiceException {
		Optional<User> optional = userRepo.findOneById(user.getEmail());
		optional.orElseThrow(() -> new ServiceException("Username must be unique!"));

		userRepo.insert(user);
	}

	public User login(String email, String password) throws ServiceException {
		Optional<User> optional = userRepo.findOneById(email);
		User user = optional.orElseThrow(() -> new ServiceException("User isn't registered."));

		if (!user.getPassword().equals(password)) {
			throw new ServiceException("Password doesn't match.");
		}

		shared.setCurrentUser(user);
		return user;
	}

	/**
	 * In this application, we need to scan email inputs
	 * from the user for something like composing an email.
	 * <p>
	 * It's here since we may always use this specific functionality
	 * to parse such as 'email1;email2' inputs.
	 *
	 * @param input The email input (for the recipients, cc, bcc, etc.) separated by ';'.
	 * @param repo The user repository, will be used to validate the email exisitence.
	 * @throws ServiceException When an email doesn't exists within the database.
	 */
	public List<User> parseMailAddresses(String input) throws ServiceException {
		List<User> users = new ArrayList<>();

		String[] emails = input.split(";");
		for (String email : emails) {
			Optional<User> optional = userRepo.findOneById(email);
			User user = optional.orElseThrow(() -> new ServiceException("Cannot find user with email '" + email + "'."));

			users.add(user);
		}

		return users;
	}

}
