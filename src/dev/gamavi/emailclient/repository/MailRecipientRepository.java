package dev.gamavi.emailclient.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dev.gamavi.emailclient.model.MailRecipient;
import dev.gamavi.emailclient.model.MailRecipientBuilder;
import dev.gamavi.emailclient.model.ReceiveType;
import dev.gamavi.emailclient.shared.Closer;
import dev.gamavi.emailclient.shared.SQLHelper;
import dev.gamavi.emailclient.shared.Shared;

public class MailRecipientRepository extends AbstractRepository<MailRecipient, Long> {

	public MailRecipientRepository(SQLHelper helper) {
		super(helper);
	}

	@Override
	public void insert(MailRecipient instance) {
		try (Closer closer = new Closer()) {
			this.getHelper().execute(
				"INSERT INTO mails_recipient" +
				" (mail_id, recipient, type, has_read) VALUES (?, ?, ?, ?)",

				instance.getMail().getId(),
				instance.getRecipient().getEmail(),
				instance.getType().ordinal(),
				instance.isHasRead()
			);

			SQLHelper helper = this.getHelper();
			String afterInsertQuery = "SELECT id, created_at, updated_at FROM mails_recipient WHERE id = ?";

			ResultSet rs = closer.add(helper.getResults(afterInsertQuery, "LAST_INSERT_ID()"));
			rs.next();

			instance.setId(rs.getLong("id"));
			instance.setCreatedAt(rs.getTimestamp("created_at"));
			instance.setCreatedAt(rs.getTimestamp("updated_at"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(MailRecipient instance) {
		try (Closer closer = new Closer()) {
			this.getHelper().execute(
				"UPDATE mails_recipients SET" +
				"  has_read = ?," +
				"  updated_at = NOW() " +
				"WHERE id = ? AND deleted_at IS NULL",

				instance.isHasRead(),
				instance.getId()
			);

			SQLHelper helper = this.getHelper();
			String afterUpdateQuery = "SELECT updated_at FROM mails_recipients WHERE id = ?";

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
				"UPDATE mails_recipients SET deleted_at = NOW() WHERE id = ? AND deleted_at IS NULL",
				id
			);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public MailRecipient findOne(Long id) {
		String query = "SELECT * FROM mails_recipients WHERE id = ? AND deleted_at IS NULL";
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
	public List<MailRecipient> findAll() {
		List<MailRecipient> mailRecipients = new ArrayList<>();
		String query = "SELECT * FROM mails_recipients WHERE deleted_at IS NULL";

		try (ResultSet rs = this.getHelper().getResults(query)) {
			while (rs.next()) {
				mailRecipients.add(this.mapToObject(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return mailRecipients;
	}

	public List<MailRecipient> findAllByMailId(Long mailId) {
		List<MailRecipient> mailRecipients = new ArrayList<>();
		String query = "SELECT * FROM mails_recipients WHERE mail_id = ? AND deleted_at IS NULL";

		try (ResultSet rs = this.getHelper().getResults(query, mailId)) {
			while (rs.next()) {
				mailRecipients.add(this.mapToObject(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return mailRecipients;
	}

	public List<MailRecipient> findAllByRecipientEmail(String recipientEmail) {
		List<MailRecipient> mailRecipients = new ArrayList<>();
		String query = "SELECT * FROM mails_recipients WHERE recipient = ? AND deleted_at IS NULL";

		try (ResultSet rs = this.getHelper().getResults(query, recipientEmail)) {
			while (rs.next()) {
				mailRecipients.add(this.mapToObject(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return mailRecipients;
	}

	@Override
	public void createTable() {
		try {
			this.getHelper().execute(
				"CREATE TABLE IF NOT EXISTS mails_recipients (" +
					"id BIGINT NOT NULL AUTO_INCREMENT, " +
					"mail_id BIGINT NOT NULL, " +
					"recipient VARCHAR(255) NOT NULL, " +
					"type INT NOT NULL, " +
					"has_read BOOLEAN NOT NULL, " +

					"created_at TIMESTAMP NOT NULL DEFAULT NOW(), " +
					"updated_at TIMESTAMP NOT NULL DEFAULT NOW(), " +
					"deleted_at TIMESTAMP, " +

					"PRIMARY KEY (id), " +
					"FOREIGN KEY (mail_id) REFERENCES mails (id), " +
					"FOREIGN KEY (recipient) REFERENCES users (email) " +
				")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public MailRecipient mapToObject(ResultSet rs) throws SQLException {
		Shared shared = Shared.getInstance();

		UserRepository userRepo = shared.getUserRepo();
		MailRepository mailRepo = shared.getMailRepo();

		Long mailId = rs.getLong("mail_id");
		String recipientEmail = rs.getString("recipient");

		MailRecipient mailRecipient = new MailRecipientBuilder()
			.setMail(mailRepo.findOne(mailId))
			.setRecipient(userRepo.findOne(recipientEmail))
			.setHasRead(rs.getBoolean("has_read"))
			.setType(ReceiveType.values()[rs.getInt("type")])
			.build();

		mailRecipient.setCreatedAt(rs.getTimestamp("created_at"));
		mailRecipient.setUpdatedAt(rs.getTimestamp("updated_at"));

		return mailRecipient;
	}

}
