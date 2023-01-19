package dev.gamavi.emailclient.model;

import java.sql.Timestamp;

public abstract class AbstractModel {

	private Timestamp createdAt;
	private Timestamp updatedAt;

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

}
