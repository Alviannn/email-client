package dev.gamavi.emailclient.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import dev.gamavi.emailclient.shared.SQLHelper;

public abstract class AbstractRepository<Type, ID> {
	
	private final SQLHelper helper;
	
	public AbstractRepository(SQLHelper helper) {
		this.helper = helper;
	}
	
	public SQLHelper getHelper() {
		return helper;
	}
	
	public abstract void insert(Type instance);

	public abstract void delete(ID id);
	
	public abstract Type findOne(ID id);
	
	public abstract List<Type> findAll();

	/**
	 * Helps mapping from `ResultSet` to object/instance class.
	 */
	public abstract Type mapToObject(ResultSet rs) throws SQLException;

}
