package aleamb.regexengine.fa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Autómata finito (puede ser determinista o no determinista) que será
 * construído a partir de una expresión regular.
 * 
 * La instancia de esta clase mantiene una referencia al primer estado del
 * autómata. Los restantes estdos se obtienen mediante las transiciones a partir
 * del primero.
 * 
 * También se mantendrá una lista con las referencias a todas los tipos de
 * transiciones que tenga el autómata, ya que de esta forma se almacena el
 * propio alfabeto que soporta. Esta lista también se usa para optimizar el
 * algoritmo de transformación de un atómata no determinista a uno determinista.
 * 
 * 
 * Finite automaton (it may be deterministic or not) result of parsing regular
 * expression.
 * 
 * This object keep a first state reference of the automaton. Rest states are
 * obtained through of transitions.
 * 
 * This object also contains a list of all type of transitions for the
 * automaton. This is for store complete alphabet that automaton manages. This
 * list is used for transform from nodeterministic automaton to deterministic
 * automaton using Powerset construction algorithm.
 * 
 */
public class Automaton {

    // number of automaton states.
    private int stateCount;

    // Initial state. Rest os states are linked through transitions.
    private State initialState;

    // last added state
    private State lastState;

    // list that stores all state types. This is used as alphabet.
    private Set<Transition> alphabet;

    // Lineal list that stores all states. Used for optimization.
    private List<State> states;

    public Automaton() {
        alphabet = new HashSet<Transition>();
        states = new ArrayList<State>();
        stateCount = 0;
    }

    public Automaton(State firstState) {
        this();
        initialState = firstState;
        build();
    }

    public int getStateCount() {
        return stateCount;
    }

    public void setStateCount(int stateCount) {
        this.stateCount = stateCount;
    }

    public State getInitialState() {
        return initialState;
    }

    public void setIntialState(State initialState) {
        this.initialState = initialState;
    }

    public boolean addToAlphabet(Transition pTransition) {
        return alphabet.add(pTransition);
    }

    public Iterable<Transition> getAlphabet() {
        return Collections.unmodifiableSet(alphabet);
    }

    public State getLastState() {
        return lastState;
    }

    public Collection<State> getStateList() {
        return Collections.unmodifiableList(states);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        boolean[] visited = new boolean[stateCount];
        navigateAndPrint(builder, initialState, visited);

        return builder.toString();
    }

    private void navigateAndPrint(StringBuilder builder, State pState, boolean[] visited) {

        if (pState == null) {
            return;
        }
        visited[pState.getId()] = true;
        builder.append(pState);
        Iterable<Transition> transitions = pState.getTransitions();
        if (transitions != null) {
            for (Transition t : pState.getTransitions()) {
                State nextState = t.getNextState();
                if (!visited[nextState.getId()]) {
                    navigateAndPrint(builder, nextState, visited);
                }
            }
        }
    }

    private void build() {

        /*
         * Depth-first traversing for count and give identifiers to the states
         * 
         */
        Queue<State> stateQueue = new LinkedList<State>();

        stateQueue.add(initialState);
        initialState.setId(stateCount++);

        while (!stateQueue.isEmpty()) {

            State state = stateQueue.poll();
            states.add(state);

            if (state.getTransitions() != null) {
                for (Transition t : state.getTransitions()) {
                    // add transition for build automaton's alphabet
                    alphabet.add(t);
                    State childState = t.getNextState();
                    if (childState.getId() == -1) {
                        childState.setId(stateCount++);
                        stateQueue.add(childState);
                    }
                }
            }
        }
    }
}
