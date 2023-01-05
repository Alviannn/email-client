package dev.gamavi.emailclient.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public abstract class AbstractRepository<Type, ID> {
	
	protected final Connection connection;
	
	public AbstractRepository(Connection connection) {
		this.connection = connection;
	}
	
	public abstract void insert(Type instance);

	public abstract void delete(ID id);
	
	public abstract Type findOne(ID id);
	
	public abstract List<Type> findAll();

	public abstract Type mapResults(ResultSet rs);

}
