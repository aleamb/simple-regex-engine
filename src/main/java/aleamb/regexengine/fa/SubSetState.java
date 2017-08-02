package es.chathispano.canalprogramacion.regexengine.fa;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase auxiliar que agrupa un conjunto de estados y los trata como uno solo.
 * Esta estructra de datos es usada en el algoritmo subconjunto para transformar
 * un AFND a un AFD
 * 
 * 
 */
class SubSetState {

	private boolean mark;
	// estados subconjunto de estado
	private Set<State> states = new HashSet<State>();
	/*
	 * Relación unívoca de un estado del autómata con el conjutno de estados.
	 * Este es el estado que finalmente irá al objeto Automaton
	 */
	private State state = new State();
	/*
	 * El id de un estado de este tipo será construído como una cadena que
	 * concatena los id's de la lista de estados que lo forman.
	 */
	private String id;

	public SubSetState(Set<State> states) {
		super();
		this.states = states;
		id = buildId();
	}

	private String buildId() {

		int[] ids = new int[states.size()];
		int i = 0;
		for (State s : states) {
			ids[i++] = s.getId();

		}
		Arrays.sort(ids);
		return Arrays.toString(ids);
	}

	public boolean isMark() {
		return mark;
	}

	public void setMark(boolean mark) {
		this.mark = mark;
	}

	public Set<State> getStates() {
		return states;
	}

	public State getState() {
		return state;
	}

	public String getId() {
		return id;
	}

	public void setInitial(boolean b) {
		state.setInitial(b);

	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubSetState other = (SubSetState) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public boolean isEmpty() {

		return states.isEmpty();
	}
}