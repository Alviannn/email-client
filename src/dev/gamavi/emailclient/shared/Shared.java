package dev.gamavi.emailclient.shared;

import dev.gamavi.emailclient.repository.UserRepository;

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
	
	private Shared() {
		this.helper = new SQLHelper("localhost", 3306, "email_client", "root", "");
		this.userRepo = new UserRepository(helper);
	}

	public SQLHelper getHelper() {
		return helper;
	}
	
	public UserRepository getUserRepo() {
		return userRepo;
	}
	
	public void createRepoTables() {
		userRepo.createTable();
	}
	
}
