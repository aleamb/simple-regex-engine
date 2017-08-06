package aleamb.regexengine.fa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * Automaton's state.
 * 
 */
public class State implements Comparable<State> {

    // transitions set for this state.
    private List<Transition> transitions;

    // this flag indicates initial state.
    private boolean initial = false;

    // thos flag indicates final state
    private boolean end = false;

    private int id = -1;

    public State() {
        transitions = new ArrayList<Transition>();
    }

    public State(int pId) {
        this();
        id = pId;
    }

    // links states through of a transition
    public void connect(Transition transition, State pState) {
        transition.setNextState(pState);
        transitions.add(transition);
    }

    public Iterable<Transition> getTransitions() {
        return Collections.unmodifiableList(transitions);
    }

    public boolean isInitial() {
        return initial;
    }

    public void setInitial(boolean initial) {
        this.initial = initial;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean hasTransitions() {
        return transitions != null && !transitions.isEmpty();
    }

    @Override
    public int compareTo(State pState) {

        return id - pState.getId();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        State other = (State) obj;
        if (id != other.id)
            return false;
        return true;
    }

    /**
     * Serialize state to Graphviz
     */
    @Override
    public String toString() {

        StringBuilder buffer = new StringBuilder();

        String label = String.valueOf(id);
        if (initial) {
            label += "-Start";
        }
        if (end) {
            label += "-Accepted";
        }

        buffer.append(String.valueOf(getId() + " [label=\""));
        buffer.append(label);

        buffer.append("\"];\n");

        for (Transition t : transitions) {

            buffer.append(String.valueOf(getId()));
            buffer.append(" -> ");
            buffer.append(String.valueOf(t.getNextState().getId()));
            buffer.append("[label=\"");
            buffer.append(t);
            buffer.append("\"]");
            buffer.append(";\n");
        }

        return buffer.toString();
    }

}
