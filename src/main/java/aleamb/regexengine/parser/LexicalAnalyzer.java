package aleamb.regexengine.parser;

import java.util.Arrays;

/**
 * Analizador léxico para el motor de regex.
 * 
 */
public class LexicalAnalyzer {

	// expresión regular dada
	private String regex;
	// posición del analizador léxico
	private int index;
	// ultimo token construído.
	private Token token = null;
	// último carácter leído
	private char character;
	// valor del token.
	private String tokenValue;

	// caracteres ignorados por el analizador lexico
	private char[] ignoredChars = { '\t', ' ' };

	// caracteres de escape permitidos (ordenados)
	private char[] escape_chars = { '(', ')', '*', '+', '-', '.', '?', '[',
			'\\', ']', '^', 's', 't', '|' };

	// valor de los caracteres de escape (ordenados)
	private char[] escape_chars_value = { '(', ')', '*', '+', '-', '.', '?',
			'[', '\\', ']', '^', ' ', '\t', '|' };

	public LexicalAnalyzer(String pRegex) {

		Arrays.sort(escape_chars);

		if (pRegex == null) {
			throw new IllegalArgumentException("Expresion regular nula.");
		}
		regex = pRegex;
		index = 0;
		character = 0;
	}

	public boolean readToken() throws LexerException {
		boolean charRead = readCharacter();
		if (charRead) {
			switch (character) {
			case '[':
				token = Token.L_BRACKET;
				tokenValue = "[";
				break;
			case '(':
				token = Token.L_PARENTHESIS;
				tokenValue = "(";
				break;
			case ')':
				token = Token.R_PARENTHESIS;
				tokenValue = ")";
				break;
			case '+':
			case '*':
			case '?':
				token = Token.QUANTIFIER;
				tokenValue = String.valueOf(character);
				break;
			case '|':
				token = Token.PIPE;
				tokenValue = "|";
				break;
			case '\\':
				verifyEscapeCharacters();
				break;
			default:
				if (isValidChar(character)) {
					token = Token.CHAR;
					tokenValue = String.valueOf(character);
				} else {
					lexError("Carácter no válido");
				}
			}
		}
		return charRead;
	}

	public int getIndex() {
		return index;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public String getTokenValue() {
		return tokenValue.toString();
	}

	private boolean isValidChar(char pCharacter) {
		return (pCharacter > 0x20 && pCharacter < Character.MAX_VALUE);
	}

	/*
	 * Detectada secuencia de escape. Verificar que el siguiente caracter
	 * pertenece al conjunto de caracteres que pueden escaparse.
	 */
	private void verifyEscapeCharacters() {

		if (readCharacter()) {
			int charIndex = findEscapedChar(character);
			if (charIndex >= 0) {
				token = Token.ESCAPE;
				tokenValue = String.valueOf(escape_chars_value[charIndex]);
			} else {
				lexError("Carácter " + character
						+ " no forma parte de los caracteres de escape.");
			}
		} else {
			lexError("Se esperaba " + Arrays.toString(escape_chars));
		}
	}

	private int findEscapedChar(char pCharacter) {
		return Arrays.binarySearch(escape_chars, pCharacter);
	}

	private boolean isIgnoredChar(char pCharacter) {
		return (Arrays.binarySearch(ignoredChars, pCharacter) >= 0);
	}

	/*
     * 
     */
	private boolean readCharacter() {

		boolean charRead = false;

		while (!charRead && index < regex.length()) {
			character = regex.charAt(index++);
			charRead = !isIgnoredChar(character);
		}
		return charRead;

	}

	private void lexError(String errorMessage) {
		throw new LexerException(index, errorMessage);

	}

}
