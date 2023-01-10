package dev.gamavi.emailclient.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dev.gamavi.emailclient.model.Mail;
import dev.gamavi.emailclient.model.MailBuilder;
import dev.gamavi.emailclient.model.User;
import dev.gamavi.emailclient.shared.Closer;
import dev.gamavi.emailclient.shared.SQLHelper;
import dev.gamavi.emailclient.shared.Shared;

public class MailRepository extends AbstractRepository<Mail, Long> {

	public MailRepository(SQLHelper helper) {
		super(helper);
	}

	@Override
	public void insert(Mail instance) {
		try (Closer closer = new Closer()) {
			this.getHelper().execute(
				"INSERT INTO mails" +
				" (sender, title, message) VALUES (?, ?, ?)",

				instance.getSender().getEmail(),
				instance.getTitle(),
				instance.getMessage()
			);
			
			SQLHelper helper = this.getHelper();
			String afterInsertQuery = "SELECT id, created_at, updated_at FROM mails WHERE id = ?";

			ResultSet rs = closer.add(helper.getResults(afterInsertQuery, "LAST_INSERT_ID()"));
			assert rs.next();
			
			instance.setId(rs.getLong("id"));
			instance.setCreatedAt(rs.getTimestamp("created_at"));
			instance.setCreatedAt(rs.getTimestamp("updated_at"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Mail instance) {
		try (Closer closer = new Closer()) {
			this.getHelper().execute(
				"UPDATE mails SET" +
				"  title = ?," +
				"  message = ?," +
				"  updated_at = NOW() " +
				"WHERE id = ? AND deleted_at IS NOT NULL",

				instance.getTitle(),
				instance.getMessage(),
				instance.getId()
			);
			
			SQLHelper helper = this.getHelper();
			String afterUpdateQuery = "SELECT updated_at FROM mails WHERE id = ?";
			
			ResultSet rs = closer.add(helper.getResults(afterUpdateQuery, instance.getId()));
			assert rs.next();

			instance.setCreatedAt(rs.getTimestamp("updated_at"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(Long id) {
		try {
			this.getHelper().execute(
				"UPDATE mails SET deleted_at = NOW() WHERE id = ? AND deleted_at IS NOT NULL",
				id
			);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Mail findOne(Long id) {
		String query = "SELECT * FROM mails WHERE id = ? AND deleted_at IS NOT NULL";
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
	public List<Mail> findAll() {
		List<Mail> mails = new ArrayList<>();
		String query = "SELECT * FROM mails WHERE deleted_at IS NOT NULL";
		
		try (ResultSet rs = this.getHelper().getResults(query)) {
			while (rs.next()) {
				mails.add(this.mapToObject(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return mails;
	}

	@Override
	public void createTable() {
		try {
			this.getHelper().execute(
				"CREATE TABLE IF NOT EXISTS mails (" +
					"id BIGINT NOT NULL AUTO_INCREMENT " +
					"sender VARCHAR(255) NOT NULL, " +
					"title VARCHAR(255) NOT NULL, " +
					"message VARCHAR(255) NOT NULL, " +
					
					"created_at TIMESTAMP NOT NULL DEFAULT NOW(), " +
					"updated_at TIMESTAMP NOT NULL DEFAULT NOW(), " +
					"deleted_at TIMESTAMP, " +
					
					"PRIMARY KEY (id), " +
					"FOREIGN KEY (sender) REFERENCES users (email) " +
				")");
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public Mail mapToObject(ResultSet rs) throws SQLException {
		String senderEmail = rs.getString("sender");
		
		UserRepository userRepo = Shared.getInstance().getUserRepo();
		User user = userRepo.findOne(senderEmail);
		
		Mail mail = new MailBuilder()
			.setTitle(rs.getString("title"))
			.setMessage(rs.getString("message"))
			.setSender(user)
			.build();
		
		mail.setCreatedAt(rs.getTimestamp("created_at"));
		mail.setUpdatedAt(rs.getTimestamp("updated_at"));
		
		return mail;
	}

}
