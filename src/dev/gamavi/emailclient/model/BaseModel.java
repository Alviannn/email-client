package dev.gamavi.emailclient.model;

import java.sql.Timestamp;

public abstract class BaseModel {
	
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private Timestamp deletedAt;

	public Timestamp getCreatedAt() {
		return createdAt;
	}
	
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	
	public Timestamp getDeletedAt() {
		return deletedAt;
	}
	
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public void setDeletedAt(Timestamp deletedAt) {
		this.deletedAt = deletedAt;
	}
	
}
