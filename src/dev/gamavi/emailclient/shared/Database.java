package dev.gamavi.emailclient.shared;

public class Database {
	
	private static Database instance;
	
	public static Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}
		
		return instance;
	}
	
	// todo: implement mysql connection
	// todo: store currently logged in user

}
