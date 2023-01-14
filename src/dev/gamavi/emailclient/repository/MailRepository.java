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
			Long replyToMailId = null;
			Mail replyTo = instance.getReplyTo();
			if (replyTo != null) {
				replyToMailId = replyTo.getId();
			}

			this.getHelper().execute(
				"INSERT INTO mails" +
				" (sender, reply_to, title, message) VALUES (?, ?, ?, ?)",

				instance.getSender().getEmail(),
				replyToMailId,
				instance.getTitle(),
				instance.getMessage()
			);

			SQLHelper helper = this.getHelper();
			String afterInsertQuery = "SELECT id, created_at, updated_at FROM mails WHERE id = LAST_INSERT_ID(id)";

			ResultSet rs = closer.add(helper.getResults(afterInsertQuery));
			rs.next();

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
				"WHERE id = ? AND deleted_at IS NULL",

				instance.getTitle(),
				instance.getMessage(),
				instance.getId()
			);

			SQLHelper helper = this.getHelper();
			String afterUpdateQuery = "SELECT updated_at FROM mails WHERE id = ?";

			ResultSet rs = closer.add(helper.getResults(afterUpdateQuery, instance.getId()));
			rs.next();

			instance.setCreatedAt(rs.getTimestamp("updated_at"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(Long id) {
		try {
			this.getHelper().execute(
				"UPDATE mails SET deleted_at = NOW() WHERE id = ? AND deleted_at IS NULL",
				id
			);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Mail findOne(Long id) {
		String query = "SELECT * FROM mails WHERE id = ? AND deleted_at IS NULL";
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
		String query = "SELECT * FROM mails WHERE deleted_at IS NULL";

		try (ResultSet rs = this.getHelper().getResults(query)) {
			while (rs.next()) {
				mails.add(this.mapToObject(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return mails;
	}

	public List<Mail> findAllBySenderEmail(String senderEmail) {
		List<Mail> mails = new ArrayList<>();
		String query = "SELECT * FROM mails WHERE sender = ? AND deleted_at IS NULL";

		try (ResultSet rs = this.getHelper().getResults(query, senderEmail)) {
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
					"id BIGINT NOT NULL AUTO_INCREMENT, " +
					"sender VARCHAR(255) NOT NULL, " +
					"reply_to BIGINT, " +
					"title VARCHAR(255) NOT NULL, " +
					"message VARCHAR(255) NOT NULL, " +

					"created_at TIMESTAMP NOT NULL DEFAULT NOW(), " +
					"updated_at TIMESTAMP NOT NULL DEFAULT NOW(), " +
					"deleted_at TIMESTAMP NULL, " +

					"PRIMARY KEY (id), " +
					"FOREIGN KEY (sender) REFERENCES users (email), " +
					"FOREIGN KEY (reply_to) REFERENCES mails (id)" +
				")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Mail mapToObject(ResultSet rs) throws SQLException {
		UserRepository userRepo = Shared.getInstance().getUserRepo();

		String senderEmail = rs.getString("sender");
		Long replyToMailId = (Long) rs.getObject("reply_to");

		User user = userRepo.findOne(senderEmail);
		Mail replyTo = null;
		if (replyToMailId != null) {
			replyTo = this.findOne(replyToMailId);
		}

		Mail mail = new MailBuilder()
			.setTitle(rs.getString("title"))
			.setMessage(rs.getString("message"))
			.setSender(user)
			.setReplyTo(replyTo)
			.build();

		mail.setCreatedAt(rs.getTimestamp("created_at"));
		mail.setUpdatedAt(rs.getTimestamp("updated_at"));

		return mail;
	}

}
