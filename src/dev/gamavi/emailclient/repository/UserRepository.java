package dev.gamavi.emailclient.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dev.gamavi.emailclient.model.User;
import dev.gamavi.emailclient.model.UserBuilder;
import dev.gamavi.emailclient.shared.SQLHelper;

public class UserRepository extends AbstractRepository<User, String> {

	public UserRepository(SQLHelper helper) {
		super(helper);
	}

	@Override
	public void insert(User instance) {
		try {
			this.getHelper().execute(
				"INSERT INTO users VALUES (?, ?, ?)",

				instance.getEmail(),
				instance.getDisplayName(),
				instance.getPassword()
			);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(String id) {
		try {
			this.getHelper().execute("DELETE users WHERE email = ?", id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public User findOne(String id) {
		String query = "SELECT * FROM users WHERE email = ?";
		try (ResultSet rs = this.getHelper().getResults(query, id)) {
			if (!rs.next()) {
				return null;
			}
			
			return this.mapToObject(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<User> findAll() {
		List<User> users = new ArrayList<>();
		String query = "SELECT * FROM users";
		
		try (ResultSet rs = this.getHelper().getResults(query)) {
			while (rs.next()) {
				users.add(this.mapToObject(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return users;
	}
	
	@Override
	public void createTable() {
		try {
			this.getHelper().execute(
				"CREATE TABLE IF NOT EXISTS users (" +
					"email VARCHAR(255) NOT NULL," +
					"display_name VARCHAR(255) NOT NULL," +
					"password VARCHAR(255)," +
				")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public User mapToObject(ResultSet rs) throws SQLException {
		return new UserBuilder()
			.setEmail(rs.getString("email"))
			.setDisplayName(rs.getString("display_name"))
			.setPassword(rs.getString("password"))
			.build();
	}

}
