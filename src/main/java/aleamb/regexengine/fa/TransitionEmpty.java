package aleamb.regexengine.fa;

/**
 * Transición tipo vacía (epsilon) Fundamental para construir el AFND y
 * convertirlo a AFD
 */
public class TransitionEmpty extends TransitionBase implements Cloneable {

	public TransitionEmpty() {
		setRepresentation("empty");
	}

	@Override
	public boolean match(char character) {
		return true;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new TransitionEmpty();
	}

}
