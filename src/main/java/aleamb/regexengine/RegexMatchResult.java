package aleamb.regexengine;

/**
 * 
 * Usado por {@link Regex#match(char[], RegexMatchResult)} para registrar la
 * posición de una coincidencia.
 * 
 * También pude ser usado para indicar a
 * {@link Regex#match(char[], RegexMatchResult)} desde donde comenzar la lectura
 * del buffer. Por defecto estará establecido a cero.
 * 
 * 
 */

public class RegexMatchResult {

	private int position = 0;

	private int matchStartPosition = -1;
	private int matchLength = 0;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getMatchStartPosition() {
		return matchStartPosition;
	}

	public void setMatchStartPosition(int matchStartPosition) {
		this.matchStartPosition = matchStartPosition;
	}

	public int getMatchLength() {
		return matchLength;
	}

	public void setMatchLength(int matchLength) {
		this.matchLength = matchLength;
	}

}
