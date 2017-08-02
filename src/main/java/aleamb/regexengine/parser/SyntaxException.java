package aleamb.regexengine.parser;

import java.util.Arrays;

import aleamb.regexengine.RegexException;

public class SyntaxException extends RegexException {

	private static final long serialVersionUID = -3159736736995590441L;

	private int position;
	private Token[] tokens;
	private String[] stringTokens;

	public SyntaxException(int pPosition, String[] pTokens) {
		super("Error sintáctico en " + pPosition + ". Se esperaba "
				+ Arrays.toString(pTokens));
		position = pPosition;
		stringTokens = pTokens;
	}

	public SyntaxException(int pPosition, Token[] pTokens) {
		super("Error sintáctico en " + pPosition + ". Se esperaba "
				+ Arrays.toString(pTokens));
		tokens = pTokens;
	}

	public SyntaxException(String msg) {
		super(msg);
	}

	public SyntaxException(int pPosition, String msg) {
		super("Error sintáctico en " + pPosition + ". " + msg);
		position = pPosition;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Token[] getTokens() {
		return tokens;
	}

	public void setTokens(Token[] tokens) {
		this.tokens = tokens;
	}

	public String[] getStringTokens() {
		return stringTokens;
	}

	public void setStringTokens(String[] stringTokens) {
		this.stringTokens = stringTokens;
	}

}
