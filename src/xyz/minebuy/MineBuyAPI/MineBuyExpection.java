package xyz.minebuy.MineBuyAPI;

public final class MineBuyExpection extends Exception {

	private static final long serialVersionUID = -586710158255345773L;
	
	private String message;

	public MineBuyExpection(String message) {
		this.message = message;
	}

	public MineBuyExpection(Exception cause) {
		this.message = cause.getMessage();
		this.initCause(cause);
	}

	public String getMessage() {
		return message;
	}

}
