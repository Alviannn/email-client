package dev.gamavi.emailclient.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dev.gamavi.emailclient.model.User;
import dev.gamavi.emailclient.model.UserBuilder;
import dev.gamavi.emailclient.shared.Closer;
import dev.gamavi.emailclient.shared.SQLHelper;

public class UserRepository extends AbstractRepository<User, String> {

	public UserRepository(SQLHelper helper) {
		super(helper);
	}

	@Override
	public void insert(User instance) {
		try (Closer closer = new Closer()) {
			this.getHelper().execute(
				"INSERT INTO users" +
				" (email, display_name, password) VALUES (?, ?, ?)",

				instance.getEmail(),
				instance.getDisplayName(),
				instance.getPassword()
			);

			String afterInsertQuery = "SELECT created_at, updated_at FROM users WHERE email = ?";
			SQLHelper helper = this.getHelper();

			ResultSet rs = closer.add(helper.getResults(afterInsertQuery, instance.getEmail()));
			rs.next();

			instance.setCreatedAt(rs.getTimestamp("created_at"));
			instance.setUpdatedAt(rs.getTimestamp("updated_at"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(User instance) {
		try (Closer closer = new Closer()) {
			this.getHelper().execute(
				"UPDATE users SET" +
				"  display_name = ?," +
				"  password = ?," +
				"  updated_at = NOW() " +
				"WHERE email = ? AND deleted_at IS NULL",

				instance.getDisplayName(),
				instance.getPassword(),
				instance.getEmail()
			);

			String afterUpdateQuery = "SELECT updated_at FROM users WHERE email = ?";
			SQLHelper helper = this.getHelper();

			ResultSet rs = closer.add(helper.getResults(afterUpdateQuery, instance.getEmail()));
			rs.next();

			instance.setUpdatedAt(rs.getTimestamp("updated_at"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(String id) {
		try {
			this.getHelper().execute(
				"UPDATE users SET deleted_at = NOW() WHERE email = ? AND deleted_at IS NULL",
				id
			);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public User findOne(String id) {
		String query = "SELECT * FROM users WHERE email = ? AND deleted_at IS NULL";
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
		String query = "SELECT * FROM users WHERE deleted_at IS NULL";

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
					"email VARCHAR(255) NOT NULL, " +
					"display_name VARCHAR(255) NOT NULL, " +
					"password VARCHAR(255), " +

					"created_at TIMESTAMP NOT NULL DEFAULT NOW(), " +
					"updated_at TIMESTAMP NOT NULL DEFAULT NOW(), " +
					"deleted_at TIMESTAMP NULL, " +

					"PRIMARY KEY (email) " +
				")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public User mapToObject(ResultSet rs) throws SQLException {
		User user = new UserBuilder()
			.setEmail(rs.getString("email"))
			.setDisplayName(rs.getString("display_name"))
			.setPassword(rs.getString("password"))
			.build();

		user.setCreatedAt(rs.getTimestamp("created_at"));
		user.setUpdatedAt(rs.getTimestamp("updated_at"));

		return user;
	}

}
