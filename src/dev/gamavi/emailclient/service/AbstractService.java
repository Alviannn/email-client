package dev.gamavi.emailclient.service;

import dev.gamavi.emailclient.shared.Shared;

public abstract class AbstractService {

	protected Shared shared;

	public AbstractService(Shared shared) {
		this.shared = shared;
	}

	public Shared getShared() {
		return shared;
	}

}
