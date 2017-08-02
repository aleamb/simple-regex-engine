package aleamb.regexengine.fa;

/**
 * Almacena un par de estados. Esta estructura será útil para ir construyendo el
 * grafo que representa el autómata.
 */
class StatePair {

	private State firstState;
	private State secondState;

	public StatePair(State firstState, State secondState) {
		this.firstState = firstState;
		this.secondState = secondState;
	}

	public State getFirstState() {
		return firstState;
	}

	public void setFirstState(State firState) {
		this.firstState = firState;
	}

	public State getSecondState() {
		return secondState;
	}

	public void setSecondState(State secondState) {
		this.secondState = secondState;
	}

}