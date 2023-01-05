package dev.gamavi.emailclient.shared;

public class Database {
	
	private static Database instance;
	
	public static Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}
		
		return instance;
	}
	
	private final SQLHelper helper;
	
	private Database() {
		this.helper = new SQLHelper("localhost", 3306, "email_client", "root", "password");
	}

	public SQLHelper getHelper() {
		return helper;
	}

}
