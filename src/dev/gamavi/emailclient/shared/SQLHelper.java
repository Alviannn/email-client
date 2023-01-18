package dev.gamavi.emailclient.shared;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLHelper {

	private final String host;
	private final int port;
	private final String database;
	private final String username, password;

	private Connection connection;

	public SQLHelper(String host, int port, String database, String username, String password) {
		super();
		this.host = host;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
	}

	public void connect() throws SQLException {
        String urlFormat = "jdbc:mysql://%s:%d/%s";
        String connUrl = String.format(urlFormat, host, port, database);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL driver is not available");
        }

        this.connection = DriverManager.getConnection(connUrl, username, password);
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to disconnect MySQL connection");
        }
    }

    public PreparedStatement prepare(String query, Object...args) throws SQLException {
    	PreparedStatement statement = connection.prepareStatement(query);
        for (int i = 0; i < args.length; i++) {
            statement.setObject(i + 1, args[i]);
        }

        return statement;
    }

    public ResultSet getResults(String query, Object... args) throws SQLException {
        return this.prepare(query, args).executeQuery();
    }

    public void execute(String query, Object... args) throws SQLException {
        try (PreparedStatement statement = this.prepare(query, args)) {
            statement.execute();
        }
    }

}
