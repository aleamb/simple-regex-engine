package aleamb.regexengine.fa;

/**
 * Transición usada para ^[a-z]
 * 
 */
public class TransitionExcludeRange extends TransitionBase implements Cloneable {

	private char initChar;
	private char endChar;

	public TransitionExcludeRange(char initChar, char endChar) {
		super();
		this.initChar = initChar;
		this.endChar = endChar;
		setRepresentation("^" + String.valueOf(initChar) + "-"
				+ String.valueOf(endChar));
	}

	public char getInitChar() {
		return initChar;
	}

	public char getEndChar() {
		return endChar;
	}

	@Override
	public boolean match(char character) {

		return (character < initChar || character > endChar);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {

		TransitionExcludeRange t = new TransitionExcludeRange(initChar, endChar);

		return t;
	}
}
