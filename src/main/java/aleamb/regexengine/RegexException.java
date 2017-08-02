package aleamb.regexengine;

public class RegexException extends RuntimeException {

	private static final long serialVersionUID = -8311650406099235758L;

	public RegexException() {
		super();
	}

	public RegexException(String message, Throwable cause) {
		super(message, cause);
	}

	public RegexException(String message) {
		super(message);
	}

	public RegexException(Throwable cause) {
		super(cause);
	}

}
