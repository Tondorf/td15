package org.game.network;

public class AboveCapacityException extends Exception {
	private static final long serialVersionUID = -3419467299295148501L;

	public AboveCapacityException() {
	}

	public AboveCapacityException(String message) {
		super(message);
	}

	public AboveCapacityException(Throwable cause) {
		super(cause);
	}

	public AboveCapacityException(String message, Throwable cause) {
		super(message, cause);
	}

	public AboveCapacityException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
