package dev.gamavi.emailclient.shared;

import java.util.ArrayList;
import java.util.List;

public class Closer implements AutoCloseable {
	
	private final List<AutoCloseable> closeableList;

	public Closer() {
		this.closeableList = new ArrayList<>();
	}
	
	public <T extends AutoCloseable> T add(T closeable) {
		if (closeable == null) {
			return null;
		}
		
		closeableList.add(closeable);
		return closeable;
	}

	@Override
	public void close() throws Exception {
		for (AutoCloseable closeable : closeableList) {
			closeable.close();
		}
	}

}
